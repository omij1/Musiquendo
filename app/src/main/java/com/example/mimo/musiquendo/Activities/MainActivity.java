package com.example.mimo.musiquendo.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.mimo.musiquendo.Adapters.CategoriesAdapter;
import com.example.mimo.musiquendo.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer)
    DrawerLayout drawer;
    @BindView(R.id.tabs)
    TabLayout tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        ViewPager pager = findViewById(R.id.pager);
        pager.setAdapter(new CategoriesAdapter(getSupportFragmentManager()));
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
        tabs.setupWithViewPager(pager);

        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_items, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawer.openDrawer(GravityCompat.START);
            return true;
            case R.id.search:
                Toast.makeText(this, "hola", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.filter:
                Toast.makeText(this, "hola", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.biblioteca:
                drawer.closeDrawers();
                Intent library = new Intent(this, LibraryActivity.class);
                startActivity(library);
                return true;
            case R.id.registro:
                drawer.closeDrawers();
                Intent sign_in = new Intent(this, SigninActivity.class);
                startActivity(sign_in);
                return true;
            case R.id.login:
                drawer.closeDrawers();
                Intent login = new Intent(this, LoginActivity.class);
                startActivity(login);
                return true;
            case R.id.ajustes:
                drawer.closeDrawers();
                Intent settings = new Intent(this, SettingsActivity.class);
                startActivity(settings);
                return true;
            case R.id.licencias:
                drawer.closeDrawers();
                Intent licenses = new Intent(this, LicensesActivity.class);
                startActivity(licenses);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return onOptionsItemSelected(item);
    }
}
