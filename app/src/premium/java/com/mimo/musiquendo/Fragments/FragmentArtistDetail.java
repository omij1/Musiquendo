package com.mimo.musiquendo.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.Toast;

import com.mimo.musiquendo.Adapters.ArtistsTracksAdapter;
import com.mimo.musiquendo.BuildConfig;
import com.mimo.musiquendo.Dialogs.SimpleDialog;
import com.mimo.musiquendo.DownloadManager.Downloader;
import com.mimo.musiquendo.Model.ArtistTracks;
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
    private static final int PADDING = 2;
    private static final int REQUEST_CODE = 1;
    private List<ArtistTracks> tracks;
    private ArtistsTracksAdapter adapter;
    private String artistCover;
    private PreferencesManager preferencesManager;
    private Downloader downloader;
    private ArtistTracks trackSelected;


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


    public FragmentArtistDetail() {}


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        JamendoProvider jamendo = new JamendoProvider(getContext());
        preferencesManager = new PreferencesManager(getContext());
        jamendo.artistDetails(getArguments().getString(ID), (tracksList, cover) -> {
            if (tracksList != null) {//El artista tiene datos
                tracks = tracksList;
                artistCover = cover;
                loadContent();
                if (!artistCover.equals(""))
                    Picasso.get().load(artistCover).into(artistImage);
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
        songs.addItemDecoration(new PaddingItemDecorator(PADDING));

        return view;
    }


    @Override
    public void onTrackClick(View view, ArtistTracks track, int position) {
        adapter.changeItem(position);

        //Comprobamos la conexión a internet
        if (isOnline()){
            String[] dialogContent = getResources().getStringArray(R.array.lost_connection);
            callDialog(R.drawable.ic_signal_wifi_off, dialogContent[0], dialogContent[1]);
            return;
        }

        Intent playTrack = new Intent(getActivity(), TrackPlayer.class);
        playTrack.setAction(BuildConfig.PLAY);
        TrackQueue.getInstance().setSection(BuildConfig.ARTISTS);
        if (preferencesManager.getPlaylistMode())
            automaticMode(playTrack, position);
        else
            normalMode(playTrack, track);
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


    private void normalMode(Intent playTrack, ArtistTracks track) {
        TrackQueue.getInstance().addTrack(new Track(track.getAudio(), track.getTrackName(),
                getArguments().getString(NAME), track.getTrackDuration(), track.getMinutes()));
        getActivity().startService(playTrack);
    }


    @Override
    public void onDownloadSongClick(ArtistTracks track) {
        trackSelected = track;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                downloadTrack();
            else
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
        }
        else
            downloadTrack();
    }


    private void downloadTrack() {
        WifiManager wifi = (WifiManager) getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        //Comprobamos la conexión a internet
        if (isOnline()){
            String[] dialogContent = getResources().getStringArray(R.array.lost_connection);
            callDialog(R.drawable.ic_signal_wifi_off, dialogContent[0], dialogContent[1]);
            return;
        }
        if (!preferencesManager.getDownloadSettings() || wifi != null && wifi.isWifiEnabled()) {
            downloader = new Downloader(getContext());
            downloader.execute(trackSelected.getAudioDownload(), trackSelected.getTrackName(),
                    getArguments().getString(NAME), String.valueOf(trackSelected.getTrackDuration()),
                    trackSelected.getMinutes(), artistCover);
        }
        else
            Toast.makeText(getContext(), R.string.unavailable_download, Toast.LENGTH_SHORT).show();
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


    @Override
    public void onStop() {
        if (downloader != null)
            downloader.cancel(true);
        super.onStop();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            downloadTrack();
    }
}
