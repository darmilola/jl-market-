package com.electonicmarket.android.emarket.Fragments;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.electonicmarket.android.emarket.Activities.MainActivity;
import com.electonicmarket.android.emarket.Activities.becomeavendor;
import com.electonicmarket.android.emarket.Activities.privacypolicy;
import com.electonicmarket.android.emarket.Models.userprofile;

import com.electonicmarket.android.emarket.R;
import com.electonicmarket.android.emarket.splashscreen;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class info extends Fragment {

    TextView becomeavendor,termsandcondition,privacypolicy;
    View view;
    static ArrayList<userprofile> userprofiles;
    public info() {
        // Required empty public constructor
    }
    public static info newInstance(ArrayList<userprofile> userprofileArrayList){
        userprofiles = userprofileArrayList;
        return  new info();
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

            initializeview();
            setOnclicklistener();
        }

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

               view = inflater.inflate(R.layout.fragment_info, container, false);

               return view;
    }
   private void initializeview(){
        becomeavendor = view.findViewById(R.id.becomeavendor);

        privacypolicy = view.findViewById(R.id.privacypolicy);
       Typeface customfont2= Typeface.createFromAsset(getActivity().getAssets(),"Kylo-Light.otf");
       becomeavendor.setTypeface(customfont2);

       privacypolicy.setTypeface(customfont2);
       MainActivity.changedisplaysearch(View.GONE);
    }

    private void setOnclicklistener(){
        becomeavendor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),becomeavendor.class);
                intent.putParcelableArrayListExtra("userprofile",userprofiles);
                startActivity(intent);
            }
        });
        privacypolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),privacypolicy.class));
            }
        });
    }

}
