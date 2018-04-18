package com.example.mimo.musiquendo.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.mimo.musiquendo.Fragments.FragmentAlbums;
import com.example.mimo.musiquendo.Fragments.FragmentArtists;
import com.example.mimo.musiquendo.Fragments.FragmentPlayLists;
import com.example.mimo.musiquendo.Model.Categories;

/**
 * Adaptador del TabLayout principal que se encarga de cambiar los fragment según el tab seleccionado
 */
public class CategoriesAdapter extends FragmentStatePagerAdapter {

    public CategoriesAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return FragmentAlbums.newInstance(getDataType(position));
            case 1:
                return FragmentArtists.newInstance(getDataType(position));
            case 2:
                return FragmentPlayLists.newInstance(getDataType(position));
        }
        return null;
    }

    @Override
    public int getCount() {
        return Categories.values().length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return getDataType(position).key;
    }

    private Categories getDataType(int position) {
        return Categories.values()[position];
    }
}
