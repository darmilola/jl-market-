package com.electonicmarket.android.emarket.Fragments;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.electonicmarket.android.emarket.Activities.MainActivity;
import com.electonicmarket.android.emarket.Models.userprofile;
import com.electonicmarket.android.emarket.R;
import com.electonicmarket.android.emarket.splashscreen;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

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


/**
 * A simple {@link Fragment} subclass.
 */
public class UserAccount extends Fragment {
  static ArrayList<userprofile> userprofiles;

    View view;
    TextView editandapply;
    TextInputEditText firstnamevalue,lastnamevalue,phonenumbervalue,emailvalue;
    String firstname,lastname,phonenumber,email,picturepath;
    SimpleDraweeView accountpicture;
    Dialog loadingdialog;
    String updatefirstname,updatelastname,updatephonenumber;
    TextInputLayout firstname_layout,lastname_layout,phone_layout;

    public UserAccount() {
        // Required empty public constructor
    }
  public static UserAccount newInstance(ArrayList<userprofile> userprofileArrayList){
        UserAccount userAccount = new UserAccount();
        userprofiles = userprofileArrayList;
        return  userAccount;
  }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Fresco.initialize(getContext());
        view = inflater.inflate(R.layout.fragment_user_account, container, false);

        return view;
    }

    @Override
    public void onActivityCreated( Bundle savedinstance){
        super.onActivityCreated(savedinstance);
        if(userprofiles == null){
            Intent intent = new Intent(getContext(),splashscreen.class);
            startActivity(intent);
            return;
        }
        else {
            Initializeview();
            getCominguserinfo();
            setOnclicklistnener();
        }

    }


    private void setOnclicklistnener() {

    editandapply.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(editandapply.getText().toString().equalsIgnoreCase("Edit")){
                editandapply.setText("Apply");
               firstnamevalue.setEnabled(true);
               lastnamevalue.setEnabled(true);
               phonenumbervalue.setEnabled(true);
            }
            else{
                editandapply.setText("Edit");
                firstnamevalue.setEnabled(false);
                lastnamevalue.setEnabled(false);
                phonenumbervalue.setEnabled(false);

                updatefirstname = firstnamevalue.getText().toString().trim();
                updatelastname = lastnamevalue.getText().toString().trim();
                updatephonenumber = phonenumbervalue.getText().toString().trim();

                if(!validateForm()){

                }
                else {
                    saveProfiletask saveProfiletask = new saveProfiletask();
                    saveProfiletask.execute();
                }

            }
        }
    });
    }

    private void Initializeview(){

        firstnamevalue = view.findViewById(R.id.accountfirstnamevalue);

        lastnamevalue = view.findViewById(R.id.accountlastnamevalue);

        phonenumbervalue = view.findViewById(R.id.accountphonenumbervalue);

        emailvalue = view.findViewById(R.id.accountemailvalue);
        accountpicture = view.findViewById(R.id.accountpicture);
        editandapply = view.findViewById(R.id.accounteditandapply);
        firstname_layout = view.findViewById(R.id.account_firstname_layout);
        lastname_layout = view.findViewById(R.id.account_lastname_layout);
        phone_layout = view.findViewById(R.id.account_phone_layout);
        Typeface customfont2= Typeface.createFromAsset(getActivity().getAssets(),"Kylo-Light.otf");
        Typeface customfont= Typeface.createFromAsset(getActivity().getAssets(),"Kylo-Regular.otf");
        firstnamevalue.setTypeface(customfont);
        emailvalue.setTypeface(customfont);
        lastnamevalue.setTypeface(customfont);
        phonenumbervalue.setTypeface(customfont);
        editandapply.setTypeface(customfont);
        firstname_layout.setTypeface(customfont);
        lastname_layout.setTypeface(customfont);
        phone_layout.setTypeface(customfont);
        firstnamevalue.setEnabled(false);
        lastnamevalue.setEnabled(false);
        phonenumbervalue.setEnabled(false);
        MainActivity.changedisplaysearch(View.GONE);


    }

    private void getCominguserinfo(){
        firstname = userprofiles.get(0).getFirstname();
        lastname = userprofiles.get(0).getLastname();
        email = userprofiles.get(0).getEmail();
        phonenumber = userprofiles.get(0).getPhonenumber();
        picturepath = userprofiles.get(0).getProfileimage();
        firstnamevalue.setText(firstname);
        lastnamevalue.setText(lastname);
        emailvalue.setText(email);
        phonenumbervalue.setText(phonenumber);
        getComingProfilepicturePath(userprofiles);
        formatphone();

    }

    private void formatphone(){
        phonenumber = userprofiles.get(0).getPhonenumber();

        if(phonenumber.startsWith("0")){
            phonenumbervalue.setText(phonenumber);
        }
        else{
            phonenumbervalue.setText("+234"+phonenumber);
        }
    }

    private void getComingProfilepicturePath(ArrayList<userprofile> userprofile){

        if(userprofile != null) {
            String ImageUri = userprofile.get(0).getProfileimage();
            Uri imageuri = Uri.parse(ImageUri);
            ImageRequest request = ImageRequest.fromUri(imageuri);
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(request)
                    .setOldController(accountpicture.getController()).build();
            accountpicture.setController(controller);
        }
    }

    public class saveProfiletask extends AsyncTask {
        String prompt;
        String urlpath = "http://jl-market.com/user/usersaveProfile.php";

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
            String serverresponse = new updateProfile().GetData(urlpath, email, updatefirstname, updatelastname, updatephonenumber);
            if (serverresponse != null) {
                try {
                    JSONObject jsonObject = new JSONObject(serverresponse);
                    JSONObject info = jsonObject.getJSONObject("info");
                    String status = info.getString("status");

                    if (status.equalsIgnoreCase("Profile updated successfully")) {
                        prompt = "Profile updated";
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
            //Toast.makeText(getContext(), result.toString(), Toast.LENGTH_SHORT).show();
             if(result == null){
             Toast.makeText(getContext(), "Error Occured", Toast.LENGTH_SHORT).show();
         }
         else if(result.toString().equalsIgnoreCase(prompt)){
             Toast.makeText(getContext(), prompt, Toast.LENGTH_SHORT).show();
         }

        }

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
    private Boolean isNetworkAvailable() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void showloadingdialog() {
        loadingdialog = new Dialog(getContext(), R.style.Dialog_Theme);
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
        boolean valid = true;

        if(TextUtils.isEmpty(updatefirstname)){
            firstname_layout.setError("Required");
            valid = false;
        }
        else{
            firstname_layout.setError(null);
        }

        if(TextUtils.isEmpty(updatelastname)){
            lastname_layout.setError("Required");
            valid = false;
        }
        else{
            lastname_layout.setError(null);
        }

        if(TextUtils.isEmpty(updatephonenumber)){
            phone_layout.setError("Required");
            valid = false;
        }
        else{
            phone_layout.setError(null);
        }

        return valid;
    }

}
