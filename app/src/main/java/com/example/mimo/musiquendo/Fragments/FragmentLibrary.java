package com.example.mimo.musiquendo.Fragments;

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
import com.example.mimo.musiquendo.Model.DataBase.AppDatabase;
import com.example.mimo.musiquendo.Model.DataBase.DownloadItem;
import com.example.mimo.musiquendo.R;

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
    public void onDownloadItemClick(View view, DownloadItem album) {

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
            adapter.setData(downloadItems);
            adapter.notifyDataSetChanged();
        }
    }
}
