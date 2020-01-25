package com.electonicmarket.android.emarket.Models;

public class DeliveryArea {
     private String Name;
     private Double longitude;
     private Double latitude;

     public DeliveryArea(String name, Double longitude, Double latitude){
         this.Name = name;
         this.latitude = latitude;
         this.longitude = longitude;
     }

    public Double getLongitude() {
        return longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
