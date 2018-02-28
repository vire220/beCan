package com.mru.becan;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import com.google.android.gms.nearby.*;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;
import com.google.android.gms.nearby.messages.MessagesClient;
import com.google.android.gms.nearby.messages.MessagesOptions;
import com.google.android.gms.nearby.messages.NearbyPermissions;
import com.mru.becan.tasks.BecanServerTask;
import com.mru.becan.tasks.OnBecanServerCompleted;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, OnBecanServerCompleted{

    private static final String TAG = "MAIN_ACTIVITY";
    public static final int MY_PERMISSIONS_REQUEST_INTERNET = 1;

    private Message mMessage;
    private MessagesClient mMessagesClient;
    private MessageListener mMessageListener;

    private EditText apiUrl;
    private Button btnSearch;
    private TextView apiResult;

    private BecanServerTask becanServerTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        apiUrl = (EditText) findViewById(R.id.apiUrl);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        apiResult = (TextView) findViewById(R.id.apiResult);

        btnSearch.setOnClickListener(this);

        becanServerTask = new BecanServerTask(this, this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMessagesClient = Nearby.getMessagesClient(this, new MessagesOptions.Builder()
                    .setPermissions(NearbyPermissions.BLE)
                    .build());
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.INTERNET)) {

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, MY_PERMISSIONS_REQUEST_INTERNET);
            }
        }

        mMessageListener = new MessageListener() {
            @Override
            public void onFound(Message message) {
                Log.d(TAG, "Found message: " + new String(message.getContent()));
            }

            @Override
            public void onLost(Message message) {
                Log.d(TAG, "Lost sight of message: " + new String(message.getContent()));
            }
        };

        mMessage = new Message("Hello World".getBytes());
    }

    @Override
    public void onStart() {
        super.onStart();
        Nearby.getMessagesClient(this).publish(mMessage);
        Nearby.getMessagesClient(this).subscribe(mMessageListener);
    }

    @Override
    public void onStop() {
        Nearby.getMessagesClient(this).unpublish(mMessage);
        Nearby.getMessagesClient(this).unsubscribe(mMessageListener);

        super.onStop();
    }

    @Override
    public void taskCompleted(String results) {
        apiResult.setText(results);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_INTERNET: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                }
                return;
            }
        }
    }

    @Override
    public void onClick(View view) {
        becanServerTask.executeBecanServerCall(apiUrl.getText().toString());
        apiUrl.setText("");
        apiResult.setText("Fetching...");
    }
}