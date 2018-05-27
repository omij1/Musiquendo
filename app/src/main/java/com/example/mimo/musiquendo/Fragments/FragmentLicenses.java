package com.example.mimo.musiquendo.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mimo.musiquendo.Model.LicenseBody;
import com.example.mimo.musiquendo.Model.LicenseHeader;
import com.example.mimo.musiquendo.R;
import com.mindorks.placeholderview.ExpandablePlaceHolderView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Fragmento que muestra las licencias de la aplicación
 */

public class FragmentLicenses extends Fragment {

    @BindView(R.id.licenses_list)
    ExpandablePlaceHolderView placeHolderView;


    public static FragmentLicenses newInstance() {
        return new FragmentLicenses();
    }


    public FragmentLicenses() {}


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_licenses, container, false);
        ButterKnife.bind(this, view);
        setLicenseHeader();
        setLicenseBody();

        return view;
    }


    /**
     * Método que establece las cabeceras de las licencias
     */
    private void setLicenseHeader() {
        String[] headers = getResources().getStringArray(R.array.licenses_headers);
        for (String header : headers) {
            placeHolderView.addView(new LicenseHeader(getContext(), header));
        }
    }


    /**
     * Método que establece el contenido de las licencias
     */
    private void setLicenseBody() {
        String[] bodies = getResources().getStringArray(R.array.licenses_body);
        for (int i=0; i < bodies.length; i++) {
            placeHolderView.addChildView(i, new LicenseBody(getContext(), bodies[i]));
        }
    }
}
