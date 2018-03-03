package com.mru.becan;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Distance;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;
import com.google.android.gms.nearby.messages.MessagesClient;
import com.google.android.gms.nearby.messages.MessagesOptions;
import com.google.android.gms.nearby.messages.NearbyPermissions;
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

    private List<Beacon> beaconList = new ArrayList<>();
    private BeaconRecyclerAdapter mAdapter;
    private RecyclerView mBeaconList;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private MessageListener mMessageListener;
    private MessagesClient mMessagesClient;

    private BecanServerTask becanServerTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mBeaconList = findViewById(R.id.rv_foundBeacons);

        mSwipeRefreshLayout = findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i(TAG, "Refreshing");
                beaconList.clear();
                mAdapter.notifyDataSetChanged();

                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mBeaconList.setLayoutManager(layoutManager);

        mBeaconList.setHasFixedSize(true);

        mAdapter = new BeaconRecyclerAdapter(beaconList);

        mBeaconList.setAdapter(mAdapter);

        becanServerTask = new BecanServerTask(this, this);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMessagesClient = Nearby.getMessagesClient(this, new MessagesOptions.Builder()
                    .setPermissions(NearbyPermissions.BLE)
                    .build());
        }

        mMessageListener = new MessageListener() {

            @Override
            public void onFound(Message message) {
                String content = new String(message.getContent());
                Log.i(TAG, "Beacon found: " + content);
                boolean found = false;
                for(Beacon beacon : beaconList) {
                    if(beacon.getBeaconId().equals(content)){
                        Log.i(TAG, "Beacon exists: " + content);
                        beacon.setInRange(true);
                        found = true;
                        break;
                    }
                }

                if(!found) {
                    becanServerTask.executeBecanServerCall("beacons/" + content);
                }
            }

            @Override
            public void onLost(Message message) {
                String content = new String(message.getContent());
                Log.d(TAG, "Lost sight of message: " + content);

                Beacon lostBeacon = null;
                for(Beacon beacon : beaconList) {
                    if(beacon.getBeaconId().equals(content)){
                        lostBeacon = beacon;
                        break;
                    }
                }

                if(lostBeacon != null) {
                    lostBeacon.setInRange(false);

                    Handler handler = new Handler();
                    int delay = 10000;

                    Log.d(TAG, "Waiting 10 seconds for beacon " + content);

                    final Beacon finalLostBeacon = lostBeacon;
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (!finalLostBeacon.isInRange()) {
                                Log.d(TAG, "Beacon not found after waiting: " + finalLostBeacon.getName());
                                beaconList.remove(finalLostBeacon);
                                mAdapter.notifyDataSetChanged();
                            }
                        }
                    }, delay);
                }
            }

            @Override
            public void onDistanceChanged(Message message, Distance distance) {
                String content = new String(message.getContent());
                Log.d(TAG, "Distance to beacon " + content + " changed: " + distance.getMeters() + "m, Accuracy: " + distance.getAccuracy());

                Beacon foundBeacon = null;
                for(Beacon beacon : beaconList) {
                    if(beacon.getName().equals(content)){
                        foundBeacon = beacon;
                        break;
                    }
                }

                if(foundBeacon != null) {
                    foundBeacon.setDistance(distance);
                    Collections.sort(beaconList);
                    mAdapter.notifyDataSetChanged();
                    Log.d(TAG, "Updated distance on beacon: " + content);
                }
            }
        };

    }

    @Override
    public void taskCompleted(String results) {
        try {
            Log.i(TAG, results);
            Beacon newBeacon = new Beacon(new JSONObject(results));
            beaconList.add(newBeacon);
            Log.d(TAG, "Added beacon: " + newBeacon.getName());
            mAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            Log.e(TAG, e.toString());
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "Subscribing.");
        SubscribeOptions options = new SubscribeOptions.Builder()
                .setStrategy(Strategy.BLE_ONLY)
                .build();
        Nearby.getMessagesClient(this).subscribe(mMessageListener, options);
    }

    @Override
    public void onStop() {
        Log.i(TAG, "Unsubscribing.");
        Nearby.getMessagesClient(this).unsubscribe(mMessageListener);
        super.onStop();
    }
}

