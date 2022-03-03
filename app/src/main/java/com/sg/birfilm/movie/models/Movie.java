package com.sg.birfilm.movie.models;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Movie implements Parcelable {
    int id; //
    String original_title;
    String original_language;
    String overview;
    String popularity; //double
    String poster_path;
    String release_date;
    boolean video;
    float vote_average; //float
    String vote_count; //int


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.original_title);
        dest.writeString(this.original_language);
        dest.writeString(this.overview);
        dest.writeString(this.popularity);
        dest.writeString(this.poster_path);
        dest.writeString(this.release_date);
        dest.writeByte(this.video ? (byte) 1 : (byte) 0);
        dest.writeFloat(this.vote_average);
        dest.writeString(this.vote_count);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readInt();
        this.original_title = source.readString();
        this.original_language = source.readString();
        this.overview = source.readString();
        this.popularity = source.readString();
        this.poster_path = source.readString();
        this.release_date = source.readString();
        this.video = source.readByte() != 0;
        this.vote_average = source.readFloat();
        this.vote_count = source.readString();
    }

    protected Movie(Parcel in) {
        this.id = in.readInt();
        this.original_title = in.readString();
        this.original_language = in.readString();
        this.overview = in.readString();
        this.popularity = in.readString();
        this.poster_path = in.readString();
        this.release_date = in.readString();
        this.video = in.readByte() != 0;
        this.vote_average = in.readFloat();
        this.vote_count = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
