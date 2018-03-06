package com.mru.becan.beacon;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.android.gms.nearby.messages.Distance;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class Beacon implements Comparable<Beacon>, Parcelable {

    private String beaconId;
    private String name;
    private String description;
    private String content;
    private boolean enabled;
    private double lat;
    private double lon;
    private String locationDescription;
    private int points;
    private String clue;

    private Question[] questions;

    private Distance distance;

    private boolean inRange;

    public Beacon(JSONObject beacon) throws JSONException {
        this.beaconId = beacon.getString("beaconId");
        this.name = beacon.getString("name");

        this.description = beacon.getString("description");
        this.content = beacon.getString("content");
        this.clue = beacon.getString("clue");
        this.enabled = beacon.getBoolean("enabled");

        JSONObject location = beacon.getJSONObject("location");

        this.lat = location.getDouble("lat");
        this.lon = location.getDouble("lon");
        this.locationDescription = location.getString("description");
        this.points = beacon.getInt("points");

        JSONArray questions = beacon.getJSONArray("quiz");

        this.questions = new Question[questions.length()];

        for(int i = 0; i < this.questions.length; i++){
            this.questions[i] = new Question(questions.getJSONObject(i));
        }

        this.distance = null;

        this.inRange = true;
    }

    public Beacon(String beaconId, String name, String content) {
        this.beaconId = beaconId;
        this.name = name;
        this.content = content;
    }

    public Question[] getQuestions() {
        return questions;
    }

    public Beacon(Parcel parcel){
        beaconId = parcel.readString();
        name = parcel.readString();
        description = parcel.readString();
        content = parcel.readString();
        enabled = parcel.readByte() != 0;
        lat = parcel.readDouble();
        lon = parcel.readDouble();
        locationDescription = parcel.readString();
        points = parcel.readInt();
        clue = parcel.readString();
        Parcelable[] parcelableArray = parcel.readParcelableArray(Question.class.getClassLoader());
        questions = Arrays.copyOf(parcelableArray, parcelableArray.length, Question[].class);

    }

    public boolean isInRange(){ return inRange; }

    public void setInRange(boolean inRange){ this.inRange = inRange; }

    public void setDistance(Distance distance){
        this.distance = distance;
    }

    public Distance getDistance(){
        return this.distance;
    }

    public String getBeaconId() {
        return beaconId;
    }

    public String getName() {
        return name;
    }

    public String getClue() { return this.clue; }

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

    @Override
    public int compareTo(@NonNull Beacon o) {
        double d1 = Double.parseDouble(String.format("%.2f", this.distance.getMeters()));
        double d2 = Double.parseDouble(String.format("%.2f", o.distance.getMeters()));

        double comp = d1 - d2;

        if (comp < 0.5 && comp > -0.5) {
            return 0;
        } else if (comp > 0.5) {
            return 1;
        } else {
            return -1;
        }
    }

    public static final Parcelable.Creator<Beacon> CREATOR = new Parcelable.Creator<Beacon>() {

        @Override
        public Beacon createFromParcel(Parcel parcel) {
            return new Beacon(parcel);
        }

        @Override
        public Beacon[] newArray(int i) {
            return new Beacon[0];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
         parcel.writeString(beaconId);
         parcel.writeString(name);
         parcel.writeString(description);
         parcel.writeString(content);
         parcel.writeByte((byte) (enabled ? 1 : 0));
         parcel.writeDouble(lat);
         parcel.writeDouble(lon);
         parcel.writeString(locationDescription);
         parcel.writeInt(points);
         parcel.writeString(clue);
         parcel.writeParcelableArray(questions, i);
    }
}