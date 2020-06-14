package com.electonicmarket.android.emarket.Activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;

import com.electonicmarket.android.emarket.Fragments.deliverylocationArea;
import com.electonicmarket.android.emarket.Fragments.deliverylocationcity;
import com.electonicmarket.android.emarket.R;
import com.electonicmarket.android.emarket.splashscreen;

public class ChooseDeliveryLocation extends AppCompatActivity implements deliverylocationcity.onCitySelected,deliverylocationArea.onAreaSelected {

    String cityname,areaname;
    Double longitude,latitude;
    Toolbar toolbar;
    TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_delivery_location);
        initview();
        if(savedInstanceState != null){

            if(savedInstanceState.getInt("pid",-1) == android.os.Process.myPid()){

            }
            else{

                startActivity(new Intent(ChooseDeliveryLocation.this,splashscreen.class));

            }
        }

        Fragment fragment =   deliverylocationcity.newInstance();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.deliverylocationcontentframe, fragment);
        fragmentTransaction.commit();

    }

    private void initview(){
        toolbar = findViewById(R.id.deliverylocationtoolbar);
        title = findViewById(R.id.deliverylocationtoolbartitle);
        Typeface customfont= Typeface.createFromAsset(getAssets(),"Kylo-Light.otf");
        title.setTypeface(customfont);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.White), PorterDuff.Mode.SRC_ATOP);



    }

    @Override
    public void passCity(String data) {
        cityname = data;
        AuthSelection();
    }

    @Override
    public void passArea(String data,double log, double lat) {
        areaname = data;
        longitude = log;
        latitude = lat;
        AuthSelection();
    }
    private void AuthSelection(){
        if(cityname != null && areaname != null && longitude != 0 && latitude != 0){
            Intent intent = new Intent();
            intent.putExtra("city",cityname);
            intent.putExtra("area",areaname);
            intent.putExtra("longitude",longitude);
            intent.putExtra("latitude",latitude);
            intent.putExtra("flag1","flag1");
            setResult(1,intent);
            finish();
        }
        else{
            Fragment fragment =   deliverylocationArea.newInstance(cityname);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.exit_to_right,0 );
            fragmentTransaction.replace(R.id.deliverylocationcontentframe, fragment);
            fragmentTransaction.commit();

        }

    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        setResult(0);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        if (menuItem.getItemId() == android.R.id.home) {

            this.onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onSaveInstanceState(Bundle outstate){
        super.onSaveInstanceState(outstate);
        outstate.putInt("pid",android.os.Process.myPid());

    }

}

