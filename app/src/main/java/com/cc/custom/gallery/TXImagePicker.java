package com.cc.custom.gallery;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.cc.custom.gallery.config.TXImagePickerConfig;
import com.cc.custom.gallery.model.TXImageModel;

import java.util.ArrayList;

/**
 * 参考uCrop
 * Builder class to ease Intent setup.
 * <p>
 * Created by Cheng on 17/3/29.
 */
public class TXImagePicker {

    // 选图结果
    public static final String INTENT_RESULT = "TXImagePicker.intent.result";
    // 选中的图片
    public static final String INTENT_SELECTED_IMAGES = "TXImagePicker.intent.selected.images";
    // 选图配置
    public static final String INTENT_CONFIG = "TXImagePicker.intent.config";
    // 相册id
    public static final String INTENT_ALBUM_ID = "TXImagePicker.intent.album.id";
    // 预览起始位置
    public static final String INTENT_START_POS = "TXImagePicker.intent.start_pos";
    // 点击确认的返回
    public static final String INTENT_RESULT_CONFIRM = "TXImagePicker.intent.result.confirm";

    public static final String FILE_SCHEME = "file";
    public static final String FILE_URI = "file://";
    public static final String FILE_NAME_FMT = "picker_%s";

    private Intent mIntent;

    private TXImagePicker(TXImagePickerConfig config) {
        TXImagePickerManager.getInstance().setPickConfig(config);
        this.mIntent = new Intent();
    }

    /**
     * get the images result.
     */
    @Nullable
    public static ArrayList<TXImageModel> getResult(Intent data) {
        if (data != null) {
            return data.getParcelableArrayListExtra(INTENT_RESULT);
        }
        return null;
    }

    /**
     * call {@link #of(TXImagePickerConfig)} first to specify the mode otherwise {@link TXImagePickerConfig#MULTI_IMG} is used.<br/>
     */
    public static TXImagePicker get() {
        TXImagePickerConfig config = TXImagePickerManager.getInstance().getPickConfig();
        if (config == null) {
            config = new TXImagePickerConfig(TXImagePickerConfig.MULTI_IMG);
            TXImagePickerManager.getInstance().setPickConfig(config);
        }
        return new TXImagePicker(config);
    }

    /**
     * create a TXImagePicker entry.
     *
     * @param config {@link TXImagePickerConfig}
     */
    public static TXImagePicker of(TXImagePickerConfig config) {
        return new TXImagePicker(config);
    }

    /**
     * create a TXImagePicker entry.
     *
     * @param mode {@link TXImagePickerConfig.MODE}
     */
    public static TXImagePicker of(int mode) {
        return new TXImagePicker(new TXImagePickerConfig(mode));
    }

    /**
     * same as {@link Intent#setClass(Context, Class)}
     */
    public TXImagePicker withIntent(Context context, Class<?> cls) {
        return withIntent(context, cls, null);
    }

    /**
     * {@link Intent#setClass(Context, Class)} with input images.
     */
    public TXImagePicker withIntent(Context context, Class<?> cls, ArrayList<TXImageModel> selectedImages) {
        mIntent.setClass(context, cls);
        if (selectedImages != null && !selectedImages.isEmpty()) {
            mIntent.putExtra(INTENT_SELECTED_IMAGES, selectedImages);
        }
        return this;
    }

    /**
     * use to start image viewer.
     *
     * @param imageList selected medias.
     * @param pos       the start position.
     * @param albumId   the specify album id.
     */
    public TXImagePicker withIntent(Context context, Class<?> cls, ArrayList<TXImageModel> imageList, int pos, String albumId) {
        mIntent.setClass(context, cls);
        if (imageList != null && !imageList.isEmpty()) {
            mIntent.putExtra(INTENT_SELECTED_IMAGES, imageList);
        }
        if (pos >= 0) {
            mIntent.putExtra(INTENT_START_POS, pos);
        }
        if (albumId != null) {
            mIntent.putExtra(INTENT_ALBUM_ID, albumId);
        }
        return this;
    }

    /**
     * same as {@link Activity#startActivity(Intent)}
     */
    public void start(@NonNull Activity activity) {
        activity.startActivity(mIntent);
    }

    /**
     * same as {@link Activity#startActivityForResult(Intent, int)}
     */
    public void start(@NonNull Activity activity, int requestCode) {
        activity.startActivityForResult(mIntent, requestCode);
    }

    /**
     * same as {@link Fragment#startActivityForResult(Intent, int)}
     */
    public void start(@NonNull Fragment fragment, int requestCode) {
        fragment.startActivityForResult(mIntent, requestCode);
    }

    /**
     * use to start raw image viewer.
     *
     * @param viewMode {@link TXImagePickerConfig#viewMode}
     */
    public void start(@NonNull Activity activity, int requestCode, int viewMode) {
        TXImagePickerManager.getInstance().getPickConfig().withViewer(viewMode);
        activity.startActivityForResult(mIntent, requestCode);
    }
}
