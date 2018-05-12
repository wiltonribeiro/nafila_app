package com.will.nafila.BoundaryClasses.ActivitiesClasses;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.will.nafila.ControlClasses.LoginControl;
import com.will.nafila.R;

public class LoginActivity extends AppCompatActivity {

    EditText inputEmail, inputPassword;
    TextView forgotPassword;
    Button btnLogin;
    LinearLayout btnGoogle, btnFacebook;
    LoginControl loginControl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        forgotPassword = findViewById(R.id.forgotPassword);
        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnFacebook = findViewById(R.id.btnFacebook);
        btnGoogle = findViewById(R.id.btnGoogle);
        loginControl = new LoginControl(LoginActivity.this);

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LoginActivity.this, getResources().getString(R.string.next_update), Toast.LENGTH_LONG).show();
            }
        });

        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LoginActivity.this, getResources().getString(R.string.next_update), Toast.LENGTH_LONG).show();
            }
        });

        btnFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LoginActivity.this, getResources().getString(R.string.next_update), Toast.LENGTH_LONG).show();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString();

                //hide keyboard after click
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                if(email.trim().length()<6)
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.alert_email), Toast.LENGTH_SHORT).show();
                else if(password.trim().length()<6)
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.short_password), Toast.LENGTH_SHORT).show();
                else
                    loginControl.loginUser(email,password);
            }
        });
    }
}
