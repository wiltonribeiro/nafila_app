package com.will.nafila.BoundaryClasses.ActivitiesClasses;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.will.nafila.ControlClasses.LoadingControl;
import com.will.nafila.ControlClasses.LoginControl;
import com.will.nafila.R;

public class BeginActivity extends AppCompatActivity {
    LoadingControl loadingControl;
    LoginControl loginControl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_begin);

        loginControl = new LoginControl(BeginActivity.this);
        if(!loginControl.checkLoggedIn()){
            loadingControl = new LoadingControl(BeginActivity.this);
            loadingControl.heartEffect();
        }
    }
}
