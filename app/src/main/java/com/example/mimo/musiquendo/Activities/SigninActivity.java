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
 * Actividad que permite al usuario crear una nueva cuenta
 */

public class SigninActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_sign_in)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);
        toolbar.setTitle(R.string.title_activity_signin);
        setSupportActionBar(toolbar);
        ActionBar actionBar_sigin = getSupportActionBar();
        if (actionBar_sigin != null){
            actionBar_sigin.setHomeAsUpIndicator(R.drawable.ic_arrow_back);
            actionBar_sigin.setDisplayHomeAsUpEnabled(true);
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
