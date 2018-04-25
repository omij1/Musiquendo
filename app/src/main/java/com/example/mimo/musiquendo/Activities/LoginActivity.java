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
 * Actividad que permite al usuario loguearse
 */

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_login)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        toolbar.setTitle(R.string.title_activity_login);
        setSupportActionBar(toolbar);
        ActionBar actionBar_login = getSupportActionBar();
        if (actionBar_login != null){
            actionBar_login.setHomeAsUpIndicator(R.drawable.ic_arrow_back);
            actionBar_login.setDisplayHomeAsUpEnabled(true);
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
