package com.example.mimo.musiquendo.DownloadManager;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.example.mimo.musiquendo.R;

import java.io.File;
import java.util.Arrays;

/**
 *Clase que se encarga de descargar las canciones
 */

public class Downloader extends AsyncTask<String, Void, String> {

    @SuppressLint("StaticFieldLeak")
    private Context mContext;

    public Downloader(Context context) {
        this.mContext = context;
    }

    @Override
    protected String doInBackground(String... strings) {
        long refid = 0;
        DownloadManager downloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri downloadUri= Uri.parse(strings[0]);
        DownloadManager.Request request = new DownloadManager.Request(downloadUri);

        request.setAllowedOverRoaming(false);
        request.setVisibleInDownloadsUi(true);
        request.setTitle(strings[1]);
        request.setDescription(mContext.getString(R.string.downloading_song));
        request.setDestinationInExternalFilesDir(mContext, Environment.DIRECTORY_DOWNLOADS, strings[1]);
        if (downloadManager != null) refid = downloadManager.enqueue(request);

        return strings[1];
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    @Override
    protected void onPostExecute(String aLong) {
        String[] list = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).list();
        Log.d("RUTA", "onPostExecute: "+ Arrays.toString(mContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).list()));
        super.onPostExecute(aLong);
    }


    @Override
    protected void onCancelled() {
        super.onCancelled();
    }
}
