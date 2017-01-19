package com.kostya.serializable;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * @author Kostya  on 10.07.2016.
 */
public class MyObject implements Parcelable {

    static final String LOG_TAG = "myLogs";

    public final String s;
    public final int i;

    // обычный конструктор
    public MyObject(String _s, int _i) {
        Log.d(LOG_TAG, "MyObject(String _s, int _i)");
        s = _s;
        i = _i;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // упаковываем объект в Parcel
    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        Log.d(LOG_TAG, "writeToParcel");
        parcel.writeString(s);
        parcel.writeInt(i);
    }

    public static final Creator<MyObject> CREATOR = new Creator<MyObject>() {
        // распаковываем объект из Parcel
        @Override
        public MyObject createFromParcel(Parcel in) {
            Log.d(LOG_TAG, "createFromParcel");
            return new MyObject(in);
        }

        @Override
        public MyObject[] newArray(int size) {
            return new MyObject[size];
        }
    };

    // конструктор, считывающий данные из Parcel
    private MyObject(Parcel parcel) {
        Log.d(LOG_TAG, "MyObject(Parcel parcel)");
        s = parcel.readString();
        i = parcel.readInt();
    }

}