package com.sg.birfilm.post;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment implements Parcelable {
    String comment;
    String sender;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.comment);
        dest.writeString(this.sender);
    }

    public void readFromParcel(Parcel source) {
        this.comment = source.readString();
        this.sender = source.readString();
    }

    protected Comment(Parcel in) {
        this.comment = in.readString();
        this.sender = in.readString();
    }

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel source) {
            return new Comment(source);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };
}
