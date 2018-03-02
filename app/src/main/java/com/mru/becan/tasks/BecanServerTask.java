package com.mru.becan.tasks;

import android.content.Context;
import android.util.Log;

import org.json.*;
import com.loopj.android.http.*;
import com.mru.becan.api.BecanRestClient;

import cz.msebera.android.httpclient.Header;

/**
 * Created by vire7 on 2/24/2018.
 * Source https://code.tutsplus.com/tutorials/an-introduction-to-loopj--cms-26781
 */

public class BecanServerTask {
    private static final String TAG = "BECAN_SERVER";

    private final Context context;
    private final OnBecanServerCompleted listener;

    private AsyncHttpClient asyncHttpClient;
    private RequestParams requestParams;

    private String BASE_URL = "https://becan-server-vire220.c9users.io/";
    private String jsonResponse;

    public BecanServerTask(Context context, OnBecanServerCompleted listener) {
        asyncHttpClient = new AsyncHttpClient();
        requestParams = new RequestParams();
        this.context = context;
        this.listener = listener;
    }

    public void executeBecanServerCall(String url){
        asyncHttpClient.get(BASE_URL + url, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                jsonResponse = response.toString();
                listener.taskCompleted(jsonResponse);
                Log.i(TAG, "onSuccess: " + jsonResponse);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                jsonResponse = response.toString();
                listener.taskCompleted(jsonResponse);
                Log.i(TAG, "onSuccess: " + jsonResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e(TAG, "onFailure: " + errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e(TAG, "onFailure");
                Log.e(TAG, "Status Code: " + statusCode);
                Log.e(TAG, "Headers: " + headers.toString());
                Log.e(TAG, "Response String: " + responseString);
                Log.e(TAG, "Exception: " + throwable.getMessage());
            }
        });
    }
}