package com.electonicmarket.android.emarket.Models;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

import com.electonicmarket.android.emarket.interfaces.InteractorLayer;
import com.electonicmarket.android.emarket.interfaces.InteractorListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

class ProductsLoader implements InteractorLayer {

    private InteractorListener interactorListener;
    Context mcontext;
    String vendoremail,useremail;
    String categoryname;
    ArrayList<ProductCategory> categorylist = new ArrayList<>();
    ArrayList<ProductCategory> categoryModelArrayList;
    private static String Vendor_GET_CATEGORY_URL = "http://jl-market.com/vendor/SelectProductCategories.php";
    private static String Vendor_GET_PRODUCT_URL = "http://jl-market.com/user/getvendorproduct.php";
    public ProductsLoader(InteractorListener interactorListener, Context context,String email,String useremail){
        this.interactorListener = interactorListener;
        this.mcontext = context;
        this.vendoremail = email;
        this.useremail = useremail;
    }

    @Override
    public void getProducts(int categoryid,String mcategoryname) { //catname will be added here for loading proper products to category

        getProductstask productstask = new getProductstask(mcategoryname,categoryid);
        productstask.execute();



    }

    @Override
    public void getCategories() {

        getCategoriestask categoriestask = new getCategoriestask();
        categoriestask.execute();

    }

    public class getCategoriestask extends AsyncTask{
      String prompt;
        @Override
        protected Object doInBackground(Object[] objects) {
            if (!isNetworkAvailable()) {
                prompt = "No Network Connection";
                return prompt;
            }

            String serverResponse = new getCategory().GetData(Vendor_GET_CATEGORY_URL, vendoremail);
            if (serverResponse != null) {
                try {
                    JSONObject jsonObject = new JSONObject(serverResponse);
                    JSONObject info = jsonObject.getJSONObject("info");
                    String status = info.getString("status");

                    if(status.equalsIgnoreCase("productcategoriesunavailable")){
                        prompt = "You have not created any product categories";
                        return prompt;
                    }

                    else if(status.equalsIgnoreCase("productcategoriesavailable")){
                        categoryModelArrayList = passCategoryJson(info);
                        return categoryModelArrayList;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            return null;
        }

        protected void onPostExecute(Object result) {

            if(result != null) {
                if (result.toString().equalsIgnoreCase(prompt)) {
                    //Toast.makeText(mcontext, prompt, Toast.LENGTH_SHORT).show();
                    }


                else if(result instanceof ArrayList){
                    interactorListener.onCategoriesLoaded((List<ProductCategory>) result);
                }
                else{
                    //Toast.makeText(mcontext, "unable to get category please try again", Toast.LENGTH_SHORT).show();


                }
            }




        }

        }

    public ArrayList<ProductCategory> passCategoryJson(JSONObject jsonObject) throws JSONException {

        JSONArray jsonArray = jsonObject.getJSONArray("data");
        for (int i = 0; i < jsonArray.length(); ++i) {
            JSONObject j = jsonArray.getJSONObject(i);
            String cat1 = j.getString("productcategory1");
            String cat2 = j.getString("productcategory2");
            String cat3 = j.getString("productcategory3");
            String cat4 = j.getString("productcategory4");
            String cat5 = j.getString("productcategory5");
            String cat6 = j.getString("productcategory6");
            String cat7 = j.getString("productcategory7");
            String cat8 = j.getString("productcategory8");
            String cat9 = j.getString("productcategory9");
            String cat10 = j.getString("productcategory10");

            ProductCategory categoryModel1 = new ProductCategory(cat1);
            ProductCategory categoryModel2 = new ProductCategory(cat2);
            ProductCategory categoryModel3 = new ProductCategory(cat3);
            ProductCategory categoryModel4 = new ProductCategory(cat4);
            ProductCategory categoryModel5 = new ProductCategory(cat5);
            ProductCategory categoryModel6 = new ProductCategory(cat6);
            ProductCategory categoryModel7 = new ProductCategory(cat7);
            ProductCategory categoryModel8 = new ProductCategory(cat8);
            ProductCategory categoryModel9 = new ProductCategory(cat9);
            ProductCategory categoryModel10 = new ProductCategory(cat10);



            categoryModel1.setRecyclervisibility(View.GONE);
            categoryModel1.setProgressbarvisibility(View.VISIBLE);
            categoryModel2.setRecyclervisibility(View.GONE);
            categoryModel2.setProgressbarvisibility(View.VISIBLE);
            categoryModel3.setRecyclervisibility(View.GONE);
            categoryModel3.setProgressbarvisibility(View.VISIBLE);
            categoryModel4.setRecyclervisibility(View.GONE);
            categoryModel4.setProgressbarvisibility(View.VISIBLE);
            categoryModel5.setRecyclervisibility(View.GONE);
            categoryModel5.setProgressbarvisibility(View.VISIBLE);

            categoryModel6.setRecyclervisibility(View.GONE);
            categoryModel6.setProgressbarvisibility(View.VISIBLE);
            categoryModel7.setRecyclervisibility(View.GONE);
            categoryModel7.setProgressbarvisibility(View.VISIBLE);
            categoryModel8.setRecyclervisibility(View.GONE);
            categoryModel8.setProgressbarvisibility(View.VISIBLE);
            categoryModel9.setRecyclervisibility(View.GONE);
            categoryModel9.setProgressbarvisibility(View.VISIBLE);
            categoryModel10.setRecyclervisibility(View.GONE);
            categoryModel10.setProgressbarvisibility(View.VISIBLE);




            if(cat1.equalsIgnoreCase("") || cat1.equalsIgnoreCase("null")){

            }
            else {
                categorylist.add(categoryModel1);
            }
            if(cat2.equalsIgnoreCase("")|| cat2.equalsIgnoreCase("null")){

            }
            else {
                categorylist.add(categoryModel2);
            }
            if(cat3.equalsIgnoreCase("")|| cat3.equalsIgnoreCase("null")){

            }
            else {
                categorylist.add(categoryModel3);
            }
            if(cat4.equalsIgnoreCase("")|| cat4.equalsIgnoreCase("null")){

            }
            else {
                categorylist.add(categoryModel4);
            }
            if(cat5.equalsIgnoreCase("")|| cat5.equalsIgnoreCase("null")){

            }
            else {
                categorylist.add(categoryModel5);
            }


            if(cat6.equalsIgnoreCase("") || cat6.equalsIgnoreCase("null")){

            }
            else {
                categorylist.add(categoryModel6);
            }
            if(cat7.equalsIgnoreCase("")|| cat7.equalsIgnoreCase("null")){

            }
            else {
                categorylist.add(categoryModel7);
            }
            if(cat8.equalsIgnoreCase("")|| cat8.equalsIgnoreCase("null")){

            }
            else {
                categorylist.add(categoryModel8);
            }
            if(cat9.equalsIgnoreCase("")|| cat9.equalsIgnoreCase("null")){

            }
            else {
                categorylist.add(categoryModel9);
            }
            if(cat10.equalsIgnoreCase("")|| cat10.equalsIgnoreCase("null")){

            }
            else {
                categorylist.add(categoryModel10);
            }







        }
        return categorylist;


    }



    public class getCategory {

        public String GetData(String url, String email) {

            try {
                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(120, TimeUnit.SECONDS)
                        .writeTimeout(120, TimeUnit.SECONDS)
                        .readTimeout(120, TimeUnit.SECONDS)
                        .build();

                RequestBody formBody = new FormBody.Builder()
                        .add("email", email)
                        .build();

                Request request = new Request.Builder().url(url).post(formBody).build();
                Response response = client.newCall(request).execute();
                String data = response.body().string();
                return data;

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public class getProductstask extends AsyncTask {
        String prompt;
        String nameofcategory;
        int mcategoryid;
        ArrayList<ProductModel> productModelArrayList;

        getProductstask(String nameofcategory,int Categoryid) {
            this.nameofcategory = nameofcategory;
            this.mcategoryid = Categoryid;
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            if (!isNetworkAvailable()) {
                prompt = "No Network Connection";
                return prompt;
            }

            String serverResponse = new getProduct().GetData(Vendor_GET_PRODUCT_URL, vendoremail, nameofcategory);

            if (serverResponse != null) {
                try {
                    JSONObject jsonObject = new JSONObject(serverResponse);
                    JSONObject info = jsonObject.getJSONObject("info");
                    String status = info.getString("status");

                    if (status.equalsIgnoreCase("productsunavailable")) {
                        //prompt = "You have not created any product categories";
                        //return prompt;
                    } else if (status.equalsIgnoreCase("productsavailable")) {
                        productModelArrayList = passProductJson(info);
                        return productModelArrayList;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            return null;

        }

        protected void onPostExecute(Object result) {

            if (result != null) {
                //if (result.toString().equalsIgnoreCase(prompt)) {
                  //  Toast.makeText(mcontext, prompt, Toast.LENGTH_SHORT).show();
               // } else
                 if (result instanceof ArrayList) {
                     interactorListener.onProductsLoaded(mcategoryid,(List<ProductModel>)result);
                }


                }
            }

        }


            public class getProduct {

        public String GetData(String url, String email,String catname) {

            try {
                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(120, TimeUnit.SECONDS)
                        .writeTimeout(120, TimeUnit.SECONDS)
                        .readTimeout(120, TimeUnit.SECONDS)
                        .build();

                RequestBody formBody = new FormBody.Builder()
                        .add("email", email)
                        .add("categoryname",catname)
                        .build();

                Request request = new Request.Builder().url(url).post(formBody).build();
                Response response = client.newCall(request).execute();
                String data = response.body().string();
                return data;

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public ArrayList<ProductModel> passProductJson(JSONObject jsonObject) throws JSONException {
            ArrayList<ProductModel> productModelArrayList = new ArrayList<>();

        JSONArray jsonArray = jsonObject.getJSONArray("data");
        for (int i = 0; i < jsonArray.length(); ++i) {
            JSONObject j = jsonArray.getJSONObject(i);
            String productname = j.getString("name");
            String productdescription = j.getString("description");
            String price = j.getString("price");
            int lastprice = j.getInt("reducedprice");
            String firstimage = j.getString("firstimage");
            String secondimage = j.getString("secondimage");
            ProductModel productModel = new ProductModel(productdescription,productname,price,firstimage);
            if(!secondimage.equalsIgnoreCase("null") && !secondimage.equalsIgnoreCase("")) {
                productModel = new ProductModel(productdescription,productname,price,firstimage,secondimage);

            }

            if(lastprice != 0){
                productModel.setProductnewPrice(Integer.toString(lastprice));
                productModel.setViewType("2");
            }
            else {
                productModel.setViewType("1");
            }
            productModel.setUseremail(useremail);
            productModelArrayList.add(productModel);

        }
        return productModelArrayList ;


    }


    private Boolean isNetworkAvailable() {

        ConnectivityManager connectivityManager = (ConnectivityManager)mcontext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}
