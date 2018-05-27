package com.example.mimo.musiquendo.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.example.mimo.musiquendo.Fragments.FragmentAlbums;
import com.example.mimo.musiquendo.Fragments.FragmentArtists;
import com.example.mimo.musiquendo.Fragments.FragmentPlayLists;
import com.example.mimo.musiquendo.Model.Categories;

/**
 * Adaptador del TabLayout principal que se encarga de cambiar los fragment según el tab seleccionado
 */

public class CategoriesAdapter extends FragmentStatePagerAdapter {

    private Fragment current;


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
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        if (current != object){
            current = (Fragment) object;
        }
        super.setPrimaryItem(container, position, object);
    }


    public Fragment getCurrent() {
        return current;
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
