package com.mru.becan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mru.becan.beacon.Beacon;
import com.mru.becan.tasks.BecanServerTask;
import com.mru.becan.tasks.OnBecanServerCompleted;

import org.json.JSONException;
import org.json.JSONObject;

public class SearchActivity extends AppCompatActivity implements OnBecanServerCompleted {

    private static final String TAG = "SEARCH_ACTIVITY";

    private int sequenceId;
    private Beacon currentBeacon;

    private TextView clueTextView;
    private TextView statusTextView;
    private ProgressBar pingProgressBar;
    private Button pingButton;

    private BecanServerTask becanServerTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        sequenceId = 1;

        clueTextView = findViewById(R.id.textView_clue);
        statusTextView = findViewById(R.id.textView_status);
        pingProgressBar = findViewById(R.id.progressBar_ping);
        pingButton = findViewById(R.id.button_ping);

        becanServerTask = new BecanServerTask(this,this);

        getInitialContent();
    }

    private void getInitialContent(){
        String url = "/sequence/" + 1 + "/first/";
        pingButton.setEnabled(false);
        becanServerTask.executeBecanServerCall(url);
        statusTextView.setText("Fetching clue, please wait...");
        pingProgressBar.setProgress(50);
    }

    @Override
    public void taskCompleted(String results) {
        try {
            currentBeacon = new Beacon(new JSONObject(results));
            Log.i(TAG, results);

            pingProgressBar.setProgress(100);
            statusTextView.setText("");
            clueTextView.setText(currentBeacon.getClue());
            pingButton.setEnabled(true);
        } catch (JSONException e) {
            Log.e(TAG, "Failed to parse result: " + e.getMessage());
        }
    }
}
