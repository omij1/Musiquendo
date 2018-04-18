package com.example.mimo.musiquendo.Provider;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.mimo.musiquendo.BuildConfig;
import com.example.mimo.musiquendo.Model.Album;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que se comunica con el API de Jamendo para obtener los datos
 */

public class JamendoProvider {

    private Gson gson;
    private static final Integer IMAGESIZE = 400;

    public JamendoProvider() {
        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();//El formato de fecha es para que no falle el parsing
    }

    ////////////////////////////////////METODOS RELACIONADOS CON LOS ALBUMES\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    public void getAlbumList(Context context, VolleyCallback callback) {

        String url = BuildConfig.ALBUM_LIST+"?client_id="+BuildConfig.JAMENDO_API_KEY+"&imagesize="+IMAGESIZE+"&format=jsonpretty&limit=all";
        CustomJSONObject objectRequest = new CustomJSONObject(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray results = response.getJSONArray("results");
                            Type list = new TypeToken<ArrayList<Album>>(){}.getType();
                            List<Album> albumRequest = gson.fromJson(String.valueOf(results), list);
                            callback.onSuccess(albumRequest);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR", "onErrorResponse: "+error);
            }
        });
        RequestManager.getInstance().addToRequestQueue(context, objectRequest);
    }

    ////////////////////////////////////METODOS RELACIONADOS CON LOS ARTISTAS\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    public void getArtistList() {


    }

    ////////////////////////////////////METODOS RELACIONADOS CON LAS LISTAS DE REPRODUCCION\\\\\\\\\\\\\\\\\\\\\\\\\\\

    public void getPlayLists() {


    }
}
