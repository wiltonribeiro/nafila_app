package com.will.nafila.BoundaryClasses.ActivitiesClasses;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.will.nafila.ControlClasses.FollowControl;
import com.will.nafila.ControlClasses.LoadingControl;
import com.will.nafila.ControlClasses.PointsControl;
import com.will.nafila.EntityClasses.User;
import com.will.nafila.R;
import com.will.nafila.StaticData.FirebaseData;

public class UserViewActivity extends AppCompatActivity {

    private AdView mAdView;
    TextView textUserName, textUserId, listFollowers;
    ImageView imgUserProfile;
    Button btnNaFila, btnShowNumber, btnBack;
    FollowControl followControl;
    public static User userView;
    Typeface typeface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_view);

        typeface = Typeface.createFromAsset(getAssets(), "fonts/Norwester.otf");

        final LoadingControl loadingControl = new LoadingControl(UserViewActivity.this);
        loadingControl.startLoading();

        followControl = new FollowControl(UserViewActivity.this);

        textUserName = findViewById(R.id.textUserName);
        textUserId = findViewById(R.id.textUserId);
        listFollowers = findViewById(R.id.listFollowers);
        imgUserProfile = findViewById(R.id.imgUserProfile);
        btnNaFila = findViewById(R.id.btnNaFila);
        btnBack = findViewById(R.id.btnBack);
        btnShowNumber =findViewById(R.id.btnShowNumber);

        textUserName.setTypeface(typeface);
        textUserId.setTypeface(typeface);

        textUserName.setText(userView.getName());
        textUserId.setText(userView.getUserId().toLowerCase());

        checkNaFila();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserViewActivity.super.onBackPressed();
            }
        });

        btnNaFila.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userView.getFollowers()==null || (!userView.getFollowers().containsKey(FirebaseData.currentUser.getUserKey()))) {
                    followControl.followUser(UserViewActivity.userView);
                    followControl.checkMatch(UserViewActivity.userView);
                } else
                    followControl.unFollowUser(UserViewActivity.userView);

                checkNaFila();
            }
        });

        Picasso.with(UserViewActivity.this).load(userView.getImageUrl()).into(imgUserProfile, new Callback() {
            @Override
            public void onSuccess() {
                loadingControl.finishLoading();
            }

            @Override
            public void onError() {

            }
        });

        MobileAds.initialize(this,
                "ca-app-pub-5161056860787491~2361931235");

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();

        mAdView.loadAd(adRequest);

        showNumberText("?");

        btnShowNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(FirebaseData.currentUser.getPoints()>=20){
                    PointsControl pointsControl = new PointsControl(UserViewActivity.this);
                    pointsControl.removePointsToUser(20);
                    int value;
                    if(UserViewActivity.userView.getFollowers() != null)
                        value = UserViewActivity.userView.getFollowers().size();
                    else
                        value = 0;

                    showNumberText(Integer.toString(value));
                }
                else {
                    Toast.makeText(UserViewActivity.this, getResources().getString(R.string.miss_points), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void showNumberText(String number){
        String followersText =  getResources().getString(R.string.count_other_line)+userView.getName().split(" ")[0];

        SpannableString ss1=  new SpannableString(number+" "+followersText);
        ss1.setSpan(new RelativeSizeSpan(2f), 0,(number).length(), 0); // set size
        listFollowers.setTypeface(typeface);
        listFollowers.setText(ss1);
    }


    private void checkNaFila(){
        if(userView.getUserId().equals(FirebaseData.currentUser.getUserId()))
            btnNaFila.setVisibility(View.GONE);
        else {
            if (userView.getFollowers() == null)
                btnNaFila.setText(getResources().getString(R.string.get_in_line));
            else if (userView.getFollowers().containsKey(FirebaseData.currentUser.getUserKey()))
                btnNaFila.setText(getResources().getString(R.string.out_in_line));
            else
                btnNaFila.setText(getResources().getString(R.string.get_in_line));
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    /** Called when leaving the activity */
    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    /** Called before the activity is destroyed */
    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

}
