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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;


import com.electonicmarket.android.emarket.Adapter.vieworderadapter;
import com.electonicmarket.android.emarket.Models.OrderModel;
import com.electonicmarket.android.emarket.Models.viewordermodel;
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

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ViewOrder extends AppCompatActivity {
    String userid, Orderid, deliveryfee;
    RecyclerView recyclerView;
    ScrollView root;
    Toolbar viewordertoolbar;
    LinearLayout vieworderprogress;
    LinearLayout error, noconnection;
    TextView errortext, noconnectiontext;
    Button noconnectionbutton, errorbutton;
    TextView toolbartitle, statustitle, Statusvalue, deliveryfeetitle, deliveryfeevalue, subtotalvalue, subtotaltitle, servicefeevalue, servicefeetitle, vattitle, vatvalue, totalvalue, totaltitle;
    String flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_order);
        initializeview();
        processintent();
        initializerecyclerviewlayout();
        if(savedInstanceState != null){

            if(savedInstanceState.getInt("pid",-1) == android.os.Process.myPid()){

            }
            else{

                startActivity(new Intent(ViewOrder.this,splashscreen.class));

            }
        }

            viewOrdertask viewOrdertask = new viewOrdertask();
            viewOrdertask.execute();


        setOnclickListener();

    }


    private void initializerecyclerviewlayout() {
        recyclerView = findViewById(R.id.viewOrderRecyclerView);
        recyclerView.setNestedScrollingEnabled(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

    }

    private void processintent() {
        userid = getIntent().getStringExtra("userid");
        Orderid = getIntent().getStringExtra("orderid");
    }

    private void initializeview() {

        root = findViewById(R.id.vieworderroot);
        viewordertoolbar = findViewById(R.id.viewordertoolbar);
        statustitle = findViewById(R.id.statustitle);
        Statusvalue = findViewById(R.id.statusvalue);
        deliveryfeevalue = findViewById(R.id.deliveryfeevalue);
        subtotaltitle = findViewById(R.id.subtotal);
        subtotalvalue = findViewById(R.id.subtotalvalue);
        servicefeetitle = findViewById(R.id.servicefee);
        servicefeevalue = findViewById(R.id.servicefeevalue);
        vattitle = findViewById(R.id.vat);
        vatvalue = findViewById(R.id.vatvalue);
        totaltitle = findViewById(R.id.Total);
        totalvalue = findViewById(R.id.totalvalue);
        toolbartitle = findViewById(R.id.toolbartitle);
        deliveryfeetitle = findViewById(R.id.deliveryfee);
        vieworderprogress = findViewById(R.id.vieworderprogress);
        error = findViewById(R.id.errorlayout);
        noconnection = findViewById(R.id.nonetworklayout);
        errorbutton = findViewById(R.id.errorbutton);
        noconnectionbutton = findViewById(R.id.nonetworkbutton);
        errortext = findViewById(R.id.errortext);
        noconnectiontext = findViewById(R.id.nonetworktext);
        Typeface customfont = Typeface.createFromAsset(getAssets(), "Kylo-Light.otf");

        Typeface customfont2 = Typeface.createFromAsset(getAssets(), "Kylo-Regular.otf");
        errortext.setTypeface(customfont2);
        noconnectiontext.setTypeface(customfont2);
        errorbutton.setTypeface(customfont2);
        noconnectionbutton.setTypeface(customfont2);
        statustitle.setTypeface(customfont);
        Statusvalue.setTypeface(customfont);
        deliveryfeevalue.setTypeface(customfont);
        deliveryfeetitle.setTypeface(customfont);
        subtotaltitle.setTypeface(customfont);
        subtotalvalue.setTypeface(customfont);
        servicefeevalue.setTypeface(customfont);
        servicefeetitle.setTypeface(customfont);
        vattitle.setTypeface(customfont);
        vatvalue.setTypeface(customfont);
        totalvalue.setTypeface(customfont);
        totaltitle.setTypeface(customfont);
        toolbartitle.setTypeface(customfont);


        setSupportActionBar(viewordertoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        viewordertoolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.White), PorterDuff.Mode.SRC_ATOP);


    }

    private void setOnclickListener() {
        errorbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new viewOrdertask().execute();
            }
        });
        noconnectionbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new viewOrdertask().execute();
            }
        });
    }

    public class viewOrdertask extends AsyncTask {
        String url = "http://jl-market.com/user/vieworder.php";
        String prompt;
        ArrayList<viewordermodel> ProductList = new ArrayList<>();

        @Override
        protected void onPreExecute() {

            root.setVisibility(View.GONE);
            vieworderprogress.setVisibility(View.VISIBLE);
            error.setVisibility(View.GONE);
            noconnection.setVisibility(View.GONE);
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            if (!isNetworkAvailable()) {
                prompt = "No Network Connection";
                return prompt;
            }
            String serverresponse = new getOrder().GetData(url, userid, Orderid);
            if (serverresponse != null) {
                try {
                    JSONObject jsonObject = new JSONObject(serverresponse);
                    JSONObject info = jsonObject.getJSONObject("info");
                    String status = info.getString("status");

                    if (status.equalsIgnoreCase("available")) {
                        ProductList = passvieworderjson(info);
                        return ProductList;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                return null;
            }

            return null;
        }

        protected void onPostExecute(Object result) {

            if (result != null) {
                if (result instanceof ArrayList) {
                    ArrayList<OrderModel> list = null;
                    ArrayList<viewordermodel> viewordermodel = (ArrayList<com.electonicmarket.android.emarket.Models.viewordermodel>) result;
                    String status = viewordermodel.get(0).getOrderstatus();
                    deliveryfee = viewordermodel.get(0).getDeliveryfee();
                    String orderjson = viewordermodel.get(0).getOrderjson();
                    try {
                        list = passjson(orderjson);
                        vieworderadapter vieworderadapter = new vieworderadapter(list);
                        recyclerView.setAdapter(vieworderadapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Statusvalue.setText(status);
                    ShowPayment(list);
                    root.setVisibility(View.VISIBLE);
                    vieworderprogress.setVisibility(View.GONE);
                    error.setVisibility(View.GONE);
                    noconnection.setVisibility(View.GONE);
                    //deliveryfeevalue.setText(deliveryfee);
                }

                if (result.equals("No Network Connection")) {
                    root.setVisibility(View.GONE);
                    vieworderprogress.setVisibility(View.GONE);
                    error.setVisibility(View.GONE);
                    noconnection.setVisibility(View.VISIBLE);
                }
            } else {
                root.setVisibility(View.GONE);
                vieworderprogress.setVisibility(View.GONE);
                error.setVisibility(View.VISIBLE);
                noconnection.setVisibility(View.GONE);
            }
        }
    }

    private ArrayList<OrderModel> passjson(String json) throws JSONException {
        ArrayList<OrderModel> orderModels = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(json);
        for (int i = 0; i < jsonArray.length(); i++) {

            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String name = jsonObject.getString("name");
            String price = jsonObject.getString("price");
            String count = jsonObject.getString("count");
            int countprice = jsonObject.getInt("countprice");

            OrderModel orderModel = new OrderModel(name, price, count, countprice);
            orderModels.add(orderModel);
        }

        return orderModels;
    }

    public class getOrder {

        public String GetData(String url, String userid, String orderid) {

            try {
                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(50, TimeUnit.SECONDS)
                        .writeTimeout(50, TimeUnit.SECONDS)
                        .readTimeout(50, TimeUnit.SECONDS)
                        .build();


                RequestBody formBody = new FormBody.Builder()
                        .add("userid", userid)
                        .add("orderid", orderid)
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

    private ArrayList<viewordermodel> passvieworderjson(JSONObject jsonObject) throws JSONException {
        ArrayList<viewordermodel> ProductList = new ArrayList<>();
        JSONArray jsonArray = jsonObject.getJSONArray("data");
        for (int i = 0; i < jsonArray.length(); ++i) {
            JSONObject j = jsonArray.getJSONObject(i);

            String orderjson = j.getString("orderjson");
            String orderstatus = j.getString("orderstatus");
            String paymentmethod = j.getString("paymentmethod");
            deliveryfee = j.getString("deliveryfee");
            viewordermodel viewordermodel = new viewordermodel(orderjson, orderstatus, paymentmethod, deliveryfee);

            ProductList.add(viewordermodel);


        }
        return ProductList;


    }


    private Boolean isNetworkAvailable() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void ShowPayment(ArrayList<OrderModel> orderList) {
        int subtotalprice = 0;
        Locale NigerianLocale = new Locale("en", "ng");
        for (OrderModel a : orderList) {
            Log.e("got here", "onPostExecute: ");
            subtotalprice = subtotalprice + a.getCountprice();
            Log.e("here", Integer.toString(subtotalprice));
        }
        String unFormattedSubtotalPrice = NumberFormat.getCurrencyInstance(NigerianLocale).format(subtotalprice);
        String formattedSubtotalPrice = unFormattedSubtotalPrice.replaceAll("\\.00", "");
        subtotalvalue.setText(formattedSubtotalPrice);

        String unFormatteddeliveryfee = NumberFormat.getCurrencyInstance(NigerianLocale).format(Integer.parseInt(deliveryfee));
        String formatteddeliveryfee = unFormatteddeliveryfee.replaceAll("\\.00", "");
        deliveryfeevalue.setText(formatteddeliveryfee);

        //double vat1 = 0.05 * subtotalprice;
        //int vat = (int) vat1;

        //String unFormattedvat = NumberFormat.getCurrencyInstance(NigerianLocale).format(vat);
        //String formattedvat = unFormattedvat.replaceAll("\\.00", "");
        //vatvalue.setText(formattedvat);

        String unFormattedTotalprice = NumberFormat.getCurrencyInstance(NigerianLocale).format(Integer.parseInt(deliveryfee) + subtotalprice);
        String formattedtotalPrice = unFormattedTotalprice.replaceAll("\\.00", "");
        totalvalue.setText(formattedtotalPrice);


    }

    @Override

    public void onBackPressed() {
            finish();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("here", "onPause: ");
    }
    @Override
    public void onResume() {
        super.onResume();
        Log.e("also", "onResume: ");


    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.e("destroy", "onDestroy: ");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        if (menuItem.getItemId() == android.R.id.home) {

            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onSaveInstanceState(Bundle outstate){
        super.onSaveInstanceState(outstate);
        outstate.putInt("pid",android.os.Process.myPid());

        }

}