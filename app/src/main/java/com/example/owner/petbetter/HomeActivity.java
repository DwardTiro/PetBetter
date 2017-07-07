package com.example.owner.petbetter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {

    private Button btnMaps;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        System.out.println("Im a pet owner.");
        btnMaps = (Button) findViewById(R.id.btnMaps);
    }

    public void toMaps(View v){
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }
}
