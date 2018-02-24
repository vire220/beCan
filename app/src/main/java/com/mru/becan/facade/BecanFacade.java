package com.mru.becan.facade;

import android.util.Log;

import org.json.*;
import com.loopj.android.http.*;
import com.mru.becan.api.BecanRestClient;
import com.mru.becan.callback.BecanCallback;

import cz.msebera.android.httpclient.Header;

/**
 * Created by vire7 on 2/24/2018.
 */

public class BecanFacade {
    public String getSteps() throws JSONException {
        BecanRestClient.get("/steps/", null, new JsonHttpResponseHandler(){
            public void onSuccess(int statusCode, Header[] headers, JSONObject response){

            }

            public void onSuccess(int statusCode, Header[] headers, JSONArray response){
                Log.d("SUCCESS RESPONSE", response.toString());

            }
        });
    }
}