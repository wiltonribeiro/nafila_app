package com.will.nafila.BoundaryClasses.ActivitiesClasses;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.will.nafila.ControlClasses.ShowUserControl;
import com.will.nafila.R;
import com.will.nafila.StaticData.FirebaseData;

public class ShowUserActivity extends AppCompatActivity {

    Button btnShow;
    ShowUserControl showUserControl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_user);
        showUserControl = new ShowUserControl(ShowUserActivity.this);

        btnShow = findViewById(R.id.btnShowUser);

    }

    @Override
    protected void onResume() {
        super.onResume();
        showUserControl.changeTextButton();
        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ShowUserControl.value==-1)
                    Toast.makeText(ShowUserActivity.this, getResources().getString(R.string.alert_noone_line), Toast.LENGTH_SHORT).show();
                else if(ShowUserControl.value==-2)
                    Toast.makeText(ShowUserActivity.this, getResources().getString(R.string.all_already_seen), Toast.LENGTH_SHORT).show();
                else {
                    if(ShowUserControl.value> FirebaseData.currentUser.getPoints())
                        Toast.makeText(ShowUserActivity.this, getResources().getString(R.string.miss_points), Toast.LENGTH_SHORT).show();
                    else showUserControl.getUserKey();
                }
            }
        });
    }
}
