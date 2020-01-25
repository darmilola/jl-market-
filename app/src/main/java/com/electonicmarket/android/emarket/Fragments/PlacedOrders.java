package com.electonicmarket.android.emarket.Fragments;


import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.electonicmarket.android.emarket.Adapter.placedorderadapter;
import com.electonicmarket.android.emarket.Models.placedordermodel;
import com.electonicmarket.android.emarket.Models.userprofile;
import com.electonicmarket.android.emarket.R;
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
public class PlacedOrders extends Fragment {

    RecyclerView recyclerView;
    ArrayList<placedordermodel> placedordermodelArrayList = new ArrayList<>();
    View view;
    LinearLayout progressbar,error,noconnection,emptyorder;
    TextView errortext,noconnectiontext,emptyordertext;
    Button noconnectionbutton,errorbutton;
    static String useremail;
    Context context;
    ArrayList<userprofile> userprofiles;
    public PlacedOrders() {
        // Required empty public constructor
    }

    public static PlacedOrders newInstance(String email){
        useremail = email;
        return new PlacedOrders();
    }

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
        this.context = context;
        }


    @Override
    public void onActivityCreated(@NonNull Bundle savedinstance){
        super.onActivityCreated(savedinstance);

        if(useremail == null){
            startActivity(new Intent(getContext(), splashscreen.class));
        }
        else{

            initializerecyclerview();
            initializeview();
            setOnclicklistener();
            if (isNetworkAvailable()) {

                getidtask getidtask = new getidtask();
                getidtask.execute();
            }
            else {

                emptyorder.setVisibility(View.GONE);
                progressbar.setVisibility(View.GONE);
                error.setVisibility(View.GONE);
                noconnection.setVisibility(View.VISIBLE);
            }

            }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


                view =  inflater.inflate(R.layout.fragment_placed_orders, container, false);



                return view;

    }

    private void setOnclicklistener(){
        noconnectionbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new getidtask().execute();
            }
        });

        errorbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new getidtask().execute();
            }
        });
    }




    private void initializeview(){
        progressbar = view.findViewById(R.id.placedorderprogressbarlayout);
        error = view.findViewById(R.id.errorlayout);
        noconnection = view.findViewById(R.id.nonetworklayout);
        emptyorder = view.findViewById(R.id.emptyorderlayout);
        errortext = view.findViewById(R.id.errortext);
        noconnectiontext = view.findViewById(R.id.nonetworktext);
        emptyordertext = view.findViewById(R.id.emptyordertext);
        noconnectionbutton = view.findViewById(R.id.nonetworkbutton);
        errorbutton = view.findViewById(R.id.errorbutton);

        Typeface customfont2 = Typeface.createFromAsset(getActivity().getAssets(), "Kylo-Regular.otf");
        errorbutton.setTypeface(customfont2);
        errortext.setTypeface(customfont2);
        noconnectionbutton.setTypeface(customfont2);
        noconnectiontext.setTypeface(customfont2);
        emptyordertext.setTypeface(customfont2);
    }
    private void initializerecyclerview(){
        recyclerView = (RecyclerView)view.findViewById(R.id.placedorderrecyclerview);
        RecyclerView.LayoutManager mLayoutManager =
                new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
    }

    public class getidtask extends AsyncTask{
        String prompt;
        private  String URL = "http://jl-market.com/user/getOrderid.php";

        @Override
        protected void onPreExecute() {

            emptyorder.setVisibility(View.GONE);
            progressbar.setVisibility(View.VISIBLE);
            error.setVisibility(View.GONE);
            noconnection.setVisibility(View.GONE);
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            if (!isNetworkAvailable()) {
                prompt = "No Network Connection";
                return prompt;
            }
            String serverResponse = new getid().GetData(URL, useremail);
            if (serverResponse != null) {
                if (serverResponse.equalsIgnoreCase("")) {
                    return serverResponse;
                } else {

                    try {
                        JSONObject jsonObject = new JSONObject(serverResponse);
                        JSONObject info = jsonObject.getJSONObject("info");
                        String status = info.getString("status");

                        if (status.equalsIgnoreCase("available")) {
                            placedordermodelArrayList = passIdJson(info);

                            return placedordermodelArrayList;
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
           return null;
        }
        protected void onPostExecute(Object result) {
             progressbar.setVisibility(View.GONE);

            //Toast.makeText(context, result.toString(), Toast.LENGTH_LONG).show();
             if (result != null) {

                 if(result.toString().equalsIgnoreCase("")){

                     emptyorder.setVisibility(View.VISIBLE);
                     progressbar.setVisibility(View.GONE);
                     error.setVisibility(View.GONE);
                     noconnection.setVisibility(View.GONE);
                 }
                if(result instanceof ArrayList) {
                    placedorderadapter placedorderadapter = new placedorderadapter((ArrayList<placedordermodel>) result, useremail);
                    recyclerView.setAdapter(placedorderadapter);
                    if(placedorderadapter.getItemCount() < 1){
                        emptyorder.setVisibility(View.VISIBLE);
                        progressbar.setVisibility(View.GONE);
                        error.setVisibility(View.GONE);
                        noconnection.setVisibility(View.GONE);
                    }
                    else{

                        emptyorder.setVisibility(View.GONE);
                        progressbar.setVisibility(View.GONE);
                        error.setVisibility(View.GONE);
                        noconnection.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }

                }

                if(result.equals("No Network Connection")){
                    emptyorder.setVisibility(View.GONE);
                    progressbar.setVisibility(View.GONE);
                    error.setVisibility(View.GONE);
                    noconnection.setVisibility(View.VISIBLE);
                }
            }
            else {

                emptyorder.setVisibility(View.GONE);
                progressbar.setVisibility(View.GONE);
                error.setVisibility(View.VISIBLE);
                noconnection.setVisibility(View.GONE);
            }

        }
    }


        public ArrayList<placedordermodel> passIdJson(JSONObject jsonObject) throws JSONException {
            ArrayList<placedordermodel> placedorderidlist = new ArrayList<>();
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); ++i) {
                JSONObject j = jsonArray.getJSONObject(i);
                String orderid = j.getString("orderid");
                placedordermodel placedordermodel = new placedordermodel(orderid);
                placedorderidlist.add(placedordermodel);
            }
            return placedorderidlist;


        }


    public class getid {

        public String GetData(String url,String useremail) {

            try {
                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(50, TimeUnit.SECONDS)
                        .writeTimeout(50, TimeUnit.SECONDS)
                        .readTimeout(50, TimeUnit.SECONDS)
                        .build();


                RequestBody formBody = new FormBody.Builder()
                        .add("userid",useremail )
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

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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


}

