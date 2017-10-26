package com.cc.custom.gallery.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 * 相册
 * <p>
 * Created by Cheng on 17/3/23.
 */
public class TXAlbumModel implements Parcelable {

    // 默认相册
    public static final String DEFAULT_ALBUM = "";

    // 相册id
    public String bucketId;
    // 相册名称
    public String bucketName;
    // 封面图片
    public TXImageModel coverImage;
    // 图片总数
    public int count;

    // 是否选中态
    public boolean isSelected;

    public TXAlbumModel() {
        this.bucketName = DEFAULT_ALBUM;
        this.isSelected = false;
    }

    /**
     * 创建默认相册
     */
    public static TXAlbumModel createDefaultAlbum(@NonNull Context context) {
        TXAlbumModel model = new TXAlbumModel();
        model.bucketId = DEFAULT_ALBUM;
//        model.bucketName = context.getString(R.string.tx_all_photo);
        model.isSelected = true;
        return model;
    }

    /**
     * 是否存在图片
     */
    public boolean hasImages() {
        return count > 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(bucketId);
        dest.writeString(bucketName);
        dest.writeInt(count);
        dest.writeByte((byte) (isSelected ? 1 : 0));
    }

    private TXAlbumModel(Parcel in) {
        bucketId = in.readString();
        bucketName = in.readString();
        count = in.readInt();
        isSelected = in.readByte() != 0;
    }

    public static final Creator<TXAlbumModel> CREATOR = new Creator<TXAlbumModel>() {
        @Override
        public TXAlbumModel createFromParcel(Parcel in) {
            return new TXAlbumModel(in);
        }

        @Override
        public TXAlbumModel[] newArray(int size) {
            return new TXAlbumModel[size];
        }
    };


    @Override
    public String toString() {
        return "TXAlbumModel{" +
                "bucketId='" + bucketId + '\'' +
                ", bucketName='" + bucketName + '\'' +
                ", count=" + count +
                ", isSelected=" + isSelected +
                '}';
    }
}
