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

public class becomeavendor extends AppCompatActivity {
    Toolbar toolbar;
    EditText becomevendorcityvalue,becomevendoremailvalue,becomeavendorstatevalue;
    TextView becomevendorcitytitle,becomevendoremailtitle,title,becomeavendorstatetitle;
    Dialog loadingdialog;
    String email,state,city;
    Button apply;
    ArrayList<userprofile> userprofiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_becomeavendor);
        initView();
        setOnclickListener();
        if(savedInstanceState != null){

            if(savedInstanceState.getInt("pid",-1) == android.os.Process.myPid()){

            }
            else{

                startActivity(new Intent(becomeavendor.this,splashscreen.class));

            }
        }
        userprofiles = getIntent().getParcelableArrayListExtra("userprofile");
    }


    private void setOnclickListener(){
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = becomevendoremailvalue.getText().toString().trim();
                state =   becomeavendorstatevalue.getText().toString().trim();
                city = becomevendorcityvalue.getText().toString().trim();

                if(!validateForm()){

                }else{
                    new becomevendortask().execute();
                }
            }
        });
    }

    private void initView(){
        becomevendorcitytitle = findViewById(R.id.becomevendorcitytitle);
        becomevendorcityvalue = findViewById(R.id.becomevendorcityvalue);
        becomevendoremailtitle = findViewById(R.id.becomeavendoremailtitle);
        becomevendoremailvalue = findViewById(R.id.becomeavendoremailvalue);
        becomeavendorstatetitle = findViewById(R.id.becomeavendorstatetitle);
        becomeavendorstatevalue = findViewById(R.id.becomeavendorstatevalue);
        toolbar = findViewById(R.id.becomeavendortoolbar);
        title = findViewById(R.id.becomeavendortitle);
        apply = findViewById(R.id.becomeavendorapply);
        Typeface customfont= Typeface.createFromAsset(getAssets(),"Kylo-Light.otf");
       becomevendoremailvalue.setTypeface(customfont);
       becomevendoremailtitle.setTypeface(customfont);
       becomevendorcityvalue.setTypeface(customfont);
       becomevendorcitytitle.setTypeface(customfont);
       becomeavendorstatevalue.setTypeface(customfont);
       becomeavendorstatetitle.setTypeface(customfont);
       title.setTypeface(customfont);
       apply.setTypeface(customfont);



        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.White), PorterDuff.Mode.SRC_ATOP);
        }

        public class becomevendortask extends AsyncTask {
            String url = "http://jl-market.com/user/becomeavendor.php";
            String prompt;

            @Override
            protected void onPreExecute() {

                showloadingdialog();
                //Toast.makeText(CheckOut.this, "Placing Order", Toast.LENGTH_SHORT).show();
            }

            @Override
            protected Object doInBackground(Object[] objects) {


                if (!isNetworkAvailable()) {
                    prompt = "No Network Connection";
                    return prompt;
                }

                String serverresponse = new becomevendor().GetData(url, email, state, city);
                //return serverresponse;
                if (serverresponse != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(serverresponse);
                        JSONObject info = jsonObject.getJSONObject("info");
                        String status = info.getString("status");

                        if (status.equalsIgnoreCase("application successfull")) {
                            prompt = "application successfull";

                            return prompt;
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                return null;
            }

            protected void onPostExecute(Object result) {
                  loadingdialog.dismiss();
                //Toast.makeText(becomeavendor.this, result.toString(), Toast.LENGTH_SHORT).show();
                if (result != null) {
                    if (result.equals("No Network Connection")) {

                        Toast.makeText(becomeavendor.this, "No Network Connection", Toast.LENGTH_SHORT).show();
                    }

                    if (result.equals("application successfull")) {
                        Intent intent = new Intent(becomeavendor.this,MainActivity.class);
                        intent.putParcelableArrayListExtra("userprofile", userprofiles);
                        startActivity(intent);
                        Toast.makeText(becomeavendor.this, "You have successfully applied to become a Seller", Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Toast.makeText(becomeavendor.this, "Error Occured", Toast.LENGTH_SHORT).show();
                }

            }
        }

    public class becomevendor {

        public String GetData(String url,String email,String state,String city) {

            try {
                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(50, TimeUnit.SECONDS)
                        .writeTimeout(50, TimeUnit.SECONDS)
                        .readTimeout(50, TimeUnit.SECONDS)
                        .build();

                RequestBody formBody = new FormBody.Builder()
                        .add("email", email)
                        .add("state",state)
                        .add("city",city)
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
    private Boolean isNetworkAvailable() {

        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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

    private boolean validateForm(){
        boolean isvalid = true;

        if(TextUtils.isEmpty(email)){
            isvalid = false;
            becomevendoremailvalue.setError("Required");
        }
        else{
            becomevendoremailvalue.setError(null);
        }

        if(TextUtils.isEmpty(state)){
            isvalid = false;
            becomeavendorstatevalue.setError("Required");
        }
        else{
            becomeavendorstatevalue.setError(null);
        }
        if(TextUtils.isEmpty(city)){
            isvalid = false;
            becomevendorcityvalue.setError("Required");
        }
        else{
            becomevendorcityvalue.setError(null);
        }
        return isvalid;
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
