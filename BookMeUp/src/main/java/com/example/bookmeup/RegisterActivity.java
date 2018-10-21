package com.example.bookmeup;

import android.content.Context;
import android.content.DialogInterface;
import android.app.AlertDialog;
//import android.support.v7.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.text.TextUtils;

import javax.xml.validation.Validator;

public class RegisterActivity extends AppCompatActivity {
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button register = findViewById(R.id.registerButton);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText username = findViewById(R.id.username);
                EditText password = findViewById(R.id.regPassword);
                EditText password2 = findViewById(R.id.regPassword2);
                String pass1 = password.getText().toString().trim();
                String pass2 = password2.getText().toString().trim();
                if(TextUtils.equals(pass1,pass2))
                    Toast.makeText(getApplicationContext(), R.string.matchPassword, Toast.LENGTH_LONG).show();
                // Do proper check before here.
                if (!username.getText().toString().isEmpty()
                        && !password.getText().toString().isEmpty()
                        && !password2.getText().toString().isEmpty()
                        && TextUtils.equals(pass1,pass2)) {
                    AlertDialog.Builder regAlertDialog = new AlertDialog.Builder(RegisterActivity.this);
                    regAlertDialog
                            .setTitle(R.string.success)
                            .setMessage(String.format("%s\n%s", getString(R.string.reg_success), getString(R.string.checkEmail)))
                            .setCancelable(false)
                            .setPositiveButton(R.string.text_homePage, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(getApplicationContext(),LoginActivity.class); //Change to landing page
                                    startActivity(intent);
                                }
                            });
                    regAlertDialog.show();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.fill_all_fields, Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}