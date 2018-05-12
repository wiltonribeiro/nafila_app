package com.will.nafila.ControlClasses;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.will.nafila.EntityClasses.Notification;
import com.will.nafila.EntityClasses.User;
import com.will.nafila.R;
import com.will.nafila.StaticData.FirebaseData;

import java.util.HashMap;

/**
 * Created by willrcneto on 29/03/18.
 */

public class FollowControl {
    Context context;
    Activity activity;

    public FollowControl(Context context) {
        this.context = context;
        this.activity = (Activity) context;
    }

    public void checkMatch(User user){
        if(FirebaseData.currentUser.getFollowers()!=null && FirebaseData.currentUser.getFollowers().containsKey(user.getUserKey())){
            MatchControl matchControl = new MatchControl(context);
            matchControl.activeMatch(FirebaseData.currentUser.getName(), user.getName());
        }
    }

    public void followUser(final User user){
        final Notification notification = new Notification();
        notification.setDate_time();
        notification.setTitle(activity.getResources().getString(R.string.notification_follower_titile));
        notification.setText(activity.getResources().getString(R.string.notification_follower_text));
        notification.setDid_by(FirebaseData.currentUser.getUserKey());
        notification.setType("new_follower");

        if(user.getFollowers()==null)
            user.setFollowers(new HashMap<String, Object>());

        user.getFollowers().put(FirebaseData.currentUser.getUserKey(), notification.getDate_time());

        FirebaseData.myRef.child("users").child(user.getUserKey()).child("data").child("followers")
                .child(FirebaseData.currentUser.getUserKey()).setValue(notification.getDate_time()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                    addMyList(user,notification);
                else
                    Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addMyList(final User user, final Notification notification){
        if(FirebaseData.currentUser.getFollowing()==null)
            FirebaseData.currentUser.setFollowing(new HashMap<String, Object>());

        FirebaseData.currentUser.getFollowing().put(user.getUserKey(), notification.getDate_time());

        FirebaseData.myRef.child("users").child(FirebaseData.currentUser.getUserKey()).child("data").child("following")
                .child(user.getUserKey()).setValue(notification.getDate_time()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    NotificationControl notificationControl = new NotificationControl(context);
                    notificationControl.addNotification(user, notification);
                }
                else
                    Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void removeMyList(final User user){
        FirebaseData.currentUser.getFollowing().remove(user.getUserKey());
        FirebaseData.myRef.child("users").child(FirebaseData.currentUser.getUserKey()).child("data").child("following")
                .child(user.getUserKey()).removeValue();
    }

    public void unFollowUser(final User user){
        user.getFollowers().remove(FirebaseData.currentUser.getUserKey());

        FirebaseData.myRef.child("users").child(user.getUserKey()).child("data").child("followers")
                .child(FirebaseData.currentUser.getUserKey()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                    removeMyList(user);
                else
                    Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
