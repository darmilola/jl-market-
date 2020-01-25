package com.electonicmarket.android.emarket.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.electonicmarket.android.emarket.Adapter.Fulldetailadapter;
import com.electonicmarket.android.emarket.Models.ProductModel;
import com.electonicmarket.android.emarket.Models.fullproductdetailimagemodel;
import com.electonicmarket.android.emarket.R;
import com.electonicmarket.android.emarket.splashscreen;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ProductFullDetail extends AppCompatActivity {
    Button BuyNow;
    TextView Title;
    View view;
    String fullimage2;
    String fullimage3;
    Typeface customfont;
    SimpleDraweeView indicatorimage1, indicatorimage2;
    TextView description,descriptionTitle,price,pricetitle, DeprecatedPrice,percentdecrease,productname;
    fullproductdetailimagemodel image2;
    fullproductdetailimagemodel image3;
    RecyclerView productimageRecycler;
    ArrayList<fullproductdetailimagemodel> list;
    static ArrayList<ProductModel> productdetail;
    Fulldetailadapter fulldetailadapter;
    Drawable smallimageback1,smallimageback2,smallimageback3;
    boolean flag3;
    Runnable runnable = null;
    Handler handler = new Handler();


    String userid ;
    String myproductname;
    String productprice ;
    String firstimage;
    String secondimage;
    String reducedprice;
    String mydescription;
    Toolbar toolbar;

    Typeface customfont2;

    Dialog loadingdialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        if(savedInstanceState != null){

            if(savedInstanceState.getInt("pid",-1) == android.os.Process.myPid()){

            }
            else{

                startActivity(new Intent(ProductFullDetail.this,splashscreen.class));
               return;
            }

        }
        setContentView(R.layout.activity_product_full_detail);



        productdetail = getIntent().getParcelableArrayListExtra("productlist");

        InitializeView();
        InitializePrice();
        displaydescription();
        InitializeProductFullImage();
        ProductsimageRecyclerInit();
        InitializeAdapter();
        startAutoscroll();
        setOnclickListener();




    }

    private void InitializeView(){

        description = findViewById(R.id.fulldetailproductdescription);
        descriptionTitle = findViewById(R.id.fulldetailproductdescriptiontitle);
        price = findViewById(R.id.fulldetailproductprice);
        pricetitle = findViewById(R.id.fulldetailproductpricetitle);
        indicatorimage1 = findViewById(R.id.fulldetailproductsmallimagefirst);
        indicatorimage2 = findViewById(R.id.fulldetailproductsmallimagesecond);

        DeprecatedPrice = findViewById(R.id.fulldetaildeprecatedprice);
        percentdecrease = findViewById(R.id.fulldetailpercentdecrease);
        productname = findViewById(R.id.productfulldetailname);
        customfont= Typeface.createFromAsset(getAssets(),"Kylo-Light.otf");
       Typeface customfont2= Typeface.createFromAsset(getAssets(),"Kylo-Regular.otf");

        BuyNow = findViewById(R.id.BuyNow);
        Title = findViewById(R.id.productfulldetailtoolbartitle);

       // customfont= Typeface.createFromAsset(getAssets(),"Roboto-Regular.ttf");
        pricetitle.setTypeface(customfont2);
        BuyNow.setTypeface(customfont);
        Title.setTypeface(customfont2);

        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.screenBrightness = 0.5f;
        getWindow().setAttributes(layoutParams);
        descriptionTitle.setTypeface(customfont2);
        DeprecatedPrice.setTypeface(customfont2);
        productname.setTypeface(customfont2);
        productname.setText(productdetail.get(0).getProductName());
        price.setTypeface(customfont2);
        percentdecrease.setTypeface(customfont2);
        description.setTypeface(customfont2);

        if(getIntent().getStringExtra("fromcart") != null){
          BuyNow.setVisibility(View.GONE);
        }
        toolbar = findViewById(R.id.productdetailtoolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.White), PorterDuff.Mode.SRC_ATOP);
        Title.setText(productdetail.get(0).getProductName());

    }

    private void displaydescription(){
        String mdescription = productdetail.get(0).getProductDescription();
        description.setText(mdescription);
    }

    private void InitializePrice(){
        Locale NigerianLocale = new Locale("en","ng");
        if(productdetail.get(0).getProductnewPrice() == null){
            Log.e("redude", "InitializePrice: null" );
            DeprecatedPrice.setVisibility(View.GONE);
            percentdecrease.setVisibility(View.GONE);
            String productprice = productdetail.get(0).getoldProductPrice();
            String unFormattedrealPrice = NumberFormat.getCurrencyInstance(NigerianLocale).format(Integer.parseInt(productprice));
            String formattedrealPrice = unFormattedrealPrice.replaceAll("\\.00","");
            price.setTextColor(getResources().getColor(R.color.colorPrimary));
            price.setText(formattedrealPrice);
        }

        else{
            DeprecatedPrice.setVisibility(View.VISIBLE);
            percentdecrease.setVisibility(View.VISIBLE);
            String reducedproductprice = productdetail.get(0).getProductnewPrice();
            String unFormattedreducedPrice = NumberFormat.getCurrencyInstance(NigerianLocale).format(Integer.parseInt(reducedproductprice));
            String formattedrealreducedPrice = unFormattedreducedPrice.replaceAll("\\.00","");
            String productprice = productdetail.get(0).getoldProductPrice();
            String unFormattedrealPrice = NumberFormat.getCurrencyInstance(NigerianLocale).format(Integer.parseInt(productprice));
            String formattedrealPrice = unFormattedrealPrice.replaceAll("\\.00","");
            DeprecatedPrice.setTextColor(getResources().getColor(R.color.grey));
            DeprecatedPrice.setText(formattedrealPrice);
            price.setTextColor(getResources().getColor(R.color.colorPrimary));
            price.setText(formattedrealreducedPrice);
            DeprecatedPrice.setPaintFlags(price.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
            String percentDecrease = productdetail.get(0).getPercentDecrease();
            percentdecrease.setText("-"+percentDecrease+"%");



        }
    }

    private void InitializeProductFullImage(){

        smallimageback1 = ContextCompat.getDrawable(getApplicationContext(),R.drawable.productsmallimagebackgrounprimary);
        indicatorimage1.setBackground(smallimageback1);//tint indicator 1 by default
        String indicatoruri1 = productdetail.get(0).getProductImagefirst();

       /* RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.marketpic);
        requestOptions.error(R.drawable.marketpic);
        Glide.with(this)
                .setDefaultRequestOptions(requestOptions)
                .load(indicatoruri1)
                .apply(RequestOptions.skipMemoryCacheOf(true))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .into(indicatorimage1);*/

        Uri imageuri = Uri.parse(indicatoruri1);
        ImageRequest request = ImageRequest.fromUri(imageuri);
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(indicatorimage1.getController()).build();
        indicatorimage1.setController(controller);



        list = new ArrayList<>();
        String fullimage1 = productdetail.get(0).getProductImagefirst();
        fullproductdetailimagemodel image1 = new fullproductdetailimagemodel(fullimage1);
        list.add(image1);
        if(!(productdetail.get(0).getProductImageSecond() == null)){//check if there is second image

            fullimage2 = productdetail.get(0).getProductImageSecond();
            image2 = new fullproductdetailimagemodel(fullimage2);
            list.add(image2);
            String indicatoruri2 = productdetail.get(0).getProductImageSecond();

           /* RequestOptions requestOptions2 = new RequestOptions();
            requestOptions2.placeholder(R.drawable.marketpic);
            requestOptions2.error(R.drawable.marketpic);
            Glide.with(this)
                    .setDefaultRequestOptions(requestOptions)
                    .load(indicatoruri2)
                    .apply(RequestOptions.skipMemoryCacheOf(true))
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                    .into(indicatorimage2);*/

            Uri imageuri2 = Uri.parse(indicatoruri2);
            ImageRequest request2 = ImageRequest.fromUri(imageuri2);
            DraweeController controller2 = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(request2)
                    .setOldController(indicatorimage2.getController()).build();
            indicatorimage2.setController(controller2);



        }
        else{
            indicatorimage2.setVisibility(View.GONE);//else if there is no second image indicator  2  is gone

        }

    }
    private void InitializeAdapter(){
        fulldetailadapter = new Fulldetailadapter(list);
        productimageRecycler.setAdapter(fulldetailadapter);
    }
    private void ProductsimageRecyclerInit() {
        productimageRecycler = (RecyclerView)findViewById(R.id.productfulldetailrecyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        linearLayoutManager.setAutoMeasureEnabled(true);
        productimageRecycler.setLayoutManager(linearLayoutManager);

        //AllproductsRecycler.setItemAnimator(new DefaultItemAnimator());

    }

    private void startAutoscroll() {
        final int[] scrollspeed = {3000};

        final int[] count = {0};
        final boolean[] flag = {true};
        runnable = new Runnable() {
            @Override
            public void run() {

                if (fulldetailadapter.getItemCount() == 1) {//there is only one image dont autoscroll
                    return;
                }

                if (count[0] < fulldetailadapter.getItemCount()) {//count is less than images the autoscroll to them


                    if (count[0] == fulldetailadapter.getItemCount() - 1) {
                        flag[0] = false;//count is at maximum decrease count
                    } else if (count[0] == 0) {
                        flag[0] = true; //count is at minimum increase count
                    }
                    if (flag[0]) {
                        count[0]++;//if flag is true increase count


                    } else {
                        count[0]--; //else decrease count

                    }
                    if (count[0] == 0) {//meaning if count is at minimum

                        //get the drawable for coloring dispayed view
                        smallimageback1 =ContextCompat.getDrawable(getApplicationContext(),R.drawable.productsmallimagebackgrounprimary);

                        smallimageback2 = ContextCompat.getDrawable(getApplicationContext(),R.drawable.productsmallimagebackgroundblack);


                        if (indicatorimage2.getVisibility() == View.GONE) {//if there is no indicator 2 color only indicator 1
                            indicatorimage1.setBackground(smallimageback1);
                        } else {
                            indicatorimage2.setBackground(smallimageback2);//color image 2 black
                            indicatorimage1.setBackground(smallimageback1);//color image 1
                        }

                    }
                    flag3 = false;
                    scrollspeed[0] = 3000;


                }
                if (count[0] == 1) {



                    smallimageback1 = ContextCompat.getDrawable(getApplicationContext(),R.drawable.productsmallimagebackgrounprimary);

                    smallimageback2 = ContextCompat.getDrawable(getApplicationContext(),R.drawable.productsmallimagebackgroundblack);


                    if (indicatorimage2.getVisibility() == View.GONE) {//count is 1, if indicator 2 is gone definitely 3 will also
                        indicatorimage1.setBackground(smallimageback1);
                    } else {
                        indicatorimage2.setBackground(smallimageback1);//else color image 2
                        indicatorimage1.setBackground(smallimageback2);
                    }

                    flag3 = false;
                    scrollspeed[0] = 3000;

                }


                if (count[0] == 2) {

                    smallimageback1 = ContextCompat.getDrawable(getApplicationContext(),R.drawable.productsmallimagebackgrounprimary);

                    smallimageback2 = ContextCompat.getDrawable(getApplicationContext(),R.drawable.productsmallimagebackgroundblack);


                    if (indicatorimage2.getVisibility() == View.GONE) {
                        indicatorimage1.setBackground(smallimageback1);
                    } else {
                        indicatorimage2.setBackground(smallimageback2);
                        indicatorimage1.setBackground(smallimageback2);
                    }

                    flag3 = false;
                    scrollspeed[0] = 1500;


                }
                if (count[0] == 3) {//bug cuasing it to move up once thats why time is reduced to half
                    flag3 = true;//flag determine it has reach peak


                    smallimageback1 = ContextCompat.getDrawable(getApplicationContext(),R.drawable.productsmallimagebackgrounprimary);

                    smallimageback2 = ContextCompat.getDrawable(getApplicationContext(),R.drawable.productsmallimagebackgroundblack);


                    if (indicatorimage2.getVisibility() == View.GONE) {
                        indicatorimage1.setBackground(smallimageback1);
                    } else {
                        indicatorimage2.setBackground(smallimageback2);
                        indicatorimage1.setBackground(smallimageback2);
                    }

                    scrollspeed[0] = 1500;

                }
                // Toast.makeText(getContext(), Integer.toString(count[0]), Toast.LENGTH_SHORT).show();
                if (flag3) {

                } else {
                    productimageRecycler.smoothScrollToPosition(count[0]);
                }

                if (handler != null) {
                    handler.postDelayed(this, scrollspeed[0]);
                }

            }

        };

        if (handler != null) {
            handler.postDelayed(runnable, scrollspeed[0]);
        }

    }




    public class addtoCartTask extends AsyncTask {
        String url = "http://jl-market.com/user/addtocart.php";
        String prompt;


        @Override
        protected void onPreExecute() {
            //Toast.makeText(getContext(), "Saving profile", Toast.LENGTH_SHORT).show();
            showloadingdialog();
        }

        @Override
        protected Object doInBackground(Object[] objects) {

            if (!isNetworkAvailable()) {
               prompt = "No Network Connection";
               return prompt;
            }
            if(secondimage != null){
                String serverresponse = new addtocartinfo().GetData(url,userid,myproductname,productprice,mydescription,firstimage,secondimage,reducedprice);
                if (serverresponse != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(serverresponse);
                        JSONObject info = jsonObject.getJSONObject("info");
                        String status = info.getString("status");

                        if (status.equalsIgnoreCase("product added successfully")) {
                            prompt = "Product added to cart";
                            return prompt;
                        }
                        else if(status.equalsIgnoreCase("Error adding product")){
                            prompt = "Error adding product";
                            return prompt;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            else {

                String serverresponse = new addtocartinfo().GetData(url, userid, myproductname, productprice, mydescription, firstimage, reducedprice);
                return serverresponse;//nothing is happening here because only two image is sent if no two image then the other image is empty because script doesnt support one image upload to cart table
            }
              return null;
        }

        protected void onPostExecute(Object result) {
            loadingdialog.dismiss();
            if (result != null) {
                if(result.toString().equalsIgnoreCase("No Network Connection")){
                    Toast.makeText(ProductFullDetail.this, "No Network Connection", Toast.LENGTH_LONG).show();
                }
                if(result.toString().equalsIgnoreCase("Product added to cart")){

                    Toast.makeText(ProductFullDetail.this, "Product Added Successfully", Toast.LENGTH_SHORT).show();
                }
                if(result.toString().equalsIgnoreCase("Error adding product")){

                    Toast.makeText(ProductFullDetail.this, "Error Adding Product please try again1", Toast.LENGTH_SHORT).show();
                }

            }
            else{
                Toast.makeText(ProductFullDetail.this, "Error Adding Product please try again2", Toast.LENGTH_SHORT).show();
            }


        }
        }



    public class addtocartinfo {

        public String GetData(String url,String userid,String productname,String productprice, String description
                ,String firstimage,String reducedprice) {

            try {
                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(50, TimeUnit.SECONDS)
                        .writeTimeout(50, TimeUnit.SECONDS)
                        .readTimeout(50, TimeUnit.SECONDS)
                        .build();


                RequestBody formBody = new FormBody.Builder()
                        .add("userid", userid)
                        .add("productname",productname)
                        .add("productprice",productprice)
                        .add("firstimage",firstimage)
                        .add("reducedprice",reducedprice)
                        .add("description",description)
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

        public String GetData(String url,String userid,String productname,String productprice, String description
                ,String firstimage,String secondimage,String reducedprice) {

            try {
                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(50, TimeUnit.SECONDS)
                        .writeTimeout(50, TimeUnit.SECONDS)
                        .readTimeout(50, TimeUnit.SECONDS)
                        .build();


                RequestBody formBody = new FormBody.Builder()
                        .add("userid", userid)
                        .add("productname",productname)
                        .add("productprice",productprice)
                        .add("firstimage",firstimage)
                        .add("reducedprice",reducedprice)
                        .add("description",description)
                        .add("secondimage",secondimage)
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

        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void setOnclickListener(){
        BuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 userid = productdetail.get(0).getUseremail();
                 myproductname = productdetail.get(0).getProductName();
                 productprice = productdetail.get(0).getoldProductPrice();
                 firstimage = productdetail.get(0).getProductImagefirst();
                 secondimage = productdetail.get(0).getProductImageSecond();
                 if(productdetail.get(0).getProductnewPrice() != null){
                     reducedprice = productdetail.get(0).getProductnewPrice();
                 }
                 else{
                     reducedprice = "0";

                 }
                 if(secondimage == null){
                     secondimage = "";
                 }
                 mydescription = productdetail.get(0).getProductDescription();
                 addtoCartTask addtoCartTask = new addtoCartTask();
                 addtoCartTask.execute();
            }
        });
    }

    private void showloadingdialog() {
        loadingdialog = new Dialog(ProductFullDetail.this, R.style.Dialog_Theme);
        loadingdialog.setContentView(R.layout.loadingdialog);
        //Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.loading);
        ImageView image = loadingdialog.findViewById(R.id.loadingimage);
        DrawableImageViewTarget imageViewTarget = new DrawableImageViewTarget(image);
        Glide.with(this).load(R.drawable.loading).into(imageViewTarget);
        loadingdialog.show();
        loadingdialog.setCancelable(false);
        //getContext().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        loadingdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            loadingdialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            loadingdialog.getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outstate){
        super.onSaveInstanceState(outstate);
        outstate.putInt("pid",android.os.Process.myPid());

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        if (menuItem.getItemId() == android.R.id.home) {

            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

}
