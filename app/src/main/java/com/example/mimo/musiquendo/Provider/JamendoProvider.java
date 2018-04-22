package com.example.mimo.musiquendo.Provider;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.example.mimo.musiquendo.Adapters.PlayListAdapter;
import com.example.mimo.musiquendo.BuildConfig;
import com.example.mimo.musiquendo.Fragments.FragmentAlbums;
import com.example.mimo.musiquendo.Fragments.FragmentArtists;
import com.example.mimo.musiquendo.Fragments.FragmentPlayLists;
import com.example.mimo.musiquendo.Model.Album;
import com.example.mimo.musiquendo.Model.Artist;
import com.example.mimo.musiquendo.Model.PlayList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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

    /**
     * Método que obtiene la lista de álbumes que se muestran en la pestaña albumes
     * @param context El contexto de la aplicación
     * @param callback Callback que se ejecuta cuando se obtienen y parsean los datos
     */
    public void getAlbumList(Context context, FragmentAlbums.AlbumsCallback callback) {

        //El API no ofrece un mecanismo de paginación y es por ello que uso el parámetro limit=all
        String url = BuildConfig.ALBUM_LIST+"?client_id="+BuildConfig.JAMENDO_API_KEY+"&imagesize="+IMAGESIZE+"&format=jsonpretty&limit=all";
        CustomJSONObject albumsRequest = new CustomJSONObject(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray results = response.getJSONArray("results");
                        Type list = new TypeToken<ArrayList<Album>>(){}.getType();
                        List<Album> albumRequest = gson.fromJson(String.valueOf(results), list);
                        callback.onAlbumsSuccess(albumRequest);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
                    Log.e("ERROR", "onErrorResponse: "+error);
                    //TODO que pasa si no hay internet
                });
        RequestManager.getInstance().addToRequestQueue(context, albumsRequest);
    }

    ////////////////////////////////////METODOS RELACIONADOS CON LOS ARTISTAS\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    /**
     * Método que obtiene la lista de artistas que se muestran en la pestaña artistas
     * @param context El contexto de la aplicación
     * @param callback Callback que se ejecuta cuando se obtienen y parsean los datos
     */
    public void getArtistList(Context context, FragmentArtists.ArtistsCallback callback) {

        String url = BuildConfig.ARTIST_LIST+"?client_id="+BuildConfig.JAMENDO_API_KEY+"&imagesize="+IMAGESIZE+"&format=jsonpretty&limit=all";
        CustomJSONObject artistsRequest = new CustomJSONObject(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray results = response.getJSONArray("results");
                        Type list = new TypeToken<ArrayList<Artist>>(){}.getType();
                        List<Artist> artistRequest = gson.fromJson(String.valueOf(results), list);
                        callback.onArtistSuccess(artistRequest);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> Log.e("ERROR", "onErrorResponse: "+error));
        RequestManager.getInstance().addToRequestQueue(context, artistsRequest);
    }

    ////////////////////////////////////METODOS RELACIONADOS CON LAS LISTAS DE REPRODUCCION\\\\\\\\\\\\\\\\\\\\\\\\\\\

    /**
     * Método que obtiene la lista de reproduciones que se muestran en la pestaña listas
     * @param context El contexto de la aplicación
     * @param callback Callback que se ejecuta cuando se obtienen y parsean los datos
     */
    public void getPlayLists(Context context, FragmentPlayLists.PlaylistsCallback callback) {

        //El API no ofrece imágenes de las playlist, debido a esto la foto de la playlist será la de la primera canción
        String url = BuildConfig.PLAYLIST_LIST+"?client_id="+BuildConfig.JAMENDO_API_KEY+"&format=jsonpretty&limit=all";
        CustomJSONObject playlistsRequest = new CustomJSONObject(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray results = response.getJSONArray("results");
                        Type list = new TypeToken<ArrayList<PlayList>>(){}.getType();
                        List<PlayList> playlistRequest = gson.fromJson(String.valueOf(results), list);
                        callback.onPlaylistsSuccess(playlistRequest);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> Log.e("ERROR", "onErrorResponse: "+error));
        RequestManager.getInstance().addToRequestQueue(context, playlistsRequest);
    }

    /**
     * Método que obtiene las portadas de las listas de reproducción que se corresponden con la portada de la primera canción
     * @param context Contexto de la aplicación
     * @param item Objeto con los datos de la lista de reproducción
     * @param callback Callback que se ejecuta cuando se obtienen y almacenan las portadas de las listas de reproducción
     */
    public void getALbumCover(Context context, PlayList item, PlayListAdapter.CoverCallback callback) {

        String url = BuildConfig.PLAYLIST_COVER+"?client_id="+BuildConfig.JAMENDO_API_KEY+"&id="+item.getId()+"&imagesize="+IMAGESIZE+"&format=jsonpretty";
        CustomJSONObject coverRequest = new CustomJSONObject(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray results = response.getJSONArray("results");
                        if (results.length() > 0) {
                            JSONObject jsonObject = results.getJSONObject(0);
                            JSONArray tracks = jsonObject.getJSONArray("tracks");
                            JSONObject track = tracks.getJSONObject(0);
                            item.setCover(track.getString("album_image"));
                        }
                        else {
                            item.setCover("");
                        }
                        callback.onCoversSuccess(item);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> Log.e("ERROR", "onErrorResponse: "+error));
        RequestManager.getInstance().addToRequestQueue(context, coverRequest);
    }
}
