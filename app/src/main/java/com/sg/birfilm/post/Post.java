package com.sg.birfilm.post;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Post implements Parcelable {
    String id;
    String sender;
    int movie;
    String criticism;
    String extract;
    float userRating;
    String poster;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.sender);
        dest.writeInt(this.movie);
        dest.writeString(this.criticism);
        dest.writeString(this.extract);
        dest.writeFloat(this.userRating);
        dest.writeString(this.poster);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readString();
        this.sender = source.readString();
        this.movie = source.readInt();
        this.criticism = source.readString();
        this.extract = source.readString();
        this.userRating = source.readFloat();
        this.poster = source.readString();
    }

    protected Post(Parcel in) {
        this.id = in.readString();
        this.sender = in.readString();
        this.movie = in.readInt();
        this.criticism = in.readString();
        this.extract = in.readString();
        this.userRating = in.readFloat();
        this.poster = in.readString();
    }

    public static final Creator<Post> CREATOR = new Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel source) {
            return new Post(source);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };
}
