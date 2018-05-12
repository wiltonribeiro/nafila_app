package com.will.nafila.ControlClasses;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.will.nafila.BoundaryClasses.ActivitiesClasses.UserViewActivity;
import com.will.nafila.EntityClasses.User;
import com.will.nafila.R;
import com.will.nafila.StaticData.FirebaseData;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by willrcneto on 01/04/18.
 */

public class ShowUserControl {

    Context context;
    Activity activity;
    LoadingControl loadingControl;
    public static int value;

    public ShowUserControl(Context context) {
        this.context = context;
        this.activity = (Activity) context;
        this.loadingControl = new LoadingControl(context);
    }

    private int checkUsersToShow(){

        if(FirebaseData.currentUser.getFollowersAlreadySeen() == null)
            FirebaseData.currentUser.setFollowersAlreadySeen(new HashMap<String, Object>());

        if(FirebaseData.currentUser.getFollowers() == null || FirebaseData.currentUser.getFollowers().size()==0){
            return -1;
        }else{
            int count = 0;
            for(String key: FirebaseData.currentUser.getFollowers().keySet()){
                if(FirebaseData.currentUser.getFollowersAlreadySeen().containsKey(key))
                    count++;
            }
            if(count==FirebaseData.currentUser.getFollowers().size())
                return -2;
            else
                return (FirebaseData.currentUser.getFollowersAlreadySeen().size()+1)*20;
        }
    }

    public void changeTextButton(){
        Button btnShowUser = activity.findViewById(R.id.btnShowUser);
        value = checkUsersToShow();
        if(value == -1)
            btnShowUser.setText(activity.getResources().getString(R.string.no_one_line));
        else if(value== -2)
            btnShowUser.setText(activity.getResources().getString(R.string.all_seen));
        else
            btnShowUser.setText((activity.getResources().getString(R.string.show_up1)+value+activity.getResources().getString(R.string.show_up2)));
    }

    public void getUserKey(){
        if(value == 20){
            for (String key : FirebaseData.currentUser.getFollowers().keySet()) {
                showUser(key);
                break;
            }
        } else{
            for (String key : FirebaseData.currentUser.getFollowers().keySet()) {
                if(!FirebaseData.currentUser.getFollowersAlreadySeen().containsKey(key)){
                    showUser(key);
                    break;
                }
            }
        }
    }

    private void showUser(String key){
        loadingControl.startLoading();
        registerShowed(key);

        FirebaseData.myRef.child("users").child(key).child("data").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    UserViewActivity.userView = dataSnapshot.getValue(User.class);
                    activity.startActivity(new Intent(context, UserViewActivity.class));
                    loadingControl.finishLoading();
                    Toast.makeText(context, activity.getResources().getString(R.string.this_in_line), Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                    loadingControl.finishLoading();
                    activity.onBackPressed();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void registerShowed(String key){

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date date = new Date();

        FirebaseData.currentUser.getFollowersAlreadySeen().put(key,dateFormat.format(date));
        FirebaseData.myRef.child("users").child(FirebaseData.currentUser.getUserKey()).child("data").child("followersAlreadySeen")
                .child(key).setValue(dateFormat.format(date));

        PointsControl pointsControl = new PointsControl(context);
        pointsControl.removePointsToUser(value);

        changeTextButton();
    }
}
