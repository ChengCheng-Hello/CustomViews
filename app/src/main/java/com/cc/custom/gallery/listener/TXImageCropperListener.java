package com.cc.custom.gallery.listener;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.cc.custom.gallery.config.TXImagePickerCropperConfig;
import com.cc.custom.gallery.model.TXImageModel;


/**
 * 图片裁剪
 * <p>
 * Created by Cheng on 17/3/23.
 */
public interface TXImageCropperListener {

    /**
     * 开始裁剪
     *
     * @param activity    activity
     * @param cropConfig  裁剪参数
     * @param image       裁剪原图片
     * @param requestCode requestCode
     */
    void onStartCrop(@NonNull Activity activity, @NonNull TXImagePickerCropperConfig cropConfig, @NonNull TXImageModel image, int requestCode);

    /**
     * 裁剪结果
     *
     * @param resultCode requestCode
     * @param data       返回的Intent数据
     * @return 返回裁剪后图片
     */
    @Nullable
    TXImageModel onCropFinished(int resultCode, Intent data);
}
