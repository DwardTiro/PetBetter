package com.example.owner.petbetter.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.owner.petbetter.R;
import com.example.owner.petbetter.classes.Message;
import com.example.owner.petbetter.classes.Post;
import com.example.owner.petbetter.classes.Topic;
import com.example.owner.petbetter.fragments.FragmentHome;
import com.example.owner.petbetter.fragments.FragmentPosts;
import com.google.gson.Gson;

public class TopicContentActivity extends AppCompatActivity {

    Topic topicItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_content);


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
}
