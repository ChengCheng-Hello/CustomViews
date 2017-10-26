package com.cc.custom.gallery.model;

import com.cc.custom.TXDate;

/**
 * 视频
 * <p>
 * Created by Cheng on 2017/10/25.
 */
public class TXVideoModel {

    private static final String TAG = "TXVideoModel";

    private String id;
    private String filePath;
    private String fileSize;
    private String duration;
    private String mimeType;
    private TXDate date;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public TXDate getDate() {
        return date;
    }

    public void setDate(TXDate date) {
        this.date = date;
    }
}
