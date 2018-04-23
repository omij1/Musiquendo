package com.example.mimo.musiquendo.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mimo.musiquendo.Adapters.PlayListAdapter;
import com.example.mimo.musiquendo.Model.Categories;
import com.example.mimo.musiquendo.Model.PlayList;
import com.example.mimo.musiquendo.Provider.JamendoProvider;
import com.example.mimo.musiquendo.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Fragmento que muestra la colección de listas de reproducción en la pantalla principal
 */

public class FragmentPlayLists extends Fragment implements PlayListAdapter.OnItemClickListener{

    @BindView(R.id.playlists_list)
    RecyclerView playLists;
    @BindView(R.id.refresh_playlists_list)
    SwipeRefreshLayout refresh;
    private static final String TYPE = "FragmentType";
    private List<PlayList> playlistsList;
    private PlayListAdapter playListAdapter;
    private Categories category;
    private JamendoProvider jamendo;

    public interface PlaylistsCallback {
        void onPlaylistsSuccess(List<PlayList> playListList);
    }

    /**
     * Función que crea un nuevo fragmento con el identificador de la categoría a la que pertenece
     * @param category Categoría a la que pertenece el fragmento
     * @return Nuevo fragmento
     */
    public static Fragment newInstance(Categories category) {
        Fragment fragment = new FragmentPlayLists();
        Bundle bundle = new Bundle();
        bundle.putString(TYPE, category.key);
        fragment.setArguments(bundle);
        return fragment;
    }

    public FragmentPlayLists() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String key = getArguments() != null ? getArguments().getString(TYPE) : "";
        category = Categories.fromKey(key);
        jamendo = new JamendoProvider(getContext());
        jamendo.getPlayLists(playListResponse -> {
            playlistsList = playListResponse;
            loadContent();
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playlists, container, false);
        ButterKnife.bind(this, view);
        playLists.addItemDecoration(new PaddingItemDecorator(3));
        refresh.setColorSchemeColors(getResources().getColor(R.color.colorPrimary),
                getResources().getColor(R.color.colorPrimaryDark), getResources().getColor(R.color.colorAccent));
        refresh.setOnRefreshListener(() -> jamendo.getPlayLists(playListResponse -> {
            playListAdapter.swapItems(playListResponse);
            refresh.setRefreshing(false);
        }));

        return view;
    }

    @Override
    public void onPlayListClick(View view, PlayList playList) {
        FragmentActivity activity = getActivity();
        if (activity == null){
            return;
        }

    }

    @Override
    public void onDownloadItemClick(PlayList playList) {

    }

    /**
     * Método que se ejecuta cuando volley obtiene los datos del API
     */
    private void loadContent() {
        playListAdapter = new PlayListAdapter(playlistsList, this);
        playLists.setAdapter(playListAdapter);
    }
}
