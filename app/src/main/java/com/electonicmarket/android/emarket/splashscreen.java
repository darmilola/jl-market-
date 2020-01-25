package com.electonicmarket.android.emarket;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.electonicmarket.android.emarket.Activities.MainActivity;
import com.electonicmarket.android.emarket.Models.userprofile;

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

public class splashscreen extends AppCompatActivity {

    CountDownTimer countDownTimer;
    Intent intent;
    String email,password;

    private static String User_Login_url = "http://jl-market.com/user/userlogin.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splashscreen);

        Typeface customfont= Typeface.createFromAsset(getAssets(),"Kylo-Light.otf");

        countDownTimer = new CountDownTimer(4000,1000);
        countDownTimer.start();
         intent = new Intent(splashscreen.this,openingpageActivity.class);
    }

    public class CountDownTimer extends android.os.CountDownTimer {


        public CountDownTimer(long millisInFuture, long countDownInterval) {

            super(millisInFuture, countDownInterval);

        }

        @Override
        public void onTick(long millisUntilFinished) {


        }

        @Override
        public void onFinish() {

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            email = preferences.getString("email","");
            password = preferences.getString("password","");
            if(!email.equalsIgnoreCase("") && !password.equalsIgnoreCase("")){
              new loginTask().execute();

            }
            else {
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                finish();
            }

        }

        }

    public class loginTask extends AsyncTask {


        String response;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Object doInBackground(Object[] objects) {

            if (!isNetworkAvailable()) {
                response = "No Network Connection";
                return response;
            }
            String serverResponse = new LoginBuyer().GetData(User_Login_url, email, password);
            //return serverResponse;
            if (serverResponse != null) {
                try {
                    JSONObject jsonObject = new JSONObject(serverResponse);
                    JSONObject info = jsonObject.getJSONObject("info");
                    String status = info.getString("status");

                    if (status.equalsIgnoreCase("loggedin")) {
                        userprofile userprofile = ParseJson(info);
                        return userprofile;
                    }

                    if(status.equalsIgnoreCase("wronguserameorpassword")){
                        response = "Wrong Username or Password";
                        return response;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
           return null;
        }


        protected void onPostExecute(Object result) {

            //Toast.makeText(splashscreen.this, result.toString(), Toast.LENGTH_LONG).show();
            if (result == null) {

            } else if (result.toString().equalsIgnoreCase("Wrong Username or Password")) {

            }
            else if(result.toString().equalsIgnoreCase("No Network Connection")){
                Toast.makeText(splashscreen.this, "No Network Connection", Toast.LENGTH_SHORT).show();
            }
            else if (result instanceof userprofile) {

                Intent intent = new Intent(splashscreen.this, MainActivity.class);
                ArrayList<userprofile> userprofile = new ArrayList<>();
                userprofile.add((userprofile) result);
                intent.putParcelableArrayListExtra("userprofile", userprofile);
                intent.putExtra("profile", "profile");
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);


            }


        }

    }


    public class LoginBuyer {

        public String GetData(String url, String email, String password) {

            try {
                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(50, TimeUnit.SECONDS)
                        .writeTimeout(50, TimeUnit.SECONDS)
                        .readTimeout(50, TimeUnit.SECONDS)
                        .build();

                RequestBody formBody = new FormBody.Builder()
                        .add("email", email)
                        .add("password", password)
                        .build();

                Request request = new Request.Builder().url(url).post(formBody).build();
                Response response = client.newCall(request).execute();
                String data = response.body().string();
                return data;

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

    }

    private Boolean isNetworkAvailable() {

        ConnectivityManager connectivityManager =  (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private userprofile ParseJson(JSONObject jsonObject) throws JSONException {
        userprofile userprofile = null;
        double longitude,latitude;
        JSONArray jsonArray = jsonObject.getJSONArray("data");
        for (int i = 0; i < jsonArray.length(); ++i) {
            JSONObject j = jsonArray.getJSONObject(i);
            String firstname = j.getString("firstname");
            String lastname = j.getString("lastname");
            String phonenumber = j.getString("phone");
            String email = j.getString("email");
            String password = j.getString("password");
            String profilepicture = j.getString("profilepicture");
            String city = j.getString("city");
            String area = j.getString("area");
            String address = j.getString("address");
            if(!city.equalsIgnoreCase("null")) {
                 longitude = j.getDouble("log");
                 latitude = j.getDouble("lat");
            }
            else{
                longitude = 0;
                latitude = 0;
            }

            userprofile = new userprofile(firstname, lastname, email, phonenumber, profilepicture);
            userprofile.setAddress(address);
            userprofile.setArea(area);
            userprofile.setCity(city);
            userprofile.setLongitude(longitude);
            userprofile.setLatitude(latitude);
            userprofile.setPassword(password);

        }

        return userprofile;

    }
    @Override
    protected void onPause() {
        super.onPause();
        countDownTimer.cancel();

    }

}
