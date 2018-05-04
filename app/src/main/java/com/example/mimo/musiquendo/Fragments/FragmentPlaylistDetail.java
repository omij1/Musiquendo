package com.example.mimo.musiquendo.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mimo.musiquendo.Adapters.PlaylistTracksAdapter;
import com.example.mimo.musiquendo.Dialogs.SimpleDialog;
import com.example.mimo.musiquendo.Model.PlayListTracks;
import com.example.mimo.musiquendo.Player.TrackPlayer;
import com.example.mimo.musiquendo.Provider.JamendoProvider;
import com.example.mimo.musiquendo.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

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
    private List<PlayListTracks> tracks;
    private PlaylistTracksAdapter adapter;
    private JamendoProvider jamendo;


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

    public FragmentPlaylistDetail() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        jamendo = new JamendoProvider(getContext());
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
        songs.addItemDecoration(new PaddingItemDecorator(2));

        return view;
    }

    @Override
    public void onTrackClick(View view, PlayListTracks track, int playing) {
        adapter.changeItem(playing);
        TrackPlayer.getInstance().playStreamTrack(track.getAudio(), track.getTrackDuration());
    }

    @Override
    public void onDownloadSongClick(PlayListTracks track) {

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
