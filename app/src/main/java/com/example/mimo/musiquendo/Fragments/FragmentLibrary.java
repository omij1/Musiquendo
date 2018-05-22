package com.example.mimo.musiquendo.Fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mimo.musiquendo.Adapters.LibraryAdapter;
import com.example.mimo.musiquendo.BuildConfig;
import com.example.mimo.musiquendo.Model.DataBase.AppDatabase;
import com.example.mimo.musiquendo.Model.DataBase.DownloadItem;
import com.example.mimo.musiquendo.Model.SharedPreferences.PreferencesManager;
import com.example.mimo.musiquendo.Model.Track;
import com.example.mimo.musiquendo.Player.TrackPlayer;
import com.example.mimo.musiquendo.Player.TrackQueue;
import com.example.mimo.musiquendo.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Fragmento que muestra las descargas del usuario
 */

public class FragmentLibrary extends Fragment implements LibraryAdapter.OnItemClickListener{

    @BindView(R.id.download_list)
    RecyclerView downloadList;
    private static AppDatabase database;
    private static LibraryAdapter adapter;
    private static List<DownloadItem> downloadItemList;
    private PreferencesManager preferencesManager;
    private GetDownloadedTracks downloadedTracks;


    public static FragmentLibrary newInstance() {
        return new FragmentLibrary();
    }


    public FragmentLibrary() {}


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_library, container, false);
        ButterKnife.bind(this, view);
        adapter = new LibraryAdapter(this);
        downloadList.setAdapter(adapter);
        database = AppDatabase.getInstance(getContext());
        downloadedTracks = new GetDownloadedTracks();
        downloadedTracks.execute();

        return view;
    }


    @Override
    public void onDownloadItemClick(View view, DownloadItem item) {
        //Se reproduce la canción correspondiente
        Intent playTrack = new Intent(getActivity(), TrackPlayer.class);
        playTrack.setAction(BuildConfig.PLAY);
        TrackQueue.getInstance().setSection(BuildConfig.DOWNLOADS);
        if (preferencesManager.getPlaylistMode())
            automaticMode(playTrack);
        else
            normalMode(playTrack, item);
    }


    private void automaticMode(Intent playTrack) {
        /*List<Track> trackList = new ArrayList<>();
        for (AlbumTracks track : tracks) {
            trackList.add(new Track(track.getAudio(), track.getTrackName(),
                    getArguments().getString(NAME), track.getTrackDuration(),track.getMinutes()));
        }
        TrackQueue.getInstance().addTrackList(trackList, position);
        getActivity().startService(playTrack);*/
    }


    private void normalMode(Intent playTrack, DownloadItem item) {
        /*TrackQueue.getInstance().addTrack(new Track(item.getPath(), item.getName(),
                getArguments().getString(NAME), track.getTrackDuration(), track.getMinutes()));*/
        getActivity().startService(playTrack);
    }


    @Override
    public void onStop() {
        if (downloadedTracks != null)
            downloadedTracks.cancel(true);
        super.onStop();
    }


    private static class GetDownloadedTracks extends AsyncTask<Void, Void, List<DownloadItem>> {

        @Override
        protected List<DownloadItem> doInBackground(Void... voids) {
            return database.downloadsDAO().getAll();
        }


        @Override
        protected void onPostExecute(List<DownloadItem> downloadItems) {
            super.onPostExecute(downloadItems);
            downloadItemList = downloadItems;
            adapter.setData(downloadItems);
            adapter.notifyDataSetChanged();
        }
    }
}
