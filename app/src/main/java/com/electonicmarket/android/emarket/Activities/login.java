package com.electonicmarket.android.emarket.Activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.electonicmarket.android.emarket.Fragments.Login;
import com.electonicmarket.android.emarket.Models.BlurBuilder;
import com.electonicmarket.android.emarket.R;


public class login extends AppCompatActivity {
 Toolbar toolbar;
 LinearLayout container;
 EditText email,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        initializeView();




        //actionBar.hide();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){

        if(menuItem.getItemId() == android.R.id.home){

            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void initializeView(){
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.screenBrightness = 0.5f;
        getWindow().setAttributes(layoutParams);
        container = (LinearLayout) findViewById(R.id.container);
        Bitmap original = BitmapFactory.decodeResource(getResources(),R.drawable.studentpic);
        Bitmap blurr = BlurBuilder.blur(login.this,original);
        container.setBackground(new BitmapDrawable(getResources(),blurr));
        Fragment fragment = new Login();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.logincontentframe,fragment);
        fragmentTransaction.commit();
        toolbar = (Toolbar) findViewById(R.id.loginpagetoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);

    }


}
