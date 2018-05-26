package com.example.mimo.musiquendo.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.mimo.musiquendo.Adapters.CategoriesAdapter;
import com.example.mimo.musiquendo.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Actividad principal de la aplicación
 */

public class ActivityMain extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer)
    DrawerLayout drawer;
    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.pager)
    ViewPager pager;
    private CategoriesAdapter adapter;


    public interface FragmentCommunicator {
        void startSearch(String search);
        void finishSearch();
        void showMenu();
    }


    public FragmentCommunicator fragmentCommunicator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        adapter = new CategoriesAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
        tabs.setupWithViewPager(pager);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(new DrawerArrowDrawable(toolbar.getContext()));
        }

        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_items, menu);
        searchData(menu);
        return super.onCreateOptionsMenu(menu);
    }


    /**
     * Método que contiene la lógica necesaria para poder hacer búsquedas
     * @param menu Elemento que abre la barra de búsqueda
     */
    private void searchData(Menu menu) {
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                /*Instancio el fragmentCommunicator cada vez que se busca porque puedo estar en tres
                fragmentos diferentes. A lo mejor la primera búsqueda es de álbumes y la siguiente de artistas
                por lo que hay que cambiar el fragmento al que se debe llamar*/
                fragmentCommunicator = (FragmentCommunicator) adapter.getCurrent();
                if (fragmentCommunicator != null)
                    fragmentCommunicator.startSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchView.setOnCloseListener(() -> {
            fragmentCommunicator.finishSearch();
            return false;
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawer.openDrawer(GravityCompat.START);
                return true;
            case R.id.filter:
                fragmentCommunicator = (FragmentCommunicator) adapter.getCurrent();
                    if (fragmentCommunicator != null)
                        fragmentCommunicator.showMenu();
                return true;
            case R.id.library_menu:
                drawer.closeDrawers();
                Intent library = new Intent(this, ActivityLibrary.class);
                ActivityCompat.startActivity(this, library, null);
                return true;
            case R.id.settings_menu:
                drawer.closeDrawers();
                Intent settings = new Intent(this, ActivitySettings.class);
                ActivityCompat.startActivity(this, settings, null);
                return true;
            case R.id.licenses_menu:
                drawer.closeDrawers();
                Intent licenses = new Intent(this, ActivityLicenses.class);
                ActivityCompat.startActivity(this, licenses, null);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return onOptionsItemSelected(item);
    }



}
