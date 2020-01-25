package com.electonicmarket.android.emarket.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class ProductModel implements Parcelable {

    private int mImageRes;
    private String productName;
    private String productCount;
    private String productDescription;
    private String productoldPrice;
    private String productImagefirst;
    private String productImageSecond;
    private String productImageThird;
    private String productnewPrice;
    private String productDeal;
    private String percentDecrease;
    private String ViewType;
    private String useremail;
    private String productid;


    protected ProductModel(Parcel in) {
        mImageRes = in.readInt();
        productName = in.readString();
        productCount = in.readString();
        productDescription = in.readString();
        productoldPrice = in.readString();
        productImagefirst = in.readString();
        productImageSecond = in.readString();
        productImageThird = in.readString();
        productnewPrice = in.readString();
        productDeal = in.readString();
        percentDecrease = in.readString();
        ViewType = in.readString();
        useremail = in.readString();
        productid = in.readString();
    }

    public ProductModel() {

    }

    public String getViewType() {
        return ViewType;
    }

    public void setViewType(String viewType) {
        ViewType = viewType;
    }

    public static final Creator<ProductModel> CREATOR = new Creator<ProductModel>() {
        @Override
        public ProductModel createFromParcel(Parcel in) {
            return new ProductModel(in);
        }

        @Override
        public ProductModel[] newArray(int size) {
            return new ProductModel[size];
        }
    };

    public String getPercentDecrease() {
        return percentDecrease;
    }

    public void setPercentDecrease(String percentDecrease) {
        this.percentDecrease = percentDecrease;
    }

    public String getProductnewPrice() {
        return productnewPrice;
    }

    public void setProductnewPrice(String productReducedPrice) {
        this.productnewPrice = productReducedPrice;
    }

    public String getUseremail() {
        return useremail;
    }

    public void setUseremail(String useremail) {
        this.useremail = useremail;
    }

    public String getProductDeal() {
        return productDeal;
    }

    public void setProductDeal(String productDeal) {
        this.productDeal = productDeal;
    }

    public int getmImageRes() {
        return mImageRes;
    }

    public void setmImageRes(int mImageRes) {
        this.mImageRes = mImageRes;
    }

     public ProductModel(String productDescription,String productName,String productPrice, String productImagefirst,String productImageSecond, String productImageThird){
        this.productName = productName;
        this.productDescription = productDescription;
        this.productoldPrice = productPrice;
        this.productImagefirst = productImagefirst;
        this.productImageSecond = productImageSecond;
        this.productImageThird = productImageThird;
     }

    public ProductModel(String productDescription,String productName,String productPrice, String productImagefirst,String productImageSecond){
        this.productName = productName;
        this.productDescription = productDescription;
        this.productoldPrice = productPrice;
        this.productImagefirst = productImagefirst;
        this.productImageSecond = productImageSecond;

    }

    public ProductModel(String productDescription,String productName,String productPrice, String productImagefirst){
        this.productName = productName;
        this.productDescription = productDescription;
        this.productoldPrice = productPrice;
        this.productImagefirst = productImagefirst;

    }

    public void setProductid(String productid) {
        this.productid = productid;
    }

    public String getProductid() {
        return productid;
    }

    public String getProductDescription() {
        return productDescription;
    }


    public String getProductCount() {
        return productCount;
    }

    public void setProductCount(String productCount) {
        this.productCount = productCount;
    }

    public String getProductName() {
        return productName;
    }

    public String getoldProductPrice() {
        return productoldPrice;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }



    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setoldProductPrice(String productPrice) {
        this.productoldPrice = productPrice;
    }

    public void setProductImageThird(String productImageThird) {
        this.productImageThird = productImageThird;
    }

    public void setProductImageSecond(String productImageSecond) {
        this.productImageSecond = productImageSecond;
    }

    public void setProductImagefirst(String productImagefirst) {
        this.productImagefirst = productImagefirst;
    }

    public String getProductImagefirst() {
        return productImagefirst;
    }

    public String getProductImageSecond() {
        return productImageSecond;
    }

    public String getProductImageThird() {
        return productImageThird;
    }

    public String getProductoldPrice() {
        return productoldPrice;
    }

    public void setProductoldPrice(String productoldPrice) {
        this.productoldPrice = productoldPrice;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mImageRes);
        dest.writeString(productName);
        dest.writeString(productCount);
        dest.writeString(productDescription);
        dest.writeString(productoldPrice);
        dest.writeString(productImagefirst);
        dest.writeString(productImageSecond);
        dest.writeString(productImageThird);
        dest.writeString(productnewPrice);
        dest.writeString(productDeal);
        dest.writeString(percentDecrease);
        dest.writeString(ViewType);
        dest.writeString(useremail);
        dest.writeString(productid);
    }
}
