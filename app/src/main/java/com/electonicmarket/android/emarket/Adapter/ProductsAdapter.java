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
import android.os.Build;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.electonicmarket.android.emarket.Activities.ProductFullDetail;
import com.electonicmarket.android.emarket.Models.ProductModel;
import com.electonicmarket.android.emarket.R;
import com.electonicmarket.android.emarket.interfaces.PresenterLayer;
import com.electonicmarket.android.emarket.service.UpdateCartCountService;
import com.electonicmarket.android.emarket.service.updateservice.UpdateCartRecyclerview;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

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

public class ProductsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ProductModel> productList;
    private PresenterLayer presenterLayer;
    public Context context;
    Intent serviceintent;
    String userid, productname, productprice, firstimage, secondimage, reducedprice, description;
    Dialog loadingdialog;
    CoordinatorLayout coordinatorLayout;
    int addtocartflag = 0,viewdetail = 0;
    public ProductsAdapter(CoordinatorLayout coordinatorLayout, List<ProductModel> productList, PresenterLayer presenterLayer, Context context) {
        this.productList = productList;
        this.context = context;
        this.presenterLayer = presenterLayer;
        this.coordinatorLayout = coordinatorLayout;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 1:
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
                return new ItemViewHolder(view);
            case 2:
                View view2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_with_percentdecrease, parent, false);
                return new ItemViewHolderWithPercentDecrease(view2);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder ViewHolder, int position) {

        switch (ViewHolder.getItemViewType()) {
            case 1:
                ItemViewHolder itemViewHolder = (ItemViewHolder) ViewHolder;

                itemViewHolder.setIsRecyclable(false);
                if (productList != null) {
                    itemViewHolder.productName.setText(productList.get(position).getProductName());

                    //String Price = productList.get(position).getoldProductPrice();
                    Locale NigerianLocale = new Locale("en", "ng");

                    String imagefirstUri = productList.get(position).getProductImagefirst();

                    /*Uri imageuri = Uri.parse(imagefirstUri);
                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions.placeholder(R.drawable.marketpic);
                    requestOptions.error(R.drawable.marketpic);
                    Glide.with(itemViewHolder.itemView.getContext())
                            .setDefaultRequestOptions(requestOptions)
                            .load(imageuri)
                            .apply(RequestOptions.skipMemoryCacheOf(true))
                            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                            .into(itemViewHolder.productImagefirst);*/

                    Uri imageuri = Uri.parse(imagefirstUri);
                    ImageRequest request = ImageRequest.fromUri(imageuri);
                    DraweeController controller = Fresco.newDraweeControllerBuilder()
                            .setImageRequest(request)
                            .setOldController(itemViewHolder.productImagefirst.getController()).build();
                    itemViewHolder.productImagefirst.setController(controller);

                    //itemViewHolder.productImagefirst.setImageResource(productList.get(position).getProductImagefirst());
                    String productprice = productList.get(position).getoldProductPrice();
                    String unFormattedrealPrice = NumberFormat.getCurrencyInstance(NigerianLocale).format(Integer.parseInt(productprice));
                    String formattedrealPrice = unFormattedrealPrice.replaceAll("\\.00", "");
                    itemViewHolder.productPrice.setText(formattedrealPrice);


                }
                break;
            case 2:
                ItemViewHolderWithPercentDecrease itemViewHolderWithPercentDecrease = (ItemViewHolderWithPercentDecrease) ViewHolder;

                itemViewHolderWithPercentDecrease.setIsRecyclable(false);
                if (productList != null) {
                    itemViewHolderWithPercentDecrease.productName.setText(productList.get(position).getProductName());
                    Locale NigerianLocale = new Locale("en", "ng");
                    String reducedprice = productList.get(position).getProductnewPrice();
                    String DeprecatedPrice = productList.get(position).getoldProductPrice();
                    String unFormattedreducedPrice = NumberFormat.getCurrencyInstance(NigerianLocale).format(Integer.parseInt(reducedprice));
                    String formattedreducedPrice = unFormattedreducedPrice.replaceAll("\\.00", "");
                    String unFormattedDeprecatedPrice = NumberFormat.getCurrencyInstance(NigerianLocale).format(Integer.parseInt(DeprecatedPrice));
                    String formattedDeprecatedPrice = unFormattedDeprecatedPrice.replaceAll("\\.00", "");
                    itemViewHolderWithPercentDecrease.productPrice.setText(formattedreducedPrice);
                    itemViewHolderWithPercentDecrease.productDeprecatedPrice.setText(formattedDeprecatedPrice);
                    itemViewHolderWithPercentDecrease.productDeprecatedPrice.setPaintFlags(itemViewHolderWithPercentDecrease.productDeprecatedPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                    Double newPrice = Double.parseDouble(reducedprice);
                    Double oldPrice = Double.parseDouble(DeprecatedPrice);
                    Double decrease = oldPrice - newPrice;
                    String percentDecrease = Integer.toString((int) ((decrease / oldPrice) * 100));
                    productList.get(position).setPercentDecrease(percentDecrease);
                    itemViewHolderWithPercentDecrease.percentDecrease.setText("-" + productList.get(position).getPercentDecrease() + "%");


                    String imagefirstUri = productList.get(position).getProductImagefirst();

                   /* Uri imageuri = Uri.parse(imagefirstUri);
                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions.placeholder(R.drawable.marketpic);
                    requestOptions.error(R.drawable.marketpic);
                    Glide.with(itemViewHolderWithPercentDecrease.itemView.getContext())
                            .setDefaultRequestOptions(requestOptions)
                            .load(imageuri)
                            .apply(RequestOptions.skipMemoryCacheOf(true))
                            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                            .into(itemViewHolderWithPercentDecrease.productImagefirst);
                            */
                    Uri imageuri = Uri.parse(imagefirstUri);
                    ImageRequest request = ImageRequest.fromUri(imageuri);
                    DraweeController controller = Fresco.newDraweeControllerBuilder()
                            .setImageRequest(request)
                            .setOldController(itemViewHolderWithPercentDecrease.productImagefirst.getController()).build();
                    itemViewHolderWithPercentDecrease.productImagefirst.setController(controller);

                }
                break;
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (productList.get(position).getViewType() == "1") {
            return 1;
        } else if (productList.get(position).getViewType() == "2") {
            return 2;
        }
        return 0;
    }


    @Override
    public int getItemCount() {
        if (productList == null) {
            return 0;
        } else {
            return productList.size();
        }

    }

    public void setProductList(List<ProductModel> productList) {
        this.productList = productList;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public SimpleDraweeView productImagefirst;

        public TextView productName;

        public TextView productPrice;
        public Button OrderButton;

        public LinearLayout parentLayout;

        public ItemViewHolder(View itemView) {
            super(itemView);
            productImagefirst = itemView.findViewById(R.id.vendorproductimage);
            productName = (TextView) itemView.findViewById(R.id.vendorproductnametext);

            productPrice = (TextView) itemView.findViewById(R.id.vendorproductpricetext);
            OrderButton = (Button) itemView.findViewById(R.id.addtocartbutton);
            parentLayout = (LinearLayout) itemView.findViewById(R.id.itemproductparentlayout);

           Typeface customfont2 = Typeface.createFromAsset(itemView.getContext().getAssets(), "Roboto-Regular.ttf");
            productPrice.setTypeface(customfont2);
            OrderButton.setTypeface(customfont2);

            productName.setTypeface(customfont2);

            parentLayout.setOnClickListener(this);
            OrderButton.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {

            if (v.getId() == R.id.addtocartbutton) {
                int i = getAdapterPosition();
                ProductModel current = productList.get(i);

                serviceintent = new Intent(context, UpdateCartCountService.class);
                serviceintent.putExtra("url", "http://jl-market.com/user/userscart.php");
                serviceintent.putExtra("userid", current.getUseremail());


                userid = current.getUseremail();
                productname = current.getProductName();
                productprice = current.getoldProductPrice();
                description = current.getProductDescription();
                firstimage = current.getProductImagefirst();
                if (current.getProductnewPrice() == null) {
                    reducedprice = "0";
                } else {
                    reducedprice = current.getProductnewPrice();
                }
                if (current.getProductImageSecond() != null) {
                    secondimage = current.getProductImageSecond();
                } else {
                    secondimage = null;
                }


                if(addtocartflag == 0) {
                    addtocartflag = 1;
                    addtoCartTask addtoCartTask = new addtoCartTask();
                    addtoCartTask.execute();

                }

                //Toast.makeText(context, "service started", Toast.LENGTH_SHORT).show();


            } else {
                String reducedprice;
                //get product send arraylist to detail
                int i = getAdapterPosition();
                String productImageFirst, productImageSecond;
                ArrayList<ProductModel> productModels = new ArrayList<>();
                ProductModel model;
                Intent intent = new Intent(v.getContext(), ProductFullDetail.class);
                ProductModel current = productList.get(i);
                String productName = current.getProductName();
                String productDescription = current.getProductDescription();
                String productPrice = current.getoldProductPrice();
                productImageFirst = current.getProductImagefirst();


                model = new ProductModel(productDescription, productName, productPrice, productImageFirst);
                if (!(current.getProductImageSecond() == null)) {

                    productImageSecond = current.getProductImageSecond();
                    model = new ProductModel(productDescription, productName, productPrice, productImageFirst, productImageSecond);


                    //if (!(current.getProductImageThird() == null)) {
                    ///  productImageThird = current.getProductImageThird();
                    //model = new ProductModel(productDescription, productName, productPrice, productImageFirst, productImageSecond, productImageThird);


                    //}

                }
                Log.e("useremail", current.getUseremail());
                model.setUseremail(current.getUseremail());
                //model.setProductnewPrice(current.getProductnewPrice());

                productModels.add(model);
                intent.putParcelableArrayListExtra("productlist", productModels);


                    v.getContext().startActivity(intent);



            }
        }

    }


    public class ItemViewHolderWithPercentDecrease extends RecyclerView.ViewHolder implements View.OnClickListener {


        public SimpleDraweeView productImagefirst;

        public TextView productName;

        public TextView productPrice;
        public Button OrderButton;
        public TextView productDeprecatedPrice;
        public LinearLayout parentLayout;
        public TextView percentDecrease;

        public ItemViewHolderWithPercentDecrease(View itemView) {
            super(itemView);
            productImagefirst = itemView.findViewById(R.id.vendorproductimage);
            productName = (TextView) itemView.findViewById(R.id.vendorproductnametext);

            productPrice = (TextView) itemView.findViewById(R.id.vendorproductpricetext);
            OrderButton = (Button) itemView.findViewById(R.id.addtocartbutton);
            parentLayout = (LinearLayout) itemView.findViewById(R.id.itemproductparentlayout);
            productDeprecatedPrice = (TextView) itemView.findViewById(R.id.vendorproductdeprecatedpricetext);
            percentDecrease = (TextView) itemView.findViewById(R.id.percentdecrease);
            Typeface customfont2 = Typeface.createFromAsset(itemView.getContext().getAssets(), "Roboto-Regular.ttf");
            productPrice.setTypeface(customfont2);
            OrderButton.setTypeface(customfont2);

            productName.setTypeface(customfont2);
            productDeprecatedPrice.setTypeface(customfont2);
            percentDecrease.setTypeface(customfont2);
            parentLayout.setOnClickListener(this);
            OrderButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {


            if (v.getId() == R.id.addtocartbutton) {
                int j = getAdapterPosition();
                ProductModel current = productList.get(j);

                serviceintent = new Intent(context, UpdateCartCountService.class);
                serviceintent.putExtra("url", "http://jl-market.com/user/userscart.php");
                serviceintent.putExtra("userid", current.getUseremail());





                userid = current.getUseremail();
                productname = current.getProductName();
                productprice = current.getoldProductPrice();
                description = current.getProductDescription();
                firstimage = current.getProductImagefirst();
                // if(current.getProductnewPrice() == null){
                //   reducedprice = "0";
                //}
                //else{ there is always a reduced price for this viewtype
                reducedprice = current.getProductnewPrice();
                //}
                if (current.getProductImageSecond() != null) {
                    secondimage = current.getProductImageSecond();
                } else {
                    secondimage = "";
                }


                if(addtocartflag == 0) {
                 addtocartflag = 1;
                    addtoCartTask addtoCartTask = new addtoCartTask();
                    addtoCartTask.execute();

                }
            } else {
                String reducedprice;
                //get product send arraylist to detail
                int i = getAdapterPosition();
                String productImageFirst, productImageSecond, productImageThird;
                ArrayList<ProductModel> productModels = new ArrayList<>();
                ProductModel model;
                Intent intent = new Intent(v.getContext(), ProductFullDetail.class);
                ProductModel current = productList.get(i);
                String productName = current.getProductName();
                String productDescription = current.getProductDescription();
                String productPrice = current.getoldProductPrice();
                productImageFirst = current.getProductImagefirst();


                model = new ProductModel(productDescription, productName, productPrice, productImageFirst);
                if (!(current.getProductImageSecond() == null)) {

                    productImageSecond = current.getProductImageSecond();
                    model = new ProductModel(productDescription, productName, productPrice, productImageFirst, productImageSecond);
                }

                reducedprice = current.getProductnewPrice();
                model.setProductnewPrice(reducedprice);
                String percentdecrease = current.getPercentDecrease();
                model.setPercentDecrease(percentdecrease);
                model.setUseremail(current.getUseremail());

                productModels.add(model);
                intent.putParcelableArrayListExtra("productlist", productModels);


                   v.getContext().startActivity(intent);



            }

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
            if (secondimage != null) {

                String serverresponse = new addtocartinfo().GetData(url, userid, productname, productprice, description, firstimage, secondimage, reducedprice);

                if (serverresponse != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(serverresponse);
                        JSONObject info = jsonObject.getJSONObject("info");
                        String status = info.getString("status");

                        if (status.equalsIgnoreCase("product added successfully")) {
                            prompt = "Product added to cart";
                            return prompt;
                        } else if (status.equalsIgnoreCase("Error adding product")) {
                            prompt = "Error adding product";
                            return prompt;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            } else {
                String serverresponse = new addtocartinfo().GetData(url, userid, productname, productprice, description, firstimage, reducedprice);

                if (serverresponse != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(serverresponse);
                        JSONObject info = jsonObject.getJSONObject("info");
                        String status = info.getString("status");

                        if (status.equalsIgnoreCase("product added successfully")) {
                            prompt = "Product added to cart";
                            return prompt;
                        } else if (status.equalsIgnoreCase("Error adding product")) {
                            prompt = "Error adding product";
                            return prompt;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }


            return null;
        }

        protected void onPostExecute(Object result) {
           addtocartflag = 0;
            loadingdialog.dismiss();


            if (result != null) {
                if (result.toString().equalsIgnoreCase("No Network Connection")) {
                    Toast.makeText(context, "No Network Connection", Toast.LENGTH_LONG).show();
                }
               else if (result.toString().equalsIgnoreCase("Product added to cart")) {
                    context.startService(serviceintent);
                    Snackbar snackbar = Snackbar.make(coordinatorLayout, "Product added to cart", Snackbar.LENGTH_LONG);
                    View view = snackbar.getView();
                    CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) view.getLayoutParams();
                    params.gravity = Gravity.TOP;
                    view.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                    view.setLayoutParams(params);
                    snackbar.show();


                    Intent serviceintent2 = new Intent(context, UpdateCartRecyclerview.class);
                    serviceintent2.putExtra("url", "http://jl-market.com/user/userscart.php");
                    serviceintent2.putExtra("userid", userid);
                    context.startService(serviceintent2);
                    //Toast.makeText(context, "Product Added Successfully", Toast.LENGTH_SHORT).show();
                }
                else if (result.toString().equalsIgnoreCase("Error adding product")) {

                    Toast.makeText(context, "Error Occured", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(context, "Error Adding Product please try again", Toast.LENGTH_SHORT).show();
            }

        }

    }

    public class addtocartinfo {


        public String GetData(String url, String userid, String productname, String productprice, String description
                , String firstimage, String reducedprice) {

            try {
                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(50, TimeUnit.SECONDS)
                        .writeTimeout(50, TimeUnit.SECONDS)
                        .readTimeout(50, TimeUnit.SECONDS)
                        .build();


                RequestBody formBody = new FormBody.Builder()
                        .add("userid", userid)
                        .add("productname", productname)
                        .add("productprice", productprice)
                        .add("firstimage", firstimage)
                        .add("reducedprice", reducedprice)
                        .add("description", description)
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

        public String GetData(String url, String userid, String productname, String productprice, String description
                , String firstimage, String secondimage, String reducedprice) {

            try {
                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(50, TimeUnit.SECONDS)
                        .writeTimeout(50, TimeUnit.SECONDS)
                        .readTimeout(50, TimeUnit.SECONDS)
                        .build();


                RequestBody formBody = new FormBody.Builder()
                        .add("userid", userid)
                        .add("productname", productname)
                        .add("productprice", productprice)
                        .add("firstimage", firstimage)
                        .add("reducedprice", reducedprice)
                        .add("description", description)
                        .add("secondimage", secondimage)
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

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            loadingdialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            loadingdialog.getWindow().setStatusBarColor(context.getResources().getColor(R.color.colorPrimaryDark));
        }
    }


}







