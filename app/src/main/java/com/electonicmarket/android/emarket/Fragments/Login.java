package com.electonicmarket.android.emarket.Fragments;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.electonicmarket.android.emarket.Activities.MainActivity;
import com.electonicmarket.android.emarket.Activities.forgotpassword;
import com.electonicmarket.android.emarket.Models.userprofile;
import com.electonicmarket.android.emarket.R;
import com.google.android.material.textfield.TextInputEditText;

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

import static android.app.PendingIntent.getActivity;


public class Login extends Fragment {

    TextInputEditText emailfield, passwordfield;
    TextView forgotpassword;
    String email, password;
    Button login;
    View view;
    Dialog loadingdialog;
    int loginflag = 0;
    private static String User_Login_url = "http://jl-market.com/user/userlogin.php";

    public Login() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_login, container, false);
        InitializeView();
        SetOnclickListener();
        return view;
    }

    private void InitializeView() {

        emailfield = view.findViewById(R.id.loginemailaddress);
        passwordfield = view.findViewById(R.id.loginpassword);
        forgotpassword = view.findViewById(R.id.forgotpassword);
        forgotpassword.setText(R.string.Forgotpassword);
        login = view.findViewById(R.id.loginlogin);
        Typeface customfont = Typeface.createFromAsset(getActivity().getAssets(), "Kylo-Light.otf");
        emailfield.setTypeface(customfont);
        login.setTypeface(customfont);
    }

    private void SetOnclickListener() {
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //emailfield.setText("dammy@yahoo.com");
                //passwordfield.setText("damilola23");
                email = emailfield.getText().toString();
                password = passwordfield.getText().toString();

               if(loginflag == 0) {
                   loginflag = 1;
                   if (!validateForm()) {

                   } else {
                       loginTask loginTask = new loginTask();
                       loginTask.execute();
                   }
               }
                //Intent intent = new Intent(getActivity().getApplicationContext(),MainActivity.class);
                //startActivity(intent);
            }
        });

        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),forgotpassword.class);
                startActivity(intent);
            }
        });
    }

    public class loginTask extends AsyncTask {


        String response;

        @Override
        protected void onPreExecute() {
             showloadingdialog();
             forgotpassword.setEnabled(false);
            //Toast.makeText(getContext(), "Logging in user", Toast.LENGTH_LONG).show();
        }

        @Override
        protected Object doInBackground(Object[] objects) {

            if (!isNetworkAvailable()) {
                response = "No Network Connection";
                return response;
            }
            String serverResponse = new LoginBuyer().GetData(User_Login_url, email, password);
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
              loadingdialog.dismiss();
            //Toast.makeText(getContext(), result.toString(), Toast.LENGTH_LONG).show();
            if (result == null) {
                loginflag = 0;
                forgotpassword.setEnabled(true);
                Toast.makeText(getContext(), "Error Occured", Toast.LENGTH_LONG).show();
            } else if (result.toString().equalsIgnoreCase("Wrong Username or Password")) {
                loginflag = 0;
                forgotpassword.setEnabled(true);
                Toast.makeText(getContext(), "Wrong Username or Password", Toast.LENGTH_LONG).show();
            }
            else if(result.toString().equalsIgnoreCase("No Network Connection")){
                loginflag = 0;
                forgotpassword.setEnabled(true);
                Toast.makeText(getContext(), "No Network Connection", Toast.LENGTH_SHORT).show();
            }
            else if (result instanceof userprofile) {

                    Intent intent = new Intent(getContext(), MainActivity.class);
                    ArrayList<userprofile> userprofile = new ArrayList<>();
                    userprofile.add((userprofile) result);
                    intent.putParcelableArrayListExtra("userprofile", userprofile);
                    intent.putExtra("profile", "profile");
                    startActivity(intent);
                    getActivity().overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);


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

            ConnectivityManager connectivityManager =  (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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
                String password = j.getString("password");
                String profilepicture = j.getString("profilepicture");
                String city = j.getString("city");
                String area = j.getString("area");
                String address = j.getString("address");
                double longitude = j.getDouble("log");
                double latitude = j.getDouble("lat");
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

    private void showloadingdialog() {
        loadingdialog = new Dialog(getContext(), R.style.Dialog_Theme);
        loadingdialog.setContentView(R.layout.loadingdialog);
        //Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.loading);
        ImageView image = loadingdialog.findViewById(R.id.loadingimage);
        DrawableImageViewTarget imageViewTarget = new DrawableImageViewTarget(image);
        Glide.with(getContext()).load(R.drawable.loading).into(imageViewTarget);
        loadingdialog.show();
        loadingdialog.setCancelable(false);
        getActivity().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        loadingdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            loadingdialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            loadingdialog.getWindow().setStatusBarColor(getActivity().getResources().getColor(R.color.colorPrimaryDark));
        }
    }

  private boolean validateForm(){
          boolean valid = true;
                  if(TextUtils.isEmpty(email)){
                      emailfield.setError("Required");
                      valid = false;
                      loginflag = 0;
          }else{
                      emailfield.setError(null);
                  }

                  if(TextUtils.isEmpty(password)){
                      passwordfield.setError("Required");
                      valid = false;
                      loginflag = 0;
                  }
                  else{
                      passwordfield.setError(null);
                  }

                  return valid;

      }
  }


