package com.electonicmarket.android.emarket.Fragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.electonicmarket.android.emarket.Activities.CompleteOrder;
import com.electonicmarket.android.emarket.Activities.MainActivity;
import com.electonicmarket.android.emarket.Adapter.cartitemadapter;
import com.electonicmarket.android.emarket.Models.ProductModel;
import com.electonicmarket.android.emarket.Models.userprofile;
import com.electonicmarket.android.emarket.R;
import com.electonicmarket.android.emarket.service.UpdateCartCountService;
import com.electonicmarket.android.emarket.service.updateservice.UpdateCartRecyclerview;
import com.electonicmarket.android.emarket.splashscreen;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class userOrders extends Fragment {

    Button completeOrder;
    View view;
    Typeface customfont;
    LinearLayout progressbarlayout,emptycart,noconnection,error;
    TextView emptycarttext,errortext,noconnectiontext;
    Button noconnectionbutton,errorbutton;
    static String muserid;
    static RecyclerView cartrecyclerview;
    ArrayList<ProductModel> productModelArrayList;
    static String mdeliveryfee;
    static cartitemadapter adapter;
   static ArrayList<userprofile> userprofiles;
    static String mvendorid,mdeliveryminute,carddelivery,cashdelivery,minimumorder;
    ArrayList<ProductModel> productlist;
    String url = "http://jl-market.com/user/userscart.php";



    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if(bundle != null){

              productlist = bundle.getParcelableArrayList("cartlist");
              if(productlist != null){

                  cartitemadapter cartitemadapter = new cartitemadapter(productlist,getContext());
                  cartrecyclerview.setAdapter(cartitemadapter);
                  cartitemadapter.notifyDataSetChanged();

                  if(cartitemadapter.getItemCount() < 1){
                      cartrecyclerview.setVisibility(View.GONE);
                      progressbarlayout.setVisibility(View.GONE);
                      emptycart.setVisibility(View.VISIBLE);
                      noconnection.setVisibility(View.GONE);
                      completeOrder.setVisibility(View.GONE);
                      error.setVisibility(View.GONE);
                  }
                  else{
                      cartrecyclerview.setVisibility(View.VISIBLE);
                      progressbarlayout.setVisibility(View.GONE);
                      emptycart.setVisibility(View.GONE);
                      noconnection.setVisibility(View.GONE);
                      completeOrder.setVisibility(View.VISIBLE);
                      error.setVisibility(View.GONE);
                  }

              }
            }
        }
    };
    public userOrders() {
        // Required empty public constructor
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

            initializeview();
            setOnclicklistener();
            allProductsRecyclerInit();
        }

    }


    public static userOrders newInstance( ArrayList<userprofile> muserprofiles,String userid,String deliveryfee,String vendorid,String deliveryminute,String mcarddelivery,String mcashdelivery,String mminimumorder){
        userOrders userOrders = new userOrders();
        muserid = userid;
        mdeliveryfee = deliveryfee;
        mvendorid = vendorid;
        mdeliveryminute = deliveryminute;
        carddelivery = mcarddelivery;
        cashdelivery = mcashdelivery;
        minimumorder = mminimumorder;
        userprofiles = muserprofiles;
      return userOrders;
  }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


                view = inflater.inflate(R.layout.fragment_user_orders, container, false);



              // updateCart updateCart = new updateCart();
               //updateCart.execute();

                return view;
    }

    private void initializeview(){
        completeOrder = view.findViewById(R.id.completeyourorder);
        progressbarlayout = view.findViewById(R.id.progressbarlayout);

        emptycart = view.findViewById(R.id.emptycartlayout);
        noconnection = view.findViewById(R.id.nonetworklayout);
        error = view.findViewById(R.id.errorlayout);

        emptycarttext = view.findViewById(R.id.emptycarttext);
        noconnectiontext =  view.findViewById(R.id.nonetworktext);
        errortext = view.findViewById(R.id.errortext);

        noconnectionbutton = view.findViewById(R.id.nonetworkbutton);
        errorbutton = view.findViewById(R.id.errorbutton);
        customfont= Typeface.createFromAsset(getActivity().getAssets(),"Kylo-Light.otf");
        Typeface customfont2 = Typeface.createFromAsset(getContext().getAssets(), "Kylo-Regular.otf");
        errortext.setTypeface(customfont2);
        errorbutton.setTypeface(customfont2);
        noconnectiontext.setTypeface(customfont2);
        noconnectionbutton.setTypeface(customfont2);
        emptycarttext.setTypeface(customfont2);
        completeOrder.setTypeface(customfont);
        completeOrder.setVisibility(View.GONE);
        MainActivity.changedisplaysearch(View.GONE);
    }

    private void setOnclicklistener(){
        completeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CompleteOrder.class);
                intent.putExtra("userid",muserid);
                intent.putExtra("deliveryfee",mdeliveryfee);
                intent.putExtra("vendorid",mvendorid);
                intent.putExtra("deliveryminute",mdeliveryminute);
                intent.putExtra("carddelivery",carddelivery);
                intent.putExtra("cashdelivery",cashdelivery);
                intent.putExtra("userprofile",userprofiles);
                intent.putExtra("minimumorder",minimumorder);
                startActivity(intent);
            }
        });

        errorbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new updateCart().execute();
            }
        });
        noconnectionbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new updateCart().execute();
            }
        });
    }
    private void allProductsRecyclerInit() {
        cartrecyclerview= (RecyclerView)view.findViewById(R.id.cartorderrecyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);

        cartrecyclerview.setLayoutManager(linearLayoutManager);
        //AllproductsRecycler.setItemAnimator(new DefaultItemAnimator());

    }

          public  class updateCart extends AsyncTask {
                String prompt;

              @Override
              protected void onPreExecute() {

                  progressbarlayout.setVisibility(View.VISIBLE);
                  emptycart.setVisibility(View.GONE);
                  noconnection.setVisibility(View.GONE);
                  error.setVisibility(View.GONE);
              }

                  @Override
                 protected Object doInBackground(Object[] objects) {
                        if (!isNetworkAvailable()) {
                            prompt = "No Network Connection";
                            return prompt;
                        }

                        String serverresponse = new updatecartinfo().GetData(url,muserid);

                        if (serverresponse != null) {
                            try {
                    JSONObject jsonObject = new JSONObject(serverresponse);
                    JSONObject info = jsonObject.getJSONObject("info");
                    String status = info.getString("status");

                    if (status.equalsIgnoreCase("orderavailable")) {

                        productModelArrayList = passCartJson(info);
                         return productModelArrayList;
                    }
                   else if (status.equalsIgnoreCase("ordersunavailable")) {
                        prompt = "order unavailable";
                        return prompt;
                        //prompt = "Nothing to display";
                        //return prompt;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            return null;
        }
        protected void onPostExecute(Object result) {

            progressbarlayout.setVisibility(View.GONE);
                  if (result == null) {

                      progressbarlayout.setVisibility(View.GONE);
                      emptycart.setVisibility(View.GONE);
                      noconnection.setVisibility(View.GONE);
                      error.setVisibility(View.VISIBLE);
                  }
            if (result != null) {
                if (result.equals("No Network Connection")) {
                    progressbarlayout.setVisibility(View.GONE);
                    emptycart.setVisibility(View.GONE);
                    noconnection.setVisibility(View.VISIBLE);
                    error.setVisibility(View.GONE);

                     }
                if (result.equals("order unavailable")) {
                    progressbarlayout.setVisibility(View.GONE);
                    emptycart.setVisibility(View.VISIBLE);
                    noconnection.setVisibility(View.GONE);
                    error.setVisibility(View.GONE);
                }
                if(result instanceof ArrayList) {
                    cartitemadapter cartitemadapter = new cartitemadapter((ArrayList<ProductModel>) result, getContext());
                    cartrecyclerview.setAdapter(cartitemadapter);
                    if (cartitemadapter.getItemCount() < 1) {
                        completeOrder.setVisibility(View.GONE);

                        progressbarlayout.setVisibility(View.GONE);
                        emptycart.setVisibility(View.VISIBLE);
                        noconnection.setVisibility(View.GONE);
                        error.setVisibility(View.GONE);
                    } else {
                        completeOrder.setVisibility(View.VISIBLE);

                        cartrecyclerview.setVisibility(View.VISIBLE);
                        progressbarlayout.setVisibility(View.GONE);
                        emptycart.setVisibility(View.GONE);
                        noconnection.setVisibility(View.GONE);
                        error.setVisibility(View.GONE);
                    }


                    Intent serviceintent = new Intent(getContext(), UpdateCartCountService.class);
                    serviceintent.putExtra("url", "http://jl-market.com/user/userscart.php");
                    serviceintent.putExtra("userid", muserid);
                    getContext().startService(serviceintent);
                }
            }

        }
    }
    public class updatecartinfo {

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

    public ArrayList<ProductModel> passCartJson(JSONObject jsonObject) throws JSONException {
        ArrayList<ProductModel> ProductList = new ArrayList<>();
        JSONArray jsonArray = jsonObject.getJSONArray("data");
        for (int i = 0; i < jsonArray.length(); ++i) {
            JSONObject j = jsonArray.getJSONObject(i);
            String productname = j.getString("productname");
            String price = j.getString("price");
            String newprice = j.getString("reducedprice");
            String firstimage = j.getString("productfirstimage");
            String secondimage = j.getString("productsecondimage");
            String count = j.getString("productcount");
            String description = j.getString("productdescription");
            String productid = j.getString("productid");
            ProductModel productModel = new ProductModel(description,productname,price,firstimage);
            if(!newprice.equalsIgnoreCase("0")){
                productModel.setProductnewPrice(newprice);
            }

            if(!secondimage.equalsIgnoreCase("")){
                productModel.setProductImageSecond(secondimage);
            }
            productModel.setProductid(productid);
            productModel.setUseremail(muserid);
            productModel.setProductCount(count);

            ProductList.add(productModel);
        }
        return ProductList;


    }


    private Boolean isNetworkAvailable() {

        ConnectivityManager connectivityManager = (ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void setUserVisibleHint(boolean isvisibletouser){
        super.setUserVisibleHint(isvisibletouser);

        if(isvisibletouser){


            Intent serviceintent = new Intent(getContext(), UpdateCartRecyclerview.class);
            serviceintent.putExtra("url", "http://jl-market.com/user/userscart.php");
            serviceintent.putExtra("userid", muserid);
            getActivity().startService(serviceintent);



        }
        else{

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(receiver, new IntentFilter("com.electonicmarket.android.emarket.service.updateservice"));

    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(receiver);
    }



}
