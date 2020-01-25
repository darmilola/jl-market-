package com.electonicmarket.android.emarket.Models;

public class viewordermodel {
    String Orderjson;
    String Orderstatus;
    String paymentmethod;
    String deliveryfee;
    public viewordermodel(String Orderjson,String Orderstatus, String paymentmethod,String deliveryfee){
        this.Orderjson = Orderjson;
        this.Orderstatus = Orderstatus;
        this.paymentmethod = paymentmethod;
        this.deliveryfee = deliveryfee;
    }

    public String getDeliveryfee() {
        return deliveryfee;
    }

    public String getOrderjson() {
        return Orderjson;
    }

    public String getOrderstatus() {
        return Orderstatus;
    }

    public String getPaymentmethod() {
        return paymentmethod;
    }

    public void setDeliveryfee(String deliveryfee) {
        this.deliveryfee = deliveryfee;
    }

    public void setOrderjson(String orderjson) {
        Orderjson = orderjson;
    }

    public void setOrderstatus(String orderstatus) {
        Orderstatus = orderstatus;
    }

    public void setPaymentmethod(String paymentmethod) {
        this.paymentmethod = paymentmethod;
    }

}
