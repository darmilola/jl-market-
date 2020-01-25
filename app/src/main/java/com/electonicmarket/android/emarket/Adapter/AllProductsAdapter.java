package com.electonicmarket.android.emarket.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.electonicmarket.android.emarket.Models.ProductCategory;
import com.electonicmarket.android.emarket.Models.ProductModel;
import com.electonicmarket.android.emarket.R;
import com.electonicmarket.android.emarket.interfaces.PresenterLayer;

import java.util.ArrayList;
import java.util.List;

public class AllProductsAdapter extends RecyclerView.Adapter<AllProductsAdapter.ItemViewHolder> {

    private List<ProductCategory> categoryList;
    private PresenterLayer presenterLayer;
    public Context context;
    CoordinatorLayout coordinatorLayout;

    public AllProductsAdapter(CoordinatorLayout coordinatorLayout,List<ProductCategory> categoryList, PresenterLayer presenterLayer,Context context){
        this.categoryList = categoryList;
        this.presenterLayer = presenterLayer;
        this.context = context;
        this.coordinatorLayout = coordinatorLayout;
    }
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category,parent,false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
     holder.setCategoryName(categoryList.get(position).getCategoryName());
     holder.setProductList(categoryList.get(position).getProductList());
     holder.setProgressbarvisibility(categoryList.get(position).getProgressbarvisibility());
     holder.setRecyclervis(categoryList.get(position).getRecyclervisibility());

    }


    public void addProductsToCategory(int categoryid,List<ProductModel> productList,int recyclervis,int progressvis){
        categoryList.get(categoryid).setProductList(productList);
        categoryList.get(categoryid).setProgressbarvisibility(progressvis);
        categoryList.get(categoryid).setRecyclervisibility(recyclervis);
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView categoryNameview;
        RecyclerView productsrecycler;
        LinearLayout progressbarlayout;
        public ItemViewHolder(View itemView) {
            super(itemView);
            categoryNameview = (TextView)itemView.findViewById(R.id.category_name);
            progressbarlayout = itemView.findViewById(R.id.progressbarlayout);
            productsrecycler = (RecyclerView)itemView.findViewById(R.id.product_item_recyclerview);
            Typeface customfont2= Typeface.createFromAsset(itemView.getContext().getAssets(),"Roboto-Regular.ttf");
            categoryNameview.setTypeface(customfont2);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(itemView.getContext(),LinearLayoutManager.HORIZONTAL,false);
            //linearLayoutManager.setAutoMeasureEnabled(true);
           // productsrecycler.setNestedScrollingEnabled(false);
            productsrecycler.setLayoutManager(linearLayoutManager);

            productsrecycler.setItemAnimator(new DefaultItemAnimator());
            productsrecycler.setVisibility(View.GONE);
            productsrecycler.setAdapter(new ProductsAdapter(coordinatorLayout,new ArrayList<ProductModel>(),presenterLayer,context));
        }


        public void setCategoryName(String categoryName) {
            categoryNameview.setText(categoryName);
        }


        public void setProductList(List<ProductModel> productList) {

            ProductsAdapter productsAdapter = (ProductsAdapter)productsrecycler.getAdapter();
            productsAdapter.setProductList(productList);
            productsAdapter.notifyDataSetChanged();

        }

        public void setProgressbarvisibility(int progressbarvalue){

            progressbarlayout.setVisibility(progressbarvalue);
        }

        public void setRecyclervis(int value){
            productsrecycler.setVisibility(value);
        }
    }
}
