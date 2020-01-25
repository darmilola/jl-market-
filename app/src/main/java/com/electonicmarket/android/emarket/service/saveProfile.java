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

public class saveProfile extends IntentService {

    public saveProfile() {
        super("saveProfile");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String urlpath = intent.getStringExtra("url");
        String userid = intent.getStringExtra("userid");
        String firstname = intent.getStringExtra("firstname");
        String lastname = intent.getStringExtra("lastname");
        String phone = intent.getStringExtra("phonenumber");


        String serverresponse = new updateProfile().GetData(urlpath, userid,firstname,lastname,phone);
        //Toast.makeText(this, serverresponse, Toast.LENGTH_SHORT).show();
        if (serverresponse != null) {
            try {
                JSONObject jsonObject = new JSONObject(serverresponse);
                JSONObject info = jsonObject.getJSONObject("info");
                String status = info.getString("status");

                if (status.equalsIgnoreCase("Profile updated successfully")) {


                PublishResult("successfull");
                }
                //return serverResponse;
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
    private void PublishResult(String notifyProfilesaved){
        Intent intent = new Intent("com.electonicmarket.android.emarket.service");
        intent.putExtra("successfull",notifyProfilesaved);
        sendBroadcast(intent);
    }





    public class updateProfile {

        public String GetData(String url,String userid,String firstname,String lastname,String phone) {

            try {
                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(50, TimeUnit.SECONDS)
                        .writeTimeout(50, TimeUnit.SECONDS)
                        .readTimeout(50, TimeUnit.SECONDS)
                        .build();


                RequestBody formBody = new FormBody.Builder()
                        .add("email", userid)
                        .add("firstname",firstname)
                        .add("lastname",lastname)
                        .add("phonenumber",phone)
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
