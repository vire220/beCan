package com.mru.becan.callback;

/**
 * Created by vire7 on 2/24/2018.
 */

public class BecanCallback {

    public String onSuccess(String response) {
        return response;
    }

    public String onFail(String msg) {
        return msg;
    }
}
