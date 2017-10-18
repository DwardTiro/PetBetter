package com.example.owner.petbetter.activities;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.owner.petbetter.R;
import com.example.owner.petbetter.fragments.FragmentBookmarkListing;
import com.example.owner.petbetter.fragments.FragmentFacilityListing;
import com.example.owner.petbetter.fragments.FragmentPetClinicListing;

public class BookmarksActivity extends AppCompatActivity {

    private ImageButton btnBookmarks;
    private ImageButton btnFaci;
    private FrameLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarks);

        Toolbar toolbar = (Toolbar) findViewById(R.id.viewPostToolbar);
        setSupportActionBar(toolbar);
        final TextView activityTitle = (TextView) findViewById(R.id.activity_title);
        activityTitle.setText("Bookmarks");
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        btnBookmarks = (ImageButton) findViewById(R.id.bookmarkMapsButton);
        btnFaci = (ImageButton) findViewById(R.id.faciMapsButton);
        container = (FrameLayout) findViewById(R.id.bookmark_container);

        btnBookmarks.setBackgroundColor(Color.WHITE);
        btnBookmarks.setImageResource(R.mipmap.ic_bookmark_border_black_24dp);
        btnFaci.setBackgroundResource(R.color.main_Color);
        btnFaci.setImageResource(R.mipmap.ic_pets_white_24dp);
        FragmentBookmarkListing fragment = new FragmentBookmarkListing();
        getSupportFragmentManager().beginTransaction().add(R.id.bookmark_container,fragment).commit();

        btnBookmarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Fragment 1");
                btnBookmarks.setBackgroundColor(Color.WHITE);
                btnBookmarks.setImageResource(R.mipmap.ic_bookmark_border_black_24dp);
                btnFaci.setBackgroundResource(R.color.main_Color);
                btnFaci.setImageResource(R.mipmap.ic_pets_white_24dp);
                container.removeAllViews();
                FragmentBookmarkListing fragment1 = new FragmentBookmarkListing();
                getSupportFragmentManager().beginTransaction().add(R.id.bookmark_container,fragment1).commit();
            }
        });
        btnFaci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Fragment 2");
                btnFaci.setBackgroundColor(Color.WHITE);
                btnFaci.setImageResource(R.mipmap.ic_pets_black_24dp);
                btnBookmarks.setBackgroundResource(R.color.main_Color);
                btnBookmarks.setImageResource(R.mipmap.ic_bookmark_border_white_24dp);
                container.removeAllViews();
                FragmentFacilityListing fragment = new FragmentFacilityListing();
                getSupportFragmentManager().beginTransaction().add(R.id.bookmark_container,fragment).commit();
            }
        });
    }

    public void viewPostBackButtonClicked(View view){
        finish();
    }
    /*
    public void bookmarkClicked(View v){
        System.out.println("We did this right I guess");
        FragmentBookmarkListing fragment = new FragmentBookmarkListing();
        getSupportFragmentManager().beginTransaction().add(R.id.bookmark_container,fragment).commit();


    }
    public void faciClicked(View v){
        FragmentFacilityListing fragment = new FragmentFacilityListing();

        getSupportFragmentManager().beginTransaction().add(R.id.bookmark_container,fragment).commit();
        //change frame_container
    }
    */
}
