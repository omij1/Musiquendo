package com.example.mimo.musiquendo.Activities;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.mimo.musiquendo.Fragments.FragmentAlbumDetail;
import com.example.mimo.musiquendo.R;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Actividad que representa el detalle de un álbum
 */

public class ActivityAlbum extends AppCompatActivity {

    @BindView(R.id.toolbar_album)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        ButterKnife.bind(this);
        toolbar.setTitle(getString(R.string.title_activity_album));
        setSupportActionBar(toolbar);
        ActionBar actionBar_album = getSupportActionBar();
        if (actionBar_album != null){
            actionBar_album.setHomeAsUpIndicator(R.drawable.ic_arrow_back);
            actionBar_album.setDisplayHomeAsUpEnabled(true);
        }
        Intent intent = getIntent();
        FragmentAlbumDetail albumDetail = FragmentAlbumDetail.newInstance(intent.getExtras().get("ID").toString(),
                intent.getExtras().get("NAME").toString(), intent.getExtras().get("ARTIST").toString());
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.album_detail, albumDetail, null).commit();
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
