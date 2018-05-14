package com.example.mimo.musiquendo.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mimo.musiquendo.Adapters.AlbumTracksAdapter;
import com.example.mimo.musiquendo.BuildConfig;
import com.example.mimo.musiquendo.Dialogs.SimpleDialog;
import com.example.mimo.musiquendo.Model.AlbumTracks;
import com.example.mimo.musiquendo.Model.SharedPreferences.PreferencesManager;
import com.example.mimo.musiquendo.Model.Track;
import com.example.mimo.musiquendo.Player.NotificationBuilder;
import com.example.mimo.musiquendo.Player.TrackPlayer;
import com.example.mimo.musiquendo.Player.TrackQueue;
import com.example.mimo.musiquendo.Provider.JamendoProvider;
import com.example.mimo.musiquendo.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Fragmento que muestra los detalles de un álbum
 */

public class FragmentAlbumDetail extends Fragment implements AlbumTracksAdapter.OnItemClickListener {

    @BindView(R.id.album_detail_songs)
    RecyclerView songs;
    @BindView(R.id.album_detail_image)
    ImageView albumImage;
    @BindView(R.id.album_detail_name)
    TextView albumName;
    @BindView(R.id.album_detail_artist)
    TextView albumArtist;
    @BindView(R.id.album_songs)
    TextView albumSongs;
    private static final String ID = "ID";
    private static final String NAME = "NAME";
    private static final String ARTIST = "ARTIST";
    private List<AlbumTracks> tracks;
    private AlbumTracksAdapter adapter;
    private JamendoProvider jamendo;
    private PreferencesManager preferencesManager;


    /**
     * Interfaz que contiene el callback que se ejecuta cuando se reciben los datos de internet
     */
    public interface AlbumDetailCallback {
        void onAlbumDetailSuccess(List<AlbumTracks> tracksList, String cover);
    }

    /**
     * Función que crea un nuevo fragmento con el identificador de la categoría a la que pertenece
     * @param id Identificador del álbum
     * @param name Nombre del álbum
     * @param artist Artista al que pertenece el álbum
     * @return Nuevo fragmento
     */
    public static FragmentAlbumDetail newInstance(String id, String name, String artist) {
        FragmentAlbumDetail fragment = new FragmentAlbumDetail();
        Bundle bundle = new Bundle();
        bundle.putString(ID, id);
        bundle.putString(NAME, name);
        bundle.putString(ARTIST, artist);
        fragment.setArguments(bundle);

        return fragment;
    }

    public FragmentAlbumDetail() {
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        jamendo = new JamendoProvider(getContext());
        preferencesManager = new PreferencesManager(getContext());
        jamendo.albumDetails(getArguments().getString(ID), (tracksList, cover) -> {
            tracks = tracksList;
            loadContent();
            if (!cover.equals(""))
                Picasso.get().load(cover).into(albumImage);
            else
                albumImage.setImageDrawable(getResources().getDrawable(R.drawable.no_image));
        }, () -> {
            String[] dialogContent = getResources().getStringArray(R.array.lost_connection);
            callDialog(R.drawable.ic_signal_wifi_off, dialogContent[0], dialogContent[1]);
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_album_detail, container, false);
        ButterKnife.bind(this, view);
        songs.addItemDecoration(new PaddingItemDecorator(2));

        return view;
    }


    @Override
    public void onTrackClick(View view, AlbumTracks track, int position) {
        adapter.changeItem(position);
        //Miramos el modo de reproducción de las canciones
        Intent playTrack = new Intent(getActivity(), TrackPlayer.class);
        playTrack.setAction(BuildConfig.PLAY);
        TrackQueue.getInstance().setSection(BuildConfig.ALBUMS);
        /*TrackQueue.getInstance().addTrack(new Track(track.getAudio(), track.getTrackName(),
                getArguments().getString(NAME), track.getTrackDuration(), track.getMinutes()));
        getActivity().startService(playTrack);*/
        automaticMode(playTrack, position);
    }

    private void automaticMode(Intent playTrack, int position) {
        List<Track> trackList = new ArrayList<>();
        for (AlbumTracks track : tracks) {
            trackList.add(new Track(track.getAudio(), track.getTrackName(),
                    getArguments().getString(NAME), track.getTrackDuration(),track.getMinutes()));
        }
        TrackQueue.getInstance().addTrackList(trackList, position);
        getActivity().startService(playTrack);
    }

    @Override
    public void onDownloadSongClick(AlbumTracks track) {

    }

    /**
     * Método que se ejecuta cuando volley obtiene los datos del API
     */
    private void loadContent() {
        albumName.setText(getArguments().getString(NAME));
        albumArtist.setText(getArguments().getString(ARTIST));
        albumSongs.setText(tracks.size()+" total");
        adapter = new AlbumTracksAdapter(tracks, this);
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
