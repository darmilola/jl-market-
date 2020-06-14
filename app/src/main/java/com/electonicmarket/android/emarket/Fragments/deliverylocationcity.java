package com.electonicmarket.android.emarket.Fragments;


import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toolbar;
import com.electonicmarket.android.emarket.Adapter.DeliveryCityAdapter;
import com.electonicmarket.android.emarket.Models.DeliveryCity;
import com.electonicmarket.android.emarket.R;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class deliverylocationcity extends Fragment {
    private static String URL = "http://jl-market.com/vendor/SelectCity.php";
    Toolbar toolbar;
    View view;
    ImageView search;
    TextInputEditText cityName;
    RecyclerView recyclerView;
    ArrayList<DeliveryCity> cities;

    LinearLayout progressbarlayout,errorlayout,nonetworklayout;
    Button errorbutton, nonetworkbutton;
    TextView errortext,nonetworktext;
    ArrayList<DeliveryCity> deliveryCities;


    public interface onCitySelected {


        void passCity(String data);
    }

    public deliverylocationcity() {
        // Required empty public constructor
    }

    public static deliverylocationcity newInstance() {

        return new deliverylocationcity();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_deliverylocationcity, container, false);
        InitializeView();
        SearchCityTask task = new SearchCityTask();
        task.execute();

        nonetworkbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SearchCityTask().execute();
            }
        });
        errorbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SearchCityTask().execute();
            }
        });
        return view;


    }

    private void InitializeView() {


        progressbarlayout = view.findViewById(R.id.cityloadingprogressBarlayout);


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

        recyclerView = view.findViewById(R.id.deliverylocationcityrecyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
    }



    private void setUpAdapter(ArrayList<DeliveryCity> cities) {
        DeliveryCityAdapter adapter = new DeliveryCityAdapter(cities, new DeliveryCityAdapter.ListItemClickListener() {
            @Override
            public void onListItemClick(int clickedItemIndex) {
                String item = cities.get(clickedItemIndex).getCityName();
                cityPasser.passCity(item);

            }
        });
        recyclerView.setAdapter(adapter);
    }

    onCitySelected cityPasser;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        cityPasser = (onCitySelected) context;


    }

    public class SearchCityTask extends AsyncTask {
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
            String serverResponse = new searchcity().GetData(URL);
            if (serverResponse != null) {


                try {
                    JSONObject jsonObject = new JSONObject(serverResponse);
                    JSONObject info = jsonObject.getJSONObject("info");
                    String status = info.getString("status");

                    if (status.equalsIgnoreCase("citiesavailable")) {
                        deliveryCities = passCityJson(info);
                        return deliveryCities;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        protected void onPostExecute(Object result) {

            progressbarlayout.setVisibility(View.GONE);


            if (result != null) {

                if (result.toString().equalsIgnoreCase("No Network Connection")) {
                    progressbarlayout.setVisibility(View.GONE);
                    errorlayout.setVisibility(View.GONE);
                    nonetworklayout.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    setUpAdapter((ArrayList<DeliveryCity>) result);
                    progressbarlayout.setVisibility(View.GONE);
                    errorlayout.setVisibility(View.GONE);
                    nonetworklayout.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }
            else{

                progressbarlayout.setVisibility(View.GONE);
                errorlayout.setVisibility(View.VISIBLE);
                nonetworklayout.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);

                //Toast.makeText(getContext(), "Unable to display area now please try again", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public class searchcity {

        public String GetData(String url) {

            try {
                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(50, TimeUnit.SECONDS)
                        .writeTimeout(50, TimeUnit.SECONDS)
                        .readTimeout(50, TimeUnit.SECONDS)
                        .build();
                Request request = new Request.Builder().url(url).build();
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

            ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }

        public ArrayList<DeliveryCity> passCityJson(JSONObject jsonObject) throws JSONException {
            ArrayList<DeliveryCity> deliverylocationcities = new ArrayList<>();
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); ++i) {
                JSONObject j = jsonArray.getJSONObject(i);
                String city = j.getString("cityname");
                DeliveryCity deliveryCity = new DeliveryCity(city);
                deliverylocationcities.add(deliveryCity);
            }
            return deliverylocationcities;


        }
    }
