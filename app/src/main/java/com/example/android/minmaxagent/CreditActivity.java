package com.example.android.minmaxagent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.android.minmaxagent.db.DBHandler;
import com.example.android.minmaxagent.db.Profile;

import static android.widget.Toast.*;

public class CreditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit);


        DBHandler db = new DBHandler(this);

        db.addProfile(new Profile("Rohit","pass",5,5));

        Profile storedProfile = db.getProfile();

        Toast toast=Toast.makeText(getApplicationContext(),"Message: " + storedProfile,Toast.LENGTH_SHORT);
        toast.setMargin(50,50);
        toast.show();


    }
}
