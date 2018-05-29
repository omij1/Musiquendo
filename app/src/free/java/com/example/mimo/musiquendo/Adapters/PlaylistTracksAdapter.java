package com.example.mimo.musiquendo.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mimo.musiquendo.Model.PlayListTracks;
import com.example.mimo.musiquendo.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Adaptador que rellena la lista de canciones de la actividad que muestra los detalles de una lista de reproducción
 */

public class PlaylistTracksAdapter extends RecyclerView.Adapter<PlaylistTracksAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onTrackClick(View view, PlayListTracks tracks, int playing);
    }


    private List<PlayListTracks> playlistTracks;
    private final OnItemClickListener listener;
    private int itemPlaying = -1;


    public PlaylistTracksAdapter(List<PlayListTracks> albumTracks, OnItemClickListener listener) {
        this.playlistTracks = albumTracks;
        this.listener = listener;
    }

    /**
     * Método que permite mostrar un item como seleccionado cuando se pulsa sobre él y que desactiva
     * los items pulsados previamente
     * @param playing Item pulsado que representa la cancion que se va a reproducir
     */
    public void changeItem(int playing) {
        if (itemPlaying != -1){
            notifyItemChanged(itemPlaying);
        }
        itemPlaying = playing;
        notifyItemChanged(itemPlaying);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.track_item, parent, false);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (position == itemPlaying)
            holder.playing.setVisibility(View.VISIBLE);
        else
            holder.playing.setVisibility(View.INVISIBLE);
        holder.bind(playlistTracks.get(position), listener, position);
    }


    @Override
    public int getItemCount() {
        return playlistTracks.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.track_number)
        TextView trackNumber;
        @BindView(R.id.track_name)
        TextView trackName;
        @BindView(R.id.track_duration)
        TextView trackDuration;
        @BindView(R.id.track_playing)
        View playing;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(PlayListTracks track, OnItemClickListener listener, int position) {
            track.setMinutes(convertToMinutes(track.getTrackDuration()));
            trackNumber.setText(String.valueOf(position+1));
            trackName.setText(track.getTrackName());
            trackDuration.setText(track.getMinutes());
            itemView.setOnClickListener(v -> listener.onTrackClick(itemView, track, position));
        }

        private String convertToMinutes(int trackDuration) {
            int minutes, seconds;
            minutes = trackDuration / 60;
            seconds = trackDuration % 60;

            return String.format("%02d:%02d",minutes, seconds);
        }
    }
}
