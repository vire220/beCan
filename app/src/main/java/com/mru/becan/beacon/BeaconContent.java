package com.mru.becan.beacon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by vire7 on 3/1/2018.
 */

public class BeaconContent {

    public static List<Beacon> ITEMS = new ArrayList<Beacon>();

    public static Map<String, Beacon> ITEM_MAP = new HashMap<String, Beacon>();

    static {
        addItem(new Beacon("Dummy_Beacon_1", "Dummy Beacon 1", "This is some content"));
        addItem(new Beacon("Dummy_Beacon_2", "Dummy Beacon 2", "This is some other content"));
        addItem(new Beacon("Dummy_Beacon_3", "Dummy Beacon 3", "This is some final content"));
    }

    private static void addItem(Beacon item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.getBeaconId(), item);
    }
}
