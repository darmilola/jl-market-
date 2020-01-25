package com.electonicmarket.android.emarket.Models;

public class CheckOutUserModel {
    private String phone,city,area,log,lat,address;

    public CheckOutUserModel(String phone, String city, String area, String log, String lat, String address){
        this.phone = phone;
        this.city = city;
        this.area = area;
        this.log = log;
        this.lat = lat;
        this.address = address;
    }

    public String getArea() {
        return area;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getLat() {
        return lat;
    }

    public String getLog() {
        return log;
    }

    public String getPhone() {
        return phone;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
