package com.example.mimo.musiquendo.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.mimo.musiquendo.Fragments.FragmentAlbumes;
import com.example.mimo.musiquendo.Model.Categories;

public class CategoriesAdapter extends FragmentStatePagerAdapter {

    public CategoriesAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return FragmentAlbumes.newInstance(getDataType(position));
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
