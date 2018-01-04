package com.example.android.minmaxagent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.minmaxagent.db.DBHandler;
import com.example.android.minmaxagent.db.Profile;

public class LoginActivity extends AppCompatActivity {

    EditText editTextUserName, editTextPassword;

    Button buttonLogin, buttonSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextUserName = findViewById(R.id.editTextUserName);
        editTextPassword = findViewById(R.id.editTextPassword);

        buttonLogin = findViewById(R.id.buttonLogin);
        buttonSignUp = findViewById(R.id.buttonSignUp);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userName = editTextUserName.getText().toString();
                String password = editTextPassword.getText().toString();

                String passwordFromDBHandler = getPasswordFromDBHandler(userName);

                Toast toast=Toast.makeText(getApplicationContext(),"Typed Password: " + password + " DB Pass is: " + passwordFromDBHandler,Toast.LENGTH_SHORT);
                toast.setMargin(50,50);
                toast.show();

                if(password.equals(passwordFromDBHandler)){
                    Intent intentMainMenu = new Intent(getApplicationContext(),MainMenuActivity.class);
                    intentMainMenu.putExtra("UserName",userName);
                    startActivity(intentMainMenu);
                }

            }
        });

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private String getPasswordFromDBHandler(String userName) {
        DBHandler db = new DBHandler(this);

        db.addProfile(new Profile("a","a",5,5));

        String passwordInDatabase = db.getPassword(userName);

        return passwordInDatabase;
    }
}
