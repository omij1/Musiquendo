package com.example.mimo.musiquendo.Provider;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class RequestManager {

    private static RequestManager jamendoInstance;
    private RequestQueue requestQueue;
    private static Context context;

    private RequestManager(){
    }

    public static synchronized RequestManager getInstance(){
        if (jamendoInstance == null){
            jamendoInstance = new RequestManager();
        }
        return jamendoInstance;
    }

    private RequestQueue getRequestQueue(Context context) {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Context ctx, Request<T> req) {
        getRequestQueue(ctx).add(req);
    }
}
