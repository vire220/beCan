package com.mru.becan;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mru.becan.beacon.Beacon;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by vire7 on 3/1/2018.
 */

public class BeaconRecyclerAdapter extends RecyclerView.Adapter<BeaconRecyclerAdapter.BeaconViewHolder> {

    private static final String TAG = BeaconRecyclerAdapter.class.getSimpleName();

    private List<Beacon> mList;

    public BeaconRecyclerAdapter(List<Beacon> list) {
        mList = list;
    }

    @Override
    public BeaconViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.beacon_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        BeaconViewHolder viewHolder = new BeaconViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(BeaconViewHolder holder, int position) {
        holder.bind(position, mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class BeaconViewHolder extends RecyclerView.ViewHolder {
        TextView listItemBeaconView;

        public BeaconViewHolder(View itemView) {
            super(itemView);
            listItemBeaconView = (TextView) itemView.findViewById(R.id.rv_item_beacon);
        }

        void bind(int listIndex, Beacon beacon) {
            listItemBeaconView.setText(beacon.getName());
        }
    }
}
