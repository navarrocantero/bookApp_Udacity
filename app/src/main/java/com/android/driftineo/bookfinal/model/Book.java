package com.android.driftineo.bookfinal.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by driftineo on 25/6/17.
 */


public class Book  implements Parcelable{

    private String author;
    private String title;

    public Book(String title, String author) {


        this.title = title;
        this.author = author;
    }


    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    protected Book(Parcel in) {
        this.title= in.readString();
        this.author = in.readString();
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.author);
    }

    public static final Parcelable.Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

}
