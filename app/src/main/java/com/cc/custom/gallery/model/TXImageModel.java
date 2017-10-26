package com.cc.custom.gallery.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.cc.custom.TXDate;
import com.cc.custom.gallery.utils.TXImageUtils;

import java.io.File;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 图片
 * <p>
 * Created by Cheng on 17/3/23.
 */
public class TXImageModel implements Parcelable {

    private static final String TAG = "TXImageModel";

    @IntDef({PNG, JPG})
    @Retention(RetentionPolicy.SOURCE)
    public @interface IMAGE_TYPE {
    }

    public static final int PNG = 0;
    public static final int JPG = 1;

    // 图片id
    private String id;
    // 图片路径
    private String filePath;
    // 图片大小
    private String fileSize;

    // 缩略图路径－可能不存在
    private String thumbnailPath;
    // 压缩图路径－可能不存在
    private String compressPath;

    // 图片高度
    private int height;
    // 图片宽度
    private int width;
    // 图片类型
    private int imageType;
    // 图片mimeType
    private String mimeType;
    // 拍照的日期
    private TXDate date;

    // 是否选中态
    private boolean isSelected;

    // 当前位置
    private int currentPosition;

    public TXImageModel() {
    }

    public TXImageModel(String id, String filePath) {
        this.id = id;
        this.filePath = filePath;
    }

    public TXImageModel(@NonNull File file) {
        this.id = String.valueOf(System.currentTimeMillis());
        this.filePath = file.getAbsolutePath();
        this.fileSize = String.valueOf(file.length());
    }

    public String getId() {
        return id;
    }

    public String getFilePath() {
        return filePath;
    }

    public long getFileSize() {
        try {
            long size = Long.parseLong(fileSize);
            return size > 0 ? size : 0;
        } catch (NumberFormatException e) {
            Log.d(TAG, "getFileSize NumberFormatException " + e.getLocalizedMessage());
            return 0;
        }
    }

    /**
     * 获取缩略图路径
     *
     * @return 优先级 缩略图路径 > 压缩图路径 > 原图路径
     */
    public String getThumbnailPath() {
        if (TXImageUtils.isFileValid(thumbnailPath)) {
            return thumbnailPath;
        } else if (TXImageUtils.isFileValid(compressPath)) {
            return compressPath;
        }
        return filePath;
    }

    public String getCompressPath() {
        if (TXImageUtils.isFileValid(compressPath)) {
            return compressPath;
        }
        return filePath;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    @IMAGE_TYPE
    public int getImageType() {
        if (getMimeType().contains("png")) {
            return PNG;
        }
        return JPG;
    }

    public String getMimeType() {
        if (TextUtils.isEmpty(mimeType)) {
            return "image/jpeg";
        }
        return mimeType;
    }

    public boolean isPNG() {
        return getImageType() == PNG;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }

    public void setCompressPath(String compressPath) {
        this.compressPath = compressPath;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public TXDate getDate() {
        return date;
    }

    public void setDate(TXDate date) {
        this.date = date;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    /**
     * save image to MediaStore.
     */
//    public void saveMediaStore(final ContentResolver cr) {
//        TXImageExecutor.getInstance().runWorker(new Runnable() {
//            @Override
//            public void run() {
//                if (cr != null && !TextUtils.isEmpty(getId())) {
//                    ContentValues values = new ContentValues();
//                    values.put(MediaStore.Images.Media.TITLE, getId());
//                    values.put(MediaStore.Images.Media.MIME_TYPE, getMimeType());
//                    values.put(MediaStore.Images.Media.DATA, getFilePath());
//                    cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
//                }
//            }
//        });
//    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(filePath);
        dest.writeString(fileSize);
        dest.writeString(thumbnailPath);
        dest.writeString(compressPath);
        dest.writeInt(height);
        dest.writeInt(width);
        dest.writeInt(imageType);
        dest.writeByte((byte) (isSelected ? 1 : 0));
        dest.writeInt(currentPosition);
    }

    protected TXImageModel(Parcel in) {
        id = in.readString();
        filePath = in.readString();
        fileSize = in.readString();
        thumbnailPath = in.readString();
        compressPath = in.readString();
        height = in.readInt();
        width = in.readInt();
        imageType = in.readInt();
        isSelected = in.readByte() != 0;
        currentPosition = in.readInt();
    }

    public static final Creator<TXImageModel> CREATOR = new Creator<TXImageModel>() {
        @Override
        public TXImageModel createFromParcel(Parcel in) {
            return new TXImageModel(in);
        }

        @Override
        public TXImageModel[] newArray(int size) {
            return new TXImageModel[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TXImageModel that = (TXImageModel) o;

        return filePath != null ? filePath.equals(that.filePath) : that.filePath == null;

    }

    @Override
    public int hashCode() {
        return filePath != null ? filePath.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "TXImageModel{" +
                "id='" + id + '\'' +
                ", filePath='" + filePath + '\'' +
                ", fileSize='" + fileSize + '\'' +
                ", thumbnailPath='" + thumbnailPath + '\'' +
                ", compressPath='" + compressPath + '\'' +
                ", height=" + height +
                ", width=" + width +
                ", imageType=" + imageType +
                ", isSelected=" + isSelected +
                ", currentPosition=" + currentPosition +
                '}';
    }
}
