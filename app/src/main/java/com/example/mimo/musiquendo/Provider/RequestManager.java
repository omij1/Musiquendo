package com.example.mimo.musiquendo.Provider;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Singleton que permite manejar la cola de Volley según las recomendaciones de Android para evitar
 * problemas con el contexto.
 */

public class RequestManager {

    private static RequestManager jamendoInstance;
    private RequestQueue requestQueue;


    private RequestManager(){}


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
