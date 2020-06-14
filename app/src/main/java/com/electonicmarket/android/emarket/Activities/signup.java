package com.electonicmarket.android.emarket.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.electonicmarket.android.emarket.Models.BlurBuilder;
import com.electonicmarket.android.emarket.Models.userprofile;
import com.electonicmarket.android.emarket.R;
import com.electonicmarket.android.emarket.splashscreen;
import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.Priority;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class signup extends AppCompatActivity {
    TextView signup, countrydigit;
    Button createaccount;
    TextInputEditText security_answer, firstnamefield, lastnamefield, emailaddressfield, passwordfield, phonenumberfield;
    String firstname, lastname, emailaddress, password, phonenumber;
    SimpleDraweeView profileimage;
    Bitmap image;
    Toolbar toolbar;
    Dialog loadingdialog;
    RelativeLayout container;
    TextInputLayout firstname_layout,lastname_layout,email_layout,password_layout,phone_layout,security_answer_layout;
    int signupflag = 0,pickimageflag = 0;
    private static String user_SignUp_url = "http://jl-market.com/user/newuserregistration.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(signup.this);
        setContentView(R.layout.activity_signup);
        if(savedInstanceState != null){

            if(savedInstanceState.getInt("pid",-1) == android.os.Process.myPid()){

            }
            else{

                startActivity(new Intent(signup.this,splashscreen.class));

            }
        }
        initializeView();
        setOnclickListener();


    }

    private void setOnclickListener() {
        profileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pickimageflag == 0) {
                    pickimageflag = 1;
                    CropImage.activity()
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setAspectRatio(100, 100)
                            .setFixAspectRatio(true)
                            .start(com.electonicmarket.android.emarket.Activities.signup.this);
                }
            }
        });
        createaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firstname = firstnamefield.getText().toString();
                lastname = lastnamefield.getText().toString();
                emailaddress = emailaddressfield.getText().toString();
                phonenumber = phonenumberfield.getText().toString();
                password = passwordfield.getText().toString();

                if (signupflag == 0) {
                    signupflag = 1;
                    if (!validateForm()) {

                    } else {
                        SignUpVendorTask signUpVendorTask = new SignUpVendorTask();
                        signUpVendorTask.execute();
                    }
                }
            }
        });
    }
    private void showloadingdialog() {
        loadingdialog = new Dialog(signup.this, R.style.Dialog_Theme);
        loadingdialog.setContentView(R.layout.loadingdialog);
        //Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.loading);
        ImageView image = loadingdialog.findViewById(R.id.loadingimage);

        DrawableImageViewTarget imageViewTarget = new DrawableImageViewTarget(image);
        Glide.with(this).load(R.drawable.loading).into(imageViewTarget);
        loadingdialog.show();
        loadingdialog.setCancelable(false);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        loadingdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            loadingdialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            loadingdialog.getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        if (menuItem.getItemId() == android.R.id.home) {

            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public class SignUpVendorTask extends AsyncTask {
        String response;

        @Override
        protected void onPreExecute() {
              showloadingdialog();
            //Toast.makeText(signup.this, "Signing up vendor", Toast.LENGTH_LONG).show();
        }

        @Override
        protected Object doInBackground(Object[] objects) {

            if (!isNetworkAvailable()) {
                response = "No Network Connection";
                return response;
            }
            String uploadImage = getStringImage(image);
            String serverResponse = new SignUpUser().GetData(user_SignUp_url, firstname, lastname, emailaddress, password, phonenumber, uploadImage,security_answer.getText().toString().trim());

            if (serverResponse != null) {
                try {
                    JSONObject jsonObject = new JSONObject(serverResponse);
                    JSONObject info = jsonObject.getJSONObject("info");
                    String status = info.getString("status");

                    if (status.equalsIgnoreCase("emailalreadyexist")) {
                        response = "Email already exist please try again with a different Email";
                        return response;
                    } else if (status.equalsIgnoreCase("Vendor Registered Successfully")) {
                        userprofile userprofile = ParseJson(info);
                        return userprofile;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        protected void onPostExecute(Object result) {
            loadingdialog.dismiss();

            //Toast.makeText(signup.this, result.toString(), Toast.LENGTH_LONG).show();
            if(result == null){
                signupflag = 0;
              Toast.makeText(signup.this, "Error Occured", Toast.LENGTH_SHORT).show();
          }

          else if (result.toString().equalsIgnoreCase(response)) {
                signupflag = 0;
              Toast.makeText(signup.this, response, Toast.LENGTH_LONG).show();
          }


          else if (result instanceof userprofile) {

                  Intent intent = new Intent(com.electonicmarket.android.emarket.Activities.signup.this, MainActivity.class);
                  ArrayList<userprofile> userprofile = new ArrayList<>();
                  userprofile.add((userprofile) result);
                  intent.putParcelableArrayListExtra("userprofile", userprofile);
                  intent.putExtra("profile", "profile");
                  startActivity(intent);
                  overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);


            }
          }

        }

    public class SignUpUser {

        public String GetData(String url, String firstname, String lastname, String Email, String password, String phonenumber,
                              String image,String security_answer) {

            try {
                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(50, TimeUnit.SECONDS)
                        .writeTimeout(50, TimeUnit.SECONDS)
                        .readTimeout(50, TimeUnit.SECONDS)
                        .build();

                RequestBody formBody = new FormBody.Builder()
                        .add("firstname", firstname)
                        .add("lastname", lastname)
                        .add("emailaddress", Email)
                        .add("password", password)
                        .add("phonenumber", phonenumber)
                        .add("image", image)
                        .add("security_answer",security_answer)
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



    public static String getStringImage(Bitmap bitmap){
        final int MAX_IMAGE_SIZE = 500 * 1024; // max final file size in kilobytes
        byte[] bmpPicByteArray;

        Bitmap scBitmap  = Bitmap.createScaledBitmap(bitmap, 300, 300, false);


        int compressQuality = 100; // quality decreasing by 5 every loop.
        int streamLength;
        do{
            ByteArrayOutputStream bmpStream = new ByteArrayOutputStream();
            Log.d("compressBitmap", "Quality: " + compressQuality);
            scBitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream);
            bmpPicByteArray = bmpStream.toByteArray();
            streamLength = bmpPicByteArray.length;
            compressQuality -= 5;
            Log.d("compressBitmap", "Size: " + streamLength/1024+" kb");
        }while (streamLength >= MAX_IMAGE_SIZE);

        String encodedImage = Base64.encodeToString(bmpPicByteArray, Base64.DEFAULT);
        return encodedImage;

    }






    private Boolean isNetworkAvailable() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
        private void initializeView() {

            // ActionBar actionBar = getSupportActionBar();
            //actionBar.hide();,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                //getWindow().setStatusBarColor(Color.parseColor("#00000000"));
            }
            // Typeface customfont= Typeface.createFromAsset(getAssets(),"Lato-Regular.ttf");
            Typeface customfont2 = Typeface.createFromAsset(getAssets(), "Kylo-Light.otf");
            signup = findViewById(R.id.signupsignup);
            firstnamefield = findViewById(R.id.firstname);
            lastnamefield = findViewById(R.id.lastname);
            emailaddressfield = findViewById(R.id.email);
            passwordfield = findViewById(R.id.password);

            phonenumberfield = findViewById(R.id.phone);
            createaccount = findViewById(R.id.createaccount);
            toolbar = findViewById(R.id.signuptoolbar);
            profileimage = findViewById(R.id.signupprofileimage);
            security_answer = findViewById(R.id.signup_security_answer);
            firstname_layout = findViewById(R.id.sign_up_firstname_layout);
            lastname_layout = findViewById(R.id.sign_up_lastname_layout);
            password_layout = findViewById(R.id.sign_up_password_layout);
            email_layout = findViewById(R.id.sign_up_email_layout);
            phone_layout = findViewById(R.id.sign_up_phonenumber_layout);
            security_answer_layout = findViewById(R.id.sign_up_security_answer_layout);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);

            toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
            signup.setTypeface(customfont2);
            firstname_layout.setTypeface(customfont2);
            lastname_layout.setTypeface(customfont2);
            password_layout.setTypeface(customfont2);
            email_layout.setTypeface(customfont2);
            phone_layout.setTypeface(customfont2);
            security_answer_layout.setTypeface(customfont2);
            createaccount.setTypeface(customfont2);
            firstnamefield.setTypeface(customfont2);
            lastnamefield.setTypeface(customfont2);
            emailaddressfield.setTypeface(customfont2);
            passwordfield.setTypeface(customfont2);
            
            phonenumberfield.setTypeface(customfont2);
            security_answer.setTypeface(customfont2);

            container =  findViewById(R.id.sign_up_root);
            Bitmap original = BitmapFactory.decodeResource(getResources(),R.drawable.studentpic);
            Bitmap blurr = BlurBuilder.blur(this,original);
            container.setBackground(new BitmapDrawable(getResources(),blurr));

        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
                pickimageflag = 0;
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    Uri resulturi = result.getUri();
                    Log.e("uri", resulturi.toString());
                    getImageFromDrawee(resulturi);
                    profileimage.setImageURI(resulturi);
                    Log.e("uri", resulturi.toString());

                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                    Log.e("uri", "eroe");
                }
            }
        }

        private void getImageFromDrawee(Uri imageUri) {
            ImagePipeline imagePipeline = Fresco.getImagePipeline();
            ImageRequest imageRequest = ImageRequestBuilder
                    .newBuilderWithSource(imageUri)
                    .setRequestPriority(Priority.HIGH)
                    .setLowestPermittedRequestLevel(ImageRequest.RequestLevel.FULL_FETCH)
                    .build();

            DataSource<CloseableReference<CloseableImage>> dataSource = imagePipeline.fetchDecodedImage(imageRequest, com.electonicmarket.android.emarket.Activities.signup.this);

            dataSource.subscribe(new BaseBitmapDataSubscriber() {
                @Override
                protected void onNewResultImpl(Bitmap bitmap) {
                    if (dataSource.isFinished() && bitmap != null) {
                        Log.e("here", "onNewResultImpl: ");
                        image = Bitmap.createBitmap(bitmap);
                        dataSource.close();
                    }

                }

                @Override
                protected void onFailureImpl(DataSource<CloseableReference<CloseableImage>> dataSource) {
                    Log.e("close", "onFailureImpl: ");
                    dataSource.close();
                }
            }, CallerThreadExecutor.getInstance());

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
            String password = j.getString("password");


            userprofile = new userprofile(firstname, lastname, email, phonenumber, profilepicture);
            userprofile.setCity(city);
            userprofile.setArea(area);
            userprofile.setPassword(password);




        }


        return userprofile;

    }

    private boolean validateForm(){

                   boolean valid = true;

        if(TextUtils.isEmpty(security_answer.getText().toString().trim())){
            security_answer_layout.setError("Required");
            valid = false;
            signupflag = 0;
        }
        else{
            security_answer_layout.setError(null);
        }
                    if(TextUtils.isEmpty(firstname)){
                       firstname_layout.setError("Required");
                       valid = false;
                       signupflag = 0;
                       }
                       else{
                        firstname_layout.setError(null);
                    }
            if(TextUtils.isEmpty(lastname)){
                lastname_layout.setError("Required");
                valid = false;
                signupflag = 0;
            }
            else{
                lastname_layout.setError(null);
            }
            if(TextUtils.isEmpty(emailaddress)){
                email_layout.setError("Required");
                valid = false;
                signupflag = 0;
            }
            else{
                email_layout.setError(null);
            }
            if(TextUtils.isEmpty(password)){
                password_layout.setError("Required");
                valid = false;
                signupflag = 0;
            }
            else{
                password_layout.setError(null);
            }
            if(TextUtils.isEmpty(phonenumber)){
                phone_layout.setError("Required");
                valid = false;
                signupflag = 0;
            }
            else{
                phone_layout.setError(null);
            }
            if(image == null){
                Toast.makeText(this, "Please Choose a Profile image", Toast.LENGTH_SHORT).show();
                 valid = false;
                signupflag = 0;
                    }
                   return valid;
        }





    @Override
    public void onSaveInstanceState(Bundle outstate){
        super.onSaveInstanceState(outstate);
        outstate.putInt("pid",android.os.Process.myPid());

    }


}