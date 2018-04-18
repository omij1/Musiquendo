package com.example.mimo.musiquendo.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mimo.musiquendo.Model.Categories;
import com.example.mimo.musiquendo.R;

/**
 * fragmento que muestra la colección de artistas en la pantalla principal
 */

public class FragmentArtists extends Fragment {

    private static final String TYPE = "FragmentType";

    /**
     * Función que crea un nuevo fragmento con el identificador de la categoría a la que pertenece
     * @param category Categoría a la que pertenece el fragmento
     * @return Nuevo fragmento
     */
    public static Fragment newInstance(Categories category) {
        Fragment fragment = new FragmentArtists();
        Bundle bundle = new Bundle();
        bundle.putString(TYPE, category.key);
        fragment.setArguments(bundle);
        return fragment;
    }

    public FragmentArtists() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_artists, container, false);

        return view;
    }
}
