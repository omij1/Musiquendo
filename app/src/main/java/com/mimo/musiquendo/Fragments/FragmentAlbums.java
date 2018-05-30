package com.mimo.musiquendo.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mimo.musiquendo.Activities.ActivityAlbum;
import com.mimo.musiquendo.Activities.ActivityMain;
import com.mimo.musiquendo.Adapters.AlbumAdapter;
import com.mimo.musiquendo.Dialogs.SimpleDialog;
import com.mimo.musiquendo.Model.Album;
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
 * Fragmento que muestra la colección de álbumes en la pantalla principal
 */

public class FragmentAlbums extends Fragment implements AlbumAdapter.OnItemClickListener,
        ActivityMain.FragmentCommunicator {

    @BindView(R.id.albums_list)
    RecyclerView albums;
    @BindView(R.id.refresh_albums_list)
    SwipeRefreshLayout refresh;
    @BindView(R.id.bottom_sheet_albums)
    BottomSheetLayout bottomSheetLayout;
    private static final String TYPE = "FragmentType";
    private static final String ID = "ID";
    private static final String NAME = "NAME";
    private static final String ARTIST = "ARTIST";
    private static final int PADDING = 4;
    private static final int COLUMNS = 2;
    private List<Album> albumList;
    private AlbumAdapter albumAdapter;
    private JamendoProvider jamendo;

    /**
     * Interfaz que contiene el callback que se ejecuta cuando se reciben los datos de internet
     */
    public interface AlbumsCallback {
        void onAlbumsSuccess(List<Album> albums);
    }


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


    public FragmentAlbums() {}


    @Override
    public void onResume() {
        super.onResume();
        checkDisplayMode();
        if (albumList != null)
            loadContent();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String key = getArguments() != null ? getArguments().getString(TYPE) : "";
        Categories category = Categories.fromKey(key);
        jamendo = new JamendoProvider(getContext());
        jamendo.getAlbumList(albumsResponse -> {
            albumList = albumsResponse;
            loadContent();
        }, this::callDialog);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_albums, container ,false);
        ButterKnife.bind(this, view);
        checkDisplayMode();
        albums.addItemDecoration(new PaddingItemDecorator(PADDING));
        refresh.setColorSchemeColors(getResources().getColor(R.color.colorPrimary),
                getResources().getColor(R.color.colorPrimaryDark), getResources().getColor(R.color.colorAccent));
        refresh.setOnRefreshListener(() -> jamendo.getAlbumList(albumsResponse -> {
            albumAdapter.swapItems(albumsResponse);
            refresh.setRefreshing(false);
        }, () -> {
            callDialog();
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
        albums.setLayoutManager(manager);
    }


    @Override
    public void onAlbumClick(View view, Album album) {
        FragmentActivity activity = getActivity();
        if (activity == null){
            return;
        }
        Intent albumDetail = new Intent(getContext(), ActivityAlbum.class);
        albumDetail.putExtra(ID, album.getId());
        albumDetail.putExtra(NAME, album.getName());
        albumDetail.putExtra(ARTIST, album.getArtist_name());

        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                activity,view.findViewById(R.id.item_image),getString(R.string.image_transition)
        );
        ActivityCompat.startActivity(activity, albumDetail, options.toBundle());
    }


    @Override
    public void startSearch(String search) {
        String formattedSearch = search.replaceAll(" ", "%20");
        jamendo.searchAlbum(formattedSearch, albumsSearch -> {
            albumAdapter.swapItems(albumsSearch);
        }, this::callDialog);
    }


    @Override
    public void finishSearch() {
        albumAdapter.swapItems(albumList);
    }


    @Override
    public void showMenu() {
        if (getActivity() != null) {
            String[] filtros = getResources().getStringArray(R.array.albums_filter);
            MenuSheetView menu = new MenuSheetView(getContext(), MenuSheetView.MenuType.LIST, getResources().getString(R.string.header_bottomsheet), item -> {
                switch (item.getItemId()){
                    case R.id.name_albums:
                        filterAlbums(filtros[0]);
                        break;
                    case R.id.popularity_total_albums:
                        filterAlbums(filtros[1]);
                        break;
                    case R.id.popularity_month_albums:
                        filterAlbums(filtros[2]);
                        break;
                    case R.id.popularity_week_albums:
                        filterAlbums(filtros[3]);
                        break;
                }
                if (bottomSheetLayout.isSheetShowing())
                    bottomSheetLayout.dismissSheet();
                return true;
            });
            menu.inflateMenu(R.menu.albums_filter);
            bottomSheetLayout.setDefaultViewTransformer(new InsetViewTransformer());
            bottomSheetLayout.showWithSheetView(menu);
        }
    }


    /**
     * Método que llama a la clase correspondiente para obtener los álbumes filtrados
     * @param filter Filtro que se va a aplicar
     */
    private void filterAlbums(String filter){
        jamendo.filterAlbums(filter, albumsFiltered -> albumAdapter.swapItems(albumsFiltered), this::callDialog);
    }


    /**
     * Método que se ejecuta cuando volley obtiene los datos del API
     */
    private void loadContent() {
        albumAdapter = new AlbumAdapter(albumList, this, getContext());
        albums.setAdapter(albumAdapter);
    }


    /**
     * Método que crea un nuevo diálogo
     */
    private void callDialog() {
        String[] dialogContent = getResources().getStringArray(R.array.lost_connection);
        SimpleDialog dialog = SimpleDialog.newInstance(R.drawable.ic_signal_wifi_off ,dialogContent[0], dialogContent[1]);
        FragmentManager fm = getFragmentManager();
        dialog.show(fm, TYPE);
    }
}