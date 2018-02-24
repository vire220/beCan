package com.mru.becan.api;

import com.loopj.android.http.*;
/**
 * Created by vire7 on 2/24/2018.
 */

public class BecanRestClient {
    private static final String BASE_URL = "https://becan-server-vire220.c9users.io/";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }
    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
