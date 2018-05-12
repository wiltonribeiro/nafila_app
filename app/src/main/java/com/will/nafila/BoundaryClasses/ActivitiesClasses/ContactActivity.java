package com.will.nafila.BoundaryClasses.ActivitiesClasses;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.will.nafila.R;

public class ContactActivity extends AppCompatActivity {

    TextView textAppName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        textAppName = findViewById(R.id.textAppName);
        textAppName.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Quantify.ttf"));
    }
}
