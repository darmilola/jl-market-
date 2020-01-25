package com.electonicmarket.android.emarket.Activities;


import android.content.Intent;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.SystemClock;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.Button;

import com.electonicmarket.android.emarket.R;
import com.electonicmarket.android.emarket.splashscreen;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class FullMapActivity extends AppCompatActivity {
   GoogleMap googleMap;
   double latitude,longitude;
   Button savepin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_map);
        initializeView();
        setOnclickListener();
        if(savedInstanceState != null){

            if(savedInstanceState.getInt("pid",-1) == android.os.Process.myPid()){

            }
            else{

                startActivity(new Intent(FullMapActivity.this,splashscreen.class));

            }
        }
        latitude = getIntent().getDoubleExtra("latitude",0);
        longitude = getIntent().getDoubleExtra("longitude",0);
        if(latitude != 0  && longitude != 0) {
            LatLng latLng = new LatLng(latitude, longitude);
            Initializemapwithlatlng(latLng);

            //Toast.makeText(this, Double.toString(latitude) + " " + Double.toString(longitude), Toast.LENGTH_SHORT).show();
        }
        else {
            InitializeMap();
        }

    }

    private void initializeView(){
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.screenBrightness = 0.5f;
        getWindow().setAttributes(layoutParams);
        savepin = findViewById(R.id.savepin);
        Typeface customfont= Typeface.createFromAsset(getAssets(),"Kylo-Light.otf");
        savepin.setTypeface(customfont);

    }
    private void setOnclickListener(){

        savepin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("latitude",latitude);
                intent.putExtra("longitude",longitude);
                intent.putExtra("flagfrommapselect","flag");
                setResult(1,intent);
                finish();

            }
        });
    }


    private void Initializemapwithlatlng(LatLng latLng1){
        if(googleMap == null){

            ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(Map -> {
                googleMap = Map;
                final MarkerOptions markerOptions = new MarkerOptions()
                        .position(latLng1)
                        .title("Institution");
                Marker m = googleMap.addMarker(markerOptions);

                m.setPosition(latLng1);
                //  CameraUpdate current = CameraUpdateFactory.newLatLngZoom(new LatLng(6.4253,3.4219),15);
                //googleMap.animateCamera(current);
                CameraUpdate current =  CameraUpdateFactory.newLatLngZoom(latLng1,15);
                googleMap.animateCamera(CameraUpdateFactory.zoomIn());
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(15),4000,null);
                googleMap.animateCamera(current);
                //moveTocurrentposition(new LatLng(6.4253,3.4219));

                googleMap.getUiSettings().setZoomControlsEnabled(true);
                googleMap.setPadding(10,10,10,90);

                googleMap.setOnMapClickListener(latLng -> {
                    googleMap.clear();
                    moveTocurrentposition(latLng);


                    //Toast.makeText(this, Double.toString(latLng.latitude)+" "+Double.toString(latLng.longitude), Toast.LENGTH_SHORT).show();
                    animateMarker(m,latLng,false);

                    Marker m2 = googleMap.addMarker(markerOptions);
                    m2.setPosition(latLng);
                    latitude = latLng.latitude;  //asign long and lat to latiutde and longitude so as ro send to set up shop
                    longitude = latLng.longitude;
                    //Toast.makeText(Fullmap.this,lat+" "+log,Toast.LENGTH_LONG).show();
                });


            });
        }
    }


    private void InitializeMap(){
        if(googleMap == null){

            ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(Map -> {
                googleMap = Map;
                final MarkerOptions markerOptions = new MarkerOptions()
                        .position(new LatLng(6.4253,3.4219))
                        .title("Victoria Island");
                Marker m = googleMap.addMarker(markerOptions);

                m.setPosition(new LatLng(6.4253,3.4219));

                moveTocurrentposition(new LatLng(6.4253,3.4219));


                googleMap.getUiSettings().setZoomControlsEnabled(true);
                googleMap.setPadding(10,10,10,90);


                googleMap.setOnMapClickListener(latLng -> {
                    googleMap.clear();
                    moveTocurrentposition(latLng);


                    //Toast.makeText(this, Double.toString(latLng.latitude)+" "+Double.toString(latLng.longitude), Toast.LENGTH_SHORT).show();
                    animateMarker(m,latLng,false);

                    Marker m2 = googleMap.addMarker(markerOptions);
                    m2.setPosition(latLng);
                    latitude = latLng.latitude;  //asign long and lat to latiutde and longitude so as ro send to set up shop
                    longitude = latLng.longitude;
                    //Toast.makeText(Fullmap.this,lat+" "+log,Toast.LENGTH_LONG).show();
                });



            });
        }
    }

    private void moveTocurrentposition(LatLng currentlocation){
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentlocation,15));
        googleMap.animateCamera(CameraUpdateFactory.zoomIn());
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15),20000,null);
    }

    private void animateMarker(final Marker marker,final LatLng toposition,final boolean hideMarker){

        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection projection = googleMap.getProjection();
        Point startPoint = projection.toScreenLocation(marker.getPosition());
        final LatLng  startLatlng = projection.fromScreenLocation(startPoint);
        long duration = 1000;
        final LinearInterpolator interpolator = new LinearInterpolator();
        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis()- start;
                float t = interpolator.getInterpolation((float)elapsed/duration);

                double lng = t * toposition.longitude + (1-t) * startLatlng.longitude;
                double lat = t * toposition.latitude +(1-t) * startLatlng.latitude;
                marker.setPosition(new LatLng(lat,lng));
                if(t<1.0){
                    handler.postDelayed(this,16);
                }
                else {
                    if(hideMarker){
                        marker.setVisible(false);
                    }
                    else{
                        marker.setVisible(true);
                    }
                }

            }
        });
    }

 @Override
    public void onBackPressed(){
        setResult(100);
        finish();
 }
    @Override
    public void onSaveInstanceState(Bundle outstate){
        super.onSaveInstanceState(outstate);
        outstate.putInt("pid",android.os.Process.myPid());

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        if (menuItem.getItemId() == android.R.id.home) {

           this.onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }

}
