package com.electonicmarket.android.emarket.Activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.electonicmarket.android.emarket.Models.OrderModel;
import com.electonicmarket.android.emarket.Models.userprofile;
import com.electonicmarket.android.emarket.R;
import com.electonicmarket.android.emarket.splashscreen;

import java.util.ArrayList;

public class PaymentOptions extends AppCompatActivity {
    Toolbar toolbar;

    TextView paymentoptions,paymentmethods,cardondelivery,cashdelivery;
    LinearLayout cardondeliverylayout,cashondeliverylayout;
    ArrayList<OrderModel> orderlist;
    String userid,vendorid,deliveryfee,deliveryminute,cardpayment,cashpayment;
    ArrayList<userprofile> userprofiles;
    String minorder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_options);
        if(savedInstanceState != null){

            if(savedInstanceState.getInt("pid",-1) == android.os.Process.myPid()){

            }
            else{

                startActivity(new Intent(PaymentOptions.this,splashscreen.class));

            }
        }
        getIntentValues();
        initView();
        setOnclicklistener();



    }
    private void getIntentValues(){
        orderlist = getIntent().getParcelableArrayListExtra("orderedlist");
        userid = getIntent().getStringExtra("userid");
        vendorid = getIntent().getStringExtra("vendorid");
        deliveryfee = getIntent().getStringExtra("deliveryfee");
        deliveryminute = getIntent().getStringExtra("deliveryminute");
        cashpayment = getIntent().getStringExtra("cashpayment");
        cardpayment = getIntent().getStringExtra("cardpayment");
        minorder = getIntent().getStringExtra("minimumorder");
        userprofiles = getIntent().getParcelableArrayListExtra("userprofile");

    }

    private void setOnclicklistener(){
        cashondeliverylayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PaymentOptions.this,CheckOut.class);
                intent.putParcelableArrayListExtra("orderedlist",  orderlist);
                intent.putExtra("userid",userid);
                intent.putExtra("deliveryfee",deliveryfee);
                intent.putExtra("vendorid",vendorid);
                intent.putExtra("paymentmethod","cash");
                intent.putExtra("deliveryminute",deliveryminute);
                intent.putExtra("minimumorder",minorder);
                intent.putParcelableArrayListExtra("userprofile",userprofiles);
                startActivity(intent);
            }
        });

        cardondeliverylayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PaymentOptions.this,CheckOut.class);
                intent.putParcelableArrayListExtra("orderedlist",  orderlist);
                intent.putExtra("userid",userid);
                intent.putExtra("deliveryfee",deliveryfee);
                intent.putExtra("vendorid",vendorid);
                intent.putExtra("paymentmethod","card");
                intent.putExtra("deliveryminute",deliveryminute);
                intent.putExtra("minimumorder",minorder);
                intent.putParcelableArrayListExtra("userprofile",userprofiles);
                startActivity(intent);
            }
        });
    }
    private void initView(){

        paymentoptions = findViewById(R.id.paymentoptionstitle);
        paymentmethods = findViewById(R.id.paymentmethods);
        cardondelivery = findViewById(R.id.cardondelivery);
        cashdelivery = findViewById(R.id.cashondelivery);
        toolbar = findViewById(R.id.paymentoptionstoolbar);
        cardondeliverylayout = findViewById(R.id.cardondeliverylayout);
        cashondeliverylayout = findViewById(R.id.cashondeliverylayout);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.White), PorterDuff.Mode.SRC_ATOP);


        Typeface  customfont= Typeface.createFromAsset(getAssets(),"Kylo-Light.otf");
        Typeface customfont2= Typeface.createFromAsset(getAssets(),"Kylo-Light.otf");

        paymentoptions.setTypeface(customfont);
        paymentmethods.setTypeface(customfont2);
        cardondelivery.setTypeface(customfont2);
        cashdelivery.setTypeface(customfont2);

        if(cashpayment.equalsIgnoreCase("yes")){
            cashondeliverylayout.setVisibility(View.VISIBLE);
        }
        else{
            cashondeliverylayout.setVisibility(View.GONE);
        }
        if(cardpayment.equalsIgnoreCase("yes")){
            cardondeliverylayout.setVisibility(View.VISIBLE);
        }
        else{
            cardondeliverylayout.setVisibility(View.GONE);
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
