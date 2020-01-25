package com.electonicmarket.android.emarket.service;

import android.app.IntentService;
import android.content.Intent;
import androidx.annotation.Nullable;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EmptyCartService extends IntentService {



    public EmptyCartService() {
        super("EmptyCartService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        String urlpath = intent.getStringExtra("url");
        String userid = intent.getStringExtra("userid");

        String serverresponse = new emptycart().GetData(urlpath, userid);

        /*if (serverresponse != null) {
            try {
                JSONObject jsonObject = new JSONObject(serverresponse);
                JSONObject info = jsonObject.getJSONObject("info");
                String status = info.getString("status");

                if (status.equalsIgnoreCase("cart emptied successfully")) {

                }
                else {
                    Toast.makeText(getApplicationContext(),"service is here",Toast.LENGTH_LONG)
                }
            }catch (JSONException E){
            E.printStackTrace();
            }
        }*/
    }

   public class emptycart {

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
}
