package com.mimo.musiquendo.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mimo.musiquendo.Activities.ActivityArtist;
import com.mimo.musiquendo.Activities.ActivityMain;
import com.mimo.musiquendo.Adapters.ArtistAdapter;
import com.mimo.musiquendo.Dialogs.SimpleDialog;
import com.mimo.musiquendo.Model.Artist;
import com.mimo.musiquendo.Model.Categories;
import com.mimo.musiquendo.Model.SharedPreferences.PreferencesManager;
import com.mimo.musiquendo.Provider.JamendoProvider;
import com.mimo.musiquendo.R;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.flipboard.bottomsheet.commons.MenuSheetView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Fragmento que muestra la colección de artistas en la pantalla principal
 */

public class FragmentArtists extends Fragment implements ArtistAdapter.OnItemClickListener,
        ActivityMain.FragmentCommunicator {

    @BindView(R.id.artists_list)
    RecyclerView artists;
    @BindView(R.id.refresh_artists_list)
    SwipeRefreshLayout refresh;
    @BindView(R.id.bottom_sheet_artists)
    BottomSheetLayout bottomSheetLayout;
    private static final String TYPE = "FragmentType";
    private static final String ID = "ID";
    private static final String NAME = "NAME";
    private static final String JOIN = "JOIN";
    private static final String WEB = "WEB";
    private static final int PADDING = 4;
    private static final int COLUMNS = 2;
    private List<Artist> artistList;
    private ArtistAdapter artistAdapter;
    private JamendoProvider jamendo;

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
    public void onResume() {
        super.onResume();
        checkDisplayMode();
        if (artistList != null)
            loadContent();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String key = getArguments() != null ? getArguments().getString(TYPE) : "";
        Categories category = Categories.fromKey(key);
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
        checkDisplayMode();
        artists.addItemDecoration(new PaddingItemDecorator(PADDING));
        refresh.setColorSchemeColors(getResources().getColor(R.color.colorPrimary),
                getResources().getColor(R.color.colorPrimaryDark), getResources().getColor(R.color.colorAccent));
        refresh.setOnRefreshListener(() -> jamendo.getArtistList(artistsResponse -> {
            artistAdapter.swapItems(artistsResponse);
            refresh.setRefreshing(false);
        }));

        return view;
    }


    /**
     * Método que cambia el layoutmanager del recycler en función del tipo de vista elegido en los ajustes
     */
    private void checkDisplayMode() {
        RecyclerView.LayoutManager manager;
        PreferencesManager preferences = new PreferencesManager(getContext());
        manager = preferences.getDisplayMode().equals(getString(R.string.grid)) ?
                new GridLayoutManager(getContext(), COLUMNS) : new LinearLayoutManager(getContext());
        artists.setLayoutManager(manager);
    }


    @Override
    public void onArtistClick(View view, Artist artist) {
        FragmentActivity activity = getActivity();
        if (activity == null){
            return;
        }
        Intent artistDetail = new Intent(getContext(), ActivityArtist.class);
        artistDetail.putExtra(ID, artist.getId());
        artistDetail.putExtra(NAME, artist.getName());
        artistDetail.putExtra(JOIN, artist.getJoindate());
        artistDetail.putExtra(WEB, artist.getWebsite());

        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                activity,view.findViewById(R.id.item_image),getString(R.string.image_transition));
        ActivityCompat.startActivity(activity, artistDetail, options.toBundle());
    }


    @Override
    public void startSearch(String search) {
        String formattedSearch = search.replaceAll(" ", "%20");
        jamendo.searchArtist(formattedSearch, artistsSearch -> artistAdapter.swapItems(artistsSearch), this::callDialog);
    }


    @Override
    public void finishSearch() {
        artistAdapter.swapItems(artistList);
    }


    @Override
    public void showMenu() {
        if (getActivity() != null) {
            String[] filtros = getResources().getStringArray(R.array.artists_filter);
            MenuSheetView menu = new MenuSheetView(getContext(), MenuSheetView.MenuType.LIST, getResources().getString(R.string.header_bottomsheet), item -> {
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
        jamendo.filterArtists(filter, artistsFiltered -> artistAdapter.swapItems(artistsFiltered), this::callDialog);
    }


    /**
     * Método que se ejecuta cuando volley obtiene los datos del API
     */
    private void loadContent() {
        artistAdapter = new ArtistAdapter(artistList, this, getContext());
        artists.setAdapter(artistAdapter);
    }


    /**
     * Método que crea un nuevo diálogo
     */
    private void callDialog() {
        String[] dialogContent = getResources().getStringArray(R.array.lost_connection);
        SimpleDialog dialog = SimpleDialog.newInstance(R.drawable.ic_signal_wifi_off, dialogContent[0], dialogContent[1]);
        FragmentManager fm = getFragmentManager();
        dialog.show(fm, TYPE);
    }
}
