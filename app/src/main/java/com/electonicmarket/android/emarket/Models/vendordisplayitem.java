package com.electonicmarket.android.emarket.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.StringReader;

public class vendordisplayitem implements Parcelable {
    String vendorname;
    String category1;
    String category2;
    String category3;
    String vendorID;
    String VendorDisplayImage;
    String Viewtype;
    String headername;
    String email;
    String deliveryhour, deliveryminute, minimumorder, deliveryfee;
    double latitude, longitude;
    String openinghour, closinghour, cardpayment, cashpayment;
    String reviewscount, starnumber;
    String paymentstate;

    public vendordisplayitem() {

    }

    public vendordisplayitem(String Vendorname, String category1, String category2, String category3, String DisplayImage, String email, String deliveryhour, String deliveryminute, String deliveryfee, String minimumorder) {
        this.vendorname = Vendorname;
        this.category1 = category1;
        this.category2 = category2;
        this.category3 = category3;
        this.VendorDisplayImage = DisplayImage;
        this.email = email;
        this.deliveryhour = deliveryhour;
        this.deliveryminute = deliveryminute;
        this.minimumorder = minimumorder;
        this.deliveryfee = deliveryfee;
    }

    public vendordisplayitem(String Vendorname, String category1, String category2, String DisplayImage, String email, String deliveryhour, String deliveryminute, String deliveryfee, String minimumorder) {
        this.vendorname = Vendorname;
        this.category1 = category1;
        this.category2 = category2;

        this.VendorDisplayImage = DisplayImage;
        this.email = email;

        this.deliveryhour = deliveryhour;
        this.deliveryminute = deliveryminute;
        this.minimumorder = minimumorder;
        this.deliveryfee = deliveryfee;
    }

    public vendordisplayitem(String Vendorname, String category1, String DisplayImage, String email, String deliveryhour, String deliveryminute, String deliveryfee, String minimumorder) {
        this.vendorname = Vendorname;
        this.category1 = category1;

        this.VendorDisplayImage = DisplayImage;
        this.email = email;

        this.deliveryhour = deliveryhour;
        this.deliveryminute = deliveryminute;
        this.minimumorder = minimumorder;
        this.deliveryfee = deliveryfee;
    }


    protected vendordisplayitem(Parcel in) {
        vendorname = in.readString();
        category1 = in.readString();
        category2 = in.readString();
        category3 = in.readString();
        vendorID = in.readString();
        VendorDisplayImage = in.readString();
        Viewtype = in.readString();
        headername = in.readString();
        email = in.readString();
        deliveryhour = in.readString();
        deliveryminute = in.readString();
        minimumorder = in.readString();
        deliveryfee = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        openinghour = in.readString();
        closinghour = in.readString();
        cardpayment = in.readString();
        cashpayment = in.readString();
        reviewscount = in.readString();
        starnumber = in.readString();
        paymentstate = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(vendorname);
        dest.writeString(category1);
        dest.writeString(category2);
        dest.writeString(category3);
        dest.writeString(vendorID);
        dest.writeString(VendorDisplayImage);
        dest.writeString(Viewtype);
        dest.writeString(headername);
        dest.writeString(email);
        dest.writeString(deliveryhour);
        dest.writeString(deliveryminute);
        dest.writeString(minimumorder);
        dest.writeString(deliveryfee);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeString(openinghour);
        dest.writeString(closinghour);
        dest.writeString(cardpayment);
        dest.writeString(cashpayment);
        dest.writeString(reviewscount);
        dest.writeString(starnumber);
        dest.writeString(paymentstate);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<vendordisplayitem> CREATOR = new Creator<vendordisplayitem>() {
        @Override
        public vendordisplayitem createFromParcel(Parcel in) {
            return new vendordisplayitem(in);
        }

        @Override
        public vendordisplayitem[] newArray(int size) {
            return new vendordisplayitem[size];
        }
    };

    public String getReviewscount() {
        return reviewscount;
    }

    public String getStarnumber() {
        return starnumber;
    }

    public void setReviewscount(String reviewscount) {
        this.reviewscount = reviewscount;
    }

    public void setStarnumber(String starnumber) {
        this.starnumber = starnumber;
    }

    public String getDeliveryhour() {
        return deliveryhour;
    }

    public String getDeliveryminute() {
        return deliveryminute;
    }

    public String getDeliveryfee() {
        return deliveryfee;
    }

    public void setDeliveryfee(String deliveryfee) {
        this.deliveryfee = deliveryfee;
    }

    public void setDeliveryhour(String deliveryhour) {
        this.deliveryhour = deliveryhour;
    }

    public void setDeliveryminute(String deliveryminute) {
        this.deliveryminute = deliveryminute;
    }

    public void setMinimumorder(String minimumorder) {
        this.minimumorder = minimumorder;
    }

    public String getMinimumorder() {
        return minimumorder;
    }

    public String getViewtype() {
        return Viewtype;
    }

    public String getHeadername() {
        return headername;
    }

    public void setHeadername(String headername) {
        this.headername = headername;
    }

    public void setViewtype(String viewtype) {
        Viewtype = viewtype;
    }

    public String getCategory1() {
        return category1;
    }

    public String getVendorDisplayImage() {
        return VendorDisplayImage;
    }

    public String getCategory2() {
        return category2;
    }

    public String getCategory3() {
        return category3;
    }


    public String getVendorID() {
        return vendorID;
    }

    public String getVendorname() {
        return vendorname;
    }

    public void setVendorDisplayImage(String vendorDisplayImage) {
        VendorDisplayImage = vendorDisplayImage;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCategory1(String category1) {
        this.category1 = category1;
    }

    public void setCategory2(String category2) {
        this.category2 = category2;
    }

    public void setCategory3(String category3) {
        this.category3 = category3;
    }

    public void setVendorID(String vendorID) {
        this.vendorID = vendorID;
    }

    public void setVendorname(String vendorname) {
        this.vendorname = vendorname;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getCardpayment() {
        return cardpayment;
    }

    public String getCashpayment() {
        return cashpayment;
    }

    public String getClosinghour() {
        return closinghour;
    }

    public String getOpeninghour() {
        return openinghour;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setCardpayment(String cardpayment) {
        this.cardpayment = cardpayment;
    }

    public void setCashpayment(String cashpayment) {
        this.cashpayment = cashpayment;
    }

    public void setClosinghour(String closinghour) {
        this.closinghour = closinghour;
    }

    public void setOpeninghour(String openinghour) {
        this.openinghour = openinghour;
    }

    public String getPaymentstate() {
        return paymentstate;
    }

    public void setPaymentstate(String paymentstate) {
        this.paymentstate = paymentstate;
    }
}
