package com.electonicmarket.android.emarket.Fragments;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;


import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.electonicmarket.android.emarket.Activities.ChooseDeliveryLocation;
import com.electonicmarket.android.emarket.Activities.FullMapActivity;
import com.electonicmarket.android.emarket.Activities.MainActivity;
import com.electonicmarket.android.emarket.Models.userprofile;
import com.electonicmarket.android.emarket.splashscreen;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.electonicmarket.android.emarket.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class DeliveryAddress extends Fragment {

    Button SeeVendor;
    Typeface customfont;
    View view;
    public GoogleMap googleMap;
    TextView setpinlocation,deliveryaddress;
    TextInputEditText city,area,address;
    MapView mapView;
    double longitude;
    double latitude;
    String cityvalue,areavalue,addressvalue;
    int seevendorflag = 0;
    static ArrayList<userprofile> userprofiles;


    public DeliveryAddress() {
        // Required empty public constructor
    }
   public static DeliveryAddress newInstance(ArrayList<userprofile> userprofileArrayList){
        DeliveryAddress deliveryAddress = new DeliveryAddress();
        userprofiles = userprofileArrayList;
        return deliveryAddress;
   }

    @Override
    public void onActivityCreated(Bundle savedinstance){
        super.onActivityCreated(savedinstance);
        if(userprofiles == null){
            Intent intent = new Intent(getContext(),splashscreen.class);
            startActivity(intent);
            return;
        }
        else {


            customfont= Typeface.createFromAsset(getActivity().getAssets(),"Kylo-Light.otf");
            setCustomfont(customfont);
            setOnclickListener();

        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       view = inflater.inflate(R.layout.fragment_delivery_address, container, false);
      //  Log.e("size",  Integer.toString(userprofiles.size()) );


        InitializeView();
        mapView.onCreate(savedInstanceState);



        return view;
    }

   private void processdeliveryAddress(){
        if(!userprofiles.get(0).getCity().equalsIgnoreCase("")) {
            //check if there is already value for delivery address
            Log.e("am here e", "processdeliveryAddress: ");
            city.setText(userprofiles.get(0).getCity());
            area.setText(userprofiles.get(0).getArea());
            address.setText(userprofiles.get(0).getAddress());
             latitude = userprofiles.get(0).getLatitude();
             longitude = userprofiles.get(0).getLongitude();
            //double lat = Double.parseDouble(latstr);
            //double log =  Double.parseDouble(longstr);
            //LatLng latLng = new LatLng(lat,log);
            InitializeMapForCoordinate(latitude,longitude);
            MainActivity.changeshowvendor(true);


        }
        else{
            InitializeMap();
            MainActivity.changeshowvendor(false);

        }
   }


    private void setOnclickListener() {

        city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ChooseDeliveryLocation.class);
                startActivityForResult(intent,1);
            }
        });
        SeeVendor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(seevendorflag == 0) {
                    seevendorflag = 1;
                    addressvalue = address.getText().toString();
                    cityvalue = city.getText().toString();
                    areavalue = area.getText().toString();
                    if (cityvalue.equalsIgnoreCase("") || areavalue.equalsIgnoreCase("") || addressvalue.equalsIgnoreCase("")) {
                        seevendorflag = 0;
                        Toast.makeText(getContext(), "Please give your delivery address", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(getContext(), MainActivity.class);
                        intent.putExtra("fromlocation", "fromlocation");
                        intent.putExtra("longitude", longitude);
                        intent.putExtra("latitude", latitude);
                        intent.putExtra("city", cityvalue);
                        intent.putExtra("area", areavalue);
                        intent.putExtra("address", addressvalue);
                        intent.putExtra("email", userprofiles.get(0).getEmail());
                        intent.putParcelableArrayListExtra("userprofile", userprofiles);
                        startActivity(intent);
                    }
                }
            }


        });
    }

    @Override
    public void onStop() {

        super.onStop();
        seevendorflag = 0;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == 1) {
             cityvalue = data.getStringExtra("city");
             areavalue = data.getStringExtra("area");

            longitude = data.getDoubleExtra("longitude",0);
            latitude = data.getDoubleExtra("latitude",0);


            city.setText(cityvalue);
            area.setText(areavalue);

            LatLng latLng = new LatLng(latitude, longitude);
            //Toast.makeText(getContext(), Double.toString(latitude) + " "+ Double.toString(longitude), Toast.LENGTH_LONG).show();
            mapView.invalidate();
            InitializeMapAfterActivityResult(latitude,longitude);

        }
        if(requestCode == 2) {
            if (resultCode == 100) {

            } else {
                latitude = data.getDoubleExtra("latitude", 0); //coming from userclickign onmap
                longitude = data.getDoubleExtra("longitude", 0);
                LatLng latLng = new LatLng(latitude, longitude);
                InitializeMapAfterActivityResult(latitude, longitude);
            }
        }

        if(resultCode == 0){

        }

    }

    private void InitializeMapAfterActivityResult(double lat, double log){


        mapView.getMapAsync( new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap Map) {
                googleMap.clear();
                googleMap = Map;


                googleMap.addMarker(new MarkerOptions().position(new LatLng(lat,log))
                        //.title("Victoria Island")
                        .draggable(false));

                moveTocurrentposition(new LatLng(lat,log));
                mapView.onResume();
                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        Intent intent = new Intent(getContext(),FullMapActivity.class);
                        if (latitude == 0 && longitude == 0) {
                            startActivityForResult(intent, 2);//user click map firsttime
                        }
                        else{
                            Log.e("startinh here", "onMapClick: ");
                            intent.putExtra("latitude",latitude);
                            intent.putExtra("longitude",longitude);

                            startActivityForResult(intent,2);
                        }
                    }
                });

            }
        });



    }
    private void InitializeMap(){
        if(googleMap == null){

            mapView.getMapAsync( new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap Map) {

                    googleMap = Map;
                    googleMap.clear();


                    googleMap.addMarker(new MarkerOptions().position(new LatLng(6.4253,3.4219))
                            //.title("Victoria Island")
                            .draggable(false));

                    moveTocurrentposition(new LatLng(6.4253,3.4219));
                    mapView.onResume();
                    googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                        @Override
                        public void onMapClick(LatLng latLng) {
                            Intent intent = new Intent(getContext(),FullMapActivity.class);
                            if (latitude == 0 && longitude == 0) {
                                startActivityForResult(intent, 2);//user click map firsttime
                            }
                            else{
                                Log.e("startinh here", "onMapClick: ");
                                intent.putExtra("latitude",latitude);
                                intent.putExtra("longitude",longitude);

                                startActivityForResult(intent,2);
                            }
                        }
                    });

                }
            });


        }
    }
    private void InitializeMapForCoordinate(double lat, double log){
        if(googleMap == null){

            mapView.getMapAsync( new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap Map) {

                    googleMap = Map;
                    googleMap.clear();


                    googleMap.addMarker(new MarkerOptions().position(new LatLng(lat,log))
                            //.title("Victoria Island")
                            .draggable(false));

                    moveTocurrentposition(new LatLng(lat,log));
                    mapView.onResume();
                    googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                        @Override
                        public void onMapClick(LatLng latLng) {
                            Intent intent = new Intent(getContext(),FullMapActivity.class);
                            if (latitude == 0 && longitude == 0) {
                                startActivityForResult(intent, 2);//user click map firsttime
                            }
                            else{
                                Log.e("startinh here", "onMapClick: ");
                                intent.putExtra("latitude",latitude);
                                intent.putExtra("longitude",longitude);

                                startActivityForResult(intent,2);
                            }
                        }
                    });

                }
            });


        }
    }

    private void moveTocurrentposition(LatLng currentlocation){
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentlocation,15));
        googleMap.animateCamera(CameraUpdateFactory.zoomIn());
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15),5000,null);
    }

    private void InitializeView(){

        setpinlocation = view.findViewById(R.id.setPinLocation);
        deliveryaddress = view.findViewById(R.id.innerdeliveryAddress);
        city = view.findViewById(R.id.city);
        area = view.findViewById(R.id.area);
        address = view.findViewById(R.id.address);
        mapView = view.findViewById(R.id.deliverylocationmap);
        SeeVendor = view.findViewById(R.id.seeVendor);
        MainActivity.changedisplaysearch(View.GONE);
        processdeliveryAddress();
    }

    public void setCustomfont(Typeface customfont) {
        setpinlocation.setTypeface(customfont);
        deliveryaddress.setTypeface(customfont);
        city.setTypeface(customfont);
        area.setTypeface(customfont);
        address.setTypeface(customfont);
        SeeVendor.setTypeface(customfont);
    }
    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onResume(){
        super.onResume();
        mapView.onResume();
        // if(googleMap != null){
        //googleMap1.clear();
        // UserSelectionAfterAreaInitializeMap(latitude,longitude);
        //}
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onStart() {

        super.onStart();
        mapView.onStart();
    }
    @Override
    public void onLowMemory(){
        super.onLowMemory();
        mapView.onLowMemory();
    }


}
