package com.will.nafila.ControlClasses;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.will.nafila.BoundaryClasses.ActivitiesClasses.MainActivity;
import com.will.nafila.R;

/**
 * Created by willrcneto on 28/03/18.
 */

public class LoadingControl {

    private Context context;
    private Activity activity;
    private ImageView imgLogo;
    private TextView textLoading;
    private RelativeLayout layoutLoading;

    public LoadingControl(Context context){
        this.context = context;
        this.activity = ((Activity) context);
        this.textLoading = activity.findViewById(R.id.textLoading);
        this.imgLogo = activity.findViewById(R.id.imgLogo);
        this.layoutLoading = activity.findViewById(R.id.layoutLoading);
        this.textLoading.setTypeface(Typeface.createFromAsset(activity.getAssets(), "fonts/Quantify.ttf"));
    }

    public void startLoading(){
        imgLogo.setVisibility(View.VISIBLE);
        textLoading.setVisibility(View.VISIBLE);
        textLoading.setText(activity.getResources().getString(R.string.loading));
        layoutLoading.setVisibility(View.VISIBLE);
    }

    public void finishLoading(){
        layoutLoading.setVisibility(View.GONE);
    }

    public void heartEffect(){
        imgLogo.setVisibility(View.INVISIBLE);
        imgLogo.setVisibility(View.VISIBLE);
        textLoading.setVisibility(View.VISIBLE);
        textLoading.setText(activity.getResources().getString(R.string.app_name));

        Animation animation = AnimationUtils.loadAnimation(context, R.anim.zoom_in);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        activity.finishAffinity();
                        activity.startActivity(new Intent(context, MainActivity.class));
                    }
                }, 500);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        imgLogo.setAnimation(animation);
    }
}
