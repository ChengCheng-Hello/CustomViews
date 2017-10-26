package com.cc.custom.gallery.presenter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.cc.custom.gallery.config.TXImagePickerConfig;
import com.cc.custom.gallery.model.TXAlbumModel;
import com.cc.custom.gallery.model.TXImageModel;

import java.util.List;

/**
 * 图片选择契约
 * <p>
 * Created by Cheng on 17/3/24.
 */
public interface TXImagePickerContract {

    interface View {

        void setPresenter(@NonNull Presenter presenter);

        // 显示相册集
        void showAlbums(@Nullable List<TXAlbumModel> albumList);

        // 显示图片集
        void showImages(@Nullable List<TXImageModel> imageList, int allCount);

        // 选完图片处理
        void onFinished(@NonNull List<TXImageModel> imageList);

        // 清楚当前页面图片
        void clearImages();

        // 裁剪
        void startCrop(@NonNull TXImageModel image, int requestCode);

        // 设置裁剪配置
        void setPickerConfig(TXImagePickerConfig pickerConfig);
    }

    interface Presenter {

        // 加载相册集
        void loadAlbums();

        // 加载相册内图片
        void loadImages(String albumId, int page);

        // 是否有下一页
        boolean hasNextPage();

        // 是否可以加载下一页，如当前正在loading，不需要重复加载
        boolean canLoadNextPage();

        // 加载下一页
        void onLoadNextPage();

        // 检查处理选中数据
        void checkSelectedImages(List<TXImageModel> allImageList, List<TXImageModel> selectedImageList);

        // 选中列表重置位置
        void resetPosition(@NonNull List<TXImageModel> selectedImageList);
    }
}
