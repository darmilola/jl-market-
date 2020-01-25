package com.electonicmarket.android.emarket;

import android.content.Intent;
import android.graphics.Typeface;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.electonicmarket.android.emarket.Fragments.openingpagefragment;
import com.electonicmarket.android.emarket.Models.MyApplication;

public class openingpageActivity extends AppCompatActivity  {
    Button login,signup;
    TextView alreadyhaveaccount;
    protected MyApplication myApplication;
    int loginflag=0,signupflag=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_openingpage);
        InitializeView();
        if(savedInstanceState != null) {

            if (savedInstanceState.getInt("pid", -1) == android.os.Process.myPid()) {

            } else {

                startActivity(new Intent(openingpageActivity.this, splashscreen.class));

            }

        }
  login.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
          if(loginflag == 0){
              loginflag = 1;
              Intent intent = new Intent(openingpageActivity.this, com.electonicmarket.android.emarket.Activities.login.class);
              startActivity(intent);
          }

      }
  });

  signup.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
          if(signupflag == 0) {
              signupflag = 1;
              Intent intent = new Intent(openingpageActivity.this, com.electonicmarket.android.emarket.Activities.signup.class);
              startActivity(intent);
          }
      }
  });

    }

    private void InitializeView(){

        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.screenBrightness = 1.0f;
        getWindow().setAttributes(layoutParams);
        //ActionBar actionBar = getSupportActionBar();
        // actionBar.hide();
        login = findViewById(R.id.login);
        signup = findViewById(R.id.signup);
        alreadyhaveaccount = findViewById(R.id.alreadyhaveanaccount);

        Typeface customfont2= Typeface.createFromAsset(getAssets(),"Kylo-Light.otf");
        login.setTypeface(customfont2);
        signup.setTypeface(customfont2);
        alreadyhaveaccount.setTypeface(customfont2);
        Fragment fragment = new openingpagefragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.openingpagefragmentcontent,fragment);
        fragmentTransaction.commit();
    }

    @Override
public void onBackPressed(){

}
  @Override
    public void onSaveInstanceState(Bundle outstate){
        super.onSaveInstanceState(outstate);
        outstate.putInt("pid",android.os.Process.myPid());
        }
        @Override
    public void onPause() {
            super.onPause();
            Log.e("here", "onPause: ");
        }
        @Override
    public void onResume() {

            super.onResume();
            Log.e("here", "onResume:" );
        }
        @Override
    public void onStop() {
           loginflag = 0;
           signupflag = 0;
            super.onStop();

        }
        @Override
    public void onDestroy() {

            super.onDestroy();
            Log.e("here", "onDestroy: " );
        }
}