package com.example.bookmeup;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {
    private static final int MIN_PASSWORD_LENGTH = 8;
    final Context context = this;
    String error = "";
    final DBAdapter db = new DBAdapter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ArrayList<String> users = new ArrayList<String>();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, users);

        Button register = findViewById(R.id.regButton);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                EditText firstName = findViewById(R.id.regFirstname);
                EditText lastName = findViewById(R.id.regLastname);
                final EditText userName = findViewById(R.id.regUsername);
                EditText password = findViewById(R.id.regPassword);
                EditText password2 = findViewById(R.id.regPassword2);
                EditText email = findViewById(R.id.regEmail);

                final String fName, lName, uName, inputPassword1, inputPassword2, inputEmail;

                fName = firstName.getText().toString().trim();
                lName = lastName.getText().toString().trim();
                inputEmail = email.getText().toString().trim();
                uName = userName.getText().toString().trim();
                inputPassword1 = password.getText().toString().trim();
                inputPassword2 = password2.getText().toString().trim();


                if (fName.isEmpty()) {
                    firstName.setError(getResources().getString(R.string.reset_errmsg));
                    firstName.requestFocus();
                    error = "Error";
                } else if (lName.isEmpty()) {
                    lastName.setError(getResources().getString(R.string.reset_errmsg));
                    lastName.requestFocus();
                    error = "Error";
                } else if (inputEmail.isEmpty()) {
                    email.setError(getResources().getString(R.string.reset_errmsg));
                    email.requestFocus();
                    error = "Error";
                } else if (inputPassword1.isEmpty()) {
                    password.setError(getResources().getString(R.string.reset_errmsg));
                    password.requestFocus();
                    error = "Error";
                } else if (inputPassword1.length() < MIN_PASSWORD_LENGTH) { //Ensure that the password length is over 8
                    password.setError(getResources().getString(R.string.error_invalid_password));
                    password.requestFocus();
                    error = "Error";
                } else if (!(inputPassword1.equals(inputPassword2))) {
                    password2.setError(getResources().getString(R.string.matchPassword));
                    password2.requestFocus();
                    error = "Error";
                } else if (uName.isEmpty()) {
                    userName.setError(getResources().getString(R.string.reset_errmsg));
                    userName.requestFocus();
                    error = "Error";
                } else if (checkUserName(uName)) {
                    userName.requestFocus();
                    userName.setError(getResources().getString(R.string.usernameUnavailable));
                    error = "Error";
                }

                // Do proper check before here.
                if (!error.equals("Error")) {
                    String  hashedPassword= null;
                    try {
                        hashedPassword = hashPassword(inputPassword1);
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }

                    try {
                        db.insertUser(fName, lName, uName, inputEmail,hashedPassword);
                    } catch (SQLException sql) {
                        sql.printStackTrace();
                    }
                    db.close();
                    AlertDialog.Builder regAlertDialog = new AlertDialog.Builder(RegisterActivity.this);
                    regAlertDialog
                            .setTitle(R.string.success)
                            .setMessage(String.format("%s\n%s", getString(R.string.reg_success), getString(R.string.checkEmail)))
                            .setCancelable(false)
                            .setPositiveButton(R.string.text_homePage, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class); //Change to landing page
                                    startActivity(intent);
                                }
                            });

                    regAlertDialog.show();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.fill_all_fields, Toast.LENGTH_LONG).show();
                    error = "";
                }
            }
        });

    }

    public boolean checkUserName(String uName) {
        try {
            String dbUname;
            final String destPath = "/data/data/" + getPackageName() + "/databases/appDB";
            try {
                db.open(destPath);
            } catch (SQLException sql) {
                sql.printStackTrace();
            }
            Cursor dbCursor = db.getAllContacts(); //db.getContact(uName);
            dbCursor.moveToFirst();
            //  while (dbCursor != null) {
            //   if(dbCursor!=null)
            while (!dbCursor.isLast()) {
                //    do {
                dbUname = dbCursor.getString(dbCursor.getColumnIndex("username"));
                if (dbUname.equals(uName)) //{ //Username is taken
                {
                    error = "Error";

                    return true;
                    // break;
                } else {
                    dbCursor.moveToNext();
                    //  db.insertUser(fName,lName,uName,inputEmail,inputPassword1);
                    //   break;
                }
                // }while(dbCursor.moveToNext());

            }
        } catch (SQLException sql) {
            sql.printStackTrace();
        }

        return false;
    }

    public String hashPassword(String password) throws NoSuchAlgorithmException {
        StringBuffer MD5Hash=null;
        try {
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(password.getBytes());
            byte messageDigest[] = digest.digest();
            MD5Hash = new StringBuffer();
            for (int i =0; i<messageDigest.length;i++){
                String hash = Integer.toHexString(0xFF & messageDigest[i]);
                while (hash.length()<2)
                    hash = "0" + hash;
                MD5Hash.append(hash);
            }

            //result.setText(MD5Hash);
        }catch(NoSuchAlgorithmException algError){
            algError.printStackTrace();
        }
        return MD5Hash.toString();
    }

}