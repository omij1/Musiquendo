package com.example.mimo.musiquendo.Activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.mimo.musiquendo.Fragments.FragmentPlaylistDetail;
import com.example.mimo.musiquendo.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Actividad que muestra el detalle de una lista de reproducción
 */

public class ActivityPlayList extends AppCompatActivity {

    @BindView(R.id.toolbar_playlist)
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_list);
        ButterKnife.bind(this);
        toolbar.setTitle(R.string.title_activity_playlist);
        setSupportActionBar(toolbar);
        ActionBar actionBar_playlist = getSupportActionBar();
        if (actionBar_playlist != null){
            actionBar_playlist.setHomeAsUpIndicator(R.drawable.ic_arrow_back);
            actionBar_playlist.setDisplayHomeAsUpEnabled(true);
        }
        Intent intent = getIntent();
        FragmentPlaylistDetail playlistDetail = FragmentPlaylistDetail.newInstance(intent.getExtras().get("ID").toString(),
                intent.getExtras().get("NAME").toString(), intent.getExtras().get("IMAGE").toString());
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.playlist_detail, playlistDetail, null).commit();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
