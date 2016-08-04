package com.guuguo.androidlib.info;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by guodeqing on 16/6/2.
 */
public class BaseSimpleBackActivityInfo implements Parcelable {
    private int title;
    private Class<?> clz;
    private int value;

    protected BaseSimpleBackActivityInfo(int value, int title, Class<?> clz) {
        this.value = value;
        this.title = title;
        this.clz = clz;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public Class<?> getClz() {
        return clz;
    }

    public void setClz(Class<?> clz) {
        this.clz = clz;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }


    @Override
    public String toString() {
        return "BaseSimpleBackActivityInfo{" +
                "title=" + title +
                ", clz=" + clz +
                ", value=" + value +
                '}';
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.title);
        dest.writeSerializable(this.clz);
        dest.writeInt(this.value);
    }

    protected BaseSimpleBackActivityInfo(Parcel in) {
        this.title = in.readInt();
        this.clz = (Class<?>) in.readSerializable();
        this.value = in.readInt();
    }

    public static final Parcelable.Creator<BaseSimpleBackActivityInfo> CREATOR = new Parcelable.Creator<BaseSimpleBackActivityInfo>() {
        public BaseSimpleBackActivityInfo createFromParcel(Parcel source) {return new BaseSimpleBackActivityInfo(source);}

        public BaseSimpleBackActivityInfo[] newArray(int size) {return new BaseSimpleBackActivityInfo[size];}
    };
}

