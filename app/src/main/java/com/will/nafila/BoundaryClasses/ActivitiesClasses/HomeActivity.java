package com.will.nafila.BoundaryClasses.ActivitiesClasses;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.will.nafila.ControlClasses.LoadingControl;
import com.will.nafila.ControlClasses.NotificationControl;
import com.will.nafila.ControlClasses.SearchControl;
import com.will.nafila.EntityClasses.User;
import com.will.nafila.R;
import com.will.nafila.StaticData.FirebaseData;

public class HomeActivity extends AppCompatActivity {

    TextView textUserName, textUserId, listFollowers, listFollowing;
    ImageView imgUserProfile;
    Button btnSearch, btnDoSearch, btnMenu, btnPoints, btnEditProfile;
    SearchControl searchControl;
    EditText inputUser;
    User user;
    Typeface typeface;
    LoadingControl loadingControl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        typeface = Typeface.createFromAsset(getAssets(), "fonts/Norwester.otf");

        loadingControl = new LoadingControl(HomeActivity.this);
        loadingControl.startLoading();

        searchControl = new SearchControl(HomeActivity.this);

        btnSearch = findViewById(R.id.btnSearch);
        btnDoSearch = findViewById(R.id.btnDoSearch);
        btnPoints = findViewById(R.id.btnPoints);
        inputUser = findViewById(R.id.inputUser);
        btnEditProfile = findViewById(R.id.btnEditProfile);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchControl.activateSearchBar();
            }
        });

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, EditProfileActivity.class));
            }
        });

        btnDoSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userId = inputUser.getText().toString();
                if(userId.trim().length()<4){
                    Toast.makeText(HomeActivity.this, getResources().getString(R.string.alert_user_name), Toast.LENGTH_SHORT).show();
                }

                else{
                    //hide keyboard after click
                    InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    searchControl.doSearch(loadingControl,user, userId);
                }
            }
        });

        btnPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, ShowUserActivity.class));
            }
        });

        user = FirebaseData.currentUser;
        textUserName = findViewById(R.id.textUserName);
        textUserId = findViewById(R.id.textUserId);
        listFollowers = findViewById(R.id.listFollowers);
        listFollowing = findViewById(R.id.listFollowing);
        imgUserProfile = findViewById(R.id.imgUserProfile);

        textUserName.setTypeface(typeface);
        textUserId.setTypeface(typeface);

        textUserId.setText(user.getUserId().toLowerCase());

        btnMenu = findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, MenuActivity.class));
            }
        });

        Picasso.with(HomeActivity.this).load(user.getImageUrl()).into(imgUserProfile, new Callback() {
            @Override
            public void onSuccess() {
                loadingControl.finishLoading();
            }

            @Override
            public void onError() {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        int qntdFollowers = 0;
        int qntdFollowing = 0;


        if(user.getFollowers()!=null)
            qntdFollowers = user.getFollowers().size();
        if(user.getFollowing()!=null)
            qntdFollowing = user.getFollowing().size();

        String followersText =  getResources().getString(R.string.in_my_line);
        String followingText =  getResources().getString(R.string.line_where_im);

        textUserName.setText(user.getName());
        Picasso.with(HomeActivity.this).load(user.getImageUrl()).into(imgUserProfile, new Callback() {
            @Override
            public void onSuccess() {
                loadingControl.finishLoading();
            }

            @Override
            public void onError() {

            }
        });

        SpannableString ss1=  new SpannableString(qntdFollowers+" "+followersText);
        ss1.setSpan(new RelativeSizeSpan(2f), 0,Integer.toString(qntdFollowers).length(), 0); // set size
        listFollowers.setTypeface(typeface);
        listFollowers.setText(ss1);

        SpannableString ss2=  new SpannableString(qntdFollowing+" "+followingText);
        ss2.setSpan(new RelativeSizeSpan(2f), 0,Integer.toString(qntdFollowing).length(), 0); // set size
        listFollowing.setTypeface(typeface);
        listFollowing.setText(ss2);

        NotificationControl.loadNotifications(HomeActivity.this);

    }

    @Override
    public void onBackPressed() {
        if(SearchControl.status){
            searchControl.hideSearchBar();
        }
        else
            super.onBackPressed();
    }
}
