package com.electonicmarket.android.emarket.Models;

public class placedordermodel {
    private String Orderid;

    public placedordermodel(String Orderid){
        this.Orderid = Orderid;
    }

    public String getOrderid() {
        return Orderid;
    }

    public void setOrderid(String orderid) {
        Orderid = orderid;
    }
}
