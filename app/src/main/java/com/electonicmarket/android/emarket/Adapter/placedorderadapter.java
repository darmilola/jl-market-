package com.electonicmarket.android.emarket.Adapter;

import android.content.Intent;
import android.graphics.Typeface;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.electonicmarket.android.emarket.Activities.ViewOrder;
import com.electonicmarket.android.emarket.Models.placedordermodel;
import com.electonicmarket.android.emarket.R;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class placedorderadapter extends RecyclerView.Adapter<placedorderadapter.viewholder> {

    List<placedordermodel> placedordermodelList;
    String muserid;
    public placedorderadapter(ArrayList<placedordermodel> placedordermodelArrayList,String userid){
        this.placedordermodelList = placedordermodelArrayList;
        this.muserid = userid;
    }

    @Override
    public placedorderadapter.viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.placedorderitem,parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(placedorderadapter.viewholder holder, int position) {
            holder.orderno.setText("Order"+" #"+placedordermodelList.get(position).getOrderid());
    }

    @Override
    public int getItemCount() {
        return placedordermodelList.size();
    }

    public class viewholder extends RecyclerView.ViewHolder implements View.OnClickListener{
     TextView orderno;
     TextView placedordertitle;
     TextView processingnote;
     Button vieworder;
        public viewholder(View itemView) {
            super(itemView);
            orderno = itemView.findViewById(R.id.orderno);
            placedordertitle = itemView.findViewById(R.id.orderplacedsuccessfully);
            processingnote = itemView.findViewById(R.id.orderprocessingnote);
            vieworder = itemView.findViewById(R.id.vieworder);
           Typeface customfont= Typeface.createFromAsset(itemView.getContext().getAssets(),"Kylo-Light.otf");
           orderno.setTypeface(customfont);
           placedordertitle.setTypeface(customfont);
           processingnote.setTypeface(customfont);
           vieworder.setTypeface(customfont);
           vieworder.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            if(v.getId() == R.id.vieworder){
                int i = getAdapterPosition();
                placedordermodel current = placedordermodelList.get(i);
                Intent intent = new Intent(v.getContext(), ViewOrder.class);
                intent.putExtra("orderid",current.getOrderid());
                intent.putExtra("userid",muserid);

              //Toast.makeText(v.getContext(), Integer.toString(i), Toast.LENGTH_SHORT).show();
                v.getContext().startActivity(intent);
            }
        }
    }
}
