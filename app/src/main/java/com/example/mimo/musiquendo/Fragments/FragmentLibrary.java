package com.example.mimo.musiquendo.Fragments;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mimo.musiquendo.Adapters.LibraryAdapter;
import com.example.mimo.musiquendo.BuildConfig;
import com.example.mimo.musiquendo.Model.DataBase.AppDatabase;
import com.example.mimo.musiquendo.Model.DataBase.DownloadItem;
import com.example.mimo.musiquendo.Model.SharedPreferences.PreferencesManager;
import com.example.mimo.musiquendo.Model.Track;
import com.example.mimo.musiquendo.Player.TrackPlayer;
import com.example.mimo.musiquendo.Player.TrackQueue;
import com.example.mimo.musiquendo.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Fragmento que muestra las descargas del usuario
 */

public class FragmentLibrary extends Fragment implements LibraryAdapter.OnItemClickListener {

    @BindView(R.id.download_list)
    RecyclerView downloadList;
    private static AppDatabase database;
    private static LibraryAdapter adapter;
    private static List<DownloadItem> downloadItemList;
    private PreferencesManager preferencesManager;
    private GetDownloadedTracks downloadedTracks;
    private DeleteDownloadedItem deleteItem;


    public static FragmentLibrary newInstance() {
        return new FragmentLibrary();
    }


    public FragmentLibrary() {}


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferencesManager = new PreferencesManager(getContext());
        downloadItemList = new ArrayList<>();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_library, container, false);
        ButterKnife.bind(this, view);
        adapter = new LibraryAdapter(this);
        registerForContextMenu(downloadList);
        downloadList.setAdapter(adapter);
        database = AppDatabase.getInstance(getContext());

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        downloadedTracks = new GetDownloadedTracks();
        downloadedTracks.execute(getContext());
    }


    @Override
    public void onDownloadItemClick(View view, DownloadItem track, int position) {
        checkPlayMode(track, position);
    }


    @Override
    public boolean onLongItemClick(View view, DownloadItem track, int position) {
        PopupMenu popupMenu = new PopupMenu(getContext(), view, Gravity.CENTER, 0, R.style.PopUpMenu);
        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.play_track:
                    checkPlayMode(track, position);
                    return true;
                case R.id.delete_track:
                    if (isMyServiceRunning(TrackPlayer.class) && TrackQueue.SECTION.equals(BuildConfig.DOWNLOADED_TRACKS))
                        Toast.makeText(getContext(), R.string.delete_track_warning, Toast.LENGTH_SHORT).show();
                    else
                        deleteFileFromDirectory(track, position);
            }
            return true;
        });
        popupMenu.inflate(R.menu.library_item_options);
        popupMenu.show();
        return true;
    }


    /**
     * Método que se encarga de borrar una canción del directorio de descargas
     * @param track Canción que se desea eliminar
     * @param position Posición de la canción dentro de la lista
     */
    private void deleteFileFromDirectory(DownloadItem track, int position) {
        File file = new File(track.getPath());
        if (file.delete()) {
            deleteItem = new DeleteDownloadedItem(position);
            deleteItem.execute(track);
        }
        else
            Toast.makeText(getContext(), R.string.error_deleting_track, Toast.LENGTH_SHORT).show();
    }


    /**
     * Método que permite conocer si el servicio que se encarga de reproducir las canciones está activo
     * @param serviceClass Servicio del que se desea conocer su estado
     * @return Verdadero si el servicio está activo y falso en caso contrario
     */
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getContext().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    /**
     * Método que comprueba el modo de reproducción actual y redirige a los métodos correspondientes
     * @param track Canción que debe reproducirse
     * @param position Posición de la canción seleccionada
     */
    private void checkPlayMode(DownloadItem track, int position) {
        //Se reproduce la canción correspondiente
        Intent playTrack = new Intent(getActivity(), TrackPlayer.class);
        playTrack.setAction(BuildConfig.PLAY);
        TrackQueue.getInstance().setSection(BuildConfig.DOWNLOADED_TRACKS);
        if (preferencesManager.getPlaylistMode())
            automaticMode(playTrack, position);
        else
            normalMode(playTrack, track);
    }


    private void automaticMode(Intent playTrack, int position) {
        List<Track> trackList = new ArrayList<>();
        for (DownloadItem item : downloadItemList) {
            trackList.add(new Track(item.getPath(), item.getTrackName(),
                    item.getParentName(), item.getDuration(),item.getMinutes()));
        }
        TrackQueue.getInstance().addTrackList(trackList, position);
        getActivity().startService(playTrack);
    }


    private void normalMode(Intent playTrack, DownloadItem item) {
        TrackQueue.getInstance().addTrack(new Track(item.getPath(), item.getTrackName(),
                item.getParentName(), item.getDuration(), item.getMinutes()));
        getActivity().startService(playTrack);
    }


    @Override
    public void onStop() {
        if (downloadedTracks != null)
            downloadedTracks.cancel(true);
        if (deleteItem != null)
            deleteItem.cancel(true);
        super.onStop();
    }


    private static class GetDownloadedTracks extends AsyncTask<Context, Void, List<DownloadItem>> {

        @Override
        protected List<DownloadItem> doInBackground(Context... contexts) {
            checkDatabaseContent(contexts[0]);
            return database.downloadsDAO().getAll();
        }


        /**
         * Método que se encarga de comprobar si el contenido de la base de datos está actualizado
         * respecto a las canciones que se encuentran descargadas
         * @param context Contexto para acceder a la base de datos
         */
        private void checkDatabaseContent(Context context) {
            List<DownloadItem> itemList = database.downloadsDAO().getAll();
            String[] list = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).list();

            for (DownloadItem item: itemList) {
                if (!Arrays.asList(list).contains(item.getTrackName().concat(".mp3")))
                    database.downloadsDAO().deleteDownload(item);
            }
        }


        @Override
        protected void onPostExecute(List<DownloadItem> downloadItems) {
            super.onPostExecute(downloadItems);
            downloadItemList = downloadItems;
            adapter.setData(downloadItemList);
            adapter.notifyDataSetChanged();
        }
    }

    private static class DeleteDownloadedItem extends AsyncTask<DownloadItem,Void,Void> {

        private int itemPosition;

        DeleteDownloadedItem(int position) {
            this.itemPosition = position;
        }


        @Override
        protected Void doInBackground(DownloadItem... downloadItems) {
            database.downloadsDAO().deleteDownload(downloadItems[0]);

            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            downloadItemList.remove(itemPosition);
            adapter.setData(downloadItemList);
            adapter.notifyDataSetChanged();
        }
    }
}
