package com.rqmana.scrambling;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.Toast;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    public final static String PASSWORD = "password";
    public static final String DEFAULT_PASSWORD = "0000";

    AppCompatEditText passwordET ;
    AppCompatButton changePasswordBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        passwordET = findViewById(R.id.passwordET);
        changePasswordBtn = findViewById(R.id.changePasswordBtn);
    }

    public void onChangePasswordBtnClicked(View view){
        startActivity(new Intent(this, ChangePasswordActivity.class));
    }

    public void onLoginBtnClicked (View v) {

        String password = Objects.requireNonNull(passwordET.getText()).toString();
        if (password.isEmpty()) {
            Toast.makeText(this, getString(R.string.enterPasswordMsg), Toast.LENGTH_LONG).show();
            return;
        }

        if ( Utils.getPassword(this).equals(password)){
            startActivity(new Intent(this, MainActivity.class));
        } else {
            Toast.makeText(this, getString(R.string.incorrectPasswordMsg), Toast.LENGTH_LONG).show();
        }
    }
}
