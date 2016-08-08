package com.cc.stepview.chart;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Cheng on 16/7/11.
 */
public class ChartItem implements Parcelable {

    public float value;

    public String date;

    public boolean isDone;

    public float x;
    public float y;

    public float valueX;
    public float valueY;

    public float dateX;
    public float dateY;

    public ChartItem(float value, String date, boolean isDone) {
        this.value = value;
        this.date = date;
        this.isDone = isDone;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(this.value);
        dest.writeString(this.date);
        dest.writeByte(this.isDone ? (byte) 1 : (byte) 0);
        dest.writeFloat(this.x);
        dest.writeFloat(this.y);
        dest.writeFloat(this.valueX);
        dest.writeFloat(this.valueY);
        dest.writeFloat(this.dateX);
        dest.writeFloat(this.dateY);
    }

    protected ChartItem(Parcel in) {
        this.value = in.readFloat();
        this.date = in.readString();
        this.isDone = in.readByte() != 0;
        this.x = in.readFloat();
        this.y = in.readFloat();
        this.valueX = in.readFloat();
        this.valueY = in.readFloat();
        this.dateX = in.readFloat();
        this.dateY = in.readFloat();
    }

    public static final Parcelable.Creator<ChartItem> CREATOR = new Parcelable.Creator<ChartItem>() {
        @Override
        public ChartItem createFromParcel(Parcel source) {
            return new ChartItem(source);
        }

        @Override
        public ChartItem[] newArray(int size) {
            return new ChartItem[size];
        }
    };
}
