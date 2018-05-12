package com.will.nafila.ControlClasses;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.will.nafila.BoundaryClasses.ActivitiesClasses.HomeActivity;
import com.will.nafila.EntityClasses.User;
import com.will.nafila.R;
import com.will.nafila.StaticData.FirebaseData;

import java.io.ByteArrayOutputStream;

/**
 * Created by willrcneto on 28/03/18.
 */

public class RegisterControl {

    private Context context;
    private Activity activity;
    private LoadingControl loadingControl;

    public RegisterControl(Context context) {
        this.context = context;
        this.activity = (Activity) context;
        this.loadingControl = new LoadingControl(context);
    }

    private UploadTask uploadImage(){
        ImageView imageView = activity.findViewById(R.id.imgUserProfile);

        StorageReference storageReference = FirebaseData.storageRef;

        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, baos);
        byte[] data = baos.toByteArray();

        StorageReference ImagesRef = storageReference.child("images/user/"+FirebaseData.mAuth.getCurrentUser().getUid()+"_profile_image.jpg");
        UploadTask uploadTask = ImagesRef.putBytes(data);

        return uploadTask;
    }

    private void authUser(String email, String password, final User user){
        FirebaseData.mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    uploadImage().addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if(task.isSuccessful()){
                                task.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                        user.setImageUrl(downloadUrl.toString());
                                        user.setUserKey(FirebaseData.mAuth.getCurrentUser().getUid());
                                        user.setPoints(0);
                                        registerUser(user);
                                    }
                                });
                            }else{
                                loadingControl.finishLoading();
                                Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

                else{
                    loadingControl.finishLoading();
                    Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void checkUser(final String email, final String password, final User user){
        loadingControl.startLoading();

        FirebaseData.myRef.child("users_by_tag").child(user.getCountry()).child(user.getUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    loadingControl.finishLoading();
                    Toast.makeText(context, activity.getResources().getString(R.string.alert_user_name4), Toast.LENGTH_LONG).show();
                } else {
                    authUser(email, password, user);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                loadingControl.finishLoading();
                Toast.makeText(context, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void registerUser(final User user){
        FirebaseData.myRef.child("users").child(user.getUserKey()).child("data").setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    registerUserById(user);
                }
                else{
                    Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void registerUserById(final User user){
        FirebaseData.myRef.child("users_by_tag").child(user.getCountry()).child(user.getUserId()).child("key").setValue(user.getUserKey()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    FirebaseData.currentUser = user;
                    activity.finishAffinity();
                    activity.startActivity(new Intent(context, HomeActivity.class));
                }else{
                    Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
