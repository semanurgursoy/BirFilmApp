package com.sg.birfilm;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class User implements Parcelable {
    public String fullName,userName,email,phoneNo,password;
    public String id,ppUrl,bio;

    public User(String id,String ppUrl, String fullName, String userName, String email, String phoneNo, String password) {
        this.id = id;
        this.ppUrl = ppUrl;
        this.fullName = fullName;
        this.userName = userName;
        this.email = email;
        this.phoneNo = phoneNo;
        this.password = password;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.fullName);
        dest.writeString(this.userName);
        dest.writeString(this.email);
        dest.writeString(this.phoneNo);
        dest.writeString(this.password);
        dest.writeString(this.id);
        dest.writeString(this.ppUrl);
        dest.writeString(this.bio);
    }

    public void readFromParcel(Parcel source) {
        this.fullName = source.readString();
        this.userName = source.readString();
        this.email = source.readString();
        this.phoneNo = source.readString();
        this.password = source.readString();
        this.id = source.readString();
        this.ppUrl = source.readString();
        this.bio = source.readString();
    }

    protected User(Parcel in) {
        this.fullName = in.readString();
        this.userName = in.readString();
        this.email = in.readString();
        this.phoneNo = in.readString();
        this.password = in.readString();
        this.id = in.readString();
        this.ppUrl = in.readString();
        this.bio = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
