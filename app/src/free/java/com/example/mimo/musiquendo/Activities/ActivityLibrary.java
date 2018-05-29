package com.example.mimo.musiquendo.Activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.mimo.musiquendo.Dialogs.SimpleDialog;
import com.example.mimo.musiquendo.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Actividad que muestra la biblioteca del usuario
 */

public class ActivityLibrary extends AppCompatActivity implements SimpleDialog.DialogListener {

    @BindView(R.id.toolbar_library)
    Toolbar toolbar;
    private static final String FREE = "FREE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);
        ButterKnife.bind(this);
        toolbar.setTitle(R.string.title_activity_library);
        setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar_library = getSupportActionBar();
        if (actionBar_library != null){
            actionBar_library.setHomeAsUpIndicator(R.drawable.ic_arrow_back);
            actionBar_library.setDisplayHomeAsUpEnabled(true);
        }
        crearDialog();
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


    @Override
    public void crearDialog() {
        SimpleDialog dialog = SimpleDialog.newInstance(R.drawable.ic_sad,
                getString(R.string.not_free_available),getString(R.string.not_free_feature));
        dialog.show(getSupportFragmentManager(), FREE);
    }
}
