package com.cc.custom.gallery.listener;

import android.support.annotation.Nullable;

import com.cc.custom.gallery.model.TXAlbumModel;

import java.util.List;


/**
 * 加载相册回调
 * <p>
 * Created by Cheng on 17/3/24.
 */
public interface TXAlbumTaskListener {

    /**
     * get all album in database
     *
     * @param list album list
     */
    void postAlbumList(@Nullable List<TXAlbumModel> list);
}
