package com.example.mimo.musiquendo.Activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.mimo.musiquendo.Fragments.FragmentArtistDetail;
import com.example.mimo.musiquendo.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Actividad que representa el detalle de un artista
 */

public class ActivityArtist extends AppCompatActivity {

    @BindView(R.id.toolbar_artist)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist);
        ButterKnife.bind(this);
        toolbar.setTitle(R.string.title_activity_artist);
        setSupportActionBar(toolbar);
        ActionBar actionBar_artist = getSupportActionBar();
        if (actionBar_artist != null){
            actionBar_artist.setHomeAsUpIndicator(R.drawable.ic_arrow_back);
            actionBar_artist.setDisplayHomeAsUpEnabled(true);
        }
        Intent intent = getIntent();
        FragmentArtistDetail artistDetail = FragmentArtistDetail.newInstance(intent.getExtras().get("ID").toString(),
                intent.getExtras().get("NAME").toString(), intent.getExtras().get("JOIN").toString(),
                intent.getExtras().get("WEB").toString());
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.artist_detail, artistDetail, null).commit();
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
