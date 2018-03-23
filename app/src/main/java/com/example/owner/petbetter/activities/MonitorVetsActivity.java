package com.example.owner.petbetter.activities;

import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.owner.petbetter.R;

public class MonitorVetsActivity extends AppCompatActivity {

    private Button monitorVetsButton;
    private Button monitorFaciButton;
    private Button monitorTopicsButton;
    private Button monitorPostsButton;
    private int currFragment = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor_vets);

        monitorVetsButton = (Button) findViewById(R.id.monitorVetsButton);
        monitorFaciButton = (Button) findViewById(R.id.monitorFaciButton);
        monitorTopicsButton = (Button) findViewById(R.id.monitorTopicsButton);
        monitorPostsButton = (Button) findViewById(R.id.monitorPostsButton);

        monitorVetsClicked(this.findViewById(android.R.id.content));
    }

    public void monitorVetsClicked(View view){
        currFragment = 1;
        monitorVetsButton.setBackgroundResource(R.color.main_White);
        monitorVetsButton.setTextColor(getResources().getColor(R.color.myrtle_green));
        monitorFaciButton.setBackgroundResource(R.color.medTurquoise);
        monitorFaciButton.setTextColor(getResources().getColor(R.color.colorWhite));
        monitorTopicsButton.setBackgroundResource(R.color.medTurquoise);
        monitorTopicsButton.setTextColor(getResources().getColor(R.color.colorWhite));
        monitorPostsButton.setBackgroundResource(R.color.medTurquoise);
        monitorPostsButton.setTextColor(getResources().getColor(R.color.colorWhite));
        monitorVetsButton.setPaintFlags(monitorVetsButton.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        monitorFaciButton.setPaintFlags(monitorFaciButton.getPaintFlags()&(~Paint.UNDERLINE_TEXT_FLAG));
        monitorTopicsButton.setPaintFlags(monitorTopicsButton.getPaintFlags()&(~Paint.UNDERLINE_TEXT_FLAG));
        monitorPostsButton.setPaintFlags(monitorPostsButton.getPaintFlags()&(~Paint.UNDERLINE_TEXT_FLAG));
    }

    public void monitorFaciClicked(View view){
        currFragment = 2;
        monitorFaciButton.setBackgroundResource(R.color.main_White);
        monitorFaciButton.setTextColor(getResources().getColor(R.color.myrtle_green));
        monitorVetsButton.setBackgroundResource(R.color.medTurquoise);
        monitorVetsButton.setTextColor(getResources().getColor(R.color.colorWhite));
        monitorTopicsButton.setBackgroundResource(R.color.medTurquoise);
        monitorTopicsButton.setTextColor(getResources().getColor(R.color.colorWhite));
        monitorPostsButton.setBackgroundResource(R.color.medTurquoise);
        monitorPostsButton.setTextColor(getResources().getColor(R.color.colorWhite));
        monitorFaciButton.setPaintFlags(monitorFaciButton.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        monitorVetsButton.setPaintFlags(monitorVetsButton.getPaintFlags()&(~Paint.UNDERLINE_TEXT_FLAG));
        monitorTopicsButton.setPaintFlags(monitorTopicsButton.getPaintFlags()&(~Paint.UNDERLINE_TEXT_FLAG));
        monitorPostsButton.setPaintFlags(monitorPostsButton.getPaintFlags()&(~Paint.UNDERLINE_TEXT_FLAG));
    }

    public void monitorTopicsClicked(View view){
        currFragment = 3;
        monitorTopicsButton.setBackgroundResource(R.color.main_White);
        monitorTopicsButton.setTextColor(getResources().getColor(R.color.myrtle_green));
        monitorFaciButton.setBackgroundResource(R.color.medTurquoise);
        monitorFaciButton.setTextColor(getResources().getColor(R.color.colorWhite));
        monitorVetsButton.setBackgroundResource(R.color.medTurquoise);
        monitorVetsButton.setTextColor(getResources().getColor(R.color.colorWhite));
        monitorPostsButton.setBackgroundResource(R.color.medTurquoise);
        monitorPostsButton.setTextColor(getResources().getColor(R.color.colorWhite));
        monitorTopicsButton.setPaintFlags(monitorTopicsButton.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        monitorFaciButton.setPaintFlags(monitorFaciButton.getPaintFlags()&(~Paint.UNDERLINE_TEXT_FLAG));
        monitorVetsButton.setPaintFlags(monitorVetsButton.getPaintFlags()&(~Paint.UNDERLINE_TEXT_FLAG));
        monitorPostsButton.setPaintFlags(monitorPostsButton.getPaintFlags()&(~Paint.UNDERLINE_TEXT_FLAG));
    }

    public void monitorPostsClicked(View view){
        currFragment = 4;
        monitorPostsButton.setBackgroundResource(R.color.main_White);
        monitorPostsButton.setTextColor(getResources().getColor(R.color.myrtle_green));
        monitorFaciButton.setBackgroundResource(R.color.medTurquoise);
        monitorFaciButton.setTextColor(getResources().getColor(R.color.colorWhite));
        monitorTopicsButton.setBackgroundResource(R.color.medTurquoise);
        monitorTopicsButton.setTextColor(getResources().getColor(R.color.colorWhite));
        monitorVetsButton.setBackgroundResource(R.color.medTurquoise);
        monitorVetsButton.setTextColor(getResources().getColor(R.color.colorWhite));
        monitorPostsButton.setPaintFlags(monitorPostsButton.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        monitorFaciButton.setPaintFlags(monitorFaciButton.getPaintFlags()&(~Paint.UNDERLINE_TEXT_FLAG));
        monitorTopicsButton.setPaintFlags(monitorTopicsButton.getPaintFlags()&(~Paint.UNDERLINE_TEXT_FLAG));
        monitorVetsButton.setPaintFlags(monitorVetsButton.getPaintFlags()&(~Paint.UNDERLINE_TEXT_FLAG));
    }

}
