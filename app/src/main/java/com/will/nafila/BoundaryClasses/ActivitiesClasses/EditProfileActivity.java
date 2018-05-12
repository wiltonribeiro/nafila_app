package com.will.nafila.BoundaryClasses.ActivitiesClasses;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.will.nafila.ControlClasses.EditProfileControl;
import com.will.nafila.ControlClasses.LoadingControl;
import com.will.nafila.R;
import com.will.nafila.StaticData.FirebaseData;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;

public class EditProfileActivity extends AppCompatActivity {

    EditText inputName;
    Button btnUpdate, btnSelectImage;
    ImageView imgProfileUser;
    boolean imageChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        inputName = findViewById(R.id.inputName);
        btnUpdate = findViewById(R.id.btnUpdateProfile);
        btnSelectImage = findViewById(R.id.btnSelectImage);

        imgProfileUser = findViewById(R.id.imgUserProfile);

        final LoadingControl loadingControl = new LoadingControl(EditProfileActivity.this);
        loadingControl.startLoading();

        Picasso.with(EditProfileActivity.this).load(FirebaseData.currentUser.getImageUrl()).into(imgProfileUser, new Callback() {
            @Override
            public void onSuccess() {
                loadingControl.finishLoading();
            }

            @Override
            public void onError() {

            }
        });

        inputName.setText(FirebaseData.currentUser.getName());

        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = inputName.getText().toString();
                if(name.trim().length()<5)
                    Toast.makeText(EditProfileActivity.this, getResources().getString(R.string.alert_name), Toast.LENGTH_SHORT).show();
                else if(!FirebaseData.currentUser.getName().trim().equals(name.trim()) || imageChanged){
                    FirebaseData.currentUser.setName(name);
                    EditProfileControl editProfileControl = new EditProfileControl(EditProfileActivity.this);
                    editProfileControl.updateUser(imageChanged);
                } else
                    Toast.makeText(EditProfileActivity.this, getResources().getString(R.string.no_change), Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {

                Uri selectedImageURI = data.getData();
                if (Build.VERSION.SDK_INT < 19){

                    String selectedImagePath = getPath(selectedImageURI);
                    Bitmap bitmap = BitmapFactory.decodeFile(selectedImagePath);
                    imageChanged = true;
                    imgProfileUser.setImageBitmap(bitmap);

                } else {

                    ParcelFileDescriptor parcelFileDescriptor;
                    try {
                        parcelFileDescriptor = getContentResolver().openFileDescriptor(selectedImageURI, "r");
                        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                        Bitmap bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                        parcelFileDescriptor.close();
                        imageChanged = true;
                        imgProfileUser.setImageBitmap(bitmap);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }

    public String getPath(Uri uri) {
        if (uri == null) {
            return null;
        }
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        return uri.getPath();
    }
}
