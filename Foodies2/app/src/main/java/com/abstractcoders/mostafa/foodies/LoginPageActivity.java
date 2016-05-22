package com.abstractcoders.mostafa.foodies;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.abstractcoders.mostafa.foodies.Handlers.DatabaseHelper;
import com.abstractcoders.mostafa.foodies.Model.CurrentUserSingleton;
import com.beardedhen.androidbootstrap.BootstrapButton;

import java.sql.Time;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class LoginPageActivity extends Activity {

    BootstrapButton loginBtn;
    DatabaseHelper dbHelper;
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        final EditText userNameTxt, passwordTxt;
        userNameTxt = (EditText) findViewById(R.id.userNameTxt);
        passwordTxt = (EditText) findViewById(R.id.passwordTxt);
        loginBtn = (BootstrapButton) findViewById(R.id.logInBtn);
        dbHelper = new DatabaseHelper(this);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = new Date();
                Log.i("Login Attempt", "Time of attempt: " + date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds());
                try {
                Cursor cursor = dbHelper.getUser(userNameTxt.getText().toString(), passwordTxt.getText().toString());
                if (cursor.moveToNext()) {
                    String userID = cursor.getString(0);
                    String UserName = cursor.getString(1);
                    if (UserName.equals(userNameTxt.getText().toString()))
                    {
                        CurrentUserSingleton csu = CurrentUserSingleton.getInstance(Integer.parseInt(userID));
                        Intent i = new Intent(LoginPageActivity.this, NavigationDrawerActivity.class);
                        Log.i("Login Success", "Time of successful login: " + date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds());
                        startActivity(i);
                    }
                }else
                {
                    Toast.makeText(getBaseContext(),"Invalid Username/Password", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e)
            {
                Log.i("check",e.getMessage());
                Toast.makeText(getBaseContext(),"Invalid Username/Password", Toast.LENGTH_SHORT).show();
            }
            }
        });
    }
}
