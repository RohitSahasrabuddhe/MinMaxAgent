package com.example.android.minmaxagent;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.minmaxagent.db.DBHandler;
import com.example.android.minmaxagent.db.Profile;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LoginActivity extends AppCompatActivity {

    EditText editTextUserName, editTextPassword;
    TextView textViewGuestUser;
    Button buttonLogin, buttonSignUp;


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
        setContentView(R.layout.activity_login);

        editTextUserName = findViewById(R.id.editTextUserName);
        editTextPassword = findViewById(R.id.editTextPassword);

        textViewGuestUser = findViewById(R.id.textViewGurstUser);

        buttonLogin = findViewById(R.id.buttonLogin);
        buttonSignUp = findViewById(R.id.buttonSignUp);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userName = editTextUserName.getText().toString();
                String password = editTextPassword.getText().toString();

                String passwordFromDBHandler = getPasswordFromDBHandler(userName);

                if(password.equals(passwordFromDBHandler)){
                    Intent intentMainMenu = new Intent(getApplicationContext(),MainMenuActivity.class);
                    intentMainMenu.putExtra("UserName",userName);

                    intentMainMenu.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intentMainMenu.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

                    startActivity(intentMainMenu);

                }

            }
        });

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Forward control to Intent Sign Up
                Intent intentSignUp = new Intent(getApplicationContext(),SignUpActivity.class);
                startActivity(intentSignUp);

            }
        });


        textViewGuestUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Creating new guest user in database
                String guestUserName = "Guest";
                createGuestUser(guestUserName);

                //Creating intent to render further
                Intent intentMainMenu = new Intent(getApplicationContext(),MainMenuActivity.class);
                intentMainMenu.putExtra("UserName",guestUserName);
                intentMainMenu.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intentMainMenu.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intentMainMenu);

                finish();
            }
        });
    }

    private void createGuestUser(String guestUserName) {
        DBHandler db = new DBHandler(this);

        db.deleteRecordIfExists(guestUserName);
        db.addProfile(new Profile(guestUserName,"",5,5));

        Profile dbProfile = db.getProfileWithName(guestUserName);

        /*Toast toast=Toast.makeText(getApplicationContext(),"DB Profile: " + dbProfile,Toast.LENGTH_LONG);
        toast.setMargin(50,50);
        toast.show();*/
    }

    private String getPasswordFromDBHandler(String userName) {
        DBHandler db = new DBHandler(this);

        db.addProfile(new Profile("a","a",5,5));

        String passwordInDatabase = db.getPassword(userName);

        return passwordInDatabase;
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
