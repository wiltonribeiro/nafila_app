package com.will.nafila.ControlClasses;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.will.nafila.BoundaryClasses.ActivitiesClasses.UserViewActivity;
import com.will.nafila.EntityClasses.Notification;
import com.will.nafila.R;
import com.will.nafila.StaticData.FirebaseData;

/**
 * Created by willrcneto on 30/03/18.
 */

public class MatchControl {

    private Context context;
    private Activity activity;
    private ImageView imgLogo;
    private TextView textDescription, texFirstUser, textSecondUser, textTitle;
    private Button btnClose;
    private int order;
    private RelativeLayout layoutMatch;


    public MatchControl(Context context){
        this.context = context;
        this.activity = (Activity) context;
        this.order = 0;

        this.layoutMatch = activity.findViewById(R.id.layoutMatch);
        this.textDescription = activity.findViewById(R.id.textDescription);
        this.texFirstUser = activity.findViewById(R.id.textFirstUser);
        this.textSecondUser = activity.findViewById(R.id.textSecondUser);
        this.textTitle = activity.findViewById(R.id.textTitle);
        this.imgLogo = activity.findViewById(R.id.imgLogoMatch);
        this.btnClose = activity.findViewById(R.id.btnClose);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutMatch.setVisibility(View.GONE);
            }
        });
    }

    public void  activeMatch(String firstUser, String secondUser){

        NotificationControl notificationControl = new NotificationControl(context);
        Notification notification = new Notification();
        notification.setDid_by(FirebaseData.currentUser.getUserKey());
        notification.setTitle(activity.getResources().getString(R.string.notification_macth_titile)+" "+firstUser);
        notification.setText(activity.getResources().getString(R.string.notification_match_text));
        notificationControl.addNotification(UserViewActivity.userView,notification);
        notification.setType("match");

        layoutMatch.setVisibility(View.VISIBLE);

        textDescription.setText((activity.getResources().getString(R.string.match_description_part1)+" "+secondUser+" "+activity.getResources().getString(R.string.match_description_part2)));
        texFirstUser.setText(firstUser);
        textSecondUser.setText(secondUser);

        imgLogo.setVisibility(View.VISIBLE);
        textTitle.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.zoom_in);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                texFirstUser.setVisibility(View.VISIBLE);
                textSecondUser.setVisibility(View.VISIBLE);
                textDescription.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        imgLogo.setAnimation(animation);

    }

}
