package com.electonicmarket.android.emarket.Models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class OrderModel  implements Parcelable{

    private String productname;
    private String price;
    private String productcount;
    private int countprice;

    public OrderModel(String productname, String price, String productcount, int countprice){
        this.productname = productname;
        this.price = price;
        this.productcount = productcount;
        this.countprice = countprice;
    }

    protected OrderModel(Parcel in) {
        productname = in.readString();
        price = in.readString();
        productcount = in.readString();
        countprice = in.readInt();
    }

    public static final Creator<OrderModel> CREATOR = new Creator<OrderModel>() {
        @Override
        public OrderModel createFromParcel(Parcel in) {
            return new OrderModel(in);
        }

        @Override
        public OrderModel[] newArray(int size) {
            return new OrderModel[size];
        }
    };

    public String getProductcount() {
        return productcount;
    }

    public int getCountprice() {
        return countprice;
    }

    public void setCountprice(int countprice) {
        this.countprice = countprice;
    }

    public void setProductcount(String productcount) {
        this.productcount = productcount;
    }

    public String getPrice() {
        return price;
    }

    public String getProductname() {
        return productname;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(productname);
        dest.writeString(price);
        dest.writeString(productcount);
        dest.writeInt(countprice);
    }
    public JSONObject getJsonObject(){
        JSONObject obj = new JSONObject();
        try{
            obj.put("name",productname);
            obj.put("price",price);
            obj.put("count",productcount);
            obj.put("countprice",countprice);
        }catch (JSONException e){
            e.printStackTrace();
        }
        return obj;
    }
}
