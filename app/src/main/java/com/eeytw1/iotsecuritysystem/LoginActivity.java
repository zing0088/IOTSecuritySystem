package com.eeytw1.iotsecuritysystem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    EditText UsernameET, PasswordET;

    ConnectDatabase_UserValidation_Callback connectDatabase_userValidation_callback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //------------------------------------------------------------------------------------------
        //Hide keyboard when pressing background
        ConstraintLayout constraintLayout = findViewById(R.id.layout_constraint);
        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HideKeyboard(view);
            }
        });

        //------------------------------------------------------------------------------------------
        //Connect to database, then verify user input with username and password in database

        //initialize edit text variables for login button
        UsernameET = findViewById(R.id.editText_username);
        PasswordET = findViewById(R.id.editText_password);

        Button loginButton = findViewById(R.id.button_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Login(view);
            }
        });

        //Display results as alerts and start main menu activity if login is successful
        final Context context = this;

        connectDatabase_userValidation_callback = new ConnectDatabase_UserValidation_Callback() {
            @Override
            public void LoginResult(String result) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context)
                        .setTitle("Login Status")
                        .setMessage(result);
                alertDialog.create().show();

                if (result.matches("login successful")) {
                    alertDialog.setMessage("login was OK");
                    alertDialog.show();

                    Intent i = new Intent(context, MainMenuActivity.class);
                    context.startActivity(i);

                    //context.startActivity(new Intent(context, MotionDetectorActivity.class));
                } else {
                    Toast toast = Toast.makeText(context, "Email or password is wrong", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        };

    }

    public void HideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void Login(View view) {
        //create string variables to store user input from edit text boxes
        String username = UsernameET.getText().toString();
        String password = PasswordET.getText().toString();
        String type = "login";

        ConnectDatabase_UserValidation_AsyncTask connectDatabase_userValidation_asyncTask = new ConnectDatabase_UserValidation_AsyncTask(connectDatabase_userValidation_callback);
        connectDatabase_userValidation_asyncTask.execute( type, username, password);
    }

}
