package com.electonicmarket.android.emarket;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class pinpage extends AppCompatActivity {

    EditText myid,to,message;
    Button getmyid,sendmesage;
    String firebaseid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pinpage);
        myid = findViewById(R.id.myid);
        to = findViewById(R.id.to);
        getmyid = findViewById(R.id.getmyid);
        sendmesage = findViewById(R.id.sendmessage);
        message = findViewById(R.id.message);

        getmyid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

                firebaseid = preferences.getString("firebaseid","");
                if(!firebaseid.equalsIgnoreCase("")) {
                         myid.setText(firebaseid);
                }
            }
        });

        sendmesage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 new placeordertask().execute();

            }
        });






        }

    public class placeordertask extends AsyncTask {

        String url = "http://dixxieboy44.000webhostapp.com/user/placeorder.php";
        String prompt;
        @Override
        protected void onPreExecute() {


            //Toast.makeText(CheckOut.this, "Placing Order", Toast.LENGTH_SHORT).show();
        }
        @Override
        protected Object doInBackground(Object[] objects) {




            String serverresponse = new pinpage.getOrder().GetData(url, "id","orderjson","paymentmethod","mdeliveryfee","vendorid", "price",to.getText().toString().trim(),message.getText().toString().trim());

            if (serverresponse != null) {
                try {
                    JSONObject jsonObject = new JSONObject(serverresponse);
                    JSONObject info = jsonObject.getJSONObject("info");
                    String status = info.getString("status");

                    if (status.equalsIgnoreCase("Order placed successfully")) {
                        prompt = "Order place successfully";
                        return prompt;
                    }
                    if (status.equalsIgnoreCase("Error placing order")) {
                        prompt = "Error placing order";
                        return prompt;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
            return serverresponse;
        }

        protected void onPostExecute(Object result) {


            //Toast.makeText(CheckOut.this, result.toString(), Toast.LENGTH_SHORT).show();
            if(result != null){

                if(result.toString().equalsIgnoreCase("Order place successfully")){
                    Toast.makeText(pinpage.this, "Message Sent", Toast.LENGTH_LONG).show();


                }
                else if(result.toString().equalsIgnoreCase("Error placing order")){
                    Toast.makeText(pinpage.this, "Error Occured", Toast.LENGTH_LONG).show();
                }
            }
            else{
                Toast.makeText(pinpage.this, "Error Occured", Toast.LENGTH_LONG).show();
            }



            //Toast.makeText(CheckOut.this, result.toString(), Toast.LENGTH_SHORT).show();
        }


    }

    public class getOrder {

        public String GetData(String url,String userid,String orderjson,String paymentmethod,String deliveryfee,String vendorid,String totalprice,String id,String message) {

            try {
                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(50, TimeUnit.SECONDS)
                        .writeTimeout(50, TimeUnit.SECONDS)
                        .readTimeout(50, TimeUnit.SECONDS)
                        .build();

                RequestBody formBody = new FormBody.Builder()
                        .add("userid", userid)
                        .add("orderjson",orderjson)
                        .add("vendorid",vendorid)
                        .add("paymentmethod",paymentmethod)
                        .add("deliveryfee",deliveryfee)
                        .add("totalprice",totalprice)
                        .add("sendid",id)
                        .add("message",message)
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
