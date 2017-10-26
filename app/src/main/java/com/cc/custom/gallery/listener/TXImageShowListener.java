package com.cc.custom.gallery.listener;

import android.support.annotation.Nullable;

/**
 * 图片显示
 * <p>
 * Created by Cheng on 17/3/23.
 */
public interface TXImageShowListener {

    /**
     * 成功的回调
     */
    void onSuccess();

    /**
     * 失败的回调
     */
    void onFailure(@Nullable Throwable t);
}
