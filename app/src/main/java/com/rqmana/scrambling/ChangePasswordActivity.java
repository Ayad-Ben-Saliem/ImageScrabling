package com.rqmana.scrambling;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.Toast;

import java.util.Objects;

public class ChangePasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
    }

    public void onChangePasswordBtnClicked(View view) {
        AppCompatEditText oldPasswordET = findViewById(R.id.oldPasswordET);
        String oldPassword = Objects.requireNonNull(oldPasswordET.getText()).toString();

        if (oldPassword.isEmpty()) {
            Toast.makeText(this, getString(R.string.enterPasswordMsg), Toast.LENGTH_LONG).show();
            return;
        }

        if (!Utils.getPassword(this).equals(oldPassword)) {
            Toast.makeText(this, getString(R.string.incorrectPasswordMsg), Toast.LENGTH_LONG).show();
            return;
        }

        AppCompatEditText newPasswordET = findViewById(R.id.newPasswordET);
        String newPassword = Objects.requireNonNull(newPasswordET.getText()).toString();
        if (newPassword.isEmpty()) {
            Toast.makeText(this, getString(R.string.enterPasswordMsg), Toast.LENGTH_LONG).show();
            return;
        }

        AppCompatEditText confirmPasswordET = findViewById(R.id.confirmPasswordET);
        String confirmPassword = Objects.requireNonNull(confirmPasswordET.getText()).toString();
        if (confirmPassword.isEmpty()) {
            Toast.makeText(this, getString(R.string.enterPasswordMsg), Toast.LENGTH_LONG).show();
            return;
        }

        Utils.savePassword(this, newPassword);
    }
}
