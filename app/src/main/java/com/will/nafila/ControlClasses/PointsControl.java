package com.will.nafila.ControlClasses;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.will.nafila.R;
import com.will.nafila.StaticData.FirebaseData;

/**
 * Created by willrcneto on 01/04/18.
 */

public class PointsControl{
    private Context context;
    private Activity activity;
    public static RewardedVideoAd mRewardedVideoAd;
    private InterstitialAd mInterstitialAd;
    private LoadingControl loadingControl;


    public PointsControl(Context context) {
        this.context = context;
        this.activity = (Activity) context;
        this.loadingControl = new LoadingControl(context);
    }

    public void getPointsVideo(){

        MobileAds.initialize(activity,
                "ca-app-pub-5161056860787491~2361931235");

        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(activity);
        mRewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
            @Override
            public void onRewardedVideoAdLoaded() {}
            @Override
            public void onRewardedVideoAdOpened() {}
            @Override
            public void onRewardedVideoStarted() {}
            @Override
            public void onRewardedVideoAdClosed() {loadingControl.finishLoading();}

            @Override
            public void onRewarded(RewardItem rewardItem) {
                addPointsToUser(rewardItem.getAmount());
            }

            @Override
            public void onRewardedVideoAdLeftApplication() {}
            @Override
            public void onRewardedVideoAdFailedToLoad(int i) {}
            @Override
            public void onRewardedVideoCompleted() {}
        });

        mRewardedVideoAd.loadAd("ca-app-pub-5161056860787491/7550428711",
                new AdRequest.Builder().build());

    }

    public void getPoints(){
        MobileAds.initialize(activity,
                "ca-app-pub-5161056860787491~2361931235");

        mInterstitialAd = new InterstitialAd(activity);
        mInterstitialAd.setAdUnitId("ca-app-pub-5161056860787491/1101987747");
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                mInterstitialAd.show();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                Toast.makeText(context, activity.getResources().getString(R.string.video_error), Toast.LENGTH_SHORT).show();
                loadingControl.finishLoading();
            }

            @Override
            public void onAdClosed() {
                addPointsToUser(5);
                loadingControl.finishLoading();
            }
        });

        mInterstitialAd.loadAd(new AdRequest.Builder().build());
    }



    private void addPointsToUser(int value){
        FirebaseData.currentUser.setPoints(FirebaseData.currentUser.getPoints()+value);
        updatePointsView();
        FirebaseData.myRef.child("users").child(FirebaseData.currentUser.getUserKey()).child("data").child("points").setValue(FirebaseData.currentUser.getPoints());
    }

    public void removePointsToUser(int value){
        FirebaseData.currentUser.setPoints(FirebaseData.currentUser.getPoints()- value);
        FirebaseData.myRef.child("users").child(FirebaseData.currentUser.getUserKey()).child("data").child("points").setValue(FirebaseData.currentUser.getPoints());
    }

    public void updatePointsView(){
        String points =  " "+activity.getResources().getString(R.string.points);
        TextView textPoints = activity.findViewById(R.id.textPoints);

        SpannableString ss1=  new SpannableString( FirebaseData.currentUser.getPoints()+" "+points);
        ss1.setSpan(new RelativeSizeSpan(2f), 0,Integer.toString(FirebaseData.currentUser.getPoints()).length(), 0); // set size
        textPoints.setTypeface(Typeface.createFromAsset(activity.getAssets(), "fonts/Norwester.otf"));
        textPoints.setText(ss1);
    }

    public LoadingControl getLoadingControl() {
        return loadingControl;
    }
}
