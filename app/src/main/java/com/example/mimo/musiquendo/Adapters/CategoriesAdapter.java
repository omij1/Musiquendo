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
        /*Los ifs son para arreglar un bug que provocaba que al llegar a la ultima tab, que es listas,
        * y volver a la tab anterior se creasen nuevas instancias de fragmentos y se reemplazase su
        * posición en el array por lo que se perdían sus referencias dando lugar a que en la tab
        * artistas el array devolviese la instancia de albums lo que provocaba que el bottomsheet no
        * se mostrase de forma correcta. Lo que se busca con el if es que cuando se pida la instancia
        * de un fragment en una determinada posición no se cree una nueva si ya existe.*/
        switch (position) {
            case 0:
                if (currentFragments.size() < position+1)
                    currentFragments.add(0, FragmentAlbums.newInstance(getDataType(position)));
                return currentFragments.get(0);
            case 1:
                if (currentFragments.size() < position+1)
                    currentFragments.add(1, FragmentArtists.newInstance(getDataType(position)));
                return currentFragments.get(1);
            case 2:
                if (currentFragments.size() < position+1)
                    currentFragments.add(2, FragmentPlayLists.newInstance(getDataType(position)));
                return currentFragments.get(2);
        }
        return null;
    }

    public Fragment getCurrentFragment(int position) {
        return currentFragments.get(position);
    }

    public List<Fragment> getCurrentFragments() {
        return currentFragments;
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
