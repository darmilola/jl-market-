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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.electonicmarket.android.emarket.Adapter.DealAdapter;
import com.electonicmarket.android.emarket.Models.vendordisplayitem;
import com.electonicmarket.android.emarket.Models.vendorinfodeal;
import com.electonicmarket.android.emarket.R;
import com.electonicmarket.android.emarket.splashscreen;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

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

public class vendorinformation extends AppCompatActivity {
    public GoogleMap googleMap;
    public MapView mapView;
    TextView title,openinghourtitle, openinghourvalue, paymentmethodtitle, paymentmethoddebitcard,
            paymentmethodcash, ratevendortitle, submitrate, dealstitle;
    ArrayList<vendordisplayitem> vendorinfo;
    String openinghr, closinghr, cashpayment, cardpayment;
    double latitude, longitude;
    RecyclerView recyclerView;
    Toolbar toolbar;
    LinearLayout ratinglayout;
    String useremail;
    String ratingvalue = "0";
    RatingBar ratingBar;
    Dialog loadingdialog;
    ProgressBar dealprogressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendorinformation);
        processintent();
        initializeView();
        setOnclickListener();
        if(savedInstanceState != null){

            if(savedInstanceState.getInt("pid",-1) == android.os.Process.myPid()){

            }
            else{

                startActivity(new Intent(vendorinformation.this,splashscreen.class));

            }
        }
        mapView.onCreate(savedInstanceState);
        LatLng latLng = new LatLng(latitude, longitude);
        InitializeMap(latLng);
        initializerecyclerview();
        displaydealfromserver displaydealfromserver = new displaydealfromserver();
        displaydealfromserver.execute();
        new verifyrated().execute();

    }

    private void initializerecyclerview() {
        recyclerView = (RecyclerView) findViewById(R.id.vendorinfodealrecyclerview);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

    }

    private void processintent() {

        if (getIntent().getParcelableArrayListExtra("vendorinfo") != null) {
            vendorinfo = getIntent().getParcelableArrayListExtra("vendorinfo");
        }

        if(getIntent().getStringExtra("useremail") != null){
          useremail = getIntent().getStringExtra("useremail");
        }
    }




    private void setOnclickListener(){
     submitrate.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             //Toast.makeText(vendorinformation.this, ratingvalue, Toast.LENGTH_SHORT).show();
           new submitrating().execute();
         }
     });

    ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
        @Override
        public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
            ratingvalue = String.valueOf(rating);
        }
    });


    }
    private void initializeView() {
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.screenBrightness = 0.5f;
        getWindow().setAttributes(layoutParams);
        mapView = findViewById(R.id.vendorinformationmap);
        ratingBar = findViewById(R.id.vendorinformationratingbar);
        dealprogressbar = findViewById(R.id.dealprogressbar);

        openinghourtitle = findViewById(R.id.vendorinformationopeninghourtitle);
        openinghourvalue = findViewById(R.id.vendorinformationopeninghourvalue);
        paymentmethodtitle = findViewById(R.id.vendorinformationpaymentmethodstitle);
        paymentmethoddebitcard = findViewById(R.id.vendorinformationpaymentmethoddebitcard);
        paymentmethodcash = findViewById(R.id.vendorinformationpaymentmethodcash);
        ratevendortitle = findViewById(R.id.vendorinformationratethisvendor);
        submitrate = findViewById(R.id.vendorinformationsubmitrate);
        dealstitle = findViewById(R.id.vendorinformationdealtitle);
        toolbar = findViewById(R.id.vendorinformationtoolbar);
        title = findViewById(R.id.vendorinfotoolbartitle);
        ratinglayout = findViewById(R.id.ratinglayout);

        Typeface customfont = Typeface.createFromAsset(getAssets(), "Kylo-Light.otf");
        openinghourtitle.setTypeface(customfont);
        title.setTypeface(customfont);
        openinghourvalue.setTypeface(customfont);
        paymentmethodtitle.setTypeface(customfont);
        paymentmethoddebitcard.setTypeface(customfont);
        paymentmethodcash.setTypeface(customfont);
        ratevendortitle.setTypeface(customfont);
        submitrate.setTypeface(customfont);
        dealstitle.setTypeface(customfont);

        vendordisplayitem vendordisplayitem = vendorinfo.get(0);
        openinghr = vendordisplayitem.getOpeninghour();
        closinghr = vendordisplayitem.getClosinghour();
        cashpayment = vendordisplayitem.getCashpayment();
        cardpayment = vendordisplayitem.getCardpayment();
        latitude = vendordisplayitem.getLatitude();
        longitude = vendordisplayitem.getLongitude();
        String vendorname = vendordisplayitem.getVendorname();
        title.setText(vendorname);
        showformattedtime();
        showdeliverymethod();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.White), PorterDuff.Mode.SRC_ATOP);



    }


    private void showdeliverymethod() {
        if (cashpayment.equalsIgnoreCase("yes")) {
            paymentmethodcash.setVisibility(View.VISIBLE);
        } else {
            paymentmethodcash.setVisibility(View.GONE);
        }

        if (cardpayment.equalsIgnoreCase("yes")) {
            paymentmethoddebitcard.setVisibility(View.VISIBLE);
        } else {
            paymentmethoddebitcard.setVisibility(View.GONE);
        }
    }

    private void showformattedtime() {
        String formattedophr, formattedcloshr;
        if (openinghr.length() > 1) {
            formattedophr = openinghr + ":" + "00";
        } else {
            formattedophr = "0" + openinghr + ":" + "00";
        }
        if (closinghr.length() > 1) {
            formattedcloshr = closinghr + ":" + "00";
        } else {
            formattedcloshr = "0" + closinghr + ":" + "00";
        }
        openinghourvalue.setText(formattedophr + " - " + formattedcloshr);
    }

    private void InitializeMap(LatLng latLng) {
        if (googleMap == null) {

            mapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap Map) {

                    googleMap = Map;
                    googleMap.clear();


                    googleMap.addMarker(new MarkerOptions().position(latLng)
                            //.title("Victoria Island")
                            .draggable(false));

                    moveTocurrentposition(latLng);
                    mapView.onResume();

                }
            });


        }
    }

    private void moveTocurrentposition(LatLng currentlocation) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentlocation, 15));
        googleMap.animateCamera(CameraUpdateFactory.zoomIn());
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 5000, null);
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onResume() {
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
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    public class displaydealfromserver extends AsyncTask {

        private String displayurl = "http://jl-market.com/vendor/displaydeals.php";

        String prompt;

        @Override
        protected void onPreExecute() {

            //Toast.makeText(vendorinformation.this, "Loading deals", Toast.LENGTH_LONG).show();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            if (!isNetworkAvailable()) {
                prompt = "No Network Connection";
                return prompt;
            }
            String serverresponse = new displaydeals().GetData(displayurl, vendorinfo.get(0).getEmail());
            if (serverresponse != null) {
                try {
                    JSONObject jsonObject = new JSONObject(serverresponse);
                    JSONObject info = jsonObject.getJSONObject("info");
                    String status = info.getString("status");

                    if (status.equalsIgnoreCase("deals unavailable")) {
                        prompt = "No deal Available for this vendor";
                        return prompt;
                    } else if (status.equalsIgnoreCase("dealsavailable")) {

                        ArrayList<vendorinfodeal> dealmodelArrayList = ParseJson(info);
                        return dealmodelArrayList;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return serverresponse;
        }

        protected void onPostExecute(Object result) {
            dealprogressbar.setVisibility(View.GONE);
            if (result == null) {
                Toast.makeText(vendorinformation.this, "Error Occured", Toast.LENGTH_LONG).show();
            } else if (result.toString().equalsIgnoreCase(prompt)) {
                Toast.makeText(vendorinformation.this, prompt, Toast.LENGTH_LONG).show();
            } else if (result != null) {
                if (result instanceof ArrayList) {
                    ArrayList<vendorinfodeal> dealmodels = (ArrayList<vendorinfodeal>) result;
                    DealAdapter adapter = new DealAdapter(dealmodels, vendorinformation.this);
                    recyclerView.setAdapter(adapter);


                }

            }

        }

    }
    public class displaydeals {


        public String GetData(String url, String vendorid) {

            try {
                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(50, TimeUnit.SECONDS)
                        .writeTimeout(50, TimeUnit.SECONDS)
                        .readTimeout(50, TimeUnit.SECONDS)
                        .build();

                RequestBody formBody = new FormBody.Builder()
                        .add("vendorid", vendorid)
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

        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private ArrayList<vendorinfodeal> ParseJson(JSONObject jsonObject) throws JSONException {
        ArrayList<vendorinfodeal> dealmodels =  new ArrayList<>();
        JSONArray jsonArray = jsonObject.getJSONArray("data");
        for (int i = 0; i < jsonArray.length(); ++i) {
            JSONObject j = jsonArray.getJSONObject(i);
            String description = j.getString("description");
            String id = j.getString("id");
            vendorinfodeal dealmodel = new vendorinfodeal(description);
            dealmodels.add(dealmodel);
        }
        return dealmodels;

    }

    public class verifyrated extends AsyncTask {

        String prompt;
        String url = "http://jl-market.com/user/checkrated.php";

        @Override
        protected Object doInBackground(Object[] objects) {
            if (!isNetworkAvailable()) {
                prompt = "No Network Connection";
                return prompt;
            }
            String serverresponse = new verifyrating().GetData(url, vendorinfo.get(0).getEmail(),useremail);
            if (serverresponse != null) {
                try {
                    JSONObject jsonObject = new JSONObject(serverresponse);
                    JSONObject info = jsonObject.getJSONObject("info");
                    String status = info.getString("status");

                    if (status.equalsIgnoreCase("rated")) {
                        prompt = "rated";
                        return prompt;
                    } else if (status.equalsIgnoreCase("notrated")) {
                        prompt = "notrated";
                        return prompt;
                        }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        protected void onPostExecute(Object result) {
            if (result == null) {
        //        Toast.makeText(vendorinformation.this, "Unable to ", Toast.LENGTH_LONG).show();
            } else if (result.toString().equalsIgnoreCase("No Network Connection")) {
                Toast.makeText(vendorinformation.this, "No Network Connection", Toast.LENGTH_LONG).show();
            }
            else if(result.toString().equalsIgnoreCase("rated")){

                //Toast.makeText(vendorinformation.this, "rated", Toast.LENGTH_SHORT).show();
                ratinglayout.setVisibility(View.GONE);
            }
            else if(result.toString().equalsIgnoreCase("notrated")){
                //Toast.makeText(vendorinformation.this, "notrated", Toast.LENGTH_SHORT).show();
                 ratinglayout.setVisibility(View.VISIBLE);
            }
        }

    }

    public class verifyrating {


        public String GetData(String url, String vendorid,String userid) {

            try {
                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(50, TimeUnit.SECONDS)
                        .writeTimeout(50, TimeUnit.SECONDS)
                        .readTimeout(50, TimeUnit.SECONDS)
                        .build();

                RequestBody formBody = new FormBody.Builder()
                        .add("vendorid", vendorid)
                        .add("userid",userid)
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

    public class submitrating extends AsyncTask {

        String prompt;
        String url = "http://jl-market.com/user/ratevendor.php";


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
            String serverresponse = new submitratingvalue().GetData(url, vendorinfo.get(0).getEmail(), useremail, ratingvalue);

            if (serverresponse != null) {
                try {
                    JSONObject jsonObject = new JSONObject(serverresponse);
                    JSONObject info = jsonObject.getJSONObject("info");
                    String status = info.getString("status");

                    if (status.equalsIgnoreCase("rating successful")) {
                        prompt = "rating successful";
                        return prompt;
                    } else if (status.equalsIgnoreCase("rating not successful")) {
                        prompt = "rating not successful";
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
                Toast.makeText(vendorinformation.this, "Error Occured", Toast.LENGTH_LONG).show();
            } else if (result.toString().equalsIgnoreCase("No Network Connection")) {
                Toast.makeText(vendorinformation.this, "No Network Connection", Toast.LENGTH_LONG).show();
            } else if (result.toString().equalsIgnoreCase("rating successful")) {

                Toast.makeText(vendorinformation.this, "Vendor rated", Toast.LENGTH_SHORT).show();
                ratinglayout.setVisibility(View.GONE);
            } else if (result.toString().equalsIgnoreCase("rating not successful")) {
                Toast.makeText(vendorinformation.this, "Error Occured", Toast.LENGTH_SHORT).show();
                ratinglayout.setVisibility(View.VISIBLE);
            }
        }
    }
        public class submitratingvalue {


            public String GetData(String url, String vendorid, String userid, String ratingvalue) {

                try {
                    OkHttpClient client = new OkHttpClient.Builder()
                            .connectTimeout(50, TimeUnit.SECONDS)
                            .writeTimeout(50, TimeUnit.SECONDS)
                            .readTimeout(50, TimeUnit.SECONDS)
                            .build();

                    RequestBody formBody = new FormBody.Builder()
                            .add("vendorid", vendorid)
                            .add("userid", userid)
                            .add("ratevalue", ratingvalue)
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


