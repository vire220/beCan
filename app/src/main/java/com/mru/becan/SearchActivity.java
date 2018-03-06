package com.mru.becan;

import android.content.Intent;
import android.os.Handler;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements OnBecanServerCompleted {

    private static final String TAG = "SEARCH_ACTIVITY";

    private static final String SEQUENCE_ID = "5a9dd312b6c4ec12881f13ca";

    private MessageListener mMessageListener;

    private int sequenceId;
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
            showClue();
        }
    };

    private View.OnClickListener startQuizistener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(SearchActivity.this, QuizActivity.class);
            startActivity(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        currentBeacon = null;

        clueTextView = findViewById(R.id.textView_clue);
        statusTextView = findViewById(R.id.textView_status);
        pingButton = findViewById(R.id.button_ping);

        pingButton.setOnClickListener(startPingListener);

        becanServerTask = new BecanServerTask(this,this);

        getInitialContent();

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
    }

    @Override
    public void onStop() {
        unsubscribe();
        super.onStop();
    }

    private void getInitialContent(){
        String url = "/sequences/" + SEQUENCE_ID + "/first/";
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
        pingButton.setOnClickListener(startQuizistener);
        pingButton.setText("Start Quiz");
        statusTextView.setText("Beacon Found!");
    }

    @Override
    public void taskCompleted(String results) {
        try {
            currentBeacon = new Beacon(new JSONObject(results));
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
