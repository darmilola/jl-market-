package com.electonicmarket.android.emarket.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.electonicmarket.android.emarket.Activities.ProductFullDetail;
import com.electonicmarket.android.emarket.Models.ProductModel;
import com.electonicmarket.android.emarket.R;
import com.electonicmarket.android.emarket.service.UpdateCartCountService;
import com.electonicmarket.android.emarket.service.updateservice.UpdateCartRecyclerview;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import androidx.recyclerview.widget.RecyclerView;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class cartitemadapter extends RecyclerView.Adapter<cartitemadapter.cartviewholder> {
    List<ProductModel> productModelList;
    Context context;
    String userid,productid;
    Dialog loadingdialog;
    public cartitemadapter(ArrayList<ProductModel> models,Context context){
        this.productModelList = models;
        this.context = context;

    }
    @Override
    public cartitemadapter.cartviewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cartitem,parent,false);
        return new cartviewholder(view);
    }

    @Override
    public void onBindViewHolder(cartviewholder holder, int position) {

        holder.productName.setText(productModelList.get(position).getProductName());
        holder.productDescription.setText(productModelList.get(position).getProductDescription());

        //holder.CartproductImage.setImageResource(productModelList.get(position).getProductImagefirst());

        Locale NigerianLocale = new Locale("en","ng");
         String productprice = productModelList.get(position).getoldProductPrice();

        String unFormattedrealPrice = NumberFormat.getCurrencyInstance(NigerianLocale).format(Integer.parseInt(productprice));
        String formattedrealPrice = unFormattedrealPrice.replaceAll("\\.00","");
        holder.oldproductPrice.setText(formattedrealPrice);

        //if(Integer.parseInt(productModelList.get(position).getProductCount()) <= 1){
          //  holder.decreaseProductCount.setEnabled(false);
       // }
        //else {
             String cartcount = productModelList.get(position).getProductCount();
             if(cartcount.equalsIgnoreCase("0")){
                 cartcount = "1";
             }
            holder.cartproductcount.setText(cartcount);
            //holder.decreaseProductCount.setEnabled(true);

        //}

        if(productModelList.get(position).getProductnewPrice() != null){
            holder.newproductPrice.setVisibility(View.VISIBLE);
            String ReducedPrice = productModelList.get(position).getProductnewPrice();
            String unFormattedPrice = NumberFormat.getCurrencyInstance(NigerianLocale).format(Integer.parseInt(ReducedPrice));
            String formattedPrice = unFormattedPrice.replaceAll("\\.00","");

            holder.oldproductPrice.setText(formattedPrice);
            holder.newproductPrice.setText(formattedrealPrice);
            holder.newproductPrice.setPaintFlags(holder.newproductPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        else{
            holder.newproductPrice.setVisibility(View.GONE);
        }

        String imagefirstUri = productModelList.get(position).getProductImagefirst();

        Uri imageuri = Uri.parse(imagefirstUri);
        ImageRequest request = ImageRequest.fromUri(imageuri);
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(holder.CartproductImage.getController()).build();
        holder.CartproductImage.setController(controller);



        /*Uri imageuri = Uri.parse(imagefirstUri);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.marketpic);
        requestOptions.error(R.drawable.marketpic);
        Glide.with(holder.itemView.getContext())
                .setDefaultRequestOptions(requestOptions)
                .load(imageuri)
                .apply(RequestOptions.skipMemoryCacheOf(true))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .into(holder.CartproductImage);*/






    }

    @Override
    public int getItemCount() {
        return productModelList.size();
    }

    public class cartviewholder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private SimpleDraweeView CartproductImage;
        private TextView productName;
        private TextView productDescription;
        private TextView oldproductPrice;
        private ImageView increaseProductCount;
        private ImageView decreaseProductCount;
        private TextView  deleteFromCart;
        private TextView newproductPrice;
        private TextView cartproductcount;
        private View parentlayout;



        public cartviewholder(View itemView) {
            super(itemView);
           parentlayout = itemView.findViewById(R.id.parentlayout);
           CartproductImage = itemView.findViewById(R.id.cartitemproductimage);
           productName = (TextView)itemView.findViewById(R.id.cartitemproductname);
           productDescription = (TextView)itemView.findViewById(R.id.cartiemproductdescription);
           oldproductPrice = (TextView)itemView.findViewById(R.id.cartitemnewproductprice);
           increaseProductCount = (ImageView)itemView.findViewById(R.id.cartitemproductincrease);
           decreaseProductCount = (ImageView)itemView.findViewById(R.id.cartitemproductdecrease);
           deleteFromCart = (TextView)itemView.findViewById(R.id.cartitemproductremove);
           newproductPrice = (TextView)itemView.findViewById(R.id.cartitemoldproductprice);

           cartproductcount = (TextView)itemView.findViewById(R.id.cartitemproductcount);

            Typeface customfont2= Typeface.createFromAsset(itemView.getContext().getAssets(),"Kylo-Light.otf");
            oldproductPrice.setTypeface(customfont2);
            productDescription.setTypeface(customfont2);
            productName.setTypeface(customfont2);
            deleteFromCart.setTypeface(customfont2);
            cartproductcount.setTypeface(customfont2);
            deleteFromCart.setOnClickListener(this);
            decreaseProductCount.setOnClickListener(this);
            increaseProductCount.setOnClickListener(this);
            parentlayout.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            int i = getAdapterPosition();

            if(v.getId() == R.id.cartitemproductremove){

             productid = productModelList.get(i).getProductid();
             userid = productModelList.get(i).getUseremail();
             DeleteFromCartTask deleteFromCartTask = new DeleteFromCartTask();
             deleteFromCartTask.execute();
            }
            else if(v.getId() == R.id.cartitemproductincrease){
                String oldcount = cartproductcount.getText().toString();
                cartproductcount.setText(Integer.toString(Integer.parseInt(oldcount)+1));
                productid = productModelList.get(i).getProductid();
                userid = productModelList.get(i).getUseremail();
                IncreaseCartCount increaseCartCount = new IncreaseCartCount();
                increaseCartCount.execute();

            }

            else if(v.getId() == R.id.cartitemproductdecrease){
                String oldcount = cartproductcount.getText().toString();
                cartproductcount.setText(Integer.toString(Integer.parseInt(oldcount)-1));
                if(Integer.parseInt(cartproductcount.getText().toString()) <= 1){
                    cartproductcount.setText("1");
                }
                productid = productModelList.get(i).getProductid();
                userid = productModelList.get(i).getUseremail();
                decreaseCartCount decreaseCartCount = new decreaseCartCount();
                decreaseCartCount.execute();
            }

            else{
                //int i = getAdapterPosition();
                String productImageFirst, productImageSecond;
                ArrayList<ProductModel> productModels = new ArrayList<>();
                ProductModel model;
                Intent intent = new Intent(v.getContext(), ProductFullDetail.class);
                ProductModel current = productModelList.get(i);
                String productName = current.getProductName();
                String productDescription = current.getProductDescription();
                String productPrice = current.getoldProductPrice();
                productImageFirst = current.getProductImagefirst();



                model = new ProductModel(productDescription, productName, productPrice, productImageFirst);
                if (!(current.getProductImageSecond()== null)) {
                    Log.e("here", "onClick: cheking" );
                    productImageSecond = current.getProductImageSecond();
                    model = new ProductModel(productDescription, productName, productPrice, productImageFirst, productImageSecond);

                    //if (!(current.getProductImageThird() == null)) {
                    ///  productImageThird = current.getProductImageThird();
                    //model = new ProductModel(productDescription, productName, productPrice, productImageFirst, productImageSecond, productImageThird);


                    //}

                }

                if(current.getProductnewPrice() == null){

                }
                else{
                    model.setProductnewPrice(current.getProductnewPrice());
                    //String percentdecrease = current.getPercentDecrease();

                    Double newPrice = Double.parseDouble(current.getProductnewPrice());
                    Double oldPrice = Double.parseDouble(productPrice);
                    Double decrease = oldPrice - newPrice;
                    String percentDecrease = Integer.toString((int) ((decrease/oldPrice)*100));

                    model.setPercentDecrease(percentDecrease);
                    Log.e("ew res ere", "onClick: ");
                }

                Log.e("useremail", current.getUseremail());
                model.setUseremail(current.getUseremail());
                //model.setProductnewPrice(current.getProductnewPrice());

                productModels.add(model);
                //Log.e("checking", current.get(i).getProductImageSecond() );
                intent.putParcelableArrayListExtra("productlist", productModels);
                intent.putExtra("fromcart","fromcart");
                v.getContext().startActivity(intent);

            }
        }
    }

    private void showloadingdialog() {
        loadingdialog = new Dialog(context, R.style.Dialog_Theme);
        loadingdialog.setContentView(R.layout.loadingdialog);
        //Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.loading);
        ImageView image = loadingdialog.findViewById(R.id.loadingimage);
        DrawableImageViewTarget imageViewTarget = new DrawableImageViewTarget(image);
        Glide.with(context).load(R.drawable.loading).into(imageViewTarget);
        loadingdialog.show();
        loadingdialog.setCancelable(false);
        //getContext().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        loadingdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }


    public class DeleteFromCartTask extends AsyncTask {
        String url = "http://jl-market.com/user/deleteitemfromcart.php";
        String prompt;

        @Override
        protected void onPreExecute() {

             showloadingdialog();
        }
        @Override
        protected Object doInBackground(Object[] objects) {

            if (!isNetworkAvailable()) {
                prompt = "No Network Connection";
                return prompt;
            }




                String serverresponse = new deletefromcartoinfo().GetData(url,userid,productid);
                return serverresponse;


        }

        protected void onPostExecute(Object result) {

            loadingdialog.dismiss();
            if(result != null) {
                if (result.equals(prompt)) {

                    Toast.makeText(context, prompt, Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(context, "Product Deleted Successfully", Toast.LENGTH_SHORT).show();

                    Intent serviceintent = new Intent(context, UpdateCartRecyclerview.class);
                    serviceintent.putExtra("url", "http://jl-market.com/user/userscart.php");
                    serviceintent.putExtra("userid", userid);
                    context.startService(serviceintent);

                    Intent serviceintent2 = new Intent(context, UpdateCartCountService.class);
                    serviceintent2.putExtra("url", "http://jl-market.com/user/userscart.php");
                    serviceintent2.putExtra("userid", userid);
                    context.startService(serviceintent2);


                }

            }



            }
        }



    public class deletefromcartoinfo {

        public String GetData(String url, String userid, String productid) {

            try {
                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(50, TimeUnit.SECONDS)
                        .writeTimeout(50, TimeUnit.SECONDS)
                        .readTimeout(50, TimeUnit.SECONDS)
                        .build();


                RequestBody formBody = new FormBody.Builder()
                        .add("userid", userid)
                        .add("productid", productid)
                        .build();
                Request request = new Request.Builder().post(formBody).url(url).build();
                Response response = client.newCall(request).execute();
                String data = response.body().string();
                return data;

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

    }



    private Boolean isNetworkAvailable() {

        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public class IncreaseCartCount extends AsyncTask {
        String url = "http://jl-market.com/user/increaseproductcount.php";
        String prompt;

        @Override
        protected void onPreExecute() {
            //Toast.makeText(context, "increasing cart product", Toast.LENGTH_SHORT).show();

        }
        @Override
        protected Object doInBackground(Object[] objects) {

            if (!isNetworkAvailable()) {
                prompt = "No Network Connection";
                return prompt;
            }




            String serverresponse = new increasecartcountinfo().GetData(url,userid,productid);
            return serverresponse;


        }

        protected void onPostExecute(Object result) {

            if(result.equals(prompt)){

                //Toast.makeText(context, prompt, Toast.LENGTH_SHORT).show();
            }



        }
    }


    public class decreaseCartCount extends AsyncTask {
        String url = "http://jl-market.com/user/decreaseproductcount.php";
        String prompt;

        @Override
        protected void onPreExecute() {
            //Toast.makeText(context, "decreasing cart product", Toast.LENGTH_SHORT).show();

        }
        @Override
        protected Object doInBackground(Object[] objects) {

            if (!isNetworkAvailable()) {
                prompt = "No Network Connection";
                return prompt;
            }




            String serverresponse = new decreasecartcountinfo().GetData(url,userid,productid);
            return serverresponse;


        }

        protected void onPostExecute(Object result) {

            if (result.equals(prompt)) {

              //  Toast.makeText(context, prompt, Toast.LENGTH_SHORT).show();
            }

        }

        }


    public class increasecartcountinfo {

        public String GetData(String url, String userid, String productid) {

            try {
                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(50, TimeUnit.SECONDS)
                        .writeTimeout(50, TimeUnit.SECONDS)
                        .readTimeout(50, TimeUnit.SECONDS)
                        .build();


                RequestBody formBody = new FormBody.Builder()
                        .add("userid", userid)
                        .add("productid", productid)
                        .build();
                Request request = new Request.Builder().post(formBody).url(url).build();
                Response response = client.newCall(request).execute();
                String data = response.body().string();
                return data;

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

    }


        public class decreasecartcountinfo {

            public String GetData(String url, String userid, String productid) {

                try {
                    OkHttpClient client = new OkHttpClient.Builder()
                            .connectTimeout(50, TimeUnit.SECONDS)
                            .writeTimeout(50, TimeUnit.SECONDS)
                            .readTimeout(50, TimeUnit.SECONDS)
                            .build();


                    RequestBody formBody = new FormBody.Builder()
                            .add("userid", userid)
                            .add("productid", productid)
                            .build();
                    Request request = new Request.Builder().post(formBody).url(url).build();
                    Response response = client.newCall(request).execute();
                    String data = response.body().string();
                    return data;

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

        }

    }
