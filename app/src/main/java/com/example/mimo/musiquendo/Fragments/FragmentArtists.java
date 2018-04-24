package com.example.mimo.musiquendo.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mimo.musiquendo.Activities.ArtistActivity;
import com.example.mimo.musiquendo.Activities.MainActivity;
import com.example.mimo.musiquendo.Adapters.ArtistAdapter;
import com.example.mimo.musiquendo.Model.Artist;
import com.example.mimo.musiquendo.Model.Categories;
import com.example.mimo.musiquendo.Provider.JamendoProvider;
import com.example.mimo.musiquendo.R;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.flipboard.bottomsheet.commons.MenuSheetView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * fragmento que muestra la colección de artistas en la pantalla principal
 */

public class FragmentArtists extends Fragment implements ArtistAdapter.OnItemClickListener,
        MainActivity.FragmentCommunicator {

    @BindView(R.id.artists_list)
    RecyclerView artists;
    @BindView(R.id.refresh_artists_list)
    SwipeRefreshLayout refresh;
    @BindView(R.id.bottom_sheet_artists)
    BottomSheetLayout bottomSheetLayout;
    private static final String TYPE = "FragmentType";
    private List<Artist> artistList;
    private ArtistAdapter artistAdapter;
    private Categories category;
    private JamendoProvider jamendo;
    private String Title;

    /**
     * Interfaz que contiene el callback que se ejecuta cuando se reciben los datos de internet
     */
    public interface ArtistsCallback {
        void onArtistSuccess(List<Artist> artists);
    }

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

    public FragmentArtists() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String key = getArguments() != null ? getArguments().getString(TYPE) : "";
        category = Categories.fromKey(key);
        jamendo = new JamendoProvider(getContext());
        jamendo.getArtistList(artistsResponse -> {
            artistList = artistsResponse;
            loadContent();
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_artists, container, false);
        ButterKnife.bind(this, view);
        refresh.setColorSchemeColors(getResources().getColor(R.color.colorPrimary),
                getResources().getColor(R.color.colorPrimaryDark), getResources().getColor(R.color.colorAccent));
        refresh.setOnRefreshListener(() -> jamendo.getArtistList( new ArtistsCallback() {
            @Override
            public void onArtistSuccess(List<Artist> artistsResponse) {
                artistAdapter.swapItems(artistsResponse);
                refresh.setRefreshing(false);
            }
        }));

        return view;
    }

    @Override
    public void onArtistClick(View view, Artist artist) {
        FragmentActivity activity = getActivity();
        if (activity == null){
            return;
        }
        Intent artistDetail = new Intent(getContext(), ArtistActivity.class);

        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                activity,view.findViewById(R.id.artist_item_image),getString(R.string.image_transition)
        );
        ActivityCompat.startActivity(activity, artistDetail, options.toBundle());
    }

    @Override
    public void startSearch(String search) {
        String formattedSearch = search.replaceAll(" ", "%20");
        jamendo.searchArtist(formattedSearch, artistsSearch -> artistAdapter.swapItems(artistsSearch));
    }

    @Override
    public void finishSearch() {
        artistAdapter.swapItems(artistList);
    }

    @Override
    public void showMenu() {
        if (getActivity() != null) {
            String[] filtros = getResources().getStringArray(R.array.artists_filter);
            MenuSheetView menu = new MenuSheetView(getContext(), MenuSheetView.MenuType.LIST, getResources().getString(R.string.cabecera_bottomsheet), item -> {
                switch (item.getItemId()){
                    case R.id.name_artists:
                        filterArtist(filtros[0]);
                        break;
                    case R.id.popularity_total_artists:
                        filterArtist(filtros[1]);
                        break;
                    case R.id.popularity_month_artists:
                        filterArtist(filtros[2]);
                        break;
                    case R.id.popularity_week_artists:
                        filterArtist(filtros[3]);
                        break;
                }
                if (bottomSheetLayout.isSheetShowing())
                    bottomSheetLayout.dismissSheet();
                return true;
            });
            menu.inflateMenu(R.menu.artists_filter);
            bottomSheetLayout.setDefaultViewTransformer(new InsetViewTransformer());
            bottomSheetLayout.showWithSheetView(menu);
        }
    }

    /**
     * Método que llama a la clase correspondiente para obtener los artistas filtrados
     * @param filter Filtro que se va a aplicar
     */
    private void filterArtist(String filter){
        jamendo.filterArtists(filter, artistsFiltered -> artistAdapter.swapItems(artistsFiltered));
    }

    /**
     * Método que se ejecuta cuando volley obtiene los datos del API
     */
    private void loadContent() {
        artistAdapter = new ArtistAdapter(artistList, this);
        artists.setAdapter(artistAdapter);
    }
}
