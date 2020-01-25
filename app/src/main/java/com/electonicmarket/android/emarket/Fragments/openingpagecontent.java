package com.electonicmarket.android.emarket.Fragments;


import android.graphics.Typeface;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.electonicmarket.android.emarket.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class openingpagecontent extends Fragment {


    View view;
    public static ViewPager TextViewPager;
    TextView welcometext;
    public openingpagecontent() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

                view = inflater.inflate(R.layout.fragment_openingpagecontent, container, false);

                welcometext = view.findViewById(R.id.openingpagecontenttitle);


        Typeface customfont= Typeface.createFromAsset(getActivity().getAssets(),"Poet.ttf");

        welcometext.setTypeface(customfont);

                return view;
    }






}
