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
 * Clase que se encarga de descargar las canciones
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
        database = AppDatabase.getInstance(mContext);
        if (!trackAlreadyDownloaded(strings[1])){
            downloadTrack(strings);
            return true;
        }
        else
            return false;
    }


    private boolean trackAlreadyDownloaded(String trackName) {
         return database.downloadsDAO().getTrack(trackName).size() > 0;
    }


    private void downloadTrack(String[] strings) {
        long refId;
        String downloadUrl = strings[0];
        String trackName = strings[1];
        String parentName = strings[2];
        int trackDuration = Integer.parseInt(strings[3]);
        String minutes = strings[4];
        String cover = strings[5];
        String trackPath = mContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath().concat("/").concat(trackName).concat(".mp3");

        DownloadManager downloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri downloadUri= Uri.parse(downloadUrl);
        DownloadManager.Request request = new DownloadManager.Request(downloadUri);

        request.setAllowedOverRoaming(false);
        request.setVisibleInDownloadsUi(true);
        request.setTitle(trackName);
        request.setDescription(mContext.getString(R.string.downloading_song));
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.allowScanningByMediaScanner();
        request.setDestinationInExternalFilesDir(mContext, Environment.DIRECTORY_DOWNLOADS, trackName.concat(".mp3"));

        if (downloadManager != null) refId = downloadManager.enqueue(request);
        database.downloadsDAO().insertDownload(new DownloadItem(trackPath, trackName, parentName, trackDuration, minutes, cover));
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    @Override
    protected void onPostExecute(Boolean state) {
        if (!state)//Si el estado es falso la canción ya existía
            Toast.makeText(mContext, R.string.track_already_downloaded, Toast.LENGTH_SHORT).show();
        super.onPostExecute(state);
    }


    @Override
    protected void onCancelled() {
        super.onCancelled();
    }
}
