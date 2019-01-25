package com.franklincbc.motoon.http;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import org.json.JSONObject;

/**
 * Created by Priscila on 20/12/2016.
 */

public class RequestDirectionRotaHttpTask extends AsyncTaskLoader<JSONObject> {
    String mUrl;

    public RequestDirectionRotaHttpTask(Context context, String url) {
        super(context);
        this.mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public JSONObject loadInBackground() {
        return requestDirectionRotaHTTP.requestDirectionRotaHTTP(mUrl);
    }
}
