package com.electonicmarket.android.emarket.Fragments;


import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ToxicBakery.viewpager.transforms.ZoomOutTranformer;
import com.electonicmarket.android.emarket.Models.MyAutoScrollViewPager;
import com.electonicmarket.android.emarket.R;
import com.github.vivchar.viewpagerindicator.ViewPagerIndicator;


/**
 * A simple {@link Fragment} subclass.
 */
public class openingpagefragment extends Fragment {

    MyAutoScrollViewPager viewPager;
    public static ViewPager TextViewPager;
    ViewPagerIndicator viewPagerIndicator;
    LinearLayout openingpagetint;

    View view;

    public openingpagefragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

             view =  inflater.inflate(R.layout.fragment_openingpagefragment, container, false);
             viewPager = (MyAutoScrollViewPager) view.findViewById(R.id.loginviewpager);
             viewPager.setPageTransformer(false,new ZoomOutTranformer());






        FragmentPagerAdapter  adapter=new LoginAdapter(getActivity().getSupportFragmentManager());


        Typeface customfont= Typeface.createFromAsset(getActivity().getAssets(),"Kylo-Light.otf");


        viewPager.setAdapter(adapter);




        viewPager.setInterval(3000);
        viewPager.startAutoScroll();
        viewPager.setStopScrollWhenTouch(true);
        Fragment fragment = new openingpagecontent();
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.openingpagecontentfragment,fragment);
        fragmentTransaction.commit();

        return view;

    }




    public class LoginAdapter extends FragmentPagerAdapter {
        int mNumofTabs;

        public LoginAdapter(FragmentManager fm) {
            super(fm);

        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {

                case 0:
                    return loginPageFirstFragment.newInstance();

                case 1:
                    return LoginPageSecondFragment.newInstance();

                case 2:
                    return loginPageThirdFragment.newInstance();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }



    @Override
    public void onResume(){

        super.onResume();
        viewPager.startAutoScroll();
    }

    @Override
    public void onPause() {
        super.onPause();
        viewPager.stopAutoScroll();
    }




}
