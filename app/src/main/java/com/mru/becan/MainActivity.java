package com.mru.becan;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity{

    private static final String TAG = "MAIN_ACTIVITY";
    public static final int MY_PERMISSIONS_REQUEST_INTERNET = 1;

    private Button startSequenceButton;
    private Button startExploreButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, MY_PERMISSIONS_REQUEST_INTERNET);
        }


        startSequenceButton = (Button) findViewById(R.id.start_sequence_button);
        startExploreButton = (Button) findViewById(R.id.start_explore_button);

        startSequenceButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });

        startExploreButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, GeneralSearchActivity.class);
                startActivity(intent);
            }
        });
    }
}