package com.electonicmarket.android.emarket.Fragments;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.electonicmarket.android.emarket.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class loginPageFirstFragment extends Fragment {


    public loginPageFirstFragment() {
        // Required empty public constructor
    }

    public static loginPageFirstFragment newInstance(){

        loginPageFirstFragment firstFragment = new loginPageFirstFragment();
        return firstFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login_page_first, container, false);
    }





}
