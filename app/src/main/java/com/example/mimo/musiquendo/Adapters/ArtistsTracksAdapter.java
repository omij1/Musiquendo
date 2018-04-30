package com.example.mimo.musiquendo.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mimo.musiquendo.Model.ArtistTracks;
import com.example.mimo.musiquendo.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArtistsTracksAdapter extends RecyclerView.Adapter<ArtistsTracksAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onTrackClick(View view, ArtistTracks tracks, int playing);
    }


    private List<ArtistTracks> artistTracks;
    private final OnItemClickListener listener;

    public ArtistsTracksAdapter(List<ArtistTracks> artistTracks, OnItemClickListener listener) {
        this.artistTracks = artistTracks;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.track_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(artistTracks.get(position), listener, position);
    }

    @Override
    public int getItemCount() {
        return artistTracks.size();
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

        public void bind(ArtistTracks track, OnItemClickListener listener, int position) {
            trackNumber.setText(String.valueOf(position+1));
            trackName.setText(track.getTrackName());
            trackDuration.setText(convertToMinutes(track.getTrackDuration()));
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
