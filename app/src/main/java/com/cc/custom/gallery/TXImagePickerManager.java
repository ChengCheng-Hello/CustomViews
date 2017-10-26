package com.cc.custom.gallery;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;

import com.cc.custom.gallery.config.TXImagePickerConfig;
import com.cc.custom.gallery.config.TXImagePickerCropperConfig;
import com.cc.custom.gallery.listener.TXAlbumTaskListener;
import com.cc.custom.gallery.listener.TXImageCropperListener;
import com.cc.custom.gallery.listener.TXImageLoaderListener;
import com.cc.custom.gallery.listener.TXImageShowListener;
import com.cc.custom.gallery.listener.TXImageTaskListener;
import com.cc.custom.gallery.model.TXImageModel;
import com.cc.custom.gallery.task.TXAlbumTask;
import com.cc.custom.gallery.task.TXImageTask;
import com.cc.custom.gallery.utils.TXImageExecutor;

/**
 * 选图Manager
 * <p>
 * 需要调用{@link #init(TXImageLoaderListener, TXImageCropperListener)}进行初始化
 * <p>
 * Created by Cheng on 17/3/24.
 */
public class TXImagePickerManager {

    private static final String TAG = "TXImagePickerManager";

    private TXImagePickerConfig mPickConfig;
    private TXImageLoaderListener mLoader;
    private TXImageCropperListener mCropLoader;

    private TXImagePickerManager() {
    }

    private static final class InnerHolder {
        private static final TXImagePickerManager INSTANCE = new TXImagePickerManager();
    }

    public static TXImagePickerManager getInstance() {
        return InnerHolder.INSTANCE;
    }

    /**
     * 初始化配置
     *
     * @param loader     图片加载回调
     * @param cropLoader 图片裁剪回调
     */
    public void init(@NonNull TXImageLoaderListener loader, @NonNull TXImageCropperListener cropLoader) {
        this.mLoader = loader;
        this.mCropLoader = cropLoader;
    }

    public TXImagePickerConfig getPickConfig() {
        return mPickConfig;
    }

    public void setPickConfig(TXImagePickerConfig mPickConfig) {
        this.mPickConfig = mPickConfig;
    }

    /**
     * 加载相册内图片
     *
     * @param albumId
     * @param page
     * @param listener
     */
    public void loadImages(final String albumId, final int page, @NonNull final TXImageTaskListener listener) {
        TXImageExecutor.getInstance().runWorker(new Runnable() {
            @Override
            public void run() {
                new TXImageTask().load(albumId, page, listener);
            }
        });
    }

    /**
     * 加载相册
     *
     * @param listener
     */
    public void loadAlbum(@NonNull final TXAlbumTaskListener listener) {
        TXImageExecutor.getInstance().runWorker(new Runnable() {
            @Override
            public void run() {
                new TXAlbumTask().load(listener);
            }
        });
    }

    /**
     * display thumbnail images for a ImageView.
     *
     * @param img     the display ImageView. Through ImageView.getTag(R.R.id.tx_ids_image_tag) to get the absolute path of the exact path to display.
     * @param absPath the absolute path to display, may be out of date when fast scrolling.
     * @param width   the resize with for the image.
     * @param height  the resize height for the image.
     */
    public void displayThumbnail(@NonNull ImageView img, @NonNull String absPath, int width, int height) {
        if (checkInit()) {
            return;
        }

        mLoader.displayThumbnail(img, absPath, width, height);
    }

    /**
     * display raw images for a ImageView, need more work to do.
     *
     * @param img      the display ImageView.Through ImageView.getTag(R.string.app_name) to get the absolute path of the exact path to display.
     * @param absPath  the absolute path to display, may be out of date when fast scrolling.
     * @param width    width
     * @param height   height
     * @param listener the callback for the load result.
     */
    public void displayRaw(@NonNull ImageView img, @NonNull String absPath, int width, int height, TXImageShowListener listener) {
        if (checkInit()) {
            return;
        }

        mLoader.displayRaw(img, absPath, width, height, listener);
    }

    /**
     * 开始裁剪
     *
     * @param activity
     * @param cropConfig
     * @param image
     * @param requestCode
     */
    public void onStartCrop(@NonNull Activity activity, @NonNull TXImagePickerCropperConfig cropConfig, @NonNull TXImageModel image, int requestCode) {
        if (checkInit()) {
            return;
        }

        mCropLoader.onStartCrop(activity, cropConfig, image, requestCode);
    }

    /**
     * 裁剪结果
     *
     * @param resultCode
     * @param data
     * @return
     */
    @Nullable
    public TXImageModel onCropFinished(int resultCode, Intent data) {
        if (checkInit()) {
            return null;
        }

        return mCropLoader.onCropFinished(resultCode, data);
    }

    private boolean checkInit() {
        boolean initFail = mLoader == null || mCropLoader == null;
        if (initFail) {
            Log.d(TAG, "not init TXImagePickerManager");
        }
        return initFail;
    }
}
