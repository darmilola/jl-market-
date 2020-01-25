package com.electonicmarket.android.emarket.Fragments;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.electonicmarket.android.emarket.Activities.MainActivity;
import com.electonicmarket.android.emarket.Activities.VendorEshop;
import com.electonicmarket.android.emarket.Adapter.vendordisplayadapter;
import com.electonicmarket.android.emarket.Models.ProductCategory;
import com.electonicmarket.android.emarket.Models.userprofile;
import com.electonicmarket.android.emarket.Models.vendordisplayitem;
import com.electonicmarket.android.emarket.R;
import com.electonicmarket.android.emarket.splashscreen;
import com.facebook.drawee.backends.pipeline.Fresco;

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
public class Vendor extends Fragment implements MainActivity.OnPassVendorQuery {
    View view;
    vendordisplayadapter vendordisplayadapter;
    ArrayList<vendordisplayitem> vendordisplayitemArrayList;
    RecyclerView recyclerView;
    int seevendorflag = 0;

    static String address, area, city, email;
    static double latitude, longitude;
    ArrayList<ProductCategory> productCategoryArrayList;
    Bitmap image;
    LinearLayout progressbar, novendor, noconnection, error;
    TextView novendortext, errortext, noconnectiontext;
    Button erroroccuredretry, nonetworkbutton;
    static ArrayList<userprofile> userprofiles;
    Context context;
    String shopname;
    LinearLayout novendorbynamelayout;
    TextView novendorbynametext;
    int offset = 0;
    RecyclerView searchrecycler;


    public static String UpdateUserURL = "http://jl-market.com/user/updateuserlocation.php";
    public static String SeeVendorURL = "http://jl-market.com/user/seevendor.php";
    public static String SeeVendorFromSearchURL = "http://jl-market.com/user/searchvendor.php";

    // public static String VendorGetCategoriesURL = "http://dixxieboy44.000webhostapp.com/user/selectcategories.php";
    public Vendor() {
        // Required empty public constructor
    }

    public static Vendor newInstance(ArrayList<userprofile> muserprofiles, String address1, String area1, String city1, double latitude1, double longitude1, String email1) {
        Vendor vendor = new Vendor();
        address = address1;
        area = area1;
        city = city1;
        latitude = latitude1;
        longitude = longitude1;
        email = email1;
        userprofiles = muserprofiles;
        return vendor;
    }

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onActivityCreated(Bundle savedinstance) {
        super.onActivityCreated(savedinstance);

        if (userprofiles == null) {
            startActivity(new Intent(getContext(), splashscreen.class));
        } else {
            if (isNetworkAvailable()) {
                UpdateUserLocation task = new UpdateUserLocation();
                task.execute();
            } else {

                progressbar.setVisibility(View.GONE);
                novendor.setVisibility(View.GONE);
                noconnection.setVisibility(View.VISIBLE);
                error.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_vendors, container, false);
        Fresco.initialize(getContext());

        initializeview();


        setOnclicklistner();
        return view;
    }

    private void setOnclicklistner() {

        erroroccuredretry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new UpdateUserLocation().execute();
            }
        });

        nonetworkbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new UpdateUserLocation().execute();
            }
        });
    }

    private void initializeview() {
        progressbar = view.findViewById(R.id.vendorprogressbarlayout);
        noconnection = view.findViewById(R.id.nonetworklayout);
        novendor = view.findViewById(R.id.vendornovendorlayout);
        error = view.findViewById(R.id.errorlayout);
        noconnectiontext = view.findViewById(R.id.nonetworktext);
        novendortext = view.findViewById(R.id.novendortext);
        errortext = view.findViewById(R.id.errortext);
        erroroccuredretry = view.findViewById(R.id.errorbutton);
        nonetworkbutton = view.findViewById(R.id.nonetworkbutton);
        searchrecycler = view.findViewById(R.id.vendorsearchrecyclerview);
        novendorbynamelayout = view.findViewById(R.id.vendornovendorbynamelayout);
        novendorbynametext = view.findViewById(R.id.novendorbynametext);
        Typeface customfont = Typeface.createFromAsset(getContext().getAssets(), "Kylo-Regular.otf");
        noconnectiontext.setTypeface(customfont);
        novendortext.setTypeface(customfont);
        errortext.setTypeface(customfont);
        erroroccuredretry.setTypeface(customfont);
        nonetworkbutton.setTypeface(customfont);
        novendorbynametext.setTypeface(customfont);
        recyclerView = (RecyclerView) view.findViewById(R.id.vendordisplayrecyclerview);
        recyclerView.setVisibility(View.VISIBLE);
        RecyclerView.LayoutManager mLayoutManager =
                new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        RecyclerView.LayoutManager mLayoutManager2 =
                new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false);
         searchrecycler.setLayoutManager(mLayoutManager2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setVisibility(View.GONE);
        MainActivity.changedisplaysearch(View.VISIBLE);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
                super.onScrolled(recyclerView,dx,dy);
                if(!recyclerView.canScrollVertically(1)){
                    new SeeMoreVendor().execute();

                }
                else{
                    //Toast.makeText(getContext(), "am in scroll", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    public void passVendorQuery(String data) {
        shopname = data;
        new SeeVendorFromSearch().execute();
    }


    public class UpdateUserLocation extends AsyncTask {
        String prompt;

        @Override
        protected void onPreExecute() {
            progressbar.setVisibility(View.VISIBLE);
            novendor.setVisibility(View.GONE);
            noconnection.setVisibility(View.GONE);
            error.setVisibility(View.GONE);

        }

        @Override
        protected Object doInBackground(Object[] objects) {
            if (!isNetworkAvailable()) {
                prompt = "No Network Connection";
                return prompt;
            }

            String serverResponse = new UserInfo().GetData(UpdateUserURL);


            return serverResponse;
        }

        protected void onPostExecute(Object result) {

            if (result != null) {
                if (getContext() != null) {

                    if (result.equals("No Network Connection")) {
                        progressbar.setVisibility(View.GONE);
                        novendor.setVisibility(View.GONE);
                        noconnection.setVisibility(View.VISIBLE);
                        error.setVisibility(View.GONE);
                    } else {
                        //Toast.makeText(getContext(), result.toString(), Toast.LENGTH_LONG).show();
                        SeeVendor seeVendor = new SeeVendor();
                        seeVendor.execute();

                    }
                }
            } else {

                progressbar.setVisibility(View.GONE);
                novendor.setVisibility(View.GONE);
                noconnection.setVisibility(View.GONE);
                error.setVisibility(View.VISIBLE);
            }


        }

    }

    private Boolean isNetworkAvailable() {

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public class UserInfo {

        public String GetData(String url) {

            try {
                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(50, TimeUnit.SECONDS)
                        .writeTimeout(50, TimeUnit.SECONDS)
                        .readTimeout(50, TimeUnit.SECONDS)
                        .build();


                RequestBody formBody = new FormBody.Builder()
                        .add("city", city)
                        .add("area", area)
                        .add("address", address)
                        .add("email", email)
                        .add("longitude", Double.toString(longitude))
                        .add("latitude", Double.toString(latitude))
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


    public class SeeVendor extends AsyncTask {
        String prompt;

        @Override
        protected void onPreExecute() {
            //recyclerView.setVisibility(View.GONE);


        }

        @Override
        protected Object doInBackground(Object[] objects) {
            if (!isNetworkAvailable()) {
                prompt = "No Network Connection";
                return prompt;
            }

            String serverResponse = new searchVendor().GetData(SeeVendorURL);

            if (serverResponse != null) {

                if (serverResponse.equalsIgnoreCase("")) {
                    return serverResponse;
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(serverResponse);
                        JSONObject info = jsonObject.getJSONObject("info");
                        String status = info.getString("status");

                        if (status.equalsIgnoreCase("vendorsavailable")) {

                            ArrayList<vendordisplayitem> vendordisplayitems = passVendorJson(info);
                            return vendordisplayitems;
                        }
                        //return serverResponse;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
            return serverResponse;
        }

        protected void onPostExecute(Object result) {
            progressbar.setVisibility(View.GONE);
                if (result != null) {

                if (result.equals("No Network Connection")) {

                    recyclerView.setVisibility(View.GONE);
                    progressbar.setVisibility(View.GONE);
                    novendor.setVisibility(View.GONE);
                    noconnection.setVisibility(View.VISIBLE);
                    error.setVisibility(View.GONE);
                }
                if (result.toString().equalsIgnoreCase("")) {
                    recyclerView.setVisibility(View.GONE);
                    progressbar.setVisibility(View.GONE);
                    novendor.setVisibility(View.VISIBLE);
                    noconnection.setVisibility(View.GONE);
                    error.setVisibility(View.GONE);

                } else {
                    vendordisplayitemArrayList = (ArrayList<vendordisplayitem>) result;
                    vendordisplayadapter = new vendordisplayadapter(getContext(), vendordisplayitemArrayList, (int clickedItemIndex) -> {
                        vendordisplayitemArrayList = ((ArrayList<vendordisplayitem>) result);
                        Intent intent = new Intent(getContext(), VendorEshop.class);
                        vendordisplayitem vendorchoosen = vendordisplayitemArrayList.get(clickedItemIndex);
                        intent.putExtra("useremail", email);
                        ArrayList<vendordisplayitem> vendorclicked = new ArrayList<>();
                        vendorclicked.add(vendorchoosen);

                        Log.e("size", Integer.toString(((ArrayList<vendordisplayitem>) result).size()));
                        intent.putParcelableArrayListExtra("vendorinfo", vendorclicked);
                        intent.putParcelableArrayListExtra("userprofile", userprofiles);

                        if (seevendorflag == 0) {
                            seevendorflag = 1;
                            startActivity(intent);
                        }

                    });
                    if (vendordisplayadapter.getItemCount() < 2) {
                        recyclerView.setVisibility(View.GONE);
                        progressbar.setVisibility(View.GONE);
                        novendor.setVisibility(View.VISIBLE);
                        noconnection.setVisibility(View.GONE);
                        error.setVisibility(View.GONE);
                    } else {
                        recyclerView.setAdapter(vendordisplayadapter);
                        recyclerView.setVisibility(View.VISIBLE);
                        progressbar.setVisibility(View.GONE);
                        novendor.setVisibility(View.GONE);
                        noconnection.setVisibility(View.GONE);
                        error.setVisibility(View.GONE);
                        offset = offset + 50;
                    }

                }
            } else {
                progressbar.setVisibility(View.GONE);
                novendor.setVisibility(View.GONE);
                noconnection.setVisibility(View.GONE);
                error.setVisibility(View.VISIBLE);
            }


        }

    }

    public class SeeMoreVendor extends AsyncTask {
        String prompt;
        ArrayList<vendordisplayitem> vendordisplayitems;

        @Override
        protected void onPreExecute() {


        }

        @Override
        protected Object doInBackground(Object[] objects) {
            if (!isNetworkAvailable()) {
                prompt = "No Network Connection";
                return prompt;
            }

            String serverResponse = new searchVendor().GetData(SeeVendorURL);

            if (serverResponse != null) {


                try {
                    JSONObject jsonObject = new JSONObject(serverResponse);
                    JSONObject info = jsonObject.getJSONObject("info");
                    String status = info.getString("status");

                    if (status.equalsIgnoreCase("vendorsavailable")) {

                        vendordisplayitems = passMoreVendorJson(info);
                        return vendordisplayitems;
                    } else if (status.equalsIgnoreCase("vendorsnotavailable")) {
                        prompt = "vendorsnotavailable";
                        return prompt;
                    }
                    //return serverResponse;
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            return null;
        }


        protected void onPostExecute(Object result) {
            if (result != null) {

                if (result.equals("No Network Connection")) {


                } else if (result.toString().equalsIgnoreCase("vendorsnotavailable")) {

                } else if(result instanceof ArrayList) {
                    for (vendordisplayitem item: (ArrayList<vendordisplayitem>)result) {
                             vendordisplayitemArrayList.add(vendordisplayadapter.getItemCount(),item);
                             vendordisplayadapter.notifyItemInserted(vendordisplayadapter.getItemCount());
                             vendordisplayadapter.notifyDataSetChanged();
                    }
                    offset = offset + 50;
                }


            } else {

            }

        }

    }




    @Override
    public void onStop() {

        super.onStop();
        seevendorflag = 0;
    }


    public class searchVendor {

        public String GetData(String url) {

            try {
                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(50, TimeUnit.SECONDS)
                        .writeTimeout(50, TimeUnit.SECONDS)
                        .readTimeout(50, TimeUnit.SECONDS)
                        .build();


                RequestBody formBody = new FormBody.Builder()
                        .add("city", city)
                        .add("area",area)
                        .add("offset", String.valueOf(offset))
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

    public class searchVendorByName {

        public String GetData(String url,String city,String area,String shopname) {

            try {
                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(50, TimeUnit.SECONDS)
                        .writeTimeout(50, TimeUnit.SECONDS)
                        .readTimeout(50, TimeUnit.SECONDS)
                        .build();


                RequestBody formBody = new FormBody.Builder()
                        .add("city", city)
                        .add("area",area)
                        .add("shopname",shopname)
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

    public ArrayList<vendordisplayitem> passVendorJson(JSONObject jsonObject) throws JSONException {
        ArrayList<vendordisplayitem> vendordisplayitems = new ArrayList<>();
        vendordisplayitem item = new vendordisplayitem();
        vendordisplayitem item1 = null;
        item.setViewtype("0");
        item.setHeadername("Delivery to"+" "+ area);
        vendordisplayitems.add(item);
        JSONArray jsonArray = jsonObject.getJSONArray("data");
        for (int i = 0; i < jsonArray.length(); ++i) {
            JSONObject j = jsonArray.getJSONObject(i);

            String shopname = j.getString("shopname");
            if (!shopname.equalsIgnoreCase("null")) {
                String shoppicture = j.getString("shoppicture");
                String deliveryhour = j.getString("deliveryhour");
                String deliverymin = j.getString("deliveryminute");
                String vendoremail = j.getString("email");
                String minimumorder = j.getString("minimumorder");
                String deliveryfee = j.getString("deliveryfee");
                String category1 = j.getString("productcategory1");
                String category2 = j.getString("productcategory2");
                String category3 = j.getString("productcategory3");

                double longitude = j.getDouble("shoplongitude");
                double latitude = j.getDouble("shoplatitude");

                String openinghour = j.getString("openinghour");
                String closinghour = j.getString("closinghour");
                String cardpayment = j.getString("cardpayment");
                String cashpayment = j.getString("cashpayment");
                String ratecount = j.getString("ratecount");
                String rateaverage = j.getString("rateaverage");
                String paymentstate = j.getString("paymentstate");


                item1 = new vendordisplayitem(shopname, category1, category2, category3, shoppicture, vendoremail, deliveryhour, deliverymin, deliveryfee, minimumorder);
                item1.setViewtype("1");

                item1.setLatitude(latitude);
                item1.setLongitude(longitude);
                item1.setOpeninghour(openinghour);
                item1.setClosinghour(closinghour);
                item1.setCardpayment(cardpayment);
                item1.setCashpayment(cashpayment);
                item1.setReviewscount(ratecount);
                item1.setStarnumber(rateaverage);
                item1.setPaymentstate(paymentstate);

                    vendordisplayitems.add(item1);

            }
        }

        return vendordisplayitems;


    }
    public ArrayList<vendordisplayitem> passMoreVendorJson(JSONObject jsonObject) throws JSONException {
        ArrayList<vendordisplayitem> vendordisplayitems = new ArrayList<>();
        //vendordisplayitem item = new vendordisplayitem();
        vendordisplayitem item1 = null;
        //item.setViewtype("0");
        //item.setHeadername("Delivery to"+" "+ area);
        //vendordisplayitems.add(item);
        JSONArray jsonArray = jsonObject.getJSONArray("data");
        for (int i = 0; i < jsonArray.length(); ++i) {
            JSONObject j = jsonArray.getJSONObject(i);

            String shopname = j.getString("shopname");
            if (!shopname.equalsIgnoreCase("null")) {
                String shoppicture = j.getString("shoppicture");
                String deliveryhour = j.getString("deliveryhour");
                String deliverymin = j.getString("deliveryminute");
                String vendoremail = j.getString("email");
                String minimumorder = j.getString("minimumorder");
                String deliveryfee = j.getString("deliveryfee");
                String category1 = j.getString("productcategory1");
                String category2 = j.getString("productcategory2");
                String category3 = j.getString("productcategory3");

                double longitude = j.getDouble("shoplongitude");
                double latitude = j.getDouble("shoplatitude");

                String openinghour = j.getString("openinghour");
                String closinghour = j.getString("closinghour");
                String cardpayment = j.getString("cardpayment");
                String cashpayment = j.getString("cashpayment");
                String ratecount = j.getString("ratecount");
                String rateaverage = j.getString("rateaverage");
                String paymentstate = j.getString("paymentstate");


                item1 = new vendordisplayitem(shopname, category1, category2, category3, shoppicture, vendoremail, deliveryhour, deliverymin, deliveryfee, minimumorder);
                item1.setViewtype("1");

                item1.setLatitude(latitude);
                item1.setLongitude(longitude);
                item1.setOpeninghour(openinghour);
                item1.setClosinghour(closinghour);
                item1.setCardpayment(cardpayment);
                item1.setCashpayment(cashpayment);
                item1.setReviewscount(ratecount);
                item1.setStarnumber(rateaverage);
                item1.setPaymentstate(paymentstate);

                    vendordisplayitems.add(item1);

            }
        }

        return vendordisplayitems;


    }


    public class SeeVendorFromSearch extends AsyncTask {
        String prompt;
        ArrayList<vendordisplayitem> vendordisplayitems;
        @Override
        protected void onPreExecute() {
            progressbar.setVisibility(View.VISIBLE);
            novendorbynamelayout.setVisibility(View.GONE);
            noconnection.setVisibility(View.GONE);
            error.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            searchrecycler.setVisibility(View.GONE);


        }

        @Override
        protected Object doInBackground(Object[] objects) {
            if (!isNetworkAvailable()) {
                prompt = "No Network Connection";
                return prompt;
            }

            String serverResponse = new searchVendorByName().GetData(SeeVendorFromSearchURL,city,area,shopname);

            if (serverResponse != null) {

                if (serverResponse.equalsIgnoreCase("")) {
                    return serverResponse;
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(serverResponse);
                        JSONObject info = jsonObject.getJSONObject("info");
                        String status = info.getString("status");

                        if (status.equalsIgnoreCase("vendorsavailable")) {

                            vendordisplayitems = passVendorJson(info);
                            return vendordisplayitems;
                        }
                        else if(status.equalsIgnoreCase("vendorsnotavailable")){
                              prompt = "vendorsnotavailable";
                              return prompt;
                        }
                        //return serverResponse;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
            return null;
        }

        protected void onPostExecute(Object result) {
            progressbar.setVisibility(View.GONE);

            if(result != null){

                if(result.equals("No Network Connection")){

                    recyclerView.setVisibility(View.GONE);
                    progressbar.setVisibility(View.GONE);
                    novendorbynamelayout.setVisibility(View.GONE);
                    noconnection.setVisibility(View.VISIBLE);
                    error.setVisibility(View.GONE);
                }
                if(result.toString().equalsIgnoreCase("vendorsnotavailable")){
                    recyclerView.setVisibility(View.GONE);
                    progressbar.setVisibility(View.GONE);
                    searchrecycler.setVisibility(View.GONE);
                    novendorbynamelayout.setVisibility(View.VISIBLE);
                    noconnection.setVisibility(View.GONE);
                    error.setVisibility(View.GONE);

                }
                else {
                     vendordisplayadapter = new vendordisplayadapter(getContext(), (ArrayList<vendordisplayitem>) result, (int clickedItemIndex) -> {
                        ArrayList<vendordisplayitem> vendordisplayitems = ((ArrayList<vendordisplayitem>) result);
                        Intent intent = new Intent(getContext(), VendorEshop.class);
                        vendordisplayitem vendorchoosen = vendordisplayitems.get(clickedItemIndex);
                        intent.putExtra("useremail", email);
                        ArrayList<vendordisplayitem> vendorclicked = new ArrayList<>();
                        vendorclicked.add(vendorchoosen);

                        Log.e("size", Integer.toString(((ArrayList<vendordisplayitem>) result).size()));
                        intent.putParcelableArrayListExtra("vendorinfo", vendorclicked);
                        intent.putParcelableArrayListExtra("userprofile", userprofiles);

                        if(seevendorflag == 0) {
                            seevendorflag = 1;
                            startActivity(intent);
                        }

                    });
                    if (vendordisplayadapter.getItemCount() < 2) {
                        recyclerView.setVisibility(View.GONE);
                        searchrecycler.setVisibility(View.GONE);
                        progressbar.setVisibility(View.GONE);
                        novendorbynamelayout.setVisibility(View.VISIBLE);
                        noconnection.setVisibility(View.GONE);
                        error.setVisibility(View.GONE);
                    } else {
                        searchrecycler.setAdapter(vendordisplayadapter);
                        recyclerView.setVisibility(View.GONE);
                        searchrecycler.setVisibility(View.VISIBLE);
                        progressbar.setVisibility(View.GONE);
                        novendorbynamelayout.setVisibility(View.GONE);
                        noconnection.setVisibility(View.GONE);
                        error.setVisibility(View.GONE);
                    }
                }
            }
            else{
                progressbar.setVisibility(View.GONE);
                novendorbynamelayout.setVisibility(View.GONE);
                noconnection.setVisibility(View.GONE);
                searchrecycler.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                error.setVisibility(View.VISIBLE);
            }



        }

    }


    @Override
    public void setUserVisibleHint(boolean isvisibletouser){
        super.setUserVisibleHint(isvisibletouser);

        if(isvisibletouser){

        MainActivity.changedisplaysearch(View.VISIBLE);
        }
        else{
            MainActivity.changedisplaysearch(View.GONE);
        }
    }



}
