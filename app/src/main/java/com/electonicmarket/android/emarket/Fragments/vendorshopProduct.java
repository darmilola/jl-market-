package com.electonicmarket.android.emarket.Fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.electonicmarket.android.emarket.Adapter.AllProductsAdapter;
import com.electonicmarket.android.emarket.Models.ProductCategory;
import com.electonicmarket.android.emarket.Models.Presenter;
import com.electonicmarket.android.emarket.Models.ProductModel;
import com.electonicmarket.android.emarket.R;
import com.electonicmarket.android.emarket.interfaces.PresenterLayer;
import com.electonicmarket.android.emarket.interfaces.ViewLayer;
import com.electonicmarket.android.emarket.splashscreen;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class vendorshopProduct extends Fragment implements ViewLayer {


    RecyclerView AllproductsRecycler;
    LinearLayout progressbarlayout;
    PresenterLayer presenter;
    View view;
    int listcount;
    static String mvendoremail,museremail;
    CoordinatorLayout coordinatorLayout;
    public vendorshopProduct() {
        // Required empty public constructor
    }


    public static vendorshopProduct newInstance(String vendoremail,String useremail){
        vendorshopProduct product = new vendorshopProduct();
        mvendoremail = vendoremail;
        museremail = useremail;
        return product;
    }
    @Override
    public void onActivityCreated(@NonNull Bundle savedinstance){
        super.onActivityCreated(savedinstance);
        if(museremail == null){
            Intent intent = new Intent(getContext(),splashscreen.class);
            startActivity(intent);
            return;
        }
        else {

            initializeView();
            allProductsRecyclerInit();
            presenter = new Presenter(this,getContext(),mvendoremail,museremail);
            presenter.onViewReady();
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_vendorshop_product, container, false);

       return view;
    }

    private void initializeView(){
        progressbarlayout = view.findViewById(R.id.progressbarlayout);
    }
    private void allProductsRecyclerInit() {
        AllproductsRecycler = (RecyclerView)view.findViewById(R.id.main_recyclerview);
        coordinatorLayout = view.findViewById(R.id.rootview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);

        AllproductsRecycler.setLayoutManager(linearLayoutManager);
        //AllproductsRecycler.getRecycledViewPool().setMaxRecycledViews(0,0);
        //AllproductsRecycler.setItemAnimator(new DefaultItemAnimator());

    }
    @Override
    public void showCategories(List<ProductCategory> categoryItemList, PresenterLayer presenter) {
         //AllproductsRecycler.setNestedScrollingEnabled(false);
        AllproductsRecycler.setAdapter(new AllProductsAdapter(coordinatorLayout,categoryItemList,presenter,getContext()));

        if(categoryItemList.size() < 1){
            AllproductsRecycler.setVisibility(View.GONE);
            progressbarlayout.setVisibility(View.VISIBLE);
        }
        else {
            AllproductsRecycler.setVisibility(View.VISIBLE);
            progressbarlayout.setVisibility(View.GONE);
        }
        listcount = categoryItemList.size();
       // Log.e("categorysize",Integer.toString(j) );

    }
    @Override
    public void showProductsInCategory(int categoryid, List<ProductModel> productList, PresenterLayer presenter) {


            AllProductsAdapter adapter = (AllProductsAdapter)AllproductsRecycler.getAdapter();
            adapter.addProductsToCategory(categoryid,productList,View.VISIBLE,View.GONE);
            adapter.notifyItemChanged(categoryid);

            }


    }





