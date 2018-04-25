package com.example.mimo.musiquendo.Activities;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.mimo.musiquendo.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Actividad que muestra las licencias de la aplicación
 */

public class LicensesActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_licenses)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_licenses);
        ButterKnife.bind(this);
        toolbar.setTitle(R.string.title_activity_licenses);
        setSupportActionBar(toolbar);
        ActionBar actionBar_album = getSupportActionBar();
        if (actionBar_album != null){
            actionBar_album.setHomeAsUpIndicator(R.drawable.ic_arrow_back);
            actionBar_album.setDisplayHomeAsUpEnabled(true);
        }
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
