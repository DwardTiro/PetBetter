package com.example.owner.petbetter.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.owner.petbetter.R;
import com.example.owner.petbetter.classes.Message;
import com.example.owner.petbetter.classes.Post;
import com.example.owner.petbetter.classes.Topic;
import com.example.owner.petbetter.fragments.FragmentHome;
import com.example.owner.petbetter.fragments.FragmentPosts;
import com.google.android.gms.vision.text.Text;
import com.google.gson.Gson;

public class TopicContentActivity extends AppCompatActivity {

    Topic topicItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_content);


        Toolbar toolbar = (Toolbar) findViewById(R.id.viewPostToolbar);
        setSupportActionBar(toolbar);
        final TextView activityTitle=  (TextView) findViewById(R.id.activity_title);
        activityTitle.setText("View Topic");
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        String jsonMyObject;
        Bundle extras = getIntent().getExtras();
        jsonMyObject = extras.getString("thisTopic");
        topicItem = new Gson().fromJson(jsonMyObject, Topic.class);

        Bundle bundle = new Bundle();
        bundle.putLong("topicId", topicItem.getId());
        FragmentPosts fragment3 = new FragmentPosts();
        fragment3.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.topic_container,fragment3).commit();
    }

    public void viewPostBackButtonClicked(View view){
        finish();
    }
}
