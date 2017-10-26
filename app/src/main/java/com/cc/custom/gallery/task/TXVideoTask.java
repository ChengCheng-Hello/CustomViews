package com.cc.custom.gallery.task;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.provider.MediaStore.Video;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;
import android.util.Log;

import com.cc.custom.TXContextManager;
import com.cc.custom.TXDate;
import com.cc.custom.gallery.listener.TXVideoTaskListener;
import com.cc.custom.gallery.model.TXAlbumModel;
import com.cc.custom.gallery.model.TXVideoModel;
import com.cc.custom.gallery.utils.TXImageExecutor;

import java.util.ArrayList;
import java.util.List;

/**
 * 加载相册内视频任务
 * <p>
 * Created by Cheng on 2017/10/25.
 */
@WorkerThread
public class TXVideoTask {

    private static final String TAG = "TXVideoTask";

    /**
     * 分页大小
     */
    public static final int PAGE_LIMIT = 1000;

    private static final String[] COLUMNS = new String[] { Video.Media.DATA, Video.Media._ID, Video.Media.MIME_TYPE,
        Video.Media.SIZE, Video.Media.DATE_TAKEN, Video.Media.DURATION };
    private static final String SELECTION_ID = MediaStore.Images.Media.BUCKET_ID + "=?";

    private String mBucketId;

    public void load(String bucketId, int page, @NonNull TXVideoTaskListener listener) {
        Context context = TXContextManager.getInstance().getApplicationContext();
        if (context == null) {
            return;
        }

        mBucketId = bucketId;

        // 构建图片
        buildAlbumList(context.getContentResolver(), bucketId, page, listener);
    }

    private List<TXVideoModel> buildAlbumList(ContentResolver cr, String bucketId, int page,
        @NonNull final TXVideoTaskListener callback) {

        List<TXVideoModel> result = new ArrayList<>();
        Cursor cursor = null;
        try {
            boolean isDefaultAlbum = TXAlbumModel.DEFAULT_ALBUM.equals(bucketId);
            int totalCount = getTotalCount(cr, bucketId, COLUMNS, isDefaultAlbum);
            String order = Video.Media.DATE_TAKEN + " DESC" + " LIMIT " + page * PAGE_LIMIT + " , " + PAGE_LIMIT;
            cursor = buildCursor(cr, bucketId, COLUMNS, isDefaultAlbum, order, SELECTION_ID);
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

    /**
     * 查询总数
     *
     * @param cr
     * @param bucketId
     * @param columns
     * @param isDefaultAlbum 是否是默认相册
     */
    private int getTotalCount(ContentResolver cr, String bucketId, String[] columns, boolean isDefaultAlbum) {
        Cursor allCursor = null;
        int result = 0;
        try {
            if (isDefaultAlbum) {
                allCursor =
                    cr.query(Video.Media.EXTERNAL_CONTENT_URI, columns, null, null, Video.Media.DATE_TAKEN + " DESC");
            } else {
                allCursor = cr.query(Video.Media.EXTERNAL_CONTENT_URI, columns, SELECTION_ID, new String[] { bucketId },
                    Video.Media.DATE_TAKEN + " DESC");
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
        String order, String selectionId) {

        Cursor resultCursor;
        if (isDefaultAlbum) {
            resultCursor = cr.query(Video.Media.EXTERNAL_CONTENT_URI, columns, null, null, order);
        } else {
            resultCursor =
                cr.query(Video.Media.EXTERNAL_CONTENT_URI, columns, selectionId, new String[] { bucketId }, order);
        }
        return resultCursor;
    }

    private void addItem(int allCount, List<TXVideoModel> result, Cursor cursor,
        @NonNull TXVideoTaskListener listener) {

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String videoPath = cursor.getString(cursor.getColumnIndex(Video.Media.DATA));
                if (listener.needFilter(videoPath)) {
                    Log.d(TAG, "path:" + videoPath + " has been filter");
                } else {
                    String id = cursor.getString(cursor.getColumnIndex(Video.Media._ID));
                    String size = cursor.getString(cursor.getColumnIndex(Video.Media.SIZE));
                    String mimeType = cursor.getString(cursor.getColumnIndex(Video.Media.MIME_TYPE));
                    String duration = cursor.getString(cursor.getColumnIndex(Video.Media.DURATION));
                    long dataModify = cursor.getLong(cursor.getColumnIndex(Video.Media.DATE_TAKEN));

                    TXVideoModel videoModel = new TXVideoModel();
                    videoModel.setId(id);
                    videoModel.setFilePath(videoPath);
                    videoModel.setFileSize(size);
                    videoModel.setMimeType(mimeType);
                    videoModel.setDuration(duration);
                    videoModel.setDate(new TXDate(dataModify));

                    if (!result.contains(videoModel)) {
                        result.add(videoModel);
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
     * 返回信息
     *
     * @param result
     * @param count
     * @param listener
     */
    private void postMedias(final List<TXVideoModel> result, final int count,
        @NonNull final TXVideoTaskListener listener, final String bucketId) {

        TXImageExecutor.getInstance().runUI(new Runnable() {
            @Override
            public void run() {
                listener.postImages(result, count, bucketId);
            }
        });
    }

    private void release() {
    }
}
