package com.example.mimo.musiquendo.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mimo.musiquendo.Adapters.AlbumAdapter;
import com.example.mimo.musiquendo.Model.Album;
import com.example.mimo.musiquendo.Model.Categories;

import com.example.mimo.musiquendo.Provider.JamendoProvider;
import com.example.mimo.musiquendo.Provider.VolleyCallback;
import com.example.mimo.musiquendo.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Fragmento que muestra la colección de álbumes en la pantalla principal
 */

public class FragmentAlbums extends Fragment implements AlbumAdapter.OnItemClickListener {

    @BindView(R.id.albums_list)
    RecyclerView albums;
    @BindView(R.id.refresh_albums_list)
    SwipeRefreshLayout refresh;
    private static final String TYPE = "FragmentType";
    private List<Album> albumList;
    private AlbumAdapter albumAdapter;
    private Categories category;
    private JamendoProvider jamendo;

    /**
     * Función que crea un nuevo fragmento con el identificador de la categoría a la que pertenece
     * @param category Categoría a la que pertenece el fragmento
     * @return Nuevo fragmento
     */
    public static Fragment newInstance(Categories category) {
        Fragment fragment = new FragmentAlbums();
        Bundle bundle = new Bundle();
        bundle.putString(TYPE, category.key);
        fragment.setArguments(bundle);

        return fragment;
    }

    public FragmentAlbums() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String key = getArguments() != null ? getArguments().getString(TYPE) : "";
        category = Categories.fromKey(key);
        jamendo = new JamendoProvider();
        jamendo.getAlbumList(getContext(), albums -> {
            if (albumList != null) {
                albumList.clear();
            }
            albumList = albums;
            loadContent();
         });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_albums, container ,false);
        ButterKnife.bind(this, view);
        refresh.setColorSchemeColors(getResources().getColor(R.color.colorPrimary),
                getResources().getColor(R.color.colorPrimaryDark), getResources().getColor(R.color.colorAccent));
        refresh.setOnRefreshListener(() -> jamendo.getArtistList(getContext(), albumsResponse -> {
            albumAdapter.swapItems(albumsResponse);
            refresh.setRefreshing(false);
        }));
        return view;
    }

    @Override
    public void onAlbumClick(View view, Album album) {
        FragmentActivity activity = getActivity();
        if (activity == null){
            return;
        }
        Toast.makeText(getContext(), "hola", Toast.LENGTH_SHORT).show();
    }


    /**
     * Método que se ejecuta cuando volley obtiene los datos del API
     */
    private void loadContent() {
        albumAdapter = new AlbumAdapter(albumList, this);
        albums.setAdapter(albumAdapter);
    }
}