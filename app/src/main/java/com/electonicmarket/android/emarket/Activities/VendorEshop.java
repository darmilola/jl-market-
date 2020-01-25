package com.electonicmarket.android.emarket.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.electonicmarket.android.emarket.Fragments.userOrders;
import com.electonicmarket.android.emarket.Fragments.vendorshopProduct;
import com.electonicmarket.android.emarket.Models.userprofile;
import com.electonicmarket.android.emarket.Models.vendordisplayitem;
import com.electonicmarket.android.emarket.R;
import com.electonicmarket.android.emarket.service.updateservice.UpdateCartRecyclerview;
import com.electonicmarket.android.emarket.splashscreen;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.app.AlertDialog.THEME_HOLO_LIGHT;

public class VendorEshop extends AppCompatActivity {

    static ViewPager viewPager;
    ImageView line;
    View view,view2;
    Toolbar toolbar;
    ImageView info;
    SimpleDraweeView vendorlogo;
    CollapsingToolbarLayout collapsingToolbarLayout;
    AppBarLayout appBarLayout;
    ViewPagerAdapter adapter;
    RatingBar ratingBar;
    String useremail,cartcount;
    String transitionname;
    ArrayList<userprofile> userprofiles;
    Bitmap image;
    ArrayList<vendordisplayitem> vendorinfos;
    String starnum,vendorreviewscount;

    int flag = 1;
    TextView vendorname,deliverytime,minorder,deliveryfee,cartitemcount,reviewscount;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
          Bundle bundle = intent.getExtras();
          if(bundle != null){
               cartcount = bundle.getString("cartcount");
               cartitemcount.setText(cartcount);

          }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_vendor_eshop);
        if(savedInstanceState != null){

            if(savedInstanceState.getInt("pid",-1) == android.os.Process.myPid()){

            }
            else{

                startActivity(new Intent(VendorEshop.this,splashscreen.class));

            }
        }

        ProcessIntentInfo();
        Log.e("emails", vendorinfos.get(0).getEmail()+" "+useremail );
        InitializeView();
        processDisplayReviews();
        setUpAppar();
        setUpViewPager();



        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VendorEshop.this,vendorinformation.class);
                intent.putParcelableArrayListExtra("vendorinfo",vendorinfos);
                intent.putExtra("useremail",useremail);
                startActivity(intent);
            }
        });


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        if (menuItem.getItemId() == android.R.id.home) {


            this.onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }

   private void processDisplayReviews(){
        vendorreviewscount = vendorinfos.get(0).getReviewscount();
        starnum = vendorinfos.get(0).getStarnumber();

        ratingBar.setRating(Float.parseFloat(starnum));
        int revcount = Integer.parseInt(vendorreviewscount);

        if(revcount < 999){
            reviewscount.setText("("+vendorreviewscount+")");
        }
        else{
            reviewscount.setText("(999+)");
        }
   }

    private void ProcessIntentInfo(){

        if(getIntent().getParcelableArrayListExtra("vendorinfo") != null){
            vendorinfos = getIntent().getParcelableArrayListExtra("vendorinfo");
            //Log.e("here now", Integer.toString(vendorinfos.size()) );
        }
        if(getIntent().getStringExtra("useremail") != null){
            useremail = getIntent().getStringExtra("useremail");
        }

        userprofiles = getIntent().getParcelableArrayListExtra("userprofile");

    }
    private void setUpAppar(){


        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShown = true;
            int scrollRange = -1;
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if(scrollRange == -1){
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if(scrollRange + verticalOffset == 0){


                    collapsingToolbarLayout.setTitle(vendorinfos.get(0).getVendorname());
                    isShown = true;
                    return;
                }
                else if(isShown){


                    isShown = false;
                    return;
                }
            }
        });
        Typeface  customfont= Typeface.createFromAsset(getAssets(),"Kylo-Light.otf");
        collapsingToolbarLayout.setCollapsedTitleTypeface(customfont);
    }
    private void setUpViewPager(){
        viewPager = (ViewPager)findViewById(R.id.vendoreshopviewpager);
         adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(vendorshopProduct.newInstance(vendorinfos.get(0).getEmail(),useremail),"Home");
        adapter.addFragment(userOrders.newInstance(userprofiles,useremail,vendorinfos.get(0).getDeliveryfee(),vendorinfos.get(0).getEmail(),vendorinfos.get(0).getDeliveryminute(),vendorinfos.get(0).getCardpayment(),vendorinfos.get(0).getCashpayment(),vendorinfos.get(0).getMinimumorder()),"Orders");
        viewPager.setAdapter(adapter);




        TabLayout tabLayout = (TabLayout)findViewById(R.id.vendoreshoptabs);
        tabLayout.setupWithViewPager(viewPager);


        tabLayout.getTabAt(1).setCustomView(view);
        tabLayout.getTabAt(0).setCustomView(view2);

    }
private void InitializeView(){

    view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.vendorcarticon,null);
    view2 = LayoutInflater.from(getApplicationContext()).inflate(R.layout.vendorshomeicon,null);
    cartitemcount = view.findViewById(R.id.cartitemcount);
    vendorlogo = findViewById(R.id.vendorlogo);
    vendorname = findViewById(R.id.vendorname);
    deliverytime = findViewById(R.id.vendorshopdeliverytime);
    minorder = findViewById(R.id.Minorder);
    deliveryfee = findViewById(R.id.deliveryfee);
    ratingBar = findViewById(R.id.eshopratingbar);
    ratingBar.setIsIndicator(true);
    reviewscount = findViewById(R.id.reviewscount);

    toolbar = (Toolbar) findViewById(R.id.eshoptoolbar);

    info = findViewById(R.id.vendoreshopinfoicon);
    collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar_layout);
    appBarLayout = (AppBarLayout)findViewById(R.id.app_bar);
    Typeface customfont2= Typeface.createFromAsset(getAssets(),"Kylo-Light.otf");
    vendorname.setTypeface(customfont2);
    deliveryfee.setTypeface(customfont2);
    minorder.setTypeface(customfont2);
    deliveryfee.setTypeface(customfont2);
    cartitemcount.setTypeface(customfont2);



    reviewscount.setTypeface(customfont2);
    deliverytime.setTypeface(customfont2);
    vendorname.setText(vendorinfos.get(0).getVendorname());

    deliverytime.setText(vendorinfos.get(0).getDeliveryminute()+"min.");
    Locale NigerianLocale = new Locale("en","ng");
    String Deliveryfee = vendorinfos.get(0).getDeliveryfee();
    String minOrder = vendorinfos.get(0).getMinimumorder();
    String unFormattedminOrder = NumberFormat.getCurrencyInstance(NigerianLocale).format(Integer.parseInt(minOrder));
    String unFormattedDeliveryFee = NumberFormat.getCurrencyInstance(NigerianLocale).format(Integer.parseInt(Deliveryfee));
    String formattedPrice = unFormattedDeliveryFee.replaceAll("\\.00","");
    String formattedMinOrder = unFormattedminOrder.replaceAll("\\.00","");
    deliveryfee.setText("Delivery Fee"+" "+formattedPrice);
    minorder.setText("Min Order"+" "+formattedMinOrder);



    WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
    layoutParams.screenBrightness = 0.5f;
    getWindow().setAttributes(layoutParams);
    setSupportActionBar(toolbar);
    getSupportActionBar().setTitle("");

    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //getWindow().setStatusBarColor(Color.parseColor("#00000000"));
    }
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setDisplayShowHomeEnabled(true);
    getSupportActionBar().setDisplayShowTitleEnabled(false);
    toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.White), PorterDuff.Mode.SRC_ATOP);



        String imageuri = vendorinfos.get(0).getVendorDisplayImage();
        /*Uri uri = Uri.parse(imageuri);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.marketpic);
        requestOptions.error(R.drawable.accountcameraicon);

         // supportPostponeEnterTransition();
        Glide.with(this)
                .setDefaultRequestOptions(requestOptions)
                .load(uri)
                .apply(RequestOptions.skipMemoryCacheOf(true))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .into(vendorlogo);
               */

    Uri uri = Uri.parse(imageuri);
    ImageRequest request = ImageRequest.fromUri(uri);
    DraweeController controller = Fresco.newDraweeControllerBuilder()
            .setImageRequest(request)
            .setOldController(vendorlogo.getController()).build();
    vendorlogo.setController(controller);



}
    class  ViewPagerAdapter extends FragmentPagerAdapter{

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> fragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment,String title){
            mFragmentList.add(fragment);
            fragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position){
            return null;
        }
    }

    @Override

    public void onBackPressed(){

        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(VendorEshop.this, R.style.dialogtheme);

        builder.setMessage("Are you sure you'd like to change Vendor? Your current order will be lost")
                .setPositiveButton("Wait", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();

                    }
                })
                .setNegativeButton("Leave", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Intent serviceintent = new Intent(VendorEshop.this, UpdateCartRecyclerview.class);
                        serviceintent.putExtra("url", "http://jl-market.com/user/emptyuserscart.php");
                        serviceintent.putExtra("userid", useremail);
                        startService(serviceintent);
                        finish();

                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();


    }

    private Bitmap returnImage(String  base64String){
        byte[] decodestring = android.util.Base64.decode(base64String, android.util.Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodestring,0,decodestring.length);
        return decodedByte;

    }



          @Override
          protected void onResume() {
              super.onResume();
              registerReceiver(receiver, new IntentFilter("com.electonicmarket.android.emarket.service"));

          }

          @Override
          protected void onPause() {
              super.onPause();
              unregisterReceiver(receiver);
          }

          public static void changetoproductfragment() {
              viewPager.setCurrentItem(0);
          }

    @Override
    public void onSaveInstanceState(Bundle outstate){
        super.onSaveInstanceState(outstate);
        outstate.putInt("pid",android.os.Process.myPid());


    }

    @Override
    public void onStop() {

        super.onStop();


    }
      }