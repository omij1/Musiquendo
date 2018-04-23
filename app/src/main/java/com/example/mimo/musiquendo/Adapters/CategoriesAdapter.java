package com.example.mimo.musiquendo.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.mimo.musiquendo.Fragments.FragmentAlbums;
import com.example.mimo.musiquendo.Fragments.FragmentArtists;
import com.example.mimo.musiquendo.Fragments.FragmentPlayLists;
import com.example.mimo.musiquendo.Model.Categories;

import java.util.ArrayList;
import java.util.List;

/**
 * Adaptador del TabLayout principal que se encarga de cambiar los fragment según el tab seleccionado
 */
public class CategoriesAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> currentFragments = new ArrayList<Fragment>();

    public CategoriesAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                currentFragments.add(0, FragmentAlbums.newInstance(getDataType(position)));
                return currentFragments.get(0);
            case 1:
                currentFragments.add(1, FragmentArtists.newInstance(getDataType(position)));
                return currentFragments.get(1);
            case 2:
                currentFragments.add(2, FragmentPlayLists.newInstance(getDataType(position)));
                return currentFragments.get(2);
        }
        return null;
    }

    public Fragment getCurrentFragment(int position) {
        return currentFragments.get(position);
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
