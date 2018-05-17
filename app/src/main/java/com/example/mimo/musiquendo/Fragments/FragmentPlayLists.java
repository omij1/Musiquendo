package com.example.mimo.musiquendo.Fragments;

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

import com.example.mimo.musiquendo.Activities.ActivityMain;
import com.example.mimo.musiquendo.Activities.ActivityPlayList;
import com.example.mimo.musiquendo.Adapters.PlayListAdapter;
import com.example.mimo.musiquendo.Dialogs.SimpleDialog;
import com.example.mimo.musiquendo.Model.Categories;
import com.example.mimo.musiquendo.Model.PlayList;
import com.example.mimo.musiquendo.Model.SharedPreferences.PreferencesManager;
import com.example.mimo.musiquendo.Provider.JamendoProvider;
import com.example.mimo.musiquendo.R;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.flipboard.bottomsheet.commons.MenuSheetView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Fragmento que muestra la colección de listas de reproducción en la pantalla principal
 */

public class FragmentPlayLists extends Fragment implements PlayListAdapter.OnItemClickListener,
        ActivityMain.FragmentCommunicator {

    @BindView(R.id.playlists_list)
    RecyclerView playLists;
    @BindView(R.id.refresh_playlists_list)
    SwipeRefreshLayout refresh;
    @BindView(R.id.bottom_sheet_playlists)
    BottomSheetLayout bottomSheetLayout;
    private static final String TYPE = "FragmentType";
    private static final String ID = "ID";
    private static final String NAME = "NAME";
    private static final String IMAGE = "IMAGE";
    private static final int COLUMNS = 2;
    private List<PlayList> playlistsList;
    private PlayListAdapter playListAdapter;
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
    public void onResume() {
        super.onResume();
        checkDisplayMode();
        if (playlistsList != null)
            loadContent();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String key = getArguments() != null ? getArguments().getString(TYPE) : "";
        Categories category = Categories.fromKey(key);
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
        checkDisplayMode();
        playLists.addItemDecoration(new PaddingItemDecorator(4));
        refresh.setColorSchemeColors(getResources().getColor(R.color.colorPrimary),
                getResources().getColor(R.color.colorPrimaryDark), getResources().getColor(R.color.colorAccent));
        refresh.setOnRefreshListener(() -> jamendo.getPlayLists(playListResponse -> {
            playListAdapter.swapItems(playListResponse);
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
        playLists.setLayoutManager(manager);
    }


    @Override
    public void onPlayListClick(View view, PlayList playList) {
        FragmentActivity activity = getActivity();
        if (activity == null){
            return;
        }
        Intent playlistDetail = new Intent(getContext(), ActivityPlayList.class);
        playlistDetail.putExtra(ID, playList.getId());
        playlistDetail.putExtra(NAME, playList.getName());
        playlistDetail.putExtra(IMAGE, playList.getCover());

        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                activity,view.findViewById(R.id.item_image),getString(R.string.image_transition));
        ActivityCompat.startActivity(activity, playlistDetail, options.toBundle());
    }


    @Override
    public void onDownloadItemClick(PlayList playList) {

    }


    @Override
    public void startSearch(String search) {
        String formattedSearch = search.replaceAll(" ", "%20");
        jamendo.searchPlayList(formattedSearch, playListListSearch -> playListAdapter.swapItems(playListListSearch), this::callDialog);
    }


    @Override
    public void finishSearch() {
        playListAdapter.swapItems(playlistsList);
    }


    @Override
    public void showMenu() {
        if (getActivity() != null) {
            String[] filtros = getResources().getStringArray(R.array.playlists_filter);
            MenuSheetView menu = new MenuSheetView(getContext(), MenuSheetView.MenuType.LIST, getString(R.string.header_bottomsheet), item -> {
                switch (item.getItemId()){
                    case R.id.name_playlists:
                        filterPlaylist(filtros[0]);
                        break;
                    case R.id.creationdate_playlists:
                        filterPlaylist(filtros[1]);
                        break;
                }
                if (bottomSheetLayout.isSheetShowing())
                    bottomSheetLayout.dismissSheet();
                return true;
            });
            menu.inflateMenu(R.menu.playlists_filter);
            bottomSheetLayout.setDefaultViewTransformer(new InsetViewTransformer());
            bottomSheetLayout.showWithSheetView(menu);
        }
    }


    /**
     * Método que llama a la clase correspondiente para obtener las listas de reproducción filtradas
     * @param filter Filtro que se va a aplicar
     */
    private void filterPlaylist(String filter){
        jamendo.filterPlaylists(filter, playlistsFiltered -> playListAdapter.swapItems(playlistsFiltered), this::callDialog);
    }


    /**
     * Método que se ejecuta cuando volley obtiene los datos del API
     */
    private void loadContent() {
        playListAdapter = new PlayListAdapter(playlistsList, this, getContext());
        playLists.setAdapter(playListAdapter);
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
