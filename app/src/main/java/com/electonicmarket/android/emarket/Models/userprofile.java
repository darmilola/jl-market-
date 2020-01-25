package com.electonicmarket.android.emarket.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class userprofile implements Parcelable {
    private String firstname;
    private String lastname;
    private String email;
    private String phonenumber;
    private String profileimage;
    private String city;
    private String area;
    private String address;
    private double latitude;
    private double longitude;
    private String password;

    public userprofile(String firstname,String lastname, String email, String phonenumber, String profileimage){
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.phonenumber = phonenumber;
        this.profileimage = profileimage;
    }


    protected userprofile(Parcel in) {
        firstname = in.readString();
        lastname = in.readString();
        email = in.readString();
        phonenumber = in.readString();
        profileimage = in.readString();
        city = in.readString();
        area = in.readString();
        address = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        password = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(firstname);
        dest.writeString(lastname);
        dest.writeString(email);
        dest.writeString(phonenumber);
        dest.writeString(profileimage);
        dest.writeString(city);
        dest.writeString(area);
        dest.writeString(address);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeString(password);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<userprofile> CREATOR = new Creator<userprofile>() {
        @Override
        public userprofile createFromParcel(Parcel in) {
            return new userprofile(in);
        }

        @Override
        public userprofile[] newArray(int size) {
            return new userprofile[size];
        }
    };

    public String getEmail() {
        return email;
    }

    public String getLastname() {
        return lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public String getProfileimage() {
        return profileimage;
    }

    public void setProfileimage(String profileimage) {
        this.profileimage = profileimage;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getArea() {
        return area;
    }



    public String getCity() {
        return city;
    }


    public String getAddress() {
        return address;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }


}
