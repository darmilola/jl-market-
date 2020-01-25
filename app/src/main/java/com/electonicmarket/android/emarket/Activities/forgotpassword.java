package com.electonicmarket.android.emarket.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.electonicmarket.android.emarket.Models.userprofile;
import com.electonicmarket.android.emarket.R;
import com.electonicmarket.android.emarket.splashscreen;

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

public class forgotpassword extends AppCompatActivity {

    TextView title, emailtitle,newpasswordtitle,retypepasswordtitle,security_answer_title;
    EditText emailvalue,newpasswordvalue,retypepasswordvalue,security_answer_value;
    Button changepassword;
    Toolbar toolbar;
    Dialog loadingdialog;
    String email,password,retypepassword,security_answer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);
        initView();
        if(savedInstanceState != null){

            if(savedInstanceState.getInt("pid",-1) == android.os.Process.myPid()){

            }
            else{

                startActivity(new Intent(forgotpassword.this,splashscreen.class));

            }
        }

        changepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                password = newpasswordvalue.getText().toString().trim();
                retypepassword = retypepasswordvalue.getText().toString().trim();
                email = emailvalue.getText().toString().trim();
                security_answer = security_answer_value.getText().toString().trim();
                if(!validateForm()){

                }
                else {
                    new forgotpassowordtask().execute();
                }
            }
        });
    }

    private void initView(){
        title = findViewById(R.id.forgotpasswordtoolbartitle);
        toolbar = findViewById(R.id.forgotpasswordtoolbar);
        emailtitle = findViewById(R.id.forgotpasswordemailtitle);
        emailvalue = findViewById(R.id.forgotpasswordemailvalue);
        newpasswordtitle = findViewById(R.id.newpasswordtitle);
        newpasswordvalue = findViewById(R.id.newpasswordvalue);
        retypepasswordtitle = findViewById(R.id.newpasswordretypetitle);
        retypepasswordvalue = findViewById(R.id.newpasswordretypevalue);
        changepassword = findViewById(R.id.forgotpasswordchangepassword);
        security_answer_title = findViewById(R.id.forgotpasswordquestiontitle);
        security_answer_value = findViewById(R.id.forgotpasswordquestionvalue);

        Typeface customfont= Typeface.createFromAsset(getAssets(),"Kylo-Light.otf");
        title.setTypeface(customfont);
        emailvalue.setTypeface(customfont);
        emailtitle.setTypeface(customfont);
        newpasswordvalue.setTypeface(customfont);
        newpasswordtitle.setTypeface(customfont);
        retypepasswordvalue.setTypeface(customfont);
        retypepasswordtitle.setTypeface(customfont);
        changepassword.setTypeface(customfont);
        security_answer_title.setTypeface(customfont);
        security_answer_value.setTypeface(customfont);

        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.screenBrightness = 0.5f;
        getWindow().setAttributes(layoutParams);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.White), PorterDuff.Mode.SRC_ATOP);

    }

    public class forgotpassowordtask extends AsyncTask{

        String prompt;
        String url = "http://jl-market.com/user/forgotpassword.php";
        @Override
        protected void onPreExecute() {
            //Toast.makeText(getContext(), "Saving profile", Toast.LENGTH_SHORT).show();
            showloadingdialog();


        }
        @Override
            protected Object doInBackground(Object[] objects) {

                if (!isNetworkAvailable()) {
                    prompt = "No Network Connection";
                    return prompt;
                }
                String serverresponse = new forgotpasswordvalue().GetData(url,email,password,security_answer);

                if (serverresponse != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(serverresponse);
                        JSONObject info = jsonObject.getJSONObject("info");
                        String status = info.getString("status");

                        if (status.equalsIgnoreCase("emaildoesnotexist")) {
                            prompt = "emaildoesnotexist";
                            return prompt;
                        } else if (status.equalsIgnoreCase("Vendor Registered Successfully")) {
                            userprofile userprofile = ParseJson(info);
                            return userprofile;
                        }
                        else if(status.equalsIgnoreCase("wronganswer")){
                            prompt = "wronganswer";
                            return prompt;
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


                return serverresponse;
        }

        protected void onPostExecute(Object result) {

            loadingdialog.dismiss();
            if (result == null) {
                Toast.makeText(forgotpassword.this, "Error Occured", Toast.LENGTH_LONG).show();
            }

            else if (result.toString().equalsIgnoreCase("No Network Connection")) {
                Toast.makeText(forgotpassword.this, "No Network Connection", Toast.LENGTH_LONG).show();
            }
            else if(result.toString().equalsIgnoreCase("emaildoesnotexist")){
                Toast.makeText(forgotpassword.this, "Email does not exist", Toast.LENGTH_SHORT).show();

            }
            else if(result.toString().equalsIgnoreCase("emaildoesnotexist")){
                Toast.makeText(forgotpassword.this, "Email does not exist", Toast.LENGTH_SHORT).show();

            }
            else if(result.toString().equalsIgnoreCase("wronganswer")){
                Toast.makeText(forgotpassword.this, "Wrong Security Answer", Toast.LENGTH_SHORT).show();

            }
            else if(result instanceof userprofile){

                Intent intent = new Intent(forgotpassword.this, MainActivity.class);
                ArrayList<userprofile> vendorprofile = new ArrayList<>();
                vendorprofile.add((userprofile) result);
                intent.putParcelableArrayListExtra("userprofile", vendorprofile);
                intent.putExtra("profile", "profile");
                Toast.makeText(forgotpassword.this, "Password changed Successfully", Toast.LENGTH_SHORT).show();

                startActivity(intent);
            }
        }
    }

    public class forgotpasswordvalue {


        public String GetData(String url, String userid,String password,String answer) {

            try {
                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(50, TimeUnit.SECONDS)
                        .writeTimeout(50, TimeUnit.SECONDS)
                        .readTimeout(50, TimeUnit.SECONDS)
                        .build();

                RequestBody formBody = new FormBody.Builder()
                        .add("userid", userid)
                        .add("password",password)
                        .add("security_answer",answer)
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



    private userprofile ParseJson(JSONObject jsonObject) throws JSONException {
        userprofile userprofile = null;
        JSONArray jsonArray = jsonObject.getJSONArray("data");
        for (int i = 0; i < jsonArray.length(); ++i) {
            JSONObject j = jsonArray.getJSONObject(i);
            String firstname = j.getString("firstname");
            String lastname = j.getString("lastname");
            String phonenumber = j.getString("phone");
            String email = j.getString("email");
            String profilepicture = j.getString("profilepicture");
            String city = j.getString("city");
            String area = j.getString("area");
            double longitude = j.getDouble("log");
            double latitude = j.getDouble("lat");

            userprofile = new userprofile(firstname, lastname, email, phonenumber, profilepicture);
            userprofile.setCity(city);
            userprofile.setArea(area);
            userprofile.setLatitude(latitude);
            userprofile.setLongitude(longitude);



        }


        return userprofile;

    }

    private void showloadingdialog() {
        loadingdialog = new Dialog(this, R.style.Dialog_Theme);
        loadingdialog.setContentView(R.layout.loadingdialog);
        //Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.loading);
        ImageView image = loadingdialog.findViewById(R.id.loadingimage);
        DrawableImageViewTarget imageViewTarget = new DrawableImageViewTarget(image);
        Glide.with(this).load(R.drawable.loading).into(imageViewTarget);
        loadingdialog.show();
        loadingdialog.setCancelable(false);
        //getContext().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        loadingdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            loadingdialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            loadingdialog.getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
    }

    private Boolean isNetworkAvailable() {

        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private boolean validateForm(){
        boolean isValid = true;

        if(TextUtils.isEmpty(security_answer)){
            isValid = false;
            security_answer_value.setError("Required");
        }
        else {
            security_answer_value.setError(null);
        }
        if(TextUtils.isEmpty(email)){
            isValid = false;
            emailvalue.setError("Required");
        }
        else {
            emailvalue.setError(null);
        }
        if(TextUtils.isEmpty(password)){
            isValid = false;
            newpasswordvalue.setError("Required");
        }
        else {
            newpasswordvalue.setError(null);
        }

        if(TextUtils.isEmpty(retypepassword)){
            isValid = false;
            retypepasswordvalue.setError("Required");
        }
        else {
            retypepasswordvalue.setError(null);
        }

        if(!password.equalsIgnoreCase(retypepassword)){
            isValid = false;
            Toast.makeText(this, "Password does  not match", Toast.LENGTH_SHORT).show();
        }

        return isValid;
    }
    @Override
    public void onSaveInstanceState(Bundle outstate){
        super.onSaveInstanceState(outstate);
        outstate.putInt("pid",android.os.Process.myPid());

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        if (menuItem.getItemId() == android.R.id.home) {

            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

}
