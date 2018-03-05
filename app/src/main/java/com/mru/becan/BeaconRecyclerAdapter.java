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

    final private ListItemClickListener mOnClickListener;

    private List<Beacon> mList;

    public BeaconRecyclerAdapter(List<Beacon> list, ListItemClickListener listener) {
        mList = list;
        mOnClickListener = listener;
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

    class BeaconViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView listItemBeaconView;

        public BeaconViewHolder(View itemView) {
            super(itemView);
            listItemBeaconView = (TextView) itemView.findViewById(R.id.rv_item_beacon);
            itemView.setOnClickListener(this);
        }

        void bind(int listIndex, Beacon beacon) {
            listItemBeaconView.setText(beacon.getName());
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }
}
