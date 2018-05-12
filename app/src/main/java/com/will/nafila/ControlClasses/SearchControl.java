package com.will.nafila.ControlClasses;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.will.nafila.BoundaryClasses.ActivitiesClasses.UserViewActivity;
import com.will.nafila.EntityClasses.User;
import com.will.nafila.R;
import com.will.nafila.StaticData.FirebaseData;

/**
 * Created by willrcneto on 29/03/18.
 */

public class SearchControl{

    private Context context;
    private Activity activity;
    private RelativeLayout layoutSearch;
    private LinearLayout cornerSearch;
    public static boolean status = false;

    public SearchControl(Context context) {
        this.context = context;
        this.activity = (Activity) context;
        this.layoutSearch = activity.findViewById(R.id.layoutSearch);
        this.cornerSearch = activity.findViewById(R.id.cornerSearch);
    }

    public void activateSearchBar(){
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.zoom_in);
        cornerSearch.setVisibility(View.INVISIBLE);
        layoutSearch.setVisibility(View.VISIBLE);
        cornerSearch.setVisibility(View.VISIBLE);
        cornerSearch.setAnimation(animation);
        status = true;
    }

    public void hideSearchBar(){
        layoutSearch.setVisibility(View.GONE);
        status = false;
    }

    public void doSearch(final LoadingControl loadingControl, User user, String user_uid){
        loadingControl.startLoading();
        FirebaseData.myRef.child("users_by_tag").child(user.getCountry().toUpperCase()).child(user_uid.trim().toUpperCase()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    activity.findViewById(R.id.textErroSearch).setVisibility(View.GONE);
                    String key = dataSnapshot.child("key").getValue(String.class);
                    getUser(key, loadingControl);
                }

                else{
                    loadingControl.finishLoading();
                    activity.findViewById(R.id.textErroSearch).setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                loadingControl.finishLoading();
                Toast.makeText(context, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getUser(String key, final LoadingControl loadingControl){
        FirebaseData.myRef.child("users").child(key).child("data").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    UserViewActivity.userView = dataSnapshot.getValue(User.class);
                    activity.startActivity(new Intent(context, UserViewActivity.class));
                    SearchControl.this.hideSearchBar();
                    loadingControl.finishLoading();
                }
                else{
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
}
