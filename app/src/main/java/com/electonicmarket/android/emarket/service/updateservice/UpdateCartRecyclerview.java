package com.electonicmarket.android.emarket.service.updateservice;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import com.electonicmarket.android.emarket.Fragments.userOrders;
import com.electonicmarket.android.emarket.Models.ProductModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class UpdateCartRecyclerview extends IntentService {
    String urlpath, userid;

    public UpdateCartRecyclerview() {
        super("UpdateCartRecyclerview");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        ArrayList<ProductModel> productModelArrayList = new ArrayList<>();
        if (intent != null) {
            urlpath = intent.getStringExtra("url");
            userid = intent.getStringExtra("userid");

            Log.e("got the int", "onHandleIntent: ");

            String serverresponse = new updatecartinfo().GetData(urlpath, userid);

            if (serverresponse != null) {
                try {
                    JSONObject jsonObject = new JSONObject(serverresponse);
                    JSONObject info = jsonObject.getJSONObject("info");
                    String status = info.getString("status");

                    if (status.equalsIgnoreCase("orderavailable")) {

                        productModelArrayList = passCartJson(info);

                        Log.e("i", "onHandleIntent: ");

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            PublishResult(productModelArrayList);


        }

    }

    public ArrayList<ProductModel> passCartJson(JSONObject jsonObject) throws JSONException {
        ArrayList<ProductModel> ProductList = new ArrayList<>();
        JSONArray jsonArray = jsonObject.getJSONArray("data");
        for (int i = 0; i < jsonArray.length(); ++i) {
            JSONObject j = jsonArray.getJSONObject(i);
            String productname = j.getString("productname");
            String price = j.getString("price");
            String newprice = j.getString("reducedprice");
            String firstimage = j.getString("productfirstimage");
            String secondimage = j.getString("productsecondimage");
            String count = j.getString("productcount");
            String description = j.getString("productdescription");
            String productid = j.getString("productid");
            ProductModel productModel = new ProductModel(description, productname, price, firstimage);
            if (!newprice.equalsIgnoreCase("0")) {
                productModel.setProductnewPrice(newprice);
            }
            if(!secondimage.equalsIgnoreCase("")){
                productModel.setProductImageSecond(secondimage);
            }
            productModel.setProductid(productid);
            productModel.setUseremail(userid);
            productModel.setProductCount(count);


            ProductList.add(productModel);
        }
        return ProductList;


    }

    public class updatecartinfo {

        public String GetData(String url, String userid) {

            try {
                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(50, TimeUnit.SECONDS)
                        .writeTimeout(50, TimeUnit.SECONDS)
                        .readTimeout(50, TimeUnit.SECONDS)
                        .build();


                RequestBody formBody = new FormBody.Builder()
                        .add("userid", userid)
                        .build();
                Request request = new Request.Builder().post(formBody).url(url).build();
                Response response = client.newCall(request).execute();
                String data = response.body().string();
                return data;

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }


    }

    private void PublishResult(ArrayList<ProductModel> cartitem) {
        Intent intent = new Intent("com.electonicmarket.android.emarket.service.updateservice");
        intent.putParcelableArrayListExtra("cartlist", cartitem);
        sendBroadcast(intent);

    }

}
