package com.electonicmarket.android.emarket.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
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

public class changepassword extends AppCompatActivity {
    Toolbar toolbar;
    TextView title,newpasswordtitle,rettypedpasswordtitle;
    EditText newpasswordvalue,rettypedpasswordvalue;
    Button changepassword;
    String newPassword,retypenewpassword;
    String useremail;
    Dialog loadingdialog;
    ArrayList<userprofile> userprofiles;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword);
        initView();
       if(getIntent().getParcelableArrayListExtra("userprofile") != null){
           userprofiles = getIntent().getParcelableArrayListExtra("userprofile");
           useremail = userprofiles.get(0).getEmail();
       }

       changepassword.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {


               retypenewpassword = rettypedpasswordvalue.getText().toString().trim();
               newPassword = newpasswordvalue.getText().toString().trim();
               if (!validateForm()) {


               } else {
                   newPassword = newpasswordvalue.getText().toString().trim();
                   new changepasswordtask().execute();
               }

           }
       });
    }

    private void initView(){
        toolbar = findViewById(R.id.changepasswordtoolbar);
        title = findViewById(R.id.changepasswordtitle);
        newpasswordtitle = findViewById(R.id.newpasswordtitle);
        rettypedpasswordtitle = findViewById(R.id.newpasswordretypetitle);
        changepassword = findViewById(R.id.changepassword);
        newpasswordvalue = findViewById(R.id.newpasswordvalue);
        rettypedpasswordvalue = findViewById(R.id.newpasswordretypevalue);

        Typeface customfont= Typeface.createFromAsset(getAssets(),"Kylo-Light.otf");
        title.setTypeface(customfont);
        newpasswordtitle.setTypeface(customfont);
        rettypedpasswordtitle.setTypeface(customfont);
        newpasswordvalue.setTypeface(customfont);
        rettypedpasswordvalue.setTypeface(customfont);
        changepassword.setTypeface(customfont);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.White), PorterDuff.Mode.SRC_ATOP);



    }

    private class changepasswordtask extends AsyncTask{


        String prompt;
        String url = "http://jl-market.com/user/changepassword.php";


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
            String serverresponse = new changepasswordvalue().GetData(url,useremail,newPassword);

            if (serverresponse != null) {
                try {
                    JSONObject jsonObject = new JSONObject(serverresponse);
                    JSONObject info = jsonObject.getJSONObject("info");
                    String status = info.getString("status");

                    if (status.equalsIgnoreCase("passwordchanged")) {
                        prompt = "passwordchanged";
                        return prompt;
                    } else if (status.equalsIgnoreCase("passwordnotchanged")) {
                        prompt = "passwordnotchanged";
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
            //Toast.makeText(vendorinformation.this, result.toString(), Toast.LENGTH_LONG).show();
            if (result == null) {
                Toast.makeText(changepassword.this, "Unable to connect Please try Again", Toast.LENGTH_LONG).show();
            } else if (result.toString().equalsIgnoreCase("No Network Connection")) {
                Toast.makeText(changepassword.this, "No Network Connection", Toast.LENGTH_LONG).show();
            } else if (result.toString().equalsIgnoreCase("passwordchanged")) {
                Intent intent = new Intent(changepassword.this,MainActivity.class);
                 userprofiles.get(0).setPassword(newPassword);
                intent.putParcelableArrayListExtra("userprofile",userprofiles);
                startActivity(intent);
                Toast.makeText(changepassword.this, "Password Changed Successfully", Toast.LENGTH_SHORT).show();
            } else if (result.toString().equalsIgnoreCase("passwordnotchanged")) {
                Toast.makeText(changepassword.this, "Error Occured Please Try again", Toast.LENGTH_SHORT).show();

            }
        }
    }


    public class changepasswordvalue {


        public String GetData(String url,String userid, String password) {

            try {
                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(50, TimeUnit.SECONDS)
                        .writeTimeout(50, TimeUnit.SECONDS)
                        .readTimeout(50, TimeUnit.SECONDS)
                        .build();

                RequestBody formBody = new FormBody.Builder()
                        .add("userid", userid)
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

    private boolean validateForm(){
        boolean isValid = true;

        if(TextUtils.isEmpty(newPassword)){
            isValid = false;
            newpasswordvalue.setError("Required");
            }
            else {
            newpasswordvalue.setError(null);
        }
        if(TextUtils.isEmpty(retypenewpassword)){
            isValid = false;
            rettypedpasswordvalue.setError("Required");
        }
        else{
            rettypedpasswordvalue.setError(null);
        }

        if(!newpasswordvalue.getText().toString().trim().equalsIgnoreCase(rettypedpasswordvalue.getText().toString().trim())){
            isValid = false;
            Toast.makeText(this, "Password does not match", Toast.LENGTH_SHORT).show();
        }

        return isValid;
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

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){

        if(menuItem.getItemId() == android.R.id.home){

            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }
    private void storeCredentialInSharedPref(String email,String password){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(com.electonicmarket.android.emarket.Activities.changepassword.this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("email",email);
        editor.putString("password",password);
        editor.commit();
    }

    private void removecredentialFromSharedpref(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("email");
        editor.remove("password");
        editor.commit();
    }

}
