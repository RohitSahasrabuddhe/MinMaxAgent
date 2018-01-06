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

public class SignUpActivity extends AppCompatActivity {
    private EditText editTExtUserName, editTExtPassword, editTextPasswordMatch;

    private String userName, password, passwordMatch;

    private Button buttonSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        editTExtUserName = findViewById(R.id.editTextUserName);
        editTExtPassword = findViewById(R.id.editTextPassword);
        editTextPasswordMatch = findViewById(R.id.editTextPasswordMatch);

        buttonSignUp = findViewById(R.id.buttonSignUp);

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userName = editTExtUserName.getText().toString();
                password = editTExtPassword.getText().toString();
                passwordMatch = editTextPasswordMatch.getText().toString();

                if(password.equals(passwordMatch)){
                    //create new user and log in
                    Intent intentMainMenu = new Intent(getApplicationContext(),MainMenuActivity.class);
                    intentMainMenu.putExtra("UserName",userName);

                    Profile profile = new Profile(userName,password,5,5);

                    storeInDatabase(profile);

                    startActivity(intentMainMenu);
                }
                else{
                    //Make a toast

                    Toast.makeText(getApplicationContext(), "Password doesn't match. Please re enter password" , Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void storeInDatabase(Profile profile) {
        DBHandler db = new DBHandler(this);
        db.deleteRecordIfExists(profile.getName());
        db.addProfile(profile);
    }
}
