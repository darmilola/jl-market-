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
import com.electonicmarket.android.emarket.Activities.changepassword;
import com.electonicmarket.android.emarket.Models.userprofile;
import com.electonicmarket.android.emarket.R;
import com.electonicmarket.android.emarket.splashscreen;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class Settings extends Fragment {

    TextView changepassword,version;
    View view;
    static ArrayList<userprofile> userprofiles;

    public Settings() {
        // Required empty public constructor
    }

    public static Settings newInstance(ArrayList<userprofile> muserprofiles){
        userprofiles = muserprofiles;
        return new Settings();
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

            initview();

            changepassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(),changepassword.class);
                    intent.putParcelableArrayListExtra("userprofile",userprofiles);
                    startActivity(intent);
                }
            });
        }

    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

               view =  inflater.inflate(R.layout.fragment_settings, container, false);

               return view;
    }

    private void initview(){
        changepassword = view.findViewById(R.id.changepassword);
        version = view.findViewById(R.id.version);
        Typeface customfont2= Typeface.createFromAsset(getActivity().getAssets(),"Kylo-Light.otf");
        changepassword.setTypeface(customfont2);
        version.setTypeface(customfont2);
        MainActivity.changedisplaysearch(View.GONE);
    }

}
