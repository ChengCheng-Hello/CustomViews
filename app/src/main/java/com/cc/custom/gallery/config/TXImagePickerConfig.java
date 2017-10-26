package com.cc.custom.gallery.config;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 选图配置
 * <p>
 * Created by Cheng on 17/3/24.
 */
public class TXImagePickerConfig implements Parcelable {

    public static final int DEFAULT_SELECTION_COUNT = 9;

    @IntDef({SINGLE_IMG, MULTI_IMG, CAMERA})
    @Retention(RetentionPolicy.SOURCE)
    public @interface MODE {
    }

    // 单选
    public static final int SINGLE_IMG = 0;
    // 多选
    public static final int MULTI_IMG = 1;
    // 相机
    public static final int CAMERA = 2;

    @IntDef({PREVIEW, EDIT, PREVIEW_EDIT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface VIEW_MODE {
    }

    // 单选预览－已选相册内预览
    public static final int PREVIEW = 0;
    // 多选预览－已选相册内预览
    public static final int EDIT = 1;
    // 多选预览－已选图片内预览－不用查询
    public static final int PREVIEW_EDIT = 2;

    private int mode = SINGLE_IMG;
    private int viewMode = PREVIEW;
    private TXImagePickerCropperConfig cropConfig;

    private boolean needPaging = true;

    private int maxCount = DEFAULT_SELECTION_COUNT;

    public TXImagePickerConfig() {
    }

    public TXImagePickerConfig(@MODE int model) {
        this.mode = model;
    }

    @MODE
    public int getMode() {
        return mode;
    }

    @VIEW_MODE
    public int getViewMode() {
        return viewMode;
    }

    public TXImagePickerCropperConfig getCropConfig() {
        return cropConfig;
    }

    public boolean isNeedPaging() {
        return needPaging;
    }

    /**
     * 多选预览－已选图片内预览－不用查询-不需要loading
     *
     * @return
     */
    public boolean isNeedLoading() {
        return viewMode != PREVIEW_EDIT;
    }

    public boolean isCamera() {
        return mode == CAMERA;
    }

    public boolean isMultiImageMode() {
        return mode == MULTI_IMG;
    }

    public int getMaxCount() {
        if (maxCount > 0) {
            return maxCount;
        }

        return DEFAULT_SELECTION_COUNT;
    }

    public TXImagePickerConfig needPaging() {
        this.needPaging = true;
        return this;
    }

    public TXImagePickerConfig withViewer(int viewModel) {
        this.viewMode = viewModel;
        return this;
    }

    public TXImagePickerConfig withCropConfig(TXImagePickerCropperConfig cropConfig) {
        this.cropConfig = cropConfig;
        return this;
    }

    public TXImagePickerConfig withMaxCount(int count) {
        if (count < 1) {
            return this;
        }
        this.maxCount = count;
        return this;
    }

    @Override
    public String toString() {
        return "TXImagePickerConfig{" +
                "mode=" + mode +
                ", viewMode=" + viewMode +
                ", cropConfig=" + cropConfig +
                ", needPaging=" + needPaging +
                ", maxCount=" + maxCount +
                '}';
    }

    protected TXImagePickerConfig(Parcel in) {
        mode = in.readInt();
        viewMode = in.readInt();
        cropConfig = in.readParcelable(TXImagePickerCropperConfig.class.getClassLoader());
        needPaging = in.readByte() != 0;
        maxCount = in.readInt();
    }

    public static final Creator<TXImagePickerConfig> CREATOR = new Creator<TXImagePickerConfig>() {
        @Override
        public TXImagePickerConfig createFromParcel(Parcel in) {
            return new TXImagePickerConfig(in);
        }

        @Override
        public TXImagePickerConfig[] newArray(int size) {
            return new TXImagePickerConfig[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mode);
        dest.writeInt(viewMode);
        dest.writeParcelable(cropConfig, flags);
        dest.writeByte((byte) (needPaging ? 1 : 0));
        dest.writeInt(maxCount);
    }
}
