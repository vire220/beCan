package com.mru.becan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.mru.becan.beacon.Beacon;
import com.mru.becan.fragments.BeaconFragment;
import com.mru.becan.fragments.MyBeaconRecyclerViewAdapter;

import java.util.List;

public class SearchActivity extends AppCompatActivity implements BeaconFragment.OnListFragmentInteractionListener {

    private static final String TAG = "SEARCH_ACTIVITY";

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private List<Beacon> beaconList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mRecyclerView = (RecyclerView) findViewById(R.id.foundBeaconsList);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MyBeaconRecyclerViewAdapter(beaconList, this);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onListFragmentInteraction(Beacon item) {

    }
}
