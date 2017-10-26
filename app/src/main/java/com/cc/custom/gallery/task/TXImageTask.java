package com.cc.custom.gallery.task;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.Media;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;
import android.support.v4.util.ArrayMap;
import android.util.Log;

import com.cc.custom.TXContextManager;
import com.cc.custom.TXDate;
import com.cc.custom.gallery.config.TXImagePickerConfig;
import com.cc.custom.gallery.listener.TXImageTaskListener;
import com.cc.custom.gallery.model.TXAlbumModel;
import com.cc.custom.gallery.model.TXImageModel;
import com.cc.custom.gallery.utils.TXImageExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 加载相册内图片任务
 * <p>
 * Created by Cheng on 17/3/24.
 */
@WorkerThread
public class TXImageTask {

    private static final String TAG = "TXImageTask";

    /**
     * 分页大小
     */
    public static final int PAGE_LIMIT = 1000;

    private static final String SELECTION_IMAGE_MIME_TYPE = Media.MIME_TYPE + "=? or " + Media.MIME_TYPE + "=? or " + Media.MIME_TYPE + "=?";
    private static final String SELECTION_ID = Media.BUCKET_ID + "=? and (" + SELECTION_IMAGE_MIME_TYPE + " )";
    private static final String[] SELECTION_ARGS_IMAGE_MIME_TYPE = {"image/jpeg", "image/png", "image/jpg"};
    private TXImagePickerConfig mPickerConfig;
    private Map<String, String> mThumbnailMap;
    private String mBucketId;

    public TXImageTask() {
        this.mPickerConfig = null;
        this.mThumbnailMap = new ArrayMap<>();
    }

    /**
     * 加载图片－－分页大小：{@link #PAGE_LIMIT}
     *
     * @param bucketId id
     * @param page     页码
     * @param listener 回调
     */
    public void load(String bucketId, int page, @NonNull TXImageTaskListener listener) {
        Context context = TXContextManager.getInstance().getApplicationContext();
        if (context == null) {
            return;
        }

        mBucketId = bucketId;

        // 构建缩略图
        buildThumbnail(context.getContentResolver());
        // 构建图片
        buildAlbumList(context.getContentResolver(), bucketId, page, listener);
    }

    private void buildThumbnail(ContentResolver cr) {
        String[] projection = {Images.Thumbnails.IMAGE_ID, Images.Thumbnails.DATA};
        queryThumbnails(cr, projection);
    }

    /**
     * 查询缩略图
     *
     * @param cr
     * @param projection
     */
    private void queryThumbnails(ContentResolver cr, String[] projection) {
        Cursor thumbCursor = null;
        try {
            thumbCursor = Images.Thumbnails.queryMiniThumbnails(cr, Images.Thumbnails.EXTERNAL_CONTENT_URI,
                    Images.Thumbnails.MINI_KIND, projection);
            if (thumbCursor != null) {
                while (thumbCursor.moveToNext()) {
                    String imageId = thumbCursor.getString(thumbCursor.getColumnIndex(Images.Thumbnails.IMAGE_ID));
                    String imagePath = thumbCursor.getString(thumbCursor.getColumnIndex(Images.Thumbnails.DATA));
                    mThumbnailMap.put(imageId, imagePath);
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "queryThumbnails " + e.getLocalizedMessage());
        } finally {
            if (thumbCursor != null) {
                thumbCursor.close();
            }
        }
    }

    private List<TXImageModel> buildAlbumList(ContentResolver cr, String bucketId, int page,
                                              @NonNull final TXImageTaskListener callback) {
        List<TXImageModel> result = new ArrayList<>();
        String columns[] = buildColumns();
        Cursor cursor = null;
        try {
            boolean isDefaultAlbum = TXAlbumModel.DEFAULT_ALBUM.equals(bucketId);
            boolean isNeedPaging = mPickerConfig == null || mPickerConfig.isNeedPaging();

            int totalCount = getTotalCount(cr, bucketId, columns, isDefaultAlbum);

            String order = isNeedPaging ?
                    Media.DATE_TAKEN + " DESC" + " LIMIT " + page * PAGE_LIMIT + " , " + PAGE_LIMIT :
                    Media.DATE_TAKEN + " DESC";

            cursor = buildCursor(cr, bucketId, columns, isDefaultAlbum, SELECTION_IMAGE_MIME_TYPE, SELECTION_ARGS_IMAGE_MIME_TYPE, order, SELECTION_ID);

            addItem(totalCount, result, cursor, callback);
        } catch (Exception e) {
            Log.d(TAG, "buildAlbumList " + e.getLocalizedMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return result;
    }

    @NonNull
    private String[] buildColumns() {
        String[] columns;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            columns = new String[]{Media._ID, Media.DATA, Media.DATE_TAKEN, Media.SIZE, Media.MIME_TYPE, Media.WIDTH, Media.HEIGHT};
        } else {
            columns = new String[]{Media._ID, Media.DATA, Media.DATE_TAKEN, Media.SIZE, Media.MIME_TYPE};
        }
        return columns;
    }

    /**
     * 查询图片总数
     *
     * @param cr
     * @param bucketId
     * @param columns
     * @param isDefaultAlbum 是否是默认相册
     * @return
     */
    private int getTotalCount(ContentResolver cr, String bucketId, String[] columns, boolean isDefaultAlbum) {
        Cursor allCursor = null;
        int result = 0;
        try {
            if (isDefaultAlbum) {
                allCursor = cr.query(Media.EXTERNAL_CONTENT_URI, columns,
                        SELECTION_IMAGE_MIME_TYPE, SELECTION_ARGS_IMAGE_MIME_TYPE,
                        Media.DATE_TAKEN + " DESC");
            } else {
                allCursor = cr.query(Media.EXTERNAL_CONTENT_URI, columns, SELECTION_ID,
                        new String[]{bucketId, "image/jpeg", "image/png", "image/jpg"},
                        Media.DATE_TAKEN + " DESC");
            }
            if (allCursor != null) {
                result = allCursor.getCount();
            }
        } catch (Exception e) {
            Log.d(TAG, "getTotalCount " + e.getLocalizedMessage());
        } finally {
            if (allCursor != null) {
                allCursor.close();
            }
        }
        return result;
    }

    private Cursor buildCursor(ContentResolver cr, String bucketId, String[] columns, boolean isDefaultAlbum,
                               String imageMimeType, String[] args, String order, String selectionId) {
        Cursor resultCursor;
        if (isDefaultAlbum) {
            resultCursor = cr.query(Media.EXTERNAL_CONTENT_URI, columns, imageMimeType,
                    args, order);
        } else {
            resultCursor = cr.query(Media.EXTERNAL_CONTENT_URI, columns, selectionId,
                    new String[]{bucketId, args[0], args[1], args[2]}, order);
        }
        return resultCursor;
    }

    private void addItem(int allCount, List<TXImageModel> result, Cursor cursor, @NonNull TXImageTaskListener listener) {
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String picPath = cursor.getString(cursor.getColumnIndex(Media.DATA));
                if (listener.needFilter(picPath)) {
                    Log.d(TAG, "path:" + picPath + " has been filter");
                } else {
                    String id = cursor.getString(cursor.getColumnIndex(Media._ID));
                    String size = cursor.getString(cursor.getColumnIndex(Media.SIZE));
                    String mimeType = cursor.getString(cursor.getColumnIndex(Media.MIME_TYPE));
                    long dataModify = cursor.getLong(cursor.getColumnIndex(Media.DATE_TAKEN));
                    int width = 0;
                    int height = 0;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        width = cursor.getInt(cursor.getColumnIndex(Media.WIDTH));
                        height = cursor.getInt(cursor.getColumnIndex(Media.HEIGHT));
                    }

                    TXImageModel imageItem = new TXImageModel(id, picPath);
                    imageItem.setThumbnailPath(mThumbnailMap.get(id));
                    imageItem.setFileSize(size);
                    imageItem.setMimeType(mimeType);
                    imageItem.setHeight(height);
                    imageItem.setWidth(width);
                    imageItem.setDate(new TXDate(dataModify));

                    if (!result.contains(imageItem)) {
                        result.add(imageItem);
                    }
                }
            }
            postMedias(result, allCount, listener, mBucketId);
        } else {
            postMedias(result, 0, listener, mBucketId);
        }
        release();
    }

    /**
     * 返回图片信息
     *
     * @param result
     * @param count
     * @param listener
     */
    private void postMedias(final List<TXImageModel> result, final int count, @NonNull final TXImageTaskListener listener, final String bucketId) {
        TXImageExecutor.getInstance().runUI(new Runnable() {
            @Override
            public void run() {
                listener.postImages(result, count, bucketId);
            }
        });
    }

    private void release() {
        if (mThumbnailMap != null) {
            mThumbnailMap.clear();
        }
    }
}
