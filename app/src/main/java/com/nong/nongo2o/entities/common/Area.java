package com.nong.nongo2o.entities.common;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017-8-21.
 */

public class Area implements Parcelable {

    private int areaCode;
    private int parentCode;
    private String areaName;

    public int getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(int areaCode) {
        this.areaCode = areaCode;
    }

    public int getParentCode() {
        return parentCode;
    }

    public void setParentCode(int parentCode) {
        this.parentCode = parentCode;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.areaCode);
        dest.writeInt(this.parentCode);
        dest.writeString(this.areaName);
    }

    public Area() {
    }

    public Area(int areaCode, int parentCode, String areaName) {
        this.areaCode = areaCode;
        this.parentCode = parentCode;
        this.areaName = areaName;
    }

    protected Area(Parcel in) {
        this.areaCode = in.readInt();
        this.parentCode = in.readInt();
        this.areaName = in.readString();
    }

    public static final Parcelable.Creator<Area> CREATOR = new Parcelable.Creator<Area>() {
        @Override
        public Area createFromParcel(Parcel source) {
            return new Area(source);
        }

        @Override
        public Area[] newArray(int size) {
            return new Area[size];
        }
    };
}
