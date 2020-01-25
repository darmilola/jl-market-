package com.electonicmarket.android.emarket.Adapter;

import android.content.Context;
import android.graphics.Typeface;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.electonicmarket.android.emarket.Models.vendorinfodeal;
import com.electonicmarket.android.emarket.R;

import java.util.ArrayList;
import java.util.List;

public class DealAdapter extends RecyclerView.Adapter<DealAdapter.mViewholder> {

    private List<vendorinfodeal> dealList;
    private Context context;

   public DealAdapter(ArrayList<vendorinfodeal> dealList, Context context){
        this.dealList = dealList;
        this.context = context;
    }
    @Override
    public mViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dealitem,parent,false);
        return new mViewholder(view);
    }

    @Override
    public void onBindViewHolder(mViewholder holder, int position) {
       holder.description.setText(dealList.get(position).getDescription());


           }




    @Override
    public int getItemCount() {
        return dealList.size();
    }

    public  class mViewholder extends RecyclerView.ViewHolder{

       TextView description;
       TextView viewoptions;

        public mViewholder(View itemView) {
            super(itemView);
            description = itemView.findViewById(R.id.dealdescription);

            Typeface customfont2= Typeface.createFromAsset(itemView.getContext().getAssets(),"Kylo-Light.otf");
            description.setTypeface(customfont2);
        }
    }


}
