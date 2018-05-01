package com.example.mimo.musiquendo.Provider;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.mimo.musiquendo.Activities.MainActivity;
import com.example.mimo.musiquendo.Adapters.PlayListAdapter;
import com.example.mimo.musiquendo.BuildConfig;
import com.example.mimo.musiquendo.Dialogs.SimpleDialog;
import com.example.mimo.musiquendo.Fragments.FragmentAlbumDetail;
import com.example.mimo.musiquendo.Fragments.FragmentAlbums;
import com.example.mimo.musiquendo.Fragments.FragmentArtistDetail;
import com.example.mimo.musiquendo.Fragments.FragmentArtists;
import com.example.mimo.musiquendo.Fragments.FragmentPlayLists;
import com.example.mimo.musiquendo.Model.Album;
import com.example.mimo.musiquendo.Model.AlbumTracks;
import com.example.mimo.musiquendo.Model.Artist;
import com.example.mimo.musiquendo.Model.ArtistTracks;
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
    private Context context;
    private static final Integer IMAGESIZE = 400;

    public JamendoProvider(Context context) {
        this.gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();//El formato de fecha es para que no falle el parsing
        this.context = context;
    }

    ////////////////////////////////////METODOS RELACIONADOS CON LOS ALBUMES\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    /**
     * Método que obtiene la lista de álbumes que se muestran en la pestaña albumes
     * @param callback Callback que se ejecuta cuando se obtienen y parsean los datos
     * @param errorCallback Callback que muestra un diálogo informando del error
     */
    public void getAlbumList(FragmentAlbums.AlbumsCallback callback, SimpleDialog.DialogListener errorCallback) {

        //El API no ofrece un mecanismo de paginación y es por ello que uso el parámetro limit=all
        String url = BuildConfig.ALBUM_LIST+"?client_id="+BuildConfig.JAMENDO_API_KEY+"&imagesize="+
                IMAGESIZE+"&format=jsonpretty&limit=all";
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
                    errorCallback.crearDialog();
                });
        RequestManager.getInstance().addToRequestQueue(context, albumsRequest);
    }


    /**
     * Método que permite buscar álbumes por su nombre
     * @param query El nombre del álbum que se quiere buscar
     * @param callback Callback que se ejecuta cuando se obtienen y parsean los datos
     * @param errorCallback Callback que muestra un diálogo informando del error
     */
    public void searchAlbum(String query, FragmentAlbums.AlbumsCallback callback, SimpleDialog.DialogListener errorCallback) {

        String url = BuildConfig.ALBUM_LIST+"?client_id="+BuildConfig.JAMENDO_API_KEY+"&imagesize="+
                IMAGESIZE+"&format=jsonpretty&namesearch="+query;
        CustomJSONObject search = new CustomJSONObject(Request.Method.GET, url, null,
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
                    errorCallback.crearDialog();
                });
        RequestManager.getInstance().addToRequestQueue(context, search);
    }


    /**
     * Método que permite realizar búsquedas de álbumes según diversos filtros
     * @param filter El filtro que se va a aplicar
     * @param callback Callback que se ejecuta cuando se obtienen y parsean los datos
     * @param errorCallback Callback que muestra un diálogo informando del error
     */
    public void filterAlbums(String filter, FragmentAlbums.AlbumsCallback callback, SimpleDialog.DialogListener errorCallback) {

        String url = BuildConfig.ALBUM_LIST+"?client_id="+BuildConfig.JAMENDO_API_KEY+"&imagesize="+
                IMAGESIZE+"&format=jsonpretty&limit=all&order="+filter;
        CustomJSONObject order = new CustomJSONObject(Request.Method.GET, url, null,
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
                    errorCallback.crearDialog();
        });
        RequestManager.getInstance().addToRequestQueue(context, order);
    }


    /**
     * Método que obtiene las canciones de un álbum
     * @param albumId Identificador del álbum del que se quieren saber las canciones
     * @param callback Callback que se ejecuta cuando se obtienen y parsean los datos
     * @param erroCallback Callback que muestra un diálogo informando del error
     */
    public void albumDetails(String albumId, FragmentAlbumDetail.AlbumDetailCallback callback, SimpleDialog.DialogListener erroCallback) {
        String url = BuildConfig.ALBUM_DETAILS+"?client_id="+BuildConfig.JAMENDO_API_KEY+"&imagesize="+
                IMAGESIZE+"&format=jsonpretty&limit=1&id="+albumId;
        CustomJSONObject details = new CustomJSONObject(Request.Method.GET, url, null, response -> {
            try {
                JSONArray results = response.getJSONArray("results");
                JSONObject jsonObject = results.getJSONObject(0);
                String cover = jsonObject.getString("image");
                JSONArray tracks = jsonObject.getJSONArray("tracks");
                Type list = new TypeToken<ArrayList<AlbumTracks>>() {}.getType();
                List<AlbumTracks> albumTracks = gson.fromJson(String.valueOf(tracks), list);
                callback.onAlbumDetailSuccess(albumTracks, cover);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            Log.e("ERROR", "onErrorResponse: "+error);
            erroCallback.crearDialog();
        });
        RequestManager.getInstance().addToRequestQueue(context, details);
    }

    ////////////////////////////////////METODOS RELACIONADOS CON LOS ARTISTAS\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    /**
     * Método que obtiene la lista de artistas que se muestran en la pestaña artistas
     * @param callback Callback que se ejecuta cuando se obtienen y parsean los datos
     */
    public void getArtistList(FragmentArtists.ArtistsCallback callback) {

        String url = BuildConfig.ARTIST_LIST+"?client_id="+BuildConfig.JAMENDO_API_KEY+"&imagesize="+
                IMAGESIZE+"&format=jsonpretty&limit=all";
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
                }, error -> {
                    Log.e("ERROR", "onErrorResponse: "+error);
        });
        RequestManager.getInstance().addToRequestQueue(context, artistsRequest);
    }


    /**
     * Método que permite buscar artistas por su nombre
     * @param query El nombre del artista que se quiere buscar
     * @param callback Callback que se ejecuta cuando se obtienen y parsean los datos
     * @param errorCallback Callback que muestra un diálogo informando del error
     */
    public void searchArtist(String query, FragmentArtists.ArtistsCallback callback, SimpleDialog.DialogListener errorCallback) {

        String url = BuildConfig.ARTIST_LIST+"?client_id="+BuildConfig.JAMENDO_API_KEY+"&imagesize="+
                IMAGESIZE+"&format=jsonpretty&namesearch="+query;
        CustomJSONObject search = new CustomJSONObject(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray results = response.getJSONArray("results");
                        Type list = new TypeToken<ArrayList<Artist>>(){}.getType();
                        List<Artist> artistRequest = gson.fromJson(String.valueOf(results), list);
                        callback.onArtistSuccess(artistRequest);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
                    Log.e("ERROR", "onErrorResponse: "+error);
                    errorCallback.crearDialog();
                });
        RequestManager.getInstance().addToRequestQueue(context,search);
    }


    /**
     * Método que permite realizar búsquedas de artistas según diversos filtros
     * @param filter Filtro que se va a aplicar
     * @param callback Callback que se ejecuta cuando se obtienen y parsean los datos
     * @param errorCallback Callback que muestra un diálogo informando del error
     */
    public void filterArtists(String filter, FragmentArtists.ArtistsCallback callback, SimpleDialog.DialogListener errorCallback) {

        String url = BuildConfig.ARTIST_LIST+"?client_id="+BuildConfig.JAMENDO_API_KEY+"&imagesize="+
                IMAGESIZE+"&format=jsonpretty&limit=all&order="+filter;
        CustomJSONObject order = new CustomJSONObject(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray results = response.getJSONArray("results");
                        Type list = new TypeToken<ArrayList<Artist>>(){}.getType();
                        List<Artist> artistRequest = gson.fromJson(String.valueOf(results), list);
                        callback.onArtistSuccess(artistRequest);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
                    Log.e("ERROR", "onErrorResponse: "+error);
                    errorCallback.crearDialog();
        });
        RequestManager.getInstance().addToRequestQueue(context, order);
    }


    /**
     * Método que obtiene las canciones de un artista determinado
     * @param artistId Identificador del artista del que se quieren obtener sus canciones
     * @param callback Callback que se ejecuta cuando se obtienen y parsean los datos
     * @param errorCallback Callback que muestra un diálogo informando del error
     */
    public void artistDetails(String artistId, FragmentArtistDetail.ArtistDetailCallback callback, SimpleDialog.DialogListener errorCallback){

        String url = BuildConfig.ARTIST_DETAILS+"?client_id="+BuildConfig.JAMENDO_API_KEY+"&imagesize="+
                IMAGESIZE+"&format=jsonpretty&limit=all&id="+artistId;
        CustomJSONObject details = new CustomJSONObject(Request.Method.GET, url, null, response -> {
            try {
                JSONArray results = response.getJSONArray("results");
                String cover = null;
                List<ArtistTracks> artistTracks = null;
                if (results.length() > 0) {//Un artista puede no tener canciones
                    JSONObject jsonObject = results.getJSONObject(0);
                    cover = jsonObject.getString("image");
                    JSONArray tracks = jsonObject.getJSONArray("tracks");
                    Type list = new TypeToken<ArrayList<ArtistTracks>>() {}.getType();
                    artistTracks = gson.fromJson(String.valueOf(tracks), list);
                }
                callback.onArtistDetailSuccess(artistTracks, cover);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            Log.e("ERROR", "onErrorResponse: "+error);
            errorCallback.crearDialog();
        });
        RequestManager.getInstance().addToRequestQueue(context, details);
    }

    ////////////////////////////////////METODOS RELACIONADOS CON LAS LISTAS DE REPRODUCCION\\\\\\\\\\\\\\\\\\\\\\\\\\\

    /**
     * Método que obtiene la lista de reproduciones que se muestran en la pestaña listas
     * @param callback Callback que se ejecuta cuando se obtienen y parsean los datos
     */
    public void getPlayLists(FragmentPlayLists.PlaylistsCallback callback) {

        //El API no ofrece imágenes de las playlist, debido a esto la foto de la playlist será la de la primera canción
        String url = BuildConfig.PLAYLIST_LIST+"?client_id="+BuildConfig.JAMENDO_API_KEY+
                "&format=jsonpretty&limit=all";
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
                }, error -> {
                    Log.e("ERROR", "onErrorResponse: "+error);
        });
        RequestManager.getInstance().addToRequestQueue(context, playlistsRequest);
    }


    /**
     * Método que permite buscar listas de reproducción por su nombre
     * @param query El nombre de la lista que se desea buscar
     * @param callback Callback que se ejecuta cuando se obtienen y parsean los datos
     * @param errorCallback Callback que muestra un diálogo con el error
     */
    public void searchPlayList(String query, FragmentPlayLists.PlaylistsCallback callback, SimpleDialog.DialogListener errorCallback) {
        String url = BuildConfig.PLAYLIST_LIST+"?client_id="+BuildConfig.JAMENDO_API_KEY+
                "&format=jsonpretty&namesearch="+query;
        CustomJSONObject search = new CustomJSONObject(Request.Method.GET, url, null, response -> {
            try {
                JSONArray results = response.getJSONArray("results");
                Type list = new TypeToken<ArrayList<PlayList>>(){}.getType();
                List<PlayList> playlistRequest = gson.fromJson(String.valueOf(results), list);
                callback.onPlaylistsSuccess(playlistRequest);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            Log.e("ERROR", "onErrorResponse: "+error);
            errorCallback.crearDialog();
        });
        RequestManager.getInstance().addToRequestQueue(context, search);
    }


    /**
     * Método que permite realizar búsquedas de listas de reproducción según diversos criterios
     * @param filter El filtro que se va a aplicar
     * @param callback Callback que se ejecuta cuando se obtienen y parsean los datos
     * @param errorCallback Callback que muestra un diálogo con el error
     */
    public void filterPlaylists(String filter, FragmentPlayLists.PlaylistsCallback callback, SimpleDialog.DialogListener errorCallback) {
        String url = BuildConfig.PLAYLIST_LIST+"?client_id="+BuildConfig.JAMENDO_API_KEY+
                "&format=jsonpretty&limit=all&order="+filter;
        CustomJSONObject order = new CustomJSONObject(Request.Method.GET, url, null, response -> {
            try {
                JSONArray results = response.getJSONArray("results");
                Type list = new TypeToken<ArrayList<PlayList>>(){}.getType();
                List<PlayList> playlistRequest = gson.fromJson(String.valueOf(results), list);
                callback.onPlaylistsSuccess(playlistRequest);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            }, error -> {
                Log.e("ERROR", "onErrorResponse: "+error);
                errorCallback.crearDialog();
        });
        RequestManager.getInstance().addToRequestQueue(context, order);
    }


    
    public void playlistDetails(String playlistId) {
        String url = BuildConfig.PLAYLIST_DETAILS;
        CustomJSONObject details = new CustomJSONObject(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR", "onErrorResponse: "+error);

            }
        });
        RequestManager.getInstance().addToRequestQueue(context, details);
    }


    /**
     * Método que obtiene las portadas de las listas de reproducción que se corresponden con la portada de la primera canción
     * @param item Objeto con los datos de la lista de reproducción
     * @param callback Callback que se ejecuta cuando se obtienen y almacenan las portadas de las listas de reproducción
     */
    public void getALbumCover(PlayList item, PlayListAdapter.CoverCallback callback) {

        String url = BuildConfig.PLAYLIST_DETAILS+"?client_id="+BuildConfig.JAMENDO_API_KEY+"&id="+
                item.getId()+"&imagesize="+IMAGESIZE+"&format=jsonpretty";
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
