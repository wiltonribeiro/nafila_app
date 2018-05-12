package com.will.nafila.BoundaryClasses.ActivitiesClasses;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.will.nafila.ControlClasses.NotificationControl;
import com.will.nafila.ControlClasses.PointsControl;
import com.will.nafila.R;
import com.will.nafila.StaticData.FirebaseData;

public class MenuActivity extends AppCompatActivity {

    Button btnLogOut, btnClose, btnNews, btnGetPoints, btnChangePassword, btnDeleteAccount, btnContact;
    TextView textPoints;
    PointsControl pointsControl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        btnLogOut = findViewById(R.id.btnLogOut);
        btnClose = findViewById(R.id.btnClose);
        btnNews = findViewById(R.id.btnNews);
        btnGetPoints = findViewById(R.id.btnGetPoints);
        btnChangePassword = findViewById(R.id.btnChangePassword);
        btnDeleteAccount = findViewById(R.id.btnDeleteAccount);
        btnContact = findViewById(R.id.btnContact);

        btnContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuActivity.this, ContactActivity.class));
            }
        });

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MenuActivity.this, getResources().getString(R.string.next_update), Toast.LENGTH_SHORT).show();
            }
        });

        btnDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MenuActivity.this, getResources().getString(R.string.next_update), Toast.LENGTH_SHORT).show();
            }
        });

        pointsControl = new PointsControl(MenuActivity.this);
        pointsControl.getPointsVideo();

        textPoints = findViewById(R.id.textPoints);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btnGetPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pointsControl.getLoadingControl().startLoading();

                if (PointsControl.mRewardedVideoAd.isLoaded())
                    PointsControl.mRewardedVideoAd.show();
                 else
                    pointsControl.getPoints();
            }
        });

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseData.mAuth.signOut();
                finishAffinity();
                startActivity(new Intent(MenuActivity.this, BeginActivity.class));
            }
        });

        btnNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuActivity.this, NewsActivity.class));
            }
        });

        pointsControl.updatePointsView();
    }



    @Override
    protected void onResume() {
        super.onResume();

        pointsControl.updatePointsView();

        if(NotificationControl.checkNotifications())
            findViewById(R.id.alertNews).setVisibility(View.VISIBLE);
        else
            findViewById(R.id.alertNews).setVisibility(View.GONE);
    }
}
