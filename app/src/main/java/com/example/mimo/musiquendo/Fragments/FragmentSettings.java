package com.example.mimo.musiquendo.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.mimo.musiquendo.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentSettings extends Fragment {

    @BindView(R.id.playmode_description)
    TextView play_mode;
    @BindView(R.id.playmode)
    Switch aSwitch;
    @BindView(R.id.language_group)
    RadioGroup languageGroup;
    @BindView(R.id.download_mode)
    ToggleButton downloadMode;

    /**
     * Crea una nueva instancia del fragmento
     * @return Instancia del fragmento de ajustes
     */
    public static FragmentSettings newInstance() {
        return new FragmentSettings();
    }

    public FragmentSettings() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("CREATEVIEW", "onCreateView: ");
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        ButterKnife.bind(this, view);
        initComponents();
        setListeners();

        return view;
    }

    private void initComponents() {

    }

    private void setListeners() {
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Log.d("SWITCH", "onCheckedChanged: "+b);
            }
        });
        languageGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                Log.d("RADIO", "onCheckedChanged: "+radioGroup.getCheckedRadioButtonId());
            }
        });
        downloadMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Log.d("TOGGLE", "onCheckedChanged: "+b);
            }
        });
    }
}
