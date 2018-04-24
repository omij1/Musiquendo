package com.example.mimo.musiquendo.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.mimo.musiquendo.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArtistActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_artist)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist);
        ButterKnife.bind(this);
        toolbar.setTitle(R.string.artist_activity_name);
    }
}
