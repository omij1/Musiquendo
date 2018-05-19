package com.example.mimo.musiquendo.Model.DataBase;

import android.arch.persistence.room.TypeConverter;
import static com.example.mimo.musiquendo.Model.DataBase.DownloadType.ALBUM;
import static com.example.mimo.musiquendo.Model.DataBase.DownloadType.ARTIST;
import static com.example.mimo.musiquendo.Model.DataBase.DownloadType.PLAYLIST;
import static com.example.mimo.musiquendo.Model.DataBase.DownloadType.SONG;

public class DownloadTypeConverter {

    @TypeConverter
    public static DownloadType toType(String type) {
        if (type.equals(ALBUM.getType())){
            return ALBUM;
        }
        else if (type.equals(ARTIST.getType())){
            return ARTIST;
        }
        else if (type.equals(PLAYLIST.getType())){
            return PLAYLIST;
        }
        else {
            return SONG;
        }
    }
}
