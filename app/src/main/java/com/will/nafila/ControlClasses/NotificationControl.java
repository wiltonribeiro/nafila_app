package com.will.nafila.ControlClasses;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.will.nafila.EntityClasses.Notification;
import com.will.nafila.EntityClasses.User;
import com.will.nafila.R;
import com.will.nafila.StaticData.FirebaseData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by willrcneto on 31/03/18.
 */

public class NotificationControl {
    Context context;
    Activity activity;
    public static List<Notification> notifications = new ArrayList<>();

    public NotificationControl(Context context) {
        this.context = context;
        this.activity = (Activity) context;
    }

    public void addNotification(User user, Notification notification){
        String key = FirebaseData.myRef.push().getKey();
        notification.setKey(key);
        FirebaseData.myRef.child("users").child(user.getUserKey()).child("notifications").child(key).setValue(notification);
    }

    public static void loadNotifications(final Activity activity){

        notifications.clear();

        FirebaseData.myRef.child("users").child(FirebaseData.currentUser.getUserKey()).child("notifications").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                        Notification notification = postSnapshot.getValue(Notification.class);
                        notifications.add(notification);
                    }

                    if(notifications.size()==dataSnapshot.getChildrenCount()){
                        if(checkNotifications())
                            activity.findViewById(R.id.alertNews).setVisibility(View.VISIBLE);
                        else
                            activity.findViewById(R.id.alertNews).setVisibility(View.GONE);
                    }
                }else{
                    activity.findViewById(R.id.alertNews).setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static boolean checkNotifications(){
        if(notifications.size()==0)
            return false;
        else{
            if(notifications.get(notifications.size()-1).getKey().equals(FirebaseData.currentUser.getLastSeenNotification()))
                return false;
            else
                return true;
        }
    }

    public void updateLastSeenNotification(String lastNotification){
        FirebaseData.myRef.child("users").child(FirebaseData.currentUser.getUserKey()).child("data").child("lastSeenNotification").setValue(lastNotification);
    }

}
