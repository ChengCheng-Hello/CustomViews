package com.cc.custom.gallery.task;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore.Images.Media;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.util.Log;

import com.cc.custom.TXContextManager;
import com.cc.custom.gallery.listener.TXAlbumTaskListener;
import com.cc.custom.gallery.model.TXAlbumModel;
import com.cc.custom.gallery.model.TXImageModel;
import com.cc.custom.gallery.utils.TXImageExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 加载相册任务
 * <p>
 * Created by Cheng on 17/3/24.
 */
@WorkerThread
public class TXAlbumTask {

    private static final String TAG = "TXAlbumTask";

    private static final String UNKNOWN_ALBUM_NAME = "unknow";
    private static final String SELECTION_IMAGE_MIME_TYPE = Media.MIME_TYPE + "=? or " + Media.MIME_TYPE + "=? or " + Media.MIME_TYPE + "=?";
    private static final String SELECTION_ID = Media.BUCKET_ID + "=? and (" + SELECTION_IMAGE_MIME_TYPE + " )";

    private int mUnknownAlbumNumber = 1;
    private Map<String, TXAlbumModel> mBucketMap;
    private TXAlbumModel mDefaultAlbum;

    public TXAlbumTask() {
        mBucketMap = new ArrayMap<>();
    }

    /**
     * 加载相册
     *
     * @param listener 回调
     */
    public void load(@NonNull TXAlbumTaskListener listener) {
        Context context = TXContextManager.getInstance().getApplicationContext();
        if (context == null) {
            return;
        }

        mDefaultAlbum = TXAlbumModel.createDefaultAlbum(context);

        ContentResolver contentResolver = context.getContentResolver();

        // 默认相册总数
        buildDefaultAlbum(contentResolver);
        // 相册信息
        buildAlbumInfo(contentResolver);
        // 整理数据，返回
        getAlbumList(listener);
    }

    /**
     * 默认相册 图片数
     *
     * @param cr
     */
    private void buildDefaultAlbum(ContentResolver cr) {
        Cursor cursor = null;
        try {
            cursor = cr.query(Media.EXTERNAL_CONTENT_URI, new String[]{Media.BUCKET_ID}, null, null, null);
            if (cursor != null) {
                mDefaultAlbum.count = cursor.getCount();
            }
        } catch (Exception e) {
            Log.d(TAG, "buildDefaultAlbum " + e.getLocalizedMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    /**
     * 构建相册信息
     *
     * @param cr
     */
    private void buildAlbumInfo(ContentResolver cr) {
        String[] distinctBucketColumns = new String[]{Media.BUCKET_ID, Media.BUCKET_DISPLAY_NAME};
        Cursor bucketCursor = null;
        try {
            bucketCursor = cr.query(Media.EXTERNAL_CONTENT_URI, distinctBucketColumns, "0==0)" + " GROUP BY(" + Media.BUCKET_ID,
                    null, Media.DATE_MODIFIED + " DESC");
            if (bucketCursor != null) {
                while (bucketCursor.moveToNext()) {
                    String buckId = bucketCursor.getString(bucketCursor.getColumnIndex(Media.BUCKET_ID));
                    String name = bucketCursor.getString(bucketCursor.getColumnIndex(Media.BUCKET_DISPLAY_NAME));
                    TXAlbumModel album = buildAlbumInfo(buckId, name);
                    if (!TextUtils.isEmpty(buckId) && album.coverImage == null) {
                        buildAlbumCover(cr, buckId, album);
                    }
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "buildAlbumInfo " + e.getLocalizedMessage());
        } finally {
            if (bucketCursor != null) {
                bucketCursor.close();
            }
        }
    }

    /**
     * 整理相册，设置默认相册封面
     *
     * @param listener
     */
    private void getAlbumList(@NonNull TXAlbumTaskListener listener) {
        List<TXAlbumModel> tmpList = new ArrayList<>();
        // 无相册
        if (mBucketMap == null) {
            postAlbums(listener, tmpList);
            release();
            return;
        }

        for (Map.Entry<String, TXAlbumModel> entry : mBucketMap.entrySet()) {
            tmpList.add(entry.getValue());
        }

        // 默认相册封面
        if (tmpList.size() > 0) {
            TXAlbumModel album = tmpList.get(0);
            if (album != null) {
                mDefaultAlbum.coverImage = album.coverImage;
            }
            tmpList.add(0, mDefaultAlbum);
        }

        postAlbums(listener, tmpList);
        release();
    }

    /**
     * 返回相册信息
     *
     * @param listener
     * @param albumList
     */
    private void postAlbums(@NonNull final TXAlbumTaskListener listener, final List<TXAlbumModel> albumList) {
        TXImageExecutor.getInstance().runUI(new Runnable() {
            @Override
            public void run() {
                listener.postAlbumList(albumList);
            }
        });
    }

    /**
     * 构建相册封面
     *
     * @param cr
     * @param buckId
     * @param album
     */
    private void buildAlbumCover(ContentResolver cr, String buckId, TXAlbumModel album) {
        String[] photoColumn = new String[]{Media._ID, Media.DATA};
        Cursor coverCursor = cr.query(Media.EXTERNAL_CONTENT_URI, photoColumn, SELECTION_ID,
                new String[]{buckId, "image/jpeg", "image/png", "image/jpg"},
                Media.DATE_MODIFIED + " DESC");
        try {
            if (coverCursor != null && coverCursor.moveToFirst()) {
                String picPath = coverCursor.getString(coverCursor.getColumnIndex(Media.DATA));
                String id = coverCursor.getString(coverCursor.getColumnIndex(Media._ID));
                album.count = coverCursor.getCount();
                album.coverImage = new TXImageModel(id, picPath);
                mBucketMap.put(buckId, album);
            }
        } finally {
            if (coverCursor != null) {
                coverCursor.close();
            }
        }
    }

    /**
     * 构建相册信息
     *
     * @param bucketId
     * @param bucketName
     */
    @NonNull
    private TXAlbumModel buildAlbumInfo(String bucketId, String bucketName) {
        TXAlbumModel album = null;

        if (!TextUtils.isEmpty(bucketId)) {
            album = mBucketMap.get(bucketId);
        }

        if (album == null) {
            album = new TXAlbumModel();

            if (TextUtils.isEmpty(bucketId)) {
                album.bucketId = String.valueOf(mUnknownAlbumNumber);
                mUnknownAlbumNumber++;
            } else {
                album.bucketId = bucketId;
            }

            if (TextUtils.isEmpty(bucketName)) {
                album.bucketName = UNKNOWN_ALBUM_NAME;
                mUnknownAlbumNumber++;
            } else {
                album.bucketName = bucketName;
            }

            if (album.hasImages()) {
                mBucketMap.put(bucketId, album);
            }
        }
        return album;
    }

    private void release() {
        if (mBucketMap != null) {
            mBucketMap.clear();
        }
    }
}