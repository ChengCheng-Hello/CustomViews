package com.cc.custom.gallery.listener;

import android.support.annotation.Nullable;

import com.cc.custom.gallery.model.TXImageModel;

import java.util.List;

/**
 * 相册加载图片回调
 * <p>
 * Created by Cheng on 17/3/24.
 */
public interface TXImageTaskListener {

    /**
     * get a page of images in a album
     *
     * @param imageList page of images
     * @param count     the count for the photo in album
     */
    void postImages(@Nullable List<TXImageModel> imageList, int count, String bucketId);

    /**
     * judge the path needing filer
     *
     * @param path photo path
     * @return true:be filter
     */
    boolean needFilter(String path);
}
