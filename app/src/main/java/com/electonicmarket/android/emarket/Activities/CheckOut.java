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
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.electonicmarket.android.emarket.Adapter.completeOrderAdapter;
import com.electonicmarket.android.emarket.Models.CheckOutUserModel;
import com.electonicmarket.android.emarket.Models.OrderModel;
import com.electonicmarket.android.emarket.Models.StaticMapFragment;
import com.electonicmarket.android.emarket.Models.userprofile;
import com.electonicmarket.android.emarket.R;
import com.electonicmarket.android.emarket.splashscreen;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import co.paystack.android.Paystack;
import co.paystack.android.PaystackSdk;
import co.paystack.android.Transaction;
import co.paystack.android.exceptions.InvalidEmailException;
import co.paystack.android.model.Card;
import co.paystack.android.model.Charge;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CheckOut extends AppCompatActivity {

    androidx.appcompat.widget.Toolbar toolbar;
    TextView title;
    Button placeOrder;
    public GoogleMap googleMap;
    View view;
    ScrollView parentview;
    RecyclerView recyclerView;
    LinearLayout progressbarlayout;
    ArrayList<OrderModel> orderlist;
    String userid,vendorid,mdeliveryfee,deliveryminute,paymentmethod,minorder;
    int subtotalprice = 0;
    TextView delivertitle,deliverytime,address,stateandarea,phonenumbertitle,phonenumber,paymentdetailstitle,paymentdetails,
            subtotal,subtotalvalue,servicefee,servicefeevalue,vat,vatvalue,deliveryfee,deliveryvalue,total,totalvalue;
    double lat,log;
    Dialog loadingdialog,dialog;
    int checkoutflag = 0;
    ArrayList<userprofile> userprofiles;
    int myvat,totalprice,vendortotal,service_fee;
    LinearLayout noconnection,error;
    TextView errortext,noconnectiontext;
    Button noconnectionbutton,errorbutton;
    LinearLayout cardnumberlayout,cardexpirylayout,cvvlayout;
    TextView cardnumbertitle,cardexpirytitle,cvvtitle,lastpaymentvalue;
    EditText cardnumbervalue,cardexpiryvalue,cvvvalue;
    Button paybutton;
    Button pay_service_fee;
    TextView footer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);
        PaystackSdk.initialize(this);
        if(savedInstanceState != null){

            if(savedInstanceState.getInt("pid",-1) == android.os.Process.myPid()){

            }
            else{

                startActivity(new Intent(CheckOut.this,splashscreen.class));

            }
        }
        getIntentValues();
        initView();
        getuserinfotask getuserinfotask = new getuserinfotask();
        getuserinfotask.execute();
        initializeRecyclerview();
        ShowPayment(orderlist);
        setOnclicklistener();



    }

    private void initializeRecyclerview(){
        recyclerView = findViewById(R.id.checkoutRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        completeOrderAdapter adapter = new completeOrderAdapter(orderlist);
        recyclerView.setAdapter(adapter);
    }

    private void getIntentValues(){
        orderlist = getIntent().getParcelableArrayListExtra("orderedlist");
        userid = getIntent().getStringExtra("userid");
        vendorid = getIntent().getStringExtra("vendorid");
        mdeliveryfee = getIntent().getStringExtra("deliveryfee");
        deliveryminute = getIntent().getStringExtra("deliveryminute");
        paymentmethod = getIntent().getStringExtra("paymentmethod");
        minorder = getIntent().getStringExtra("minimumorder");

        userprofiles = getIntent().getParcelableArrayListExtra("userprofile");
    }

    private void initView(){
        toolbar = findViewById(R.id.checkouttoolbar);
        title = findViewById(R.id.checkouttoolbartitle);
        placeOrder = findViewById(R.id.placeorder);
        delivertitle = findViewById(R.id.checkoutdeliverytimetitle);
        deliverytime = findViewById(R.id.checkoutdeliverytime);
        address = findViewById(R.id.checkoutaddress);
        stateandarea = findViewById(R.id.checkoutstateandarea);
        phonenumber = findViewById(R.id.checkoutphonenumber);
        phonenumbertitle = findViewById(R.id.checkoutphonenumbertitle);
        paymentdetails = findViewById(R.id.checkoutpaymentdetails);
        paymentdetailstitle = findViewById(R.id.checkoutpaymentdetailstitle);
        subtotal = findViewById(R.id.checkoutsubtotal);
        subtotalvalue = findViewById(R.id.checkoutsubtotalvalue);
        servicefee = findViewById(R.id.checkoutservicefee);
        servicefeevalue = findViewById(R.id.checkoutservicefeevalue);
        vat = findViewById(R.id.checkoutvat);
        vatvalue = findViewById(R.id.checkoutvatvalue);
        deliveryfee = findViewById(R.id.checkoutdeliveryfee);
        deliveryvalue = findViewById(R.id.checkoutdeliveryfeevalue);
        total = findViewById(R.id.checkoutTotal);
        totalvalue = findViewById(R.id.checkouttotalvalue);
        parentview = findViewById(R.id.parentlayout);
        progressbarlayout = findViewById(R.id.progressbarlayout);



        noconnection = findViewById(R.id.nonetworklayout);
        error = findViewById(R.id.errorlayout);
        errortext = findViewById(R.id.errortext);
        noconnectiontext = findViewById(R.id.nonetworktext);
        noconnectionbutton = findViewById(R.id.nonetworkbutton);
        errorbutton = findViewById(R.id.errorbutton);


        Typeface customfont3 = Typeface.createFromAsset(getAssets(), "Kylo-Regular.otf");

        errortext.setTypeface(customfont3);
        errorbutton.setTypeface(customfont3);
        noconnectionbutton.setTypeface(customfont3);
        noconnectiontext.setTypeface(customfont3);
        placeOrder.setVisibility(View.GONE);
        Typeface customfont= Typeface.createFromAsset(getAssets(),"Kylo-Light.otf");
        title.setTypeface(customfont);
        placeOrder.setTypeface(customfont);
        Typeface customfont2= Typeface.createFromAsset(getAssets(),"Kylo-Light.otf");

        delivertitle.setTypeface(customfont2);
        deliverytime.setTypeface(customfont2);
        address.setTypeface(customfont2);
        stateandarea.setTypeface(customfont2);
        phonenumber.setTypeface(customfont2);
        phonenumbertitle.setTypeface(customfont2);
        paymentdetails.setTypeface(customfont2);
        paymentdetailstitle.setTypeface(customfont2);
        subtotal.setTypeface(customfont2);
        subtotalvalue.setTypeface(customfont2);
        servicefee.setTypeface(customfont2);
        servicefeevalue.setTypeface(customfont2);
        vat.setTypeface(customfont2);
        vatvalue.setTypeface(customfont2);
        deliveryfee.setTypeface(customfont2);
        deliveryvalue.setTypeface(customfont2);
        totalvalue.setTypeface(customfont2);
        total.setTypeface(customfont2);

        deliverytime.setText("Delivery in"+" "+ deliveryminute +"mins");
        if(paymentmethod.equalsIgnoreCase("cash")){
            paymentdetails.setText("Cash on delivery");
        }
        else {
            paymentdetails.setText("Card on delivery");
        }


        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.screenBrightness = 0.5f;
        getWindow().setAttributes(layoutParams);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.White), PorterDuff.Mode.SRC_ATOP);
    }


    private void InitializeMap(double lat, double log){
        if(googleMap == null){
            StaticMapFragment mapFragment = ((StaticMapFragment)getSupportFragmentManager().findFragmentById(R.id.checkoutmap));
            ((StaticMapFragment)getSupportFragmentManager().findFragmentById(R.id.checkoutmap)).getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap Map) {
                    googleMap = Map;
                    googleMap.addMarker(new MarkerOptions().position(new LatLng(lat,log))
                            .draggable(false));

                    moveTocurrentposition(new LatLng(lat,log));
                    mapFragment.setOntouchListener(new StaticMapFragment.OnTouchListener() {
                        @Override
                        public void onTouch() {
                            //Intent intent = new Intent(getContext(), FullMapActivity.class);
                            //startActivity(intent);
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

    public class getuserinfotask extends AsyncTask{

        String url = "http://jl-market.com/user/getuserinfo.php";
        String prompt;
        ArrayList<CheckOutUserModel> userModelArrayList;

        @Override
        protected void onPreExecute() {
            parentview.setVisibility(View.GONE);
            progressbarlayout.setVisibility(View.VISIBLE);
            error.setVisibility(View.GONE);
            noconnection.setVisibility(View.GONE);
        }
        @Override
        protected Object doInBackground(Object[] objects) {
            if (!isNetworkAvailable()) {
                prompt = "No Network Connection";
                return prompt;
            }

            String serverresponse = new getuserinfo().GetData(url,userid);
            if (serverresponse != null) {
                try {
                    JSONObject jsonObject = new JSONObject(serverresponse);
                    JSONObject info = jsonObject.getJSONObject("info");
                    String status = info.getString("status");

                    if (status.equalsIgnoreCase("available")) {
                        userModelArrayList = passJson(info);
                        return userModelArrayList;
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
                        return null;
        }

        protected void onPostExecute(Object result) {

            if (result != null) {
                if (result.equals("No Network Connection")) {

                    parentview.setVisibility(View.GONE);
                    progressbarlayout.setVisibility(View.GONE);
                    error.setVisibility(View.GONE);
                    noconnection.setVisibility(View.VISIBLE);
                }
                else if(result instanceof ArrayList) {

                    ArrayList<CheckOutUserModel> model = (ArrayList<CheckOutUserModel>) result;
                    lat = Double.parseDouble(model.get(0).getLat());
                    log = Double.parseDouble(model.get(0).getLog());
                    InitializeMap(lat,log);
                    String mphone = model.get(0).getPhone();

                    String maddress = model.get(0).getAddress();
                    String cityandarea = model.get(0).getArea()+"  "+model.get(0).getCity();

                    stateandarea.setText(cityandarea);
                    address.setText(maddress);
                    if(mphone.startsWith("0")){
                        phonenumber.setText(mphone);
                    }
                    else{
                        phonenumber.setText("+234" +" "+ mphone);
                    }

                    parentview.setVisibility(View.VISIBLE);
                    progressbarlayout.setVisibility(View.GONE);
                    placeOrder.setVisibility(View.VISIBLE);
                    error.setVisibility(View.GONE);
                    noconnection.setVisibility(View.GONE);

                }

            }
            else{

                parentview.setVisibility(View.GONE);
                progressbarlayout.setVisibility(View.GONE);
                error.setVisibility(View.VISIBLE);
                noconnection.setVisibility(View.GONE);
                 }
        }

    }
    public ArrayList<CheckOutUserModel> passJson(JSONObject jsonObject) throws JSONException {
        ArrayList<CheckOutUserModel> ProductList = new ArrayList<>();
        JSONArray jsonArray = jsonObject.getJSONArray("data");
        for (int i = 0; i < jsonArray.length(); ++i) {
            JSONObject j = jsonArray.getJSONObject(i);
            String phone = j.getString("phone");
            String city = j.getString("city");
            String area = j.getString("area");
            String address = j.getString("address");
            String log = j.getString("log");
            String lat = j.getString("lat");

            CheckOutUserModel model = new CheckOutUserModel(phone,city,area,log,lat,address);
            ProductList.add(model);
        }
        return ProductList;


    }


    public class getuserinfo {

        public String GetData(String url, String userid) {

            try {
                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(50, TimeUnit.SECONDS)
                        .writeTimeout(50, TimeUnit.SECONDS)
                        .readTimeout(50, TimeUnit.SECONDS)
                        .build();


                RequestBody formBody = new FormBody.Builder()
                        .add("userid", userid)
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

        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    private void ShowPayment(ArrayList<OrderModel> orderList){

        Locale NigerianLocale = new Locale("en","ng");
        for (OrderModel a:orderList) {
            Log.e("got here", "onPostExecute: " );
            subtotalprice = subtotalprice + a.getCountprice();
            Log.e("here", Integer.toString(subtotalprice) );
        }
        String unFormattedSubtotalPrice = NumberFormat.getCurrencyInstance(NigerianLocale).format(subtotalprice);
        String formattedSubtotalPrice = unFormattedSubtotalPrice.replaceAll("\\.00","");
        subtotalvalue.setText(formattedSubtotalPrice);

        String unFormatteddeliveryfee = NumberFormat.getCurrencyInstance(NigerianLocale).format(Integer.parseInt(mdeliveryfee));
        String formatteddeliveryfee = unFormatteddeliveryfee.replaceAll("\\.00","");
        deliveryvalue.setText(formatteddeliveryfee);

        double servicefee1 = 0.03 * subtotalprice;
         service_fee = (int) servicefee1;

        String unFormattedserv = NumberFormat.getCurrencyInstance(NigerianLocale).format(service_fee);
        String formattedserv = unFormattedserv.replaceAll("\\.00","");
        servicefeevalue.setText(formattedserv);
        //double vat1 = 0.05 * subtotalprice;
         //myvat = (int) vat1;

        //String unFormattedvat = NumberFormat.getCurrencyInstance(NigerianLocale).format(myvat);
        //String formattedvat = unFormattedvat.replaceAll("\\.00","");
        //vatvalue.setText(formattedvat);
        int deliveryfee = Integer.parseInt(mdeliveryfee);
        totalprice = deliveryfee+service_fee+subtotalprice;
        vendortotal = deliveryfee+subtotalprice;
        String unFormattedTotalprice = NumberFormat.getCurrencyInstance(NigerianLocale).format(totalprice);
        String formattedtotalPrice = unFormattedTotalprice.replaceAll("\\.00","");
        totalvalue.setText(formattedtotalPrice);

    }
    private void setOnclicklistener(){
        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (subtotalprice < Integer.parseInt(minorder)) {
                    Toast.makeText(CheckOut.this, "Your Order did not meet the Seller's minimum order please add more products", Toast.LENGTH_LONG).show();
                } else {



                        DisplayPayServiceFeeDialog();

                }

            }
        });

        errorbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new getuserinfotask().execute();
            }
        });

        noconnectionbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new getuserinfotask().execute();
            }
        });
    }

    public class placeordertask extends AsyncTask{

        String url = "http://jl-market.com/user/placeorder.php";
        String prompt;
        @Override
        protected void onPreExecute() {

            showloadingdialog();
            //Toast.makeText(CheckOut.this, "Placing Order", Toast.LENGTH_SHORT).show();
        }
        @Override
        protected Object doInBackground(Object[] objects) {

            if (!isNetworkAvailable()) {
                prompt = "No Network Connection";
                return prompt;
            }

            String orderjson = convertListToJsonString(orderlist);
            String paymentmethod = paymentdetails.getText().toString();
            String serverresponse = new getOrder().GetData(url, userid,orderjson,paymentmethod,mdeliveryfee,vendorid, String.valueOf(vendortotal));

            if (serverresponse != null) {
                try {
                    JSONObject jsonObject = new JSONObject(serverresponse);
                    JSONObject info = jsonObject.getJSONObject("info");
                    String status = info.getString("status");

                    if (status.equalsIgnoreCase("Order placed successfully")) {
                        prompt = "Order place successfully";
                        return prompt;
                    }
                    if (status.equalsIgnoreCase("Error placing order")) {
                        prompt = "Error placing order";
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
            checkoutflag = 0;

            //Toast.makeText(CheckOut.this, result.toString(), Toast.LENGTH_SHORT).show();
            if(result != null){
                if(result.toString().equalsIgnoreCase("No Network Connection")){
                    Toast.makeText(CheckOut.this, "No Network Connection", Toast.LENGTH_SHORT).show();
                }
                else if(result.toString().equalsIgnoreCase("Order place successfully")){
                    Intent intent = new Intent(CheckOut.this, MainActivity.class);
                    Toast.makeText(CheckOut.this, "Your Order has been successfully placed", Toast.LENGTH_LONG).show();
                    intent.putParcelableArrayListExtra("userprofile", userprofiles);
                    startActivity(intent);

                }
                else if(result.toString().equalsIgnoreCase("Error placing order")){
                    Toast.makeText(CheckOut.this, "Error Occured", Toast.LENGTH_LONG).show();
                }
            }
            else{
                Toast.makeText(CheckOut.this, "Error Occured", Toast.LENGTH_LONG).show();
            }



            //Toast.makeText(CheckOut.this, result.toString(), Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        if (menuItem.getItemId() == android.R.id.home) {

            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private String convertListToJsonString(ArrayList<OrderModel> orderlist){
        JSONArray jsonArray = new JSONArray();
        for(int i = 0; i<orderlist.size(); i++){
            jsonArray.put(orderlist.get(i).getJsonObject());
        }
        return jsonArray.toString();
    }
    public class getOrder {

        public String GetData(String url,String userid,String orderjson,String paymentmethod,String deliveryfee,String vendorid,String totalprice) {

            try {
                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(50, TimeUnit.SECONDS)
                        .writeTimeout(50, TimeUnit.SECONDS)
                        .readTimeout(50, TimeUnit.SECONDS)
                        .build();

                RequestBody formBody = new FormBody.Builder()
                        .add("userid", userid)
                        .add("orderjson",orderjson)
                        .add("vendorid",vendorid)
                        .add("paymentmethod",paymentmethod)
                        .add("deliveryfee",deliveryfee)
                        .add("totalprice",totalprice)
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



    private void DisplayPayServiceFeeDialog(){


        dialog = new Dialog(CheckOut.this,android.R.style.Theme_Light);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = LayoutInflater.from(CheckOut.this).inflate(R.layout.checkout_payservice_fee_dialog,null);
        dialog.setContentView(view);

        TextView title = view.findViewById(R.id.service_fee_dialog_title);
        cardexpirylayout = view.findViewById(R.id.cardexpirylayout);
        cardnumberlayout = view.findViewById(R.id.cardnumberlayout);
        cvvlayout = view.findViewById(R.id.cvvlayout);
        cardnumbertitle = view.findViewById(R.id.cardnumbertitle);
        cardexpirytitle = view.findViewById(R.id.cardexpirytitle);
        cvvtitle = view.findViewById(R.id.cvvtitle);
        cardnumbervalue = view.findViewById(R.id.cardnumbervalue);
        cardexpiryvalue = view.findViewById(R.id.cardexpiryvalue);
        cvvvalue = view.findViewById(R.id.cvvvalue);
        footer = view.findViewById(R.id.service_fee_dialog_footer);
        pay_service_fee = view.findViewById(R.id.pay_service_fee_button);

        Typeface customfont= Typeface.createFromAsset(getAssets(),"Kylo-Regular.otf");

        cardnumbertitle.setTypeface(customfont);
        cardexpirytitle.setTypeface(customfont);
        cvvtitle.setTypeface(customfont);
        cardnumbervalue.setTypeface(customfont);
        cardexpiryvalue.setTypeface(customfont);
        cvvvalue.setTypeface(customfont);
        pay_service_fee.setTypeface(customfont);
        footer.setTypeface(customfont);
        title.setTypeface(customfont);
        setListener();

        Locale NigerianLocale = new Locale("en", "ng");
        String unFormattedamount = NumberFormat.getCurrencyInstance(NigerianLocale).format(service_fee);
        String formattedamount = unFormattedamount.replaceAll("\\.00","");

        pay_service_fee.setText("pay"+" "+formattedamount);






        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int displayWidth = displayMetrics.widthPixels;
        int displayHeight = displayMetrics.heightPixels;
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        int dialogWindowWidth = (int) (displayWidth * 0.9f);
        int dialogWindowHeight = (int) (displayHeight * 0.60f);

        layoutParams.width = dialogWindowWidth;
        layoutParams.height = dialogWindowHeight;
        dialog.getWindow().setAttributes(layoutParams);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void setListener(){
        cardnumbervalue.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    cardnumberlayout.setBackground(ContextCompat.getDrawable(CheckOut.this,R.drawable.productsmallimagebackgrounprimary));

                    cardexpirylayout.setBackground(ContextCompat.getDrawable(CheckOut.this,R.drawable.productsmallimagebackgroundblack));
                    cvvlayout.setBackground(ContextCompat.getDrawable(CheckOut.this,R.drawable.productsmallimagebackgroundblack));
                }
            }
        });

        cvvvalue.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    cardnumberlayout.setBackground(ContextCompat.getDrawable(CheckOut.this,R.drawable.productsmallimagebackgroundblack));

                    cardexpirylayout.setBackground(ContextCompat.getDrawable(CheckOut.this,R.drawable.productsmallimagebackgroundblack));
                    cvvlayout.setBackground(ContextCompat.getDrawable(CheckOut.this,R.drawable.productsmallimagebackgrounprimary));


                }
            }
        });

        cardexpiryvalue.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    cardnumberlayout.setBackground(ContextCompat.getDrawable(CheckOut.this,R.drawable.productsmallimagebackgroundblack));

                    cardexpirylayout.setBackground(ContextCompat.getDrawable(CheckOut.this,R.drawable.productsmallimagebackgrounprimary));
                    cvvlayout.setBackground(ContextCompat.getDrawable(CheckOut.this,R.drawable.productsmallimagebackgroundblack));

                }
            }
        });

        cardexpiryvalue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String value = s.toString();
                if(value.length() == 2){
                    String cardvalue = "";
                    cardvalue = cardvalue + cardexpiryvalue.getText().toString()+"/";
                    cardexpiryvalue.setText(cardvalue);
                    cardexpiryvalue.setSelection(cardvalue.length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        pay_service_fee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validateform()) {
                    String cardnum = cardnumbervalue.getText().toString(); //"4084084084084081";
                    String mandy = cardexpiryvalue.getText().toString();
                    String arrmandy[] = mandy.split("\\/");
                    int expirymonth = Integer.parseInt(arrmandy[0]); //11;
                    int expiryyear = Integer.parseInt(arrmandy[1]); //19;
                    String cvv = cvvvalue.getText().toString(); //"408"
                    showloadingdialog();
                    Card card = new Card(cardnum, expirymonth, expiryyear, cvv);
                    if (card.isValid()) {

                        Charge charge = new Charge();
                        charge.setCard(card);
                        charge.setAmount(service_fee * 100);
                        try {
                            charge.setEmail(userprofiles.get(0).getEmail());
                        } catch (InvalidEmailException E) {
                            loadingdialog.dismiss();
                            Toast.makeText(CheckOut.this, "Invalid Email", Toast.LENGTH_LONG).show();
                        }
                        PaystackSdk.chargeCard(CheckOut.this, charge, new Paystack.TransactionCallback() {
                            @Override
                            public void onSuccess(Transaction transaction) {
                                 new placeordertask().execute();
                                //Toast.makeText(getContext(), "success", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void beforeValidate(Transaction transaction) {

                                //Toast.makeText(getContext(), "before validate", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(Throwable error, Transaction transaction) {

                                loadingdialog.dismiss();
                                if (!isNetworkAvailable()) {
                                    Toast.makeText(CheckOut.this, "No Network Connection", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(CheckOut.this, "Error Occured", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

                    } else {
                        loadingdialog.dismiss();
                        Toast.makeText(CheckOut.this, "Card not valid", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private boolean validateform(){
        boolean valid = true;

        if(TextUtils.isEmpty(cardnumbervalue.getText().toString())){
            cardnumbervalue.setError("Required");
            valid = false;
        }
        else{
            cardnumbervalue.setError(null);
        }
        if(TextUtils.isEmpty(cardexpiryvalue.getText().toString())){
            cardexpiryvalue.setError("Required");
            valid = false;
        }
        else{
            cardexpiryvalue.setError(null);
        }
        if(TextUtils.isEmpty(cvvvalue.getText().toString())){
            cvvvalue.setError("Required");
            valid = false;
        }
        else{
            cvvvalue.setError(null);
        }


        if(cardexpiryvalue.getText().toString().length() <  5){
            cardexpiryvalue.setError("Required");
            valid = false;
        }
        else{
            cardexpiryvalue.setError(null);
        }
        return valid;
    }


}
