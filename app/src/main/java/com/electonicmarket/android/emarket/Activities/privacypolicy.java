package com.electonicmarket.android.emarket.Activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;

import com.electonicmarket.android.emarket.R;
import com.electonicmarket.android.emarket.splashscreen;

public class privacypolicy extends AppCompatActivity {

    Toolbar toolbar;
    TextView title,policytext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacypolicy);
        if (savedInstanceState != null) {

            if (savedInstanceState.getInt("pid", -1) == android.os.Process.myPid()) {


            } else {

                startActivity(new Intent(privacypolicy.this, splashscreen.class));

            }

        }
            toolbar = findViewById(R.id.privacypolicytoolbar);
            title = findViewById(R.id.privacypolicytitle);
            policytext = findViewById(R.id.policytext);

        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.screenBrightness = 0.5f;
        getWindow().setAttributes(layoutParams);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.White), PorterDuff.Mode.SRC_ATOP);
            Typeface customfont2 = Typeface.createFromAsset(getAssets(), "Kylo-Regular.otf");
            Typeface customfont = Typeface.createFromAsset(getAssets(), "Kylo-Light.otf");
            title.setTypeface(customfont);
            policytext.setTypeface(customfont2);



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        if (menuItem.getItemId() == android.R.id.home) {

            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }
    @Override
    public void onSaveInstanceState(Bundle outstate){
        super.onSaveInstanceState(outstate);
        outstate.putInt("pid",android.os.Process.myPid());
    }
}
