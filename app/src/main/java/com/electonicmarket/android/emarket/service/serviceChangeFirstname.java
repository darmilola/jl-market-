package com.electonicmarket.android.emarket.service;

import android.app.IntentService;
import android.content.Intent;
import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class serviceChangeFirstname extends IntentService {

    public serviceChangeFirstname() {
        super("serviceChangeFirstname");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String urlpath = intent.getStringExtra("url");
        String userid = intent.getStringExtra("userid");
        String newfirstname = intent.getStringExtra("firstname");

        String serverresponse = new changeFirstname().GetData(urlpath, userid,newfirstname);
        //Toast.makeText(this, serverresponse, Toast.LENGTH_SHORT).show();
        if (serverresponse != null) {
            try {
                JSONObject jsonObject = new JSONObject(serverresponse);
                JSONObject info = jsonObject.getJSONObject("info");
                String status = info.getString("status");

                //if (status.equalsIgnoreCase("firstnameupdated")) {


                          PublishResult(serverresponse);
                //}
                //return serverResponse;
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
    private void PublishResult(String notifyFirstnameChange){
        Intent intent = new Intent("com.electonicmarket.android.emarket.service");
        intent.putExtra("successfull",notifyFirstnameChange);
        sendBroadcast(intent);
    }

    public class changeFirstname {

        public String GetData(String url,String userid,String firstname) {

            try {
                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(50, TimeUnit.SECONDS)
                        .writeTimeout(50, TimeUnit.SECONDS)
                        .readTimeout(50, TimeUnit.SECONDS)
                        .build();


                RequestBody formBody = new FormBody.Builder()
                        .add("email", userid)
                        .add("firstname",firstname)
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

}
