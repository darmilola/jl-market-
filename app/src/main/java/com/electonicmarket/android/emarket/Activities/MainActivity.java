package com.electonicmarket.android.emarket.Activities;

import android.content.ComponentCallbacks2;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import androidx.fragment.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.widget.SearchView;
import android.view.View;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.electonicmarket.android.emarket.Fragments.DeliveryAddress;
import com.electonicmarket.android.emarket.Fragments.PlacedOrders;
import com.electonicmarket.android.emarket.Fragments.Settings;
import com.electonicmarket.android.emarket.Fragments.Vendor;
import com.electonicmarket.android.emarket.Fragments.UserAccount;
import com.electonicmarket.android.emarket.Models.userprofile;
import com.electonicmarket.android.emarket.R;
import com.electonicmarket.android.emarket.openingpageActivity;
import com.electonicmarket.android.emarket.service.EmptyCartService;
import com.electonicmarket.android.emarket.splashscreen;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,ComponentCallbacks2 {

    TextView Emailfield;
    TextView location;
    static TextView vendor;
    TextView deals;
    TextView account;
    TextView orders;
    TextView info;
    TextView settings;
    TextView logout;
    TextView Title;
    TextView welcome;
    TextView firstnamefield;
    Typeface customfont;
    Toolbar toolbar;
    Fragment fragment = new DeliveryAddress();
    SimpleDraweeView profileimage;
    String email;
    ArrayList<userprofile> userprofiles;
    LinearLayout locationlayout;
    static LinearLayout vendorlayout;
    LinearLayout accountlayout;
    LinearLayout orderslayout;
    LinearLayout infolayout;
    LinearLayout settingslayout,logoutlayout;
    String firebaseid;
    static SearchView seachvendor;
    OnPassVendorQuery vendorQueryPasser;

    int flag = 0;


    public interface OnPassVendorQuery {
        void passVendorQuery(String data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);



            setContentView(R.layout.activity_main);

        if(savedInstanceState != null){

            if(savedInstanceState.getInt("pid",-1) == android.os.Process.myPid()){

            }
            else{

                startActivity(new Intent(MainActivity.this, splashscreen.class));

            }
        }



         InitializeView();
         locationlayout.setBackgroundColor(Color.parseColor("#b3fa2d65"));
         setOnclicklistner();
         getComingInfo();
         EmptyCart();



         DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
         ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer,toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        firebaseid = preferences.getString("firebaseid","");
        if(!firebaseid.equalsIgnoreCase("")) {
            new saveidtask().execute();
        }
        storeCredentialInSharedPref(userprofiles.get(0).getEmail(),userprofiles.get(0).getPassword());

            if (getIntent().getStringExtra("fromlocation") != null) {

                Title.setText("Vendors");
                double longitude = getIntent().getDoubleExtra("longitude", 0);
                double latitude = getIntent().getDoubleExtra("latitude", 0);
                String address = getIntent().getStringExtra("address");
                String city = getIntent().getStringExtra("city");
                String area = getIntent().getStringExtra("area");
                String useremail = getIntent().getStringExtra("email");
                ArrayList<userprofile> userprofiles = getIntent().getParcelableArrayListExtra("userprofile");
                changetovendor();
                fragment = Vendor.newInstance(userprofiles, address, area, city, latitude, longitude, useremail);
                vendorQueryPasser = (OnPassVendorQuery)fragment;
                seachvendor.setVisibility(View.VISIBLE);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment, "vendor");
                fragmentTransaction.commit();

            }
            else {
                fragment = DeliveryAddress.newInstance(userprofiles);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment, "deliveryaddress");
                fragmentTransaction.commit();
            }
        //}
        //NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
       // navigationView.setNavigationItemSelectedListener(this);


    }




    private void EmptyCart(){
        Intent serviceintent2 = new Intent(this, EmptyCartService.class);
        serviceintent2.putExtra("url", "http://jl-market.com/user/emptyuserscart.php");
        serviceintent2.putExtra("userid", userprofiles.get(0).getEmail());
        startService(serviceintent2);
    }
    private void storeCredentialInSharedPref(String email,String password){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("email",email);
        editor.putString("password",password);
        editor.commit();
    }
    private void removecredentialFromSharedpref(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("email");
        editor.remove("password");
        editor.commit();
    }

    public class saveidtask extends AsyncTask {
        private String url = "http://jl-market.com/user/userSavefirebasetoken.php";
        @Override
        protected Object doInBackground(Object[] objects) {
            String serverresponse = new saveid().GetData(url,userprofiles.get(0).getEmail(),firebaseid);
            return serverresponse;
        }
    }

    public class saveid {

        public String GetData(String url,String userid,String firebaseid) {

            try {
                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(50, TimeUnit.SECONDS)
                        .writeTimeout(50, TimeUnit.SECONDS)
                        .readTimeout(50, TimeUnit.SECONDS)
                        .build();


                RequestBody formBody = new FormBody.Builder()
                        .add("userid", userid)
                        .add("firebaseid",firebaseid)
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        } else {
            if(getIntent().getStringExtra("fromlocation") != null){
                super.onBackPressed();
            }
            else{

            }

        }
    }

    private void getComingInfo(){
        userprofiles = getIntent().getParcelableArrayListExtra("userprofile");
        getComingProfilepicturePath(userprofiles);
        getComingOtherDetails(userprofiles);
    }


    private void getComingProfilepicturePath(ArrayList<userprofile> userprofiles){

        if(userprofiles != null) {
            String ImageUri = userprofiles.get(0).getProfileimage();
            Uri imageuri = Uri.parse(ImageUri);
            ImageRequest request = ImageRequest.fromUri(imageuri);
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(request)
                    .setOldController(profileimage.getController()).build();
            profileimage.setController(controller);
        }
    }

    private void getComingOtherDetails(ArrayList<userprofile> userprofiles){
        if(userprofiles != null){
         String firstname = userprofiles.get(0).getFirstname();
         email = userprofiles.get(0).getEmail();
         firstnamefield.setText(firstname);
         Emailfield.setText(email);


        }
    }

    private  void InitializeView(){


        Emailfield = findViewById(R.id.useremail);
        vendor = findViewById(R.id.vendor);
        location = findViewById(R.id.location);
        seachvendor = findViewById(R.id.searchvendor);
        EditText seachtext = seachvendor.findViewById(androidx.appcompat.R.id.search_src_text);
        seachtext.setTextColor(Color.parseColor("#ffffff"));
        seachtext.setHintTextColor(Color.parseColor("#50ffffff"));
        ImageView icon = seachvendor.findViewById(androidx.appcompat.R.id.search_button);
        icon.setColorFilter(Color.WHITE);
        ImageView searchclose = seachvendor.findViewById(androidx.appcompat.R.id.search_close_btn);
        searchclose.setColorFilter(Color.WHITE);
        ImageView searchgo = seachvendor.findViewById(androidx.appcompat.R.id.search_go_btn);
        searchgo.setColorFilter(Color.WHITE);
        account = findViewById(R.id.account);
        orders = findViewById(R.id.orders);
        info = findViewById(R.id.info);
        settings = findViewById(R.id.settings);
        logout = findViewById(R.id.logout);
        Title = findViewById(R.id.maintoolbartitle);
        welcome = findViewById(R.id.activity_main_welcome);
        firstnamefield = findViewById(R.id.activity_main_user_firstname);
        profileimage = findViewById(R.id.mainuserprofileimage);

        locationlayout = findViewById(R.id.yourlocationlayout);
        vendorlayout = findViewById(R.id.vendorlayout);
        accountlayout = findViewById(R.id.accountlayout);
        orderslayout = findViewById(R.id.orderslayout);
        infolayout = findViewById(R.id.infolayout);
        settingslayout = findViewById(R.id.settingslayout);
        logoutlayout = findViewById(R.id.logoutlayout);
    WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
    layoutParams.screenBrightness = 0.5f;
    getWindow().setAttributes(layoutParams);
    toolbar  = (Toolbar) findViewById(R.id.maintoolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayShowTitleEnabled(false);
    customfont= Typeface.createFromAsset(getAssets(),"Kylo-Light.otf");
    setCustomfont(customfont);
    seachtext.setTypeface(customfont);

    seachvendor.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean b) {
            if(b){
               Title.setVisibility(View.GONE);
            }
            else{
                Title.setVisibility(View.VISIBLE);
            }
        }
    });

    seachvendor.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String s) {
            vendorQueryPasser.passVendorQuery(s);
            return true;
        }

        @Override
        public boolean onQueryTextChange(String s) {
            vendorQueryPasser.passVendorQuery(s);
            return true;
        }
    });
}

private void setCustomfont(Typeface customfont){

    location.setTypeface(customfont);
    vendor.setTypeface(customfont);
    account.setTypeface(customfont);
    orders.setTypeface(customfont);
    info.setTypeface(customfont);
    info.setTypeface(customfont);
    logout.setTypeface(customfont);
    Title.setTypeface(customfont);
    settings.setTypeface(customfont);

    Typeface customfont2= Typeface.createFromAsset(getAssets(),"Kylo-Light.otf");
    Emailfield.setTypeface(customfont2);
    welcome.setTypeface(customfont2);
    firstnamefield.setTypeface(customfont2);
}

private void setOnclicklistner(){
      /*  location.setOnClickListener(this);
        vendor.setOnClickListener(this);

        account.setOnClickListener(this);
        orders.setOnClickListener(this);
        info.setOnClickListener(this);
        settings.setOnClickListener(this);
        logout.setOnClickListener(this);*/

        locationlayout.setOnClickListener(this);
        vendorlayout.setOnClickListener(this);
        accountlayout.setOnClickListener(this);
        orderslayout.setOnClickListener(this);
        infolayout.setOnClickListener(this);
        settingslayout.setOnClickListener(this);
        logoutlayout.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
           int flag = 0;
        DrawerLayout drawerLayout;
        switch (v.getId()){

            case R.id.yourlocationlayout:
                Fragment deliveryaddress = getSupportFragmentManager().findFragmentByTag("deliveryaddress");
                if(deliveryaddress != null && deliveryaddress.isVisible()){

                }
                else{
                   changetodeliveryaddressfragment();
                   changetolocation();
                }
                 drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.vendorlayout:
                Fragment vendor = getSupportFragmentManager().findFragmentByTag("vendor");
                if(vendor != null && vendor.isVisible()){

                }
                else{
                    changetovendor();
                    changetovendorfragment();
                }

                 drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
                drawerLayout.closeDrawer(GravityCompat.START);
                break;

            case R.id.accountlayout:
                Fragment account = getSupportFragmentManager().findFragmentByTag("account");
                if(account != null && account.isVisible()){

                }
                else{
                    changetoaccount();
                    changetoaccountfragment();
                }

                drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
                drawerLayout.closeDrawer(GravityCompat.START);
                break;

            case R.id.orderslayout:

                Fragment order = getSupportFragmentManager().findFragmentByTag("orders");
                if(order != null && order.isVisible()){

                }
                else{

                    changetoorders();
                    changetordersfragment();
                }
                drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.infolayout:
                Fragment info = getSupportFragmentManager().findFragmentByTag("info");
                if(info != null && info.isVisible()){

                }
                else{
                    changetoinfo();
                    changetoinfofragment();
                }
                drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.settingslayout:
                Fragment settings = getSupportFragmentManager().findFragmentByTag("settings");
                if(settings != null && settings.isVisible()){

                }
                else{
                    changetosettingsfragment();
                    changetosettings();

                }
                drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.logoutlayout:
                Intent intent = new Intent(MainActivity.this,openingpageActivity.class);
                removecredentialFromSharedpref();
                startActivity(intent);
        }


    }




    private void changetodeliveryaddressfragment(){
        Fragment fragment =  DeliveryAddress.newInstance(userprofiles);
        Title.setText("Delivery Address");
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment, "deliveryaddress");
        fragmentTransaction.commit();
    }

    private void changetovendorfragment(){
        String address = userprofiles.get(0).getAddress();
        String area = userprofiles.get(0).getArea();
        String city = userprofiles.get(0).getCity();
        double lat = userprofiles.get(0).getLatitude();
        double log = userprofiles.get(0).getLongitude();
        String email = userprofiles.get(0).getEmail();
        seachvendor.setVisibility(View.VISIBLE);
        Fragment fragment =  Vendor.newInstance(userprofiles,address,area,city,lat,log,email);
        vendorQueryPasser = (OnPassVendorQuery)fragment;
        Title.setText("Vendors");

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment, "vendor");
        fragmentTransaction.commit();
    }

    private void changetoaccountfragment(){
        Fragment fragment =  UserAccount.newInstance(userprofiles);
        Title.setText("Account");
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment, "account");
        fragmentTransaction.commit();
    }

    private void changetordersfragment(){

        Fragment orderfragment = PlacedOrders.newInstance(userprofiles.get(0).getEmail());
        Title.setText("Orders");
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, orderfragment, "orders");
        fragmentTransaction.commit();
    }

    private void changetoinfofragment(){
        Fragment fragment = com.electonicmarket.android.emarket.Fragments.info.newInstance(userprofiles);
        Title.setText("Help");
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment, "info");
        fragmentTransaction.commit();
    }

    private void changetosettingsfragment(){
        Fragment fragment = Settings.newInstance(userprofiles);
        Title.setText("Settings");
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment, "settings");
        fragmentTransaction.commit();
    }
    private void changetolocation(){
        locationlayout.setBackgroundColor(Color.parseColor("#b3fa2d65"));
        vendorlayout.setBackgroundColor(Color.parseColor("#00000000"));
        accountlayout.setBackgroundColor(Color.parseColor("#00000000"));
        orderslayout.setBackgroundColor(Color.parseColor("#00000000"));
        infolayout.setBackgroundColor(Color.parseColor("#00000000"));
        settingslayout.setBackgroundColor(Color.parseColor("#00000000"));
    }
    private void changetovendor(){
        locationlayout.setBackgroundColor(Color.parseColor("#00000000"));
        vendorlayout.setBackgroundColor(Color.parseColor("#b3fa2d65"));
        accountlayout.setBackgroundColor(Color.parseColor("#00000000"));
        orderslayout.setBackgroundColor(Color.parseColor("#00000000"));
        infolayout.setBackgroundColor(Color.parseColor("#00000000"));
        settingslayout.setBackgroundColor(Color.parseColor("#00000000"));
    }

    private void changetoaccount(){
        locationlayout.setBackgroundColor(Color.parseColor("#00000000"));
        vendorlayout.setBackgroundColor(Color.parseColor("#00000000"));
        accountlayout.setBackgroundColor(Color.parseColor("#b3fa2d65"));
        orderslayout.setBackgroundColor(Color.parseColor("#00000000"));
        infolayout.setBackgroundColor(Color.parseColor("#00000000"));
        settingslayout.setBackgroundColor(Color.parseColor("#00000000"));
    }
    private void changetoorders(){
        locationlayout.setBackgroundColor(Color.parseColor("#00000000"));
        vendorlayout.setBackgroundColor(Color.parseColor("#00000000"));
        accountlayout.setBackgroundColor(Color.parseColor("#00000000"));
        orderslayout.setBackgroundColor(Color.parseColor("#b3fa2d65"));
        infolayout.setBackgroundColor(Color.parseColor("#00000000"));
        settingslayout.setBackgroundColor(Color.parseColor("#00000000"));
    }

    private void changetoinfo(){
        locationlayout.setBackgroundColor(Color.parseColor("#00000000"));
        vendorlayout.setBackgroundColor(Color.parseColor("#00000000"));
        accountlayout.setBackgroundColor(Color.parseColor("#00000000"));
        orderslayout.setBackgroundColor(Color.parseColor("#00000000"));
        infolayout.setBackgroundColor(Color.parseColor("#b3fa2d65"));
        settingslayout.setBackgroundColor(Color.parseColor("#00000000"));
    }

    private void changetosettings(){
        locationlayout.setBackgroundColor(Color.parseColor("#00000000"));
        vendorlayout.setBackgroundColor(Color.parseColor("#00000000"));
        accountlayout.setBackgroundColor(Color.parseColor("#00000000"));
        orderslayout.setBackgroundColor(Color.parseColor("#00000000"));
        infolayout.setBackgroundColor(Color.parseColor("#00000000"));
        settingslayout.setBackgroundColor(Color.parseColor("#b3fa2d65"));
    }

   public static void changeshowvendor(boolean state){
        vendorlayout.setEnabled(state);
   }


   public static void changedisplaysearch(int Visibility){
         seachvendor.setVisibility(Visibility);
   }


    @Override
    public void onSaveInstanceState(Bundle outstate){
        super.onSaveInstanceState(outstate);
        outstate.putInt("pid",android.os.Process.myPid());


    }

}
