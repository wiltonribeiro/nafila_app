package com.will.nafila.ControlClasses;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.will.nafila.BoundaryClasses.ActivitiesClasses.BeginActivity;
import com.will.nafila.StaticData.FirebaseData;

/**
 * Created by willrcneto on 12/04/18.
 */

public class DeleteAccountControl {
    Context context;
    Activity activity;
    private int count_followers;
    private int count_following;

    public DeleteAccountControl(Context context) {
        this.context = context;
        this.activity = (Activity) context;
        this.count_followers = 0;
        this.count_following = 0;
    }

    public void deleteListFollowers(){
        for(String key: FirebaseData.currentUser.getFollowers().keySet()){
            FirebaseData.myRef.child("users").child(key).child("following").child(FirebaseData.currentUser.getUserKey()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                        count_followers++;

                    if(count_followers == FirebaseData.currentUser.getFollowers().size())
                        deleteListFollowing();
                }
            });
        }
    }

    private  void deleteListFollowing(){
        for(String key: FirebaseData.currentUser.getFollowing().keySet()){
            FirebaseData.myRef.child("users").child(key).child("followers")
                    .child(FirebaseData.currentUser.getUserKey()).removeValue()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                        count_following++;

                    if(count_following == FirebaseData.currentUser.getFollowing().size())
                        deleteUser();
                }
            });
        }
    }

    private void deleteUser(){
        FirebaseData.myRef.child("users").child(FirebaseData.currentUser.getUserKey()).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                    deleteUsername();
            }
        });
    }

    private void deleteUsername(){
        FirebaseData.myRef.child("users_by_tag").child(FirebaseData.currentUser.getCountry().toUpperCase())
                .child(FirebaseData.currentUser.getUserId()).removeValue().
                addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                    deleteLoginAccount();
            }
        });
    }

    private void deleteLoginAccount(){
        FirebaseData.mAuth.getCurrentUser().delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            activity.finishAffinity();
                            activity.startActivity(new Intent(context, BeginActivity.class));
                        }
                    }
                });
    }
}
