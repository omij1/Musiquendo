package com.example.mimo.musiquendo.Provider;

import com.example.mimo.musiquendo.Model.Album;

import java.util.List;

public interface VolleyCallback {

    List<Album> onSuccess(List<Album> albums);
}
