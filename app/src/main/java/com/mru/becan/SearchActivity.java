package com.mru.becan;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Distance;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;
import com.google.android.gms.nearby.messages.Strategy;
import com.google.android.gms.nearby.messages.SubscribeOptions;
import com.mru.becan.beacon.Beacon;
import com.mru.becan.tasks.BecanServerTask;
import com.mru.becan.tasks.OnBecanServerCompleted;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements OnBecanServerCompleted {

    private static final String TAG = "SEARCH_ACTIVITY";

    private static final String SEQUENCE_ID = "5ad389f940a1710ccb1a7b8b";

    private MessageListener mMessageListener;

    private Beacon[] beacons;
    private Beacon currentBeacon;

    private TextView clueTextView;
    private TextView statusTextView;
    private Button pingButton;

    private BecanServerTask becanServerTask;

    private View.OnClickListener startPingListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            searchingForBeacon();
        }
    };

    private View.OnClickListener stopPingListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            stopSearching();
        }
    };

    private View.OnClickListener startQuizListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(SearchActivity.this, QuizActivity.class);
            intent.putExtra("beacon", currentBeacon);
            intent.putExtra("beaconArray", beacons);
            startActivity(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        clueTextView = findViewById(R.id.textView_clue);
        statusTextView = findViewById(R.id.textView_status);
        pingButton = findViewById(R.id.button_ping);

        pingButton.setOnClickListener(startPingListener);

        becanServerTask = new BecanServerTask(this,this);

        mMessageListener = new MessageListener() {

            @Override
            public void onFound(Message message) {
                String content = new String(message.getContent());
                Log.i(TAG, "Beacon found: " + content);

                if(currentBeacon != null && currentBeacon.getBeaconId().equals(content)){
                    beaconFound();
                }
            }

            @Override
            public void onLost(Message message) {
                String content = new String(message.getContent());
                Log.d(TAG, "Lost sight of message: " + content);
            }
        };

        Intent intent = getIntent();

        if (intent.getBooleanExtra("victory", false)){
            displayFinishDialog();
        }

        currentBeacon = intent.getParcelableExtra("beacon");
        Parcelable[] parceledArray = intent.getParcelableArrayExtra("beaconArray");
        if (parceledArray != null && currentBeacon != null) {
            beacons = new Beacon[parceledArray.length];
            for (int i = 0; i < parceledArray.length; i++) {
                beacons[i] = (Beacon)parceledArray[i];
            }
            showClue();
        } else {
            getInitialContent();
        }
    }

    private void displayFinishDialog(){
        new AlertDialog.Builder(SearchActivity.this)
                .setTitle("Complete!")
                .setMessage("Congrats! You have found the final clue and finished the Adventure!")
                .setNeutralButton("Menu", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(SearchActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                })
                .show();
    }

    @Override
    public void onStop() {
        unsubscribe();
        super.onStop();
    }

    private void getInitialContent(){
        String url = "sequences/" + SEQUENCE_ID + "/beacons/";
        pingButton.setEnabled(false);
        becanServerTask.executeBecanServerCall(url);
        statusTextView.setText("Fetching clue, please wait...");
    }

    private void showClue(){
        statusTextView.setText("");
        clueTextView.setText(currentBeacon.getClue());
        pingButton.setText("Ping");
        pingButton.setEnabled(true);
    }

    private void searchingForBeacon(){
        subscribe();
        statusTextView.setText("Searching for BeCan...");
        pingButton.setText("Stop Searching");
        pingButton.setOnClickListener(stopPingListener);
    }

    private void beaconFound(){
        unsubscribe();
        pingButton.setOnClickListener(startQuizListener);
        pingButton.setText("Start Quiz");
        statusTextView.setText("Beacon Found!");
        clueTextView.setText(currentBeacon.getDescription());
    }

    private void stopSearching(){
        unsubscribe();
        pingButton.setOnClickListener(startPingListener);
        showClue();
    }

    @Override
    public void taskCompleted(String results) {
        try {
            JSONArray resultArray = new JSONArray(results);
            beacons = new Beacon[resultArray.length()];
            for (int i = 0; i < resultArray.length(); i++) {
                beacons[i] = new Beacon(resultArray.getJSONObject(i));
            }
            currentBeacon = beacons[0];
            showClue();
            Log.i(TAG, results);

        } catch (JSONException e) {
            Log.e(TAG, "Failed to parse result: " + e.getMessage());
        }
    }

    public void subscribe(){
        Log.i(TAG, "Subscribing.");
        SubscribeOptions options = new SubscribeOptions.Builder()
                .setStrategy(Strategy.BLE_ONLY)
                .build();
        Nearby.getMessagesClient(this).subscribe(mMessageListener, options);
    }

    public void unsubscribe(){
        Log.i(TAG, "Unsubscribing.");
        Nearby.getMessagesClient(this).unsubscribe(mMessageListener);
    }
}
