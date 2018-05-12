package com.will.nafila.BoundaryClasses.ActivitiesClasses;

import android.content.Context;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.will.nafila.BoundaryClasses.JSONUse;
import com.will.nafila.ControlClasses.RegisterControl;
import com.will.nafila.EntityClasses.User;
import com.will.nafila.R;
import com.will.nafila.StaticData.FirebaseData;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;

public class RegisterActivity extends AppCompatActivity {

    RegisterControl registerControl;
    EditText inputName, inputUser, inputPassword, inputConfirmPassword, inputEmail;
    Button btnRegister, btnSelectImage;
    ImageView imgProfileUser;
    Spinner spinnerCountry;
    boolean imageChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerControl = new RegisterControl(RegisterActivity.this);

        inputName = findViewById(R.id.inputName);
        inputUser = findViewById(R.id.inputUser);
        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        inputConfirmPassword = findViewById(R.id.inputConfirmPassword);

        btnRegister = findViewById(R.id.btnRegister);
        btnSelectImage = findViewById(R.id.btnSelectImage);

        imgProfileUser = findViewById(R.id.imgUserProfile);

        spinnerCountry = findViewById(R.id.spinnerCountry);

        JSONUse jsonUse = new JSONUse(RegisterActivity.this);
        jsonUse.loadCountries(spinnerCountry);

        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user_id = inputUser.getText().toString();
                String name = inputName.getText().toString();
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString();
                String confirmPassword = inputConfirmPassword.getText().toString();

                if(email.trim().length()<=6)
                    Toast.makeText(RegisterActivity.this, getResources().getString(R.string.alert_email), Toast.LENGTH_SHORT).show();
                else if (password.trim().length()<6)
                    Toast.makeText(RegisterActivity.this, getResources().getString(R.string.alert_password), Toast.LENGTH_SHORT).show();
                else if(name.trim().length()<5)
                    Toast.makeText(RegisterActivity.this, getResources().getString(R.string.alert_name), Toast.LENGTH_SHORT).show();
                else if(user_id.trim().length()<4)
                    Toast.makeText(RegisterActivity.this, getResources().getString(R.string.alert_user_name2), Toast.LENGTH_SHORT).show();
                else if(user_id.trim().contains(" ") || user_id.trim().contains("\""))
                    Toast.makeText(RegisterActivity.this, getResources().getString(R.string.alert_user_name3) , Toast.LENGTH_SHORT).show();
                else if(!password.equals(confirmPassword))
                    Toast.makeText(RegisterActivity.this, getResources().getString(R.string.alert_confirm_password), Toast.LENGTH_SHORT).show();
                else if(!imageChanged)
                    Toast.makeText(RegisterActivity.this, getResources().getString(R.string.alert_image), Toast.LENGTH_SHORT).show();
                else if(spinnerCountry.getSelectedItemPosition()<=0)
                    Toast.makeText(RegisterActivity.this, getResources().getString(R.string.alert_country), Toast.LENGTH_SHORT).show();
                else{
                    FirebaseData.mAuth.signOut();
                    User user = new User();
                    user.setCountry(spinnerCountry.getSelectedItem().toString().toUpperCase());
                    user.setUserId(user_id.trim().toUpperCase());
                    user.setName(name);
                    user.setEmail(email);

                    //hide keyboard after click
                    InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);


                    registerControl.checkUser(email, password, user);
                }
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
