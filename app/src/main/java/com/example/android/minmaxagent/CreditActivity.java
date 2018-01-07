package com.example.android.minmaxagent;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.android.minmaxagent.db.DBHandler;
import com.example.android.minmaxagent.db.Profile;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static android.widget.Toast.*;

public class CreditActivity extends AppCompatActivity {

    Intent receivedIntent;
    private String userName;

    /**
     * Allow Calligraphy to set its default font.
     */
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit);

        receivedIntent = getIntent();

        userName = receivedIntent.getStringExtra("UserName");

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Cartwheel.otf")
                .setFontAttrId(R.attr.fontPath)
                .build() );


        DBHandler db = new DBHandler(this);

        // wtf is this shit
        db.addProfile(new Profile("Rohit","pass",5,5));

        Profile storedProfile = db.getProfile();

        Toast toast=Toast.makeText(getApplicationContext(),"Message: " + storedProfile,Toast.LENGTH_SHORT);
        toast.setMargin(50,50);
        toast.show();


    }
}
