package com.example.mimo.musiquendo.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mimo.musiquendo.Adapters.ArtistsTracksAdapter;
import com.example.mimo.musiquendo.BuildConfig;
import com.example.mimo.musiquendo.Dialogs.SimpleDialog;
import com.example.mimo.musiquendo.Model.ArtistTracks;
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
 * Fragmento que muestra los detalles de un artista
 */

public class FragmentArtistDetail extends Fragment implements ArtistsTracksAdapter.OnItemClickListener {

    @BindView(R.id.artist_detail_songs)
    RecyclerView songs;
    @BindView(R.id.artist_detail_image)
    ImageView artistImage;
    @BindView(R.id.artist_detail_name)
    TextView artistName;
    @BindView(R.id.artist_detail_joindate)
    TextView artistJoinDate;
    @BindView(R.id.artist_detail_website)
    TextView artistWeb;
    private static final String ID = "ID";
    private static final String NAME = "NAME";
    private static final String JOIN = "JOIN";
    private static final String WEB = "WEB";
    private List<ArtistTracks> tracks;
    private ArtistsTracksAdapter adapter;
    private JamendoProvider jamendo;


    /**
     * Interfaz que contiene el callback que se ejecuta cuando se reciben los datos de internet
     */
    public interface ArtistDetailCallback {
        void onArtistDetailSuccess(List<ArtistTracks> tracksList, String cover);
    }

    /**
     * Función que crea un nuevo fragmento que muestra los detalles de un artista
     * @param artistId Identificador del artista
     * @param name Nombre del artista del que se va a mostrar la información
     * @param joinDate Fecha de registro del artista en Jamendo
     * @param web Sitio web del artista
     * @return Nuevo fragmento
     */
    public static FragmentArtistDetail newInstance(String artistId, String name, String joinDate,
                                                   String web) {
        FragmentArtistDetail fragment = new FragmentArtistDetail();
        Bundle bundle = new Bundle();
        bundle.putString(ID, artistId);
        bundle.putString(NAME, name);
        bundle.putString(JOIN, joinDate);
        bundle.putString(WEB, web);
        fragment.setArguments(bundle);

        return fragment;
    }

    public FragmentArtistDetail() {
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        jamendo = new JamendoProvider(getContext());
        jamendo.artistDetails(getArguments().getString(ID), (tracksList, cover) -> {
            if (tracksList != null) {//El artista tiene datos
                tracks = tracksList;
                loadContent();
                if (!cover.equals(""))
                    Picasso.get().load(cover).into(artistImage);
                else
                    artistImage.setImageDrawable(getResources().getDrawable(R.drawable.no_image));
            } else {
                //El artista no tiene ningún dato relacionado a esta categoría
                String[] dialogContent = getResources().getStringArray(R.array.artist_without_data);
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
        View view = inflater.inflate(R.layout.fragment_artist_detail, container, false);
        ButterKnife.bind(this, view);
        songs.addItemDecoration(new PaddingItemDecorator(2));

        return view;
    }

    @Override
    public void onTrackClick(View view, ArtistTracks track, int position) {
        adapter.changeItem(position);
        Intent playTrack = new Intent(getActivity(), TrackPlayer.class);
        playTrack.setAction(BuildConfig.PLAY);
        TrackQueue.getInstance().setSection(BuildConfig.ARTISTS);
        /*TrackQueue.getInstance().addTrack(new Track(track.getAudio(), track.getTrackName(),
                getArguments().getString(NAME), track.getTrackDuration(), track.getMinutes()));
        getActivity().startService(playTrack);*/
        automaticMode(playTrack, position);
    }

    private void automaticMode(Intent playTrack, int position) {
        List<Track> trackList = new ArrayList<>();
        for (ArtistTracks track : tracks) {
            trackList.add(new Track(track.getAudio(), track.getTrackName(),
                    getArguments().getString(NAME), track.getTrackDuration(),track.getMinutes()));
        }
        TrackQueue.getInstance().addTrackList(trackList, position);
        getActivity().startService(playTrack);
    }

    @Override
    public void onDownloadSongClick(ArtistTracks track) {

    }

    /**
     * Método que se ejecuta cuando volley obtiene los datos del API
     */
    private void loadContent() {
        artistName.setText(getArguments().getString(NAME));
        if (getArguments().getString(WEB).equals("")) {
            artistWeb.setText(R.string.no_website);
        } else {
            artistWeb.setText(getArguments().getString(WEB));
        }
        artistJoinDate.setText(getArguments().getString(JOIN));
        adapter = new ArtistsTracksAdapter(tracks, this);
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
