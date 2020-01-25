package com.electonicmarket.android.emarket.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;

import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.electonicmarket.android.emarket.Models.vendordisplayitem;
import com.electonicmarket.android.emarket.R;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;

import java.util.ArrayList;

public class vendordisplayadapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private ArrayList<vendordisplayitem> vendordisplayitems;
    private Context c;
    private ListItemClickListener mOnClickListener;
    private static final int headertype = 0;
    private static final int item = 1;


    public vendordisplayadapter(Context c,ArrayList<vendordisplayitem> vendordisplayitems,ListItemClickListener mOnClickListener){
        this.c = c;
        this.vendordisplayitems = vendordisplayitems;
        this.mOnClickListener = mOnClickListener;
    }

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }
    @Override
    public int getItemViewType(int position){
        if(vendordisplayitems.get(position).getViewtype() == "0"){
            return 0;
        }
        else if(vendordisplayitems.get(position).getViewtype() == "1"){
            return 1;
        }
        return 0;
    }

    @Override
    public int getItemCount() {
        return vendordisplayitems.size();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case 0:

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vendorlocationheader, parent, false);
                return new headerviewholder(view);
            case 1:
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.vendordisplayitem,parent,false);
                return new vendordisplayviewholder(v);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        switch (holder.getItemViewType()) {
            case 0:

                headerviewholder headerviewholder = (vendordisplayadapter.headerviewholder)holder;
                vendordisplayitem currentitem1 = vendordisplayitems.get(position);
                headerviewholder.headerlocation.setText(currentitem1.getHeadername());
              break;
                case 1:
                vendordisplayviewholder vendorholder = (vendordisplayadapter.vendordisplayviewholder)holder;
                vendordisplayitem currentitem = vendordisplayitems.get(position);
                vendorholder.Vendorname.setText(currentitem.getVendorname());
                vendorholder.ratingBar.setRating(Float.parseFloat(currentitem.getStarnumber()));

                    vendorholder.deliveringtime.setText(currentitem.getDeliveryminute()+"min.");

                if(currentitem.getCategory1().equalsIgnoreCase("")){
                    vendorholder.category1image.setVisibility(View.GONE);
                    vendorholder.category1.setVisibility(View.GONE);
                }
                else{
                    vendorholder.category1.setText(currentitem.getCategory1());
                    vendorholder.category1image.setVisibility(View.VISIBLE);
                    vendorholder.category1.setVisibility(View.VISIBLE);

                }
                if(currentitem.getCategory2().equalsIgnoreCase("")){
                    vendorholder.category2image.setVisibility(View.GONE);
                    vendorholder.category2.setVisibility(View.GONE);
                }
                else{
                    vendorholder.category2.setText(currentitem.getCategory2());
                    vendorholder.category2image.setVisibility(View.VISIBLE);
                    vendorholder.category2.setVisibility(View.VISIBLE);
                }

                    if(currentitem.getCategory3().equalsIgnoreCase("")){
                        vendorholder.category3image.setVisibility(View.GONE);
                        vendorholder.category3.setVisibility(View.GONE);
                    }
                    else{
                        vendorholder.category3.setText(currentitem.getCategory2());
                        vendorholder.category3image.setVisibility(View.VISIBLE);
                        vendorholder.category3.setVisibility(View.VISIBLE);
                    }

                vendorholder.category2.setText(currentitem.getCategory2());
                vendorholder.category3.setText(currentitem.getCategory3());
                    String ImageUri = currentitem.getVendorDisplayImage();
                    final Bitmap[] transimage = {null};
                    /*Uri imageuri = Uri.parse(ImageUri);
                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions.placeholder(R.drawable.marketpic);
                    requestOptions.error(R.drawable.marketpic);
                    Glide.with(vendorholder.itemView.getContext())
                            .setDefaultRequestOptions(requestOptions)
                            .load(imageuri)
                            .apply(RequestOptions.skipMemoryCacheOf(true))
                            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                            .into(vendorholder.DisplayImage);
                            */
                    Uri imageuri = Uri.parse(ImageUri);
                    ImageRequest request = ImageRequest.fromUri(imageuri);
                    DraweeController controller = Fresco.newDraweeControllerBuilder()
                            .setImageRequest(request)
                            .setOldController(vendorholder.DisplayImage.getController()).build();
                    vendorholder.DisplayImage.setController(controller);









                    ViewCompat.setTransitionName(((vendordisplayviewholder) holder).DisplayImage,currentitem.getVendorname());
                    vendorholder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                         mOnClickListener.onListItemClick(holder.getAdapterPosition());

                         }



                    });
                break;
        }

    }

    public class headerviewholder extends RecyclerView.ViewHolder{

        TextView headerlocation;
        public headerviewholder(View itemView) {
            super(itemView);
            headerlocation = itemView.findViewById(R.id.headerdeliverylocation);
            Typeface customfont= Typeface.createFromAsset(itemView.getContext().getAssets(),"Kylo-Regular.otf");
            headerlocation.setTypeface(customfont);
        }
    }    public class vendordisplayviewholder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView Vendorname;
        public TextView deliveringtime;
        public TextView category1;
        public TextView category2;
        public TextView category3;
        public TextView deliversin;
        public ImageView category1image;
        public ImageView category2image;
        public ImageView category3image;
        public SimpleDraweeView DisplayImage;
        public RatingBar ratingBar;
        public TextView favcount;


        public vendordisplayviewholder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            Vendorname = itemView.findViewById(R.id.vendorname);
            ratingBar = itemView.findViewById(R.id.vendordisplayrating);
            deliveringtime = itemView.findViewById(R.id.deliveringtimetext);
            category1 = itemView.findViewById(R.id.vendordisplayitemctegory1);
            category2 = itemView.findViewById(R.id.vendordisplayitemctegory2);
            category3 = itemView.findViewById(R.id.vendordisplayitemctegory3);
            deliversin = itemView.findViewById(R.id.deliversin);
            DisplayImage = itemView.findViewById(R.id.vendordisplayimage);
            category1image = itemView.findViewById(R.id.category1image);
            category2image = itemView.findViewById(R.id.category2image);
            category3image = itemView.findViewById(R.id.category3image);


            Typeface customfont= Typeface.createFromAsset(itemView.getContext().getAssets(),"Kylo-Light.otf");
            deliversin.setTypeface(customfont);
            category3.setTypeface(customfont);
            category2.setTypeface(customfont);
            category1.setTypeface(customfont);
            deliveringtime.setTypeface(customfont);
            Vendorname.setTypeface(customfont);
        }


        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            //mOnClickListener.onListItemClick(clickedPosition);
        }



    }
}
