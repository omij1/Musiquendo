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
 * Actividad que muestra los ajustes de la aplicación
 */

public class ActivitySettings extends AppCompatActivity {

    @BindView(R.id.toolbar_settings)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        toolbar.setTitle(R.string.title_activity_settings);
        setSupportActionBar(toolbar);
        ActionBar actionBar_settings = getSupportActionBar();
        if (actionBar_settings != null){
            actionBar_settings.setHomeAsUpIndicator(R.drawable.ic_arrow_back);
            actionBar_settings.setDisplayHomeAsUpEnabled(true);
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
