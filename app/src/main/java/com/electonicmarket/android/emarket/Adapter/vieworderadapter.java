package com.electonicmarket.android.emarket.Adapter;

import android.graphics.Typeface;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.electonicmarket.android.emarket.Models.OrderModel;
import com.electonicmarket.android.emarket.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class vieworderadapter extends RecyclerView.Adapter<vieworderadapter.viewholder> {

    ArrayList<OrderModel> productModelArrayList;

    public vieworderadapter(ArrayList<OrderModel> modelArrayList){
        this.productModelArrayList = modelArrayList;
    }

    @Override
    public vieworderadapter.viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.completeorderrecyclerviewitem,parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(vieworderadapter.viewholder holder, int position) {

        String productName = productModelArrayList.get(position).getProductname();
        String productCount = productModelArrayList.get(position).getProductcount();
        String price = productModelArrayList.get(position).getPrice();

        int countprice = productModelArrayList.get(position).getCountprice();
        Locale NigerianLocale = new Locale("en","ng");
        String unFormattedrealPrice = NumberFormat.getCurrencyInstance(NigerianLocale).format(countprice);
        String formattedrealPrice = unFormattedrealPrice.replaceAll("\\.00","");

        if(productCount.equalsIgnoreCase("0")){
            productCount = "1";
        }
        holder.CompleteOrderProductName.setText(productCount+"x"+"  "+ productName);
        holder.CompleteOrderProductPrice.setText(formattedrealPrice);
    }

    @Override
    public int getItemCount() {
        return productModelArrayList.size();
    }

    public class viewholder extends RecyclerView.ViewHolder{
        private TextView CompleteOrderProductName;
        private  TextView CompleteOrderProductPrice;
        public viewholder(View itemView) {
            super(itemView);
            CompleteOrderProductName = itemView.findViewById(R.id.completeOrderProductName);
            CompleteOrderProductPrice = itemView.findViewById(R.id.completeOrderItemPrice);
            Typeface customfont2= Typeface.createFromAsset(itemView.getContext().getAssets(),"Kylo-Light.otf");
            CompleteOrderProductName.setTypeface(customfont2);
            CompleteOrderProductPrice.setTypeface(customfont2);
        }
    }
}
