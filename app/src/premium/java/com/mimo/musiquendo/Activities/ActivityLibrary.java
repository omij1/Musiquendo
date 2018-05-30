package com.mimo.musiquendo.Activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.mimo.musiquendo.Fragments.FragmentLibrary;
import com.mimo.musiquendo.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Actividad que muestra la biblioteca del usuario
 */

public class ActivityLibrary extends AppCompatActivity {

    @BindView(R.id.toolbar_library)
    Toolbar toolbar;
    private static final int REQUEST_CODE = 1;


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
        checkReadPermission();
    }


    private void checkReadPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                loadFragment();
            else
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE);
        }
        else {
            loadFragment();
        }
    }


    /**
     * Método que carga el fragmento que muestra las canciones descargadas
     */
    private void loadFragment() {
        FragmentLibrary library = FragmentLibrary.newInstance();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.library_detail, library, null).commit();
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            loadFragment();
    }
}
