package com.cc.custom.gallery.listener;

import android.support.annotation.NonNull;
import android.widget.ImageView;

/**
 * 图片显示
 * <p>
 * Created by Cheng on 17/3/23.
 */
public interface TXImageLoaderListener {

    /**
     * display thumbnail images for a ImageView.
     *
     * @param img     the display ImageView. Through ImageView.getTag(R.R.id.tx_ids_image_tag) to get the absolute path of the exact path to display.
     * @param absPath the absolute path to display, may be out of date when fast scrolling.
     * @param width   the resize with for the image.
     * @param height  the resize height for the image.
     */
    void displayThumbnail(@NonNull ImageView img, @NonNull String absPath, int width, int height);

    /**
     * display raw images for a ImageView, need more work to do.
     *
     * @param img      the display ImageView.Through ImageView.getTag(R.string.app_name) to get the absolute path of the exact path to display.
     * @param absPath  the absolute path to display, may be out of date when fast scrolling.
     * @param width    width
     * @param height   height
     * @param listener the callback for the load result.
     */
    void displayRaw(@NonNull ImageView img, @NonNull String absPath, int width, int height, TXImageShowListener listener);
}
