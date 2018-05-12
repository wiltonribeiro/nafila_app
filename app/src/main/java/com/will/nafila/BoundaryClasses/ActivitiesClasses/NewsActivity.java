package com.will.nafila.BoundaryClasses.ActivitiesClasses;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.will.nafila.ControlClasses.AdaptersClasses.PageAdapterNews;
import com.will.nafila.ControlClasses.NotificationControl;
import com.will.nafila.R;
import com.will.nafila.StaticData.FirebaseData;

public class NewsActivity extends AppCompatActivity {

    ListView lvDetail;
    NotificationControl notificationControl;
    RelativeLayout nothing_here;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        lvDetail = findViewById(R.id.listNews);
        notificationControl = new NotificationControl(NewsActivity.this);
        nothing_here = findViewById(R.id.nothing_here);
    }

    @Override
    protected void onResume() {
        super.onResume();
        lvDetail.setAdapter(null);
        lvDetail.requestLayout();

        if(NotificationControl.notifications.size()!=0) {
            nothing_here.setVisibility(View.GONE);
            lvDetail.setAdapter(new PageAdapterNews(NewsActivity.this, NotificationControl.notifications));
            lvDetail.requestLayout();
            FirebaseData.currentUser.setLastSeenNotification(NotificationControl.notifications.get(NotificationControl.notifications.size() - 1).getKey());
            notificationControl.updateLastSeenNotification(NotificationControl.notifications.get(NotificationControl.notifications.size() - 1).getKey());
        }else{
            nothing_here.setVisibility(View.VISIBLE);
        }
    }
}
