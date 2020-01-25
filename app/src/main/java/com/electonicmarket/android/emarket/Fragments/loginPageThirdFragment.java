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
public class loginPageThirdFragment extends Fragment {


    public loginPageThirdFragment() {
        // Required empty public constructor
    }

    public static loginPageThirdFragment newInstance() {

        loginPageThirdFragment loginPageThirdFragment = new loginPageThirdFragment();
        return loginPageThirdFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login_page_third, container, false);
    }


}