package com.god.magic;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.DateFormat;

import java.io.ByteArrayOutputStream;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * HISTORY
 * 	Date			Author				Comments
 * 	23.02.2017		Eduard Arefjev 		ToDoStructure
 */

class ToDoList implements Parcelable{
    //EA init variables
    public int id;
    public String name;
    Date date;
    boolean check;
    String info;
    Bitmap photo = null;

    //EA Constructors
    ToDoList(){
    }

    ToDoList(String name, String info, boolean check, Date date, Bitmap photo) {
        this.name = name;
        this.info = info;
        this.check = check;
        this.date = date;
        this.photo = photo;
    }

    ToDoList(int id, String name, String info, boolean check, Date date, Bitmap photo) {
        this.id = id;
        this.name = name;
        this.check = check;
        this.info = info;
        this.date = date;
        this.photo = photo;
    }

    //EA ID
    public int getId(){
        return this.id;
    }

    public void setId(int id){
        this.id = id;
    }

    //EA Name
    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    //EA Checkbox
    boolean getSelected(){
        return this.check;
    }

    void setSelected(boolean check){
        this.check = check;
    }

    //EA Date
    Date getDate(){
        return this.date;
    }

    void setDate(Date date){
        this.date = date;
    }

    String dateToString(Date date) {
        return DateFormat.format("dd.MM.yyyy", date).toString();
    }

    Date stringToDate(String sDate)
    {   if (sDate != null){
        ParsePosition pos = new ParsePosition(0);
        SimpleDateFormat simpledateformat = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);
        return simpledateformat.parse(sDate, pos);
        } else {
            return null;
        }
    }

    //EA Info
    String getInfo(){
        return this.info;
    }

    void setInfo(String info){
        this.info = info;
    }

    //EA Photo
    Bitmap setPhoto(Bitmap photo){
        return this.photo = photo;
    }

    Bitmap getPhoto(){
        return this.photo;
    }

    byte[] bitmapToByte(Bitmap bitmap){
        if(bitmap != null) {
            byte[] bitmapdata;
            ByteArrayOutputStream blob = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*EA ignored for PNGs */, blob);
            bitmapdata = blob.toByteArray();
            return bitmapdata;
        }
        return null;
    }

    Bitmap byteToBitmap(byte[] bPhoto){
        if (bPhoto != null) {
            return BitmapFactory.decodeByteArray(bPhoto, 0,  bPhoto.length);
        } else {
            return null;
        }
    }

    //EA Parcel Data
    private ToDoList(Parcel in) {
        name = in.readString();
        long tmpDate = in.readLong();
        date = tmpDate != -1 ? new Date(tmpDate) : null;
        check = in.readByte() != 0;
        info = in.readString();
    }

    public static final Creator<ToDoList> CREATOR = new Creator<ToDoList>() {
        @Override
        public ToDoList createFromParcel(Parcel in) {
            return new ToDoList(in);
        }

        @Override
        public ToDoList[] newArray(int size) {
            return new ToDoList[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeLong(date != null ? date.getTime() : -1L);
        dest.writeByte((byte) (check ? 1 : 0));
        dest.writeString(info);
    }
}

