package com.example.bookmeup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextClock;
import android.widget.TextView;

public class PassResetActivity extends AppCompatActivity {

    private Button Preset;
    int clicked = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_reset);
 ;

        Preset = findViewById(R.id.preset_btn);
        Preset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView tv_msg = findViewById(R.id.feedback_tv);
                EditText account = findViewById(R.id.account_fld);

                Intent back = new Intent(getApplicationContext(), LoginActivity.class);
                if(isEmpty(account) == true){
                    account.setError("Field cannot be empty");

                }else{
                    clicked++;

                if(clicked == 1){



                    //Change button functionality to go back after checking on how many times it has been clicked
                    Preset.setText(R.string.reset_goback);

                    //removing the account field from view on click
                    account.setVisibility(View.GONE);

                    //View the reset message
                    tv_msg.setText(R.string.reset_clickmsg);

                    /*
                    TODO: need to add logic for sending the reset password instructions
                     */

                    }
                else if(clicked > 1){
                    startActivity(back);
                }
                }
            }
        });
    }
    boolean isEmpty(EditText textField) {
        return textField.getText().toString().trim().length() == 0;
    }



}
