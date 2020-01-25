package com.electonicmarket.android.emarket.service;

import android.app.IntentService;
import android.content.Intent;
import androidx.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

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

public class UpdateCartCountService extends IntentService {


    public UpdateCartCountService() {
        super("UpdateCartCountService");
    }




    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String urlpath = intent.getStringExtra("url");
        String userid = intent.getStringExtra("userid");
        Log.e("got the intent", "onHandleIntent: ");
        ArrayList<ProductModel> modelArrayList = new ArrayList<>();
        String serverresponse = new countCartitem().GetData(urlpath, userid);
        Toast.makeText(this, serverresponse, Toast.LENGTH_SHORT).show();
        if (serverresponse != null) {
            try {
                JSONObject jsonObject = new JSONObject(serverresponse);
                JSONObject info = jsonObject.getJSONObject("info");
                String status = info.getString("status");

                if (status.equalsIgnoreCase("orderavailable")) {

                    modelArrayList = passCartCountJson(info);

                }
                //return serverResponse;
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
           PublishResult(Integer.toString(modelArrayList.size()));
        Log.e("size", Integer.toString(modelArrayList.size()) );
    }

    private void PublishResult(String cartitemCount){
        Intent intent = new Intent("com.electonicmarket.android.emarket.service");
        intent.putExtra("cartcount",cartitemCount);
        sendBroadcast(intent);
    }
    public class countCartitem {

        public String GetData(String url,String userid) {

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

    public ArrayList<ProductModel> passCartCountJson(JSONObject jsonObject) throws JSONException {
        ArrayList<ProductModel> ProductList = new ArrayList<>();
        JSONArray jsonArray = jsonObject.getJSONArray("data");
        for (int i = 0; i < jsonArray.length(); ++i) {
            JSONObject j = jsonArray.getJSONObject(i);
            String productname = j.getString("productname");
            ProductModel productModel = new ProductModel();
            productModel.setProductName(productname);
            ProductList.add(productModel);
        }
        return ProductList;


    }

}
