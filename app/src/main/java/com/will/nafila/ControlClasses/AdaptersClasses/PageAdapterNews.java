package com.will.nafila.ControlClasses.AdaptersClasses;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.will.nafila.BoundaryClasses.ActivitiesClasses.ShowUserActivity;
import com.will.nafila.R;

import java.util.ArrayList;
import java.util.List;


public class PageAdapterNews extends BaseAdapter {
    List<com.will.nafila.EntityClasses.Notification> myList = new ArrayList<>();
    LayoutInflater inflater;
    Context context;
    private FirebaseAuth mAuth;

    public PageAdapterNews(Context context, List<com.will.nafila.EntityClasses.Notification> myList) {
        this.myList = myList;
        mAuth = FirebaseAuth.getInstance();
        this.context = context;
        inflater = LayoutInflater.from(this.context);
    }


    @Override
    public int getCount() {
        return myList.size();
    }

    @Override
    public com.will.nafila.EntityClasses.Notification getItem(int position) {
        return myList.get(getCount() - position - 1);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public   View getView(int position, View convertView, ViewGroup parent) {
        final MyViewHolder mViewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_news, parent, false);
            mViewHolder = new MyViewHolder(convertView);
            convertView.setTag(mViewHolder);

        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }


        final com.will.nafila.EntityClasses.Notification currentListData = getItem(position);
        mViewHolder.tvTitle.setText(currentListData.getTitle());
        mViewHolder.tvDescription.setText(currentListData.getText());
        mViewHolder.lnNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentListData.getType() != null){
                    if(currentListData.getType().equals("new_follower")){
                        context.startActivity(new Intent(context, ShowUserActivity.class));
                    }
                }

            }
        });

        return convertView;
    }

    private class MyViewHolder {
        TextView tvTitle, tvDescription;
        LinearLayout lnNews;
        public MyViewHolder(View item) {
            tvDescription = item.findViewById(R.id.textDescription);
            tvTitle = item.findViewById(R.id.textTitle);
            lnNews = item.findViewById(R.id.news);
        }
    }
}
