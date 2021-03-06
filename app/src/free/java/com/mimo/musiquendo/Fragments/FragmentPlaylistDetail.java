package com.mimo.musiquendo.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mimo.musiquendo.Adapters.PlaylistTracksAdapter;
import com.mimo.musiquendo.BuildConfig;
import com.mimo.musiquendo.Dialogs.SimpleDialog;
import com.mimo.musiquendo.DownloadManager.Downloader;
import com.mimo.musiquendo.Model.PlayListTracks;
import com.mimo.musiquendo.Model.SharedPreferences.PreferencesManager;
import com.mimo.musiquendo.Model.Track;
import com.mimo.musiquendo.Player.TrackPlayer;
import com.mimo.musiquendo.Player.TrackQueue;
import com.mimo.musiquendo.Provider.JamendoProvider;
import com.mimo.musiquendo.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Fragmento que muestra los detalles de una lista de reproducción.
 */

public class FragmentPlaylistDetail extends Fragment implements PlaylistTracksAdapter.OnItemClickListener{

    @BindView(R.id.playlist_detail_image)
    CircleImageView playlistImage;
    @BindView(R.id.playlist_detail_name)
    TextView playlitsName;
    @BindView(R.id.playlist_detail_songs)
    RecyclerView songs;
    private static final String ID = "ID";
    private static final String NAME = "NAME";
    private static final String IMAGE = "IMAGE";
    private static final int PADDING = 2;
    private List<PlayListTracks> tracks;
    private PlaylistTracksAdapter adapter;
    private PreferencesManager preferencesManager;


    /**
     * Interfaz que contiene el callback que se ejecuta cuando se reciben los datos de internet
     */
    public interface PlaylistDetailCallback {
        void onPlaylistDetailSuccess(List<PlayListTracks> tracksList);
    }


    /**
     * Método que devuelve un nuevo fragmento correspondiente a los detalles de una lista de reproducción
     * @param id Identificador de la lista de reproducción
     * @param name Nombre de la lista de reproducción
     * @param image Imagen inicial de la lista de reproducción
     * @return El nuevo fragmento
     */
    public static FragmentPlaylistDetail newInstance(String id, String name, String image) {
        FragmentPlaylistDetail fragment = new FragmentPlaylistDetail();
        Bundle bundle = new Bundle();
        bundle.putString(ID, id);
        bundle.putString(NAME, name);
        bundle.putString(IMAGE, image);
        fragment.setArguments(bundle);

        return fragment;
    }


    public FragmentPlaylistDetail() {}


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        JamendoProvider jamendo = new JamendoProvider(getContext());
        preferencesManager = new PreferencesManager(getContext());
        jamendo.playlistDetails(getArguments().getString(ID), tracksList -> {
            if (tracksList != null){
                tracks = tracksList;
                loadContent();
            }
            else {
                //El artista no tiene ningún dato relacionado a esta categoría
                String[] dialogContent = getResources().getStringArray(R.array.playlist_without_data);
                callDialog(R.drawable.ic_sad, dialogContent[0], dialogContent[1]);
            }
        }, () -> {
            String[] dialogContent = getResources().getStringArray(R.array.lost_connection);
            callDialog(R.drawable.ic_signal_wifi_off, dialogContent[0], dialogContent[1]);
        });
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playlist_detail, container, false);
        ButterKnife.bind(this, view);
        songs.addItemDecoration(new PaddingItemDecorator(PADDING));

        return view;
    }


    @Override
    public void onTrackClick(View view, PlayListTracks track, int position) {
        adapter.changeItem(position);

        //Comprobamos la conexión a internet
        if (isOnline()){
            String[] dialogContent = getResources().getStringArray(R.array.lost_connection);
            callDialog(R.drawable.ic_signal_wifi_off, dialogContent[0], dialogContent[1]);
            return;
        }

        Intent playTrack = new Intent(getActivity(), TrackPlayer.class);
        playTrack.setAction(BuildConfig.PLAY);
        TrackQueue.getInstance().setSection(BuildConfig.PLAYLISTS);
        if (preferencesManager.getPlaylistMode())
            automaticMode(playTrack, position);
        else
            normalMode(playTrack, track);
    }


    private void automaticMode(Intent playTrack, int position) {
        List<Track> trackList = new ArrayList<>();
        for (PlayListTracks track : tracks) {
            trackList.add(new Track(track.getAudio(), track.getTrackName(),
                    getArguments().getString(NAME), track.getTrackDuration(),track.getMinutes()));
        }
        TrackQueue.getInstance().addTrackList(trackList, position);
        getActivity().startService(playTrack);
    }


    private void normalMode(Intent playTrack, PlayListTracks track) {
        TrackQueue.getInstance().addTrack(new Track(track.getAudio(), track.getTrackName(),
                getArguments().getString(NAME), track.getTrackDuration(), track.getMinutes()));
        getActivity().startService(playTrack);
    }


    /**
     * Método que comprueba si el dispositivo tiene conexión a internet
     * @return Si tiene internet o no
     */
    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = null;
        if (cm != null) {
            netInfo = cm.getActiveNetworkInfo();
        }
        return netInfo == null || !netInfo.isConnectedOrConnecting();
    }


    /**
     * Método que se ejecuta cuando volley obtiene los datos del API
     */
    private void loadContent() {
        if (getArguments().get(IMAGE).equals(""))
            playlistImage.setImageDrawable(getResources().getDrawable(R.drawable.no_image));
        else
            Picasso.get().load(getArguments().get(IMAGE).toString()).into(playlistImage);
        playlitsName.setText(getArguments().getString(NAME));
        adapter = new PlaylistTracksAdapter(tracks, this);
        songs.setAdapter(adapter);
    }


    /**
     * Método que crea un nuevo diálogo
     */
    private void callDialog(int icon, String title, String content) {

        SimpleDialog dialog = SimpleDialog.newInstance(icon, title, content);
        FragmentManager fm = getFragmentManager();
        dialog.show(fm, ID);
    }
}
