package com.cc.custom.gallery.listener;

import android.support.annotation.Nullable;

import com.cc.custom.gallery.model.TXVideoModel;

import java.util.List;

/**
 * 相册加载视频回调
 * <p>
 * Created by Cheng on 2017/10/25.
 */
public interface TXVideoTaskListener {

    /**
     * get a page of videos in a album
     *
     * @param videoList page of videos 
     * @param count     the count for the video in album
     */
    void postImages(@Nullable List<TXVideoModel> videoList, int count, String bucketId);

    /**
     * judge the path needing filer
     *
     * @param path video path
     * @return true:be filter
     */
    boolean needFilter(String path);
}
