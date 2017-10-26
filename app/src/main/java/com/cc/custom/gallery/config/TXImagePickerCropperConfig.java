package com.cc.custom.gallery.config;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 * 选图裁剪配置
 * <p>
 * Created by Cheng on 17/3/24.
 */
public class TXImagePickerCropperConfig implements Parcelable {

    // 裁剪后图片的Uri
    private Uri destination;
    // 比例 x
    private float aspectRatioX;
    // 比例 y
    private float aspectRatioY;

    public TXImagePickerCropperConfig(Uri destination) {
        this.destination = destination;
    }

    public static TXImagePickerCropperConfig with(@NonNull Uri destination) {
        return new TXImagePickerCropperConfig(destination);
    }

    public TXImagePickerCropperConfig aspectRatio(float x, float y) {
        this.aspectRatioX = x;
        this.aspectRatioY = y;
        return this;
    }

    public Uri getDestination() {
        return destination;
    }

    public float getAspectRatioX() {
        return aspectRatioX;
    }

    public float getAspectRatioY() {
        return aspectRatioY;
    }

    @Override
    public String toString() {
        return "TXImagePickerCropperConfig{" +
                "destination=" + destination +
                ", aspectRatioX=" + aspectRatioX +
                ", aspectRatioY=" + aspectRatioY +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(destination, flags);
        dest.writeFloat(aspectRatioX);
        dest.writeFloat(aspectRatioY);
    }

    protected TXImagePickerCropperConfig(Parcel in) {
        destination = in.readParcelable(Uri.class.getClassLoader());
        aspectRatioX = in.readFloat();
        aspectRatioY = in.readFloat();
    }

    public static final Creator<TXImagePickerCropperConfig> CREATOR = new Creator<TXImagePickerCropperConfig>() {
        @Override
        public TXImagePickerCropperConfig createFromParcel(Parcel in) {
            return new TXImagePickerCropperConfig(in);
        }

        @Override
        public TXImagePickerCropperConfig[] newArray(int size) {
            return new TXImagePickerCropperConfig[size];
        }
    };
}
