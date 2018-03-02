package com.mru.becan.beacon;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by vire7 on 3/1/2018.
 */

public class Beacon {

    private String beaconId;
    private String name;
    private String description;
    private String content;
    private boolean enabled;
    private double lat;
    private double lon;
    private String locationDescription;
    private int points;

    public Beacon(JSONObject beacon) throws JSONException {
        this.beaconId = beacon.getString("beaconId");
        this.name = beacon.getString("name");
        this.description = beacon.getString("description");
        this.content = beacon.getString("content");
        this.enabled = beacon.getBoolean("enabled");
        this.lat = beacon.getDouble("lat");
        this.lon = beacon.getDouble("lon");
        this.locationDescription = beacon.getString("locationDescription");
        this.points = beacon.getInt("points");
    }

    public Beacon(String beaconId, String name, String content) {
        this.beaconId = beaconId;
        this.name = name;
        this.content = content;
    }

    public String getBeaconId() {
        return beaconId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getContent() {
        return content;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public String getLocationDescription() {
        return locationDescription;
    }

    public int getPoints() {
        return points;
    }
}
