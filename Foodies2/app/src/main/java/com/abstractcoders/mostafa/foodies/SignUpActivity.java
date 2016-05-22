package com.abstractcoders.mostafa.foodies;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.abstractcoders.mostafa.foodies.Handlers.DatabaseHelper;

public class SignUpActivity extends Activity {
    Button cancelBtn;
    EditText userNameTxt, fNameTxt, lNameTxt, emailAddressTxt, passwordTxt;
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        userNameTxt = (EditText) findViewById(R.id.userNameTxt);
        fNameTxt = (EditText) findViewById(R.id.fNameTxt);
        lNameTxt = (EditText) findViewById(R.id.lNameTxt);
        emailAddressTxt = (EditText) findViewById(R.id.addressTxt);
        passwordTxt = (EditText) findViewById(R.id.passwordTxt);
        final String fName = fNameTxt.getText().toString();
        final String lName = lNameTxt.getText().toString();
        final String address = emailAddressTxt.getText().toString();
        final String password = passwordTxt.getText().toString();
        final byte[] photo = new byte[1];
        Button signUpBtn = (Button) findViewById(R.id.submitDetailsBtn);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper db = new DatabaseHelper(getBaseContext());
                db.insertUser(userNameTxt.getText().toString(), fNameTxt.getText().toString(),lNameTxt.getText().toString(),
                        emailAddressTxt.getText().toString(),passwordTxt.getText().toString(),photo);
            }
        });

    }

}
