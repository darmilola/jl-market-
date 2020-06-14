package com.electonicmarket.android.emarket.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.electonicmarket.android.emarket.Adapter.completeOrderAdapter;
import com.electonicmarket.android.emarket.Models.OrderModel;
import com.electonicmarket.android.emarket.Models.userprofile;
import com.electonicmarket.android.emarket.R;
import com.electonicmarket.android.emarket.splashscreen;

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
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CompleteOrder extends AppCompatActivity {
     Toolbar toolbar;
     Button proceedToCheckOut;
     TextView Order;
     RecyclerView recyclerView;
     String muserid,mdeliveryfee,mdeliveryminute,cardpayment,cashpayment,minorder;
     LinearLayout receipt;
     TextView subtotal,subtotalvalue,servicefee,servicefeevalue,vat,vatvalue,deliveryfee, deliveryfeevalue,total,totalvalue;
     ArrayList<OrderModel> orderList = new ArrayList<>();
     ArrayList<OrderModel> productModelArrayList;
     int subtotalprice = 0;
     String vendorid;
     ScrollView container;
     LinearLayout progressbar;
    ArrayList<userprofile> userprofiles;
    LinearLayout noconnection,error;
    TextView errortext,noconnectiontext;
    Button noconnectionbutton,errorbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_order);
        processIntent();
        initializeview();
        initializerecyclerviewlayout();
        setonclicklistener();
        if(savedInstanceState != null){

            if(savedInstanceState.getInt("pid",-1) == android.os.Process.myPid()){

            }
            else{

                startActivity(new Intent(CompleteOrder.this,splashscreen.class));

            }
        }
        getOrderedProduct getOrderedProduct = new getOrderedProduct();
        getOrderedProduct.execute();

    }


    private void processIntent(){
        muserid = getIntent().getStringExtra("userid");
        mdeliveryfee = getIntent().getStringExtra("deliveryfee");
        vendorid = getIntent().getStringExtra("vendorid");
        mdeliveryminute = getIntent().getStringExtra("deliveryminute");
        cashpayment = getIntent().getStringExtra("cashdelivery");
        cardpayment = getIntent().getStringExtra("carddelivery");
        userprofiles = getIntent().getParcelableArrayListExtra("userprofile");
        minorder = getIntent().getStringExtra("minimumorder");


        Log.e("card and cash",cashpayment+" "+cardpayment );
    }
    private void setonclicklistener() {

        proceedToCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CompleteOrder.this,PaymentOptions.class);
                intent.putParcelableArrayListExtra("orderedlist",  productModelArrayList);
                intent.putExtra("userid",muserid);
                intent.putExtra("deliveryfee",mdeliveryfee);
                intent.putExtra("vendorid",vendorid);
                intent.putExtra("deliveryminute",mdeliveryminute);
                intent.putExtra("cardpayment",cardpayment);
                intent.putExtra("cashpayment",cashpayment);
                intent.putExtra("minimumorder",minorder);
                intent.putParcelableArrayListExtra("userprofile",userprofiles);
                startActivity(intent);
            }
        });

        noconnectionbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new getOrderedProduct().execute();
            }
        });

        errorbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new getOrderedProduct().execute();
            }
        });
    }

    private void initializerecyclerviewlayout(){

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);

        recyclerView.setLayoutManager(linearLayoutManager);

    }

    private void initializeview(){




        noconnection = findViewById(R.id.nonetworklayout);
        error = findViewById(R.id.errorlayout);
        noconnectionbutton = findViewById(R.id.nonetworkbutton);
        errorbutton = findViewById(R.id.errorbutton);
        noconnectiontext = findViewById(R.id.nonetworktext);
        errortext = findViewById(R.id.errortext);
        Typeface customfont2 = Typeface.createFromAsset(getAssets(), "Kylo-Regular.otf");
        errortext.setTypeface(customfont2);
        noconnectiontext.setTypeface(customfont2);
        errorbutton.setTypeface(customfont2);
        noconnectionbutton.setTypeface(customfont2);
        recyclerView = findViewById(R.id.completeOrderRecyclerView);
        proceedToCheckOut = findViewById(R.id.proceedtocheckout);
        Order = findViewById(R.id.completeordertoolbartitle);
        toolbar = findViewById(R.id.completeordertoolbar);
        progressbar = findViewById(R.id.progressbarlayout);
        container = findViewById(R.id.ordercontainerscrollview);
        Typeface customfont= Typeface.createFromAsset(getAssets(),"Kylo-Light.otf");
        proceedToCheckOut.setTypeface(customfont);
        Order.setTypeface(customfont);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.White), PorterDuff.Mode.SRC_ATOP);


        subtotal = findViewById(R.id.subtotal);
        subtotalvalue = findViewById(R.id.subtotalvalue);
        servicefee = findViewById(R.id.servicefee);
        servicefeevalue = findViewById(R.id.servicefeevalue);
        vat = findViewById(R.id.vat);
        vatvalue = findViewById(R.id.vatvalue);
        deliveryfee = findViewById(R.id.deliveryfee);
        deliveryfeevalue = findViewById(R.id.deliveryfeevalue);
        total = findViewById(R.id.Total);
        totalvalue = findViewById(R.id.totalvalue);
        receipt = findViewById(R.id.receiptcontainer);
        receipt.setVisibility(View.GONE);

        subtotalvalue.setTypeface(customfont);
        subtotal.setTypeface(customfont);
        servicefeevalue.setTypeface(customfont);
        servicefee.setTypeface(customfont);
        vat.setTypeface(customfont);
        vatvalue.setTypeface(customfont);
        deliveryfee.setTypeface(customfont);
        deliveryfeevalue.setTypeface(customfont);
        totalvalue.setTypeface(customfont);
        total.setTypeface(customfont);
        progressbar.setVisibility(View.VISIBLE);
        proceedToCheckOut.setVisibility(View.GONE);
    }

    public class getOrderedProduct extends AsyncTask{
        String url = "http://jl-market.com/user/userscart.php";
         String prompt;

          @Override
          protected void onPreExecute() {

              progressbar.setVisibility(View.VISIBLE);
              container.setVisibility(View.GONE);
              receipt.setVisibility(View.GONE);
              error.setVisibility(View.VISIBLE);
              noconnection.setVisibility(View.GONE);
              proceedToCheckOut.setVisibility(View.GONE);
              noconnection.setVisibility(View.GONE);
              error.setVisibility(View.GONE);
          }
            @Override
            protected Object doInBackground(Object[] objects) {
                if (!isNetworkAvailable()) {
                    prompt = "No Network Connection";
                    return prompt;
                }

                String serverresponse = new getProduct().GetData(url, muserid);

                if (serverresponse != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(serverresponse);
                        JSONObject info = jsonObject.getJSONObject("info");
                        String status = info.getString("status");

                        if (status.equalsIgnoreCase("orderavailable")) {
                            productModelArrayList = passCartJson(info);
                            return productModelArrayList;
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    return null;
                }
                return null;
            }

        protected void onPostExecute(Object result) {


              progressbar.setVisibility(View.GONE);

            if (result == null) {
                container.setVisibility(View.GONE);
                progressbar.setVisibility(View.GONE);
                error.setVisibility(View.VISIBLE);
                noconnection.setVisibility(View.GONE);
                proceedToCheckOut.setVisibility(View.GONE);
                //Toast.makeText(CompleteOrder.this, "Error in loading Products", Toast.LENGTH_SHORT).show();
            }
            if (result != null) {
                if (result.equals("No Network Connection")) {

                    container.setVisibility(View.VISIBLE);
                    progressbar.setVisibility(View.GONE);
                    error.setVisibility(View.GONE);
                    noconnection.setVisibility(View.VISIBLE);
                    proceedToCheckOut.setVisibility(View.GONE);
                    container.setVisibility(View.GONE);
                    //Toast.makeText(CompleteOrder.this, prompt, Toast.LENGTH_SHORT).show();
                }

                if(result instanceof ArrayList){
                 completeOrderAdapter completeOrderAdapter = new completeOrderAdapter((ArrayList<OrderModel>) result);
                 recyclerView.setAdapter(completeOrderAdapter);
                    receipt.setVisibility(View.VISIBLE);
                    container.setVisibility(View.VISIBLE);
                    progressbar.setVisibility(View.GONE);
                    error.setVisibility(View.GONE);
                    noconnection.setVisibility(View.GONE);
                    proceedToCheckOut.setVisibility(View.VISIBLE);

                  ShowPayment((ArrayList<OrderModel>) result);
                      }
            }
            }
        }


    public ArrayList<OrderModel> passCartJson(JSONObject jsonObject) throws JSONException {
        ArrayList<OrderModel> ProductList = new ArrayList<>();
        JSONArray jsonArray = jsonObject.getJSONArray("data");
        for (int i = 0; i < jsonArray.length(); ++i) {
            JSONObject j = jsonArray.getJSONObject(i);
            String realprice;
            String productname = j.getString("productname");
            String price = j.getString("price");
            String newprice = j.getString("reducedprice");
            String count = j.getString("productcount");
            if(count.equalsIgnoreCase("0")){
                count = "1";
            }
            Log.e("pcount", count );
            OrderModel orderModel;
             if(!newprice.equalsIgnoreCase("0")){
                 realprice = newprice;
             }
             else{
                 realprice = price;
             }

              int countprice = Integer.parseInt(realprice) * Integer.parseInt(count);
              orderModel = new OrderModel(productname,realprice,count,countprice);
              ProductList.add(orderModel);



        }
        return ProductList;


    }



    public class getProduct {

        public String GetData(String url,String userid) {

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

            subtotalprice = subtotalprice + a.getCountprice();

        }
        String unFormattedSubtotalPrice = NumberFormat.getCurrencyInstance(NigerianLocale).format(subtotalprice);
        String formattedSubtotalPrice = unFormattedSubtotalPrice.replaceAll("\\.00","");
        subtotalvalue.setText(formattedSubtotalPrice);

        String unFormatteddeliveryfee = NumberFormat.getCurrencyInstance(NigerianLocale).format(Integer.parseInt(mdeliveryfee));
        String formatteddeliveryfee = unFormatteddeliveryfee.replaceAll("\\.00","");
        deliveryfeevalue.setText(formatteddeliveryfee);

        double servicefee1 = 0.03 * subtotalprice;
        int servicefee = (int) servicefee1;

        String unFormattedserv = NumberFormat.getCurrencyInstance(NigerianLocale).format(servicefee);
        String formattedserv = unFormattedserv.replaceAll("\\.00","");
        servicefeevalue.setText(formattedserv);

        //String unFormattedvat = NumberFormat.getCurrencyInstance(NigerianLocale).format(vat);
        //String formattedvat = unFormattedvat.replaceAll("\\.00","");
        //vatvalue.setText(formattedvat);

        String unFormattedTotalprice = NumberFormat.getCurrencyInstance(NigerianLocale).format(Integer.parseInt(mdeliveryfee) +servicefee+ subtotalprice);
        String formattedtotalPrice = unFormattedTotalprice.replaceAll("\\.00","");
        totalvalue.setText(formattedtotalPrice);


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
