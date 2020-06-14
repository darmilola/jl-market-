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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.electonicmarket.android.emarket.Adapter.AreaAdapter;
import com.electonicmarket.android.emarket.Models.DeliveryArea;
import com.electonicmarket.android.emarket.R;
import com.electonicmarket.android.emarket.splashscreen;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.TimeUnit;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class deliverylocationArea extends Fragment {

    Toolbar toolbar;
    View view;
    ImageView search;
    RecyclerView recyclerView;
    TextInputEditText areaName;
    String flag;
    static String mycityname;
    ArrayList<DeliveryArea> deliveryAreas;
    LinearLayout progressbarlayout,errorlayout,nonetworklayout;
    Button errorbutton, nonetworkbutton;
    TextView errortext,nonetworktext;
    String URL = "http://jl-market.com/vendor/SelectArea.php";

    public deliverylocationArea() {
        // Required empty public constructor
    }

    public static deliverylocationArea newInstance(String cityname) {
        mycityname = cityname;
        return new deliverylocationArea();
    }
    @Override
    public void onActivityCreated(@NonNull Bundle savedinstance){
        super.onActivityCreated(savedinstance);
        if(mycityname == null){
            Intent intent = new Intent(getContext(),splashscreen.class);
            startActivity(intent);
            return;
        }
        else {
            InitializeView();
            InitializeRecyclerView();
            SearchAreaTask areaTask = new SearchAreaTask();
            areaTask.execute();

            errorbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new SearchAreaTask().execute();
                }
            });

            nonetworkbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new SearchAreaTask().execute();
                }
            });


        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        view = inflater.inflate(R.layout.fragment_deliverylocation_area, container, false);
        InitializeView();





        return view;
    }


    public interface onAreaSelected {


        void passArea(String data,double log, double lat);
    }




    private void InitializeView() {

        progressbarlayout = view.findViewById(R.id.arealoadingprogressBarlayout);
        nonetworklayout = view.findViewById(R.id.nonetworklayout);
        errorlayout = view.findViewById(R.id.errorlayout);
        errorbutton = view.findViewById(R.id.errorbutton);
        nonetworkbutton = view.findViewById(R.id.nonetworkbutton);
        errortext = view.findViewById(R.id.errortext);
        nonetworktext = view.findViewById(R.id.nonetworktext);
        Typeface customfont2 = Typeface.createFromAsset(getActivity().getAssets(), "Kylo-Light.otf");
        nonetworktext.setTypeface(customfont2);
        nonetworkbutton.setTypeface(customfont2);
        errortext.setTypeface(customfont2);
        errorbutton.setTypeface(customfont2);

    }


    onAreaSelected areapasser;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        areapasser = (onAreaSelected) context;



    }

    private void InitializeRecyclerView() {
        recyclerView = (RecyclerView) view.findViewById(R.id.deliveryareasrecyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

    }

    private void setUpAdapter(ArrayList<DeliveryArea> deliveryAreas) {
        AreaAdapter areaAdapter = new AreaAdapter(deliveryAreas, new AreaAdapter.ListItemClickListener() {
            @Override
            public void onListItemClick(int clickedItemIndex) {
                String areaname = deliveryAreas.get(clickedItemIndex).getName();
                Double longitude = deliveryAreas.get(clickedItemIndex).getLongitude();
                Double latitude = deliveryAreas.get(clickedItemIndex).getLatitude();
                areapasser.passArea(areaname,longitude,latitude);
            }
        });
        Collections.sort(deliveryAreas, new Comparator<DeliveryArea>() {

            @Override
            public int compare(DeliveryArea o1, DeliveryArea o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        recyclerView.setAdapter(areaAdapter);


    }


    public class SearchAreaTask extends AsyncTask {
        String prompt;

        @Override
        protected void onPreExecute() {
            progressbarlayout.setVisibility(View.VISIBLE);
            errorlayout.setVisibility(View.GONE);
            nonetworklayout.setVisibility(View.GONE);

        }

        @Override
        protected Object doInBackground(Object[] objects) {



            if (!isNetworkAvailable()) {
                prompt = "No Network Connection";
                return prompt;
            }
            String serverResponse = new searcharea().GetData(URL);
            if (serverResponse != null) {
                Log.e("i am wrer", "doInBackground: ");

                try {
                    JSONObject jsonObject = new JSONObject(serverResponse);
                    JSONObject info = jsonObject.getJSONObject("info");
                    String status = info.getString("status");

                    if (status.equalsIgnoreCase("areasavailable")) {
                        deliveryAreas = passAreaJson(info);
                        Log.e("i reachh wrer", "doInBackground: ");
                        return deliveryAreas;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return serverResponse;
        }

        protected void onPostExecute(Object result) {

            progressbarlayout.setVisibility(View.GONE);


            if (result != null) {

                if(result.toString().equalsIgnoreCase("No Network Connection")){
                    progressbarlayout.setVisibility(View.GONE);
                    errorlayout.setVisibility(View.GONE);
                    nonetworklayout.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
                else {
                    progressbarlayout.setVisibility(View.GONE);
                    errorlayout.setVisibility(View.GONE);
                    nonetworklayout.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);

                    setUpAdapter((ArrayList<DeliveryArea>) result);
                    // Toast.makeText(getContext(), result.toString(), Toast.LENGTH_SHORT).show();
                }

                      }
            else{

                progressbarlayout.setVisibility(View.GONE);
                errorlayout.setVisibility(View.VISIBLE);
                nonetworklayout.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
            }
        }
    }

    public class searcharea {

        public String GetData(String url) {

            try {
                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(50, TimeUnit.SECONDS)
                        .writeTimeout(50, TimeUnit.SECONDS)
                        .readTimeout(50, TimeUnit.SECONDS)
                        .build();


                RequestBody formBody = new FormBody.Builder()
                        .add("cityname", mycityname)
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
    public ArrayList<DeliveryArea> passAreaJson(JSONObject jsonObject) throws JSONException {
        ArrayList<DeliveryArea> deliverylocationareas = new ArrayList<>();
        JSONArray jsonArray = jsonObject.getJSONArray("data");
        for (int i = 0; i < jsonArray.length(); ++i) {
            JSONObject j = jsonArray.getJSONObject(i);
            String areaname = j.getString("areaname");
            double log = j.getDouble("arealong");
            double lat = j.getDouble("arealat");
            DeliveryArea deliveryArea = new DeliveryArea(areaname,log,lat);
            deliverylocationareas.add(deliveryArea);
        }
        return deliverylocationareas;


    }





    private Boolean isNetworkAvailable() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}