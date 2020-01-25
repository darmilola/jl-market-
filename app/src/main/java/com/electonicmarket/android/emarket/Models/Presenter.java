package com.electonicmarket.android.emarket.Models;

import android.content.Context;
import android.util.Log;

import com.electonicmarket.android.emarket.interfaces.InteractorLayer;
import com.electonicmarket.android.emarket.interfaces.InteractorListener;
import com.electonicmarket.android.emarket.interfaces.PresenterLayer;
import com.electonicmarket.android.emarket.interfaces.ViewLayer;

import java.util.List;

public class Presenter implements PresenterLayer, InteractorListener{
    private InteractorLayer minteractorLayer;
    private ViewLayer mviewLayer;
    Context mcontext;
    String memail,useremail;

    public Presenter(ViewLayer viewLayer,Context context,String vendoremail,String useremail){
        mviewLayer = viewLayer;
        this.mcontext = context;
        this.memail = vendoremail;
        this.useremail = useremail;
        minteractorLayer = new ProductsLoader(this,context,vendoremail,useremail);
    }

    @Override
    public void onProductsLoaded(int id, List<ProductModel> products) {
        mviewLayer.showProductsInCategory(id,products,this);
    }

    @Override
    public void onCategoriesLoaded(List<ProductCategory> categories) {
        List<ProductCategory> mcategoryList = categories;
        mviewLayer.showCategories(categories,this);
        for(int i = 0; i < categories.size(); i++){
            Log.e("category name", categories.get(i).getCategoryName() );
            minteractorLayer.getProducts(i,categories.get(i).getCategoryName());//i,for indexing,catname to load product for the category
        }
    }

    @Override
    public void onViewReady() {
     minteractorLayer.getCategories();

    }
}
