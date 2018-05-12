package com.will.nafila.ControlClasses;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.will.nafila.R;
import com.will.nafila.StaticData.FirebaseData;

import java.io.ByteArrayOutputStream;

/**
 * Created by willrcneto on 02/04/18.
 */

public class EditProfileControl {
    Context context;
    Activity activity;
    LoadingControl loadingControl;

    public EditProfileControl(Context context) {
        this.context = context;
        this.activity = (Activity) context;
        this.loadingControl = new LoadingControl(context);
    }

    public void updateUser(boolean imageChanged){
        loadingControl.startLoading();
        if(imageChanged){
            uploadImage().addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if(task.isSuccessful()){
                        task.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Uri imageUrl = taskSnapshot.getDownloadUrl();
                                FirebaseData.currentUser.setImageUrl(imageUrl.toString());
                                registerUser();
                            }
                        });
                    }
                    else{
                        Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else{
            registerUser();
        }
    }

    private void registerUser(){
        FirebaseData.myRef.child("users").child(FirebaseData.currentUser.getUserKey()).child("data").setValue(FirebaseData.currentUser).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    loadingControl.finishLoading();
                    activity.finish();
                }
                else
                    Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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

}
