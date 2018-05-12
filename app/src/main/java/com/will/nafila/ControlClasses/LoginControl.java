package com.will.nafila.ControlClasses;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.will.nafila.BoundaryClasses.ActivitiesClasses.HomeActivity;
import com.will.nafila.EntityClasses.User;
import com.will.nafila.StaticData.FirebaseData;

/**
 * Created by willrcneto on 29/03/18.
 */

public class LoginControl {
    Context context;
    Activity activity;
    LoadingControl loadingControl;

    public LoginControl(Context context) {
        this.context = context;
        this.activity = (Activity) context;
        this.loadingControl = new LoadingControl(context);
    }

    public boolean checkLoggedIn(){
        if(FirebaseData.mAuth.getCurrentUser()!=null){
            loadingControl.startLoading();
            getUser(FirebaseData.mAuth.getCurrentUser().getUid());

            return true;
        }
        else
            return false;
    }

    public void loginUser(String email, String password){
        loadingControl.startLoading();
        FirebaseData.mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    getUser(FirebaseData.mAuth.getCurrentUser().getUid());
                }
                else {
                    loadingControl.finishLoading();
                    Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getUser(String key){
        FirebaseData.myRef.child("users").child(key).child("data").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                FirebaseData.currentUser = dataSnapshot.getValue(User.class);
                activity.finishAffinity();
                activity.startActivity(new Intent(context, HomeActivity.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                loadingControl.finishLoading();
                Toast.makeText(context, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
