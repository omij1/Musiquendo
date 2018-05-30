package com.mimo.musiquendo.Model;

import android.content.res.Resources;

import com.mimo.musiquendo.R;

/**
 * Enumeración que representa las secciones principales de la aplicación
 */
public enum Categories {

    ALBUMS("Albums"),
    ARTISTS("Artists"),
    PLAYLISTS("Playlists");

    public final String key;

    Categories(String key) {
        this.key = key;
    }

    /**
     * Función que devuelve una categoría a partir de su clave
     * @param key Clave que identifica a la categoría
     * @return La categoría
     */
    public static Categories fromKey(String key) {
        for (Categories category : values()) {
            if (category.key.equals(key)) {
                return category;
            }
        }
        return null;
    }
}
