package com.example.mimo.musiquendo.DownloadManager;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

import com.example.mimo.musiquendo.Model.DataBase.AppDatabase;
import com.example.mimo.musiquendo.Model.DataBase.DownloadItem;
import com.example.mimo.musiquendo.R;

/**
 *Clase que se encarga de descargar las canciones
 */

public class Downloader extends AsyncTask<String, Void, Boolean> {

    @SuppressLint("StaticFieldLeak")
    private Context mContext;
    private AppDatabase database;

    
    public Downloader(Context context) {
        this.mContext = context;
    }


    @Override
    protected Boolean doInBackground(String... strings) {
        String downloadUrl = strings[0];
        String trackName = strings[1];
        String cover = strings[2];

        database = AppDatabase.getInstance(mContext);
        if (!trackAlreadyDownloaded(trackName)){
            downloadTrack(downloadUrl, trackName, cover);
            return true;
        }
        else
            return false;
    }


    private boolean trackAlreadyDownloaded(String trackName) {
         return database.downloadsDAO().getTrack(trackName).size() > 0;
    }


    private void downloadTrack(String downloadUrl, String trackName, String cover) {
        long refId;
        DownloadManager downloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri downloadUri= Uri.parse(downloadUrl);
        DownloadManager.Request request = new DownloadManager.Request(downloadUri);

        request.setAllowedOverRoaming(false);
        request.setVisibleInDownloadsUi(true);
        request.setTitle(trackName);
        request.setDescription(mContext.getString(R.string.downloading_song));
        request.setDestinationInExternalFilesDir(mContext, Environment.DIRECTORY_DOWNLOADS, trackName);
        if (downloadManager != null) refId = downloadManager.enqueue(request);
        database.downloadsDAO().insertDownload(new DownloadItem(Environment.DIRECTORY_DOWNLOADS+"/"+trackName,
                trackName, cover));
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    @Override
    protected void onPostExecute(Boolean state) {
        if (!state)
            Toast.makeText(mContext, R.string.track_already_downloaded, Toast.LENGTH_SHORT).show();
        super.onPostExecute(state);
    }


    @Override
    protected void onCancelled() {
        super.onCancelled();
    }
}
