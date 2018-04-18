package com.example.mimo.musiquendo.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mimo.musiquendo.Model.Artist;
import com.example.mimo.musiquendo.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Adaptador que rellena el RecyclerView de la lista de artistas
 */

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ViewHolder> {

    interface OnItemClickListener {
        void onItemClick(View view, Artist artist);
    }

    private List<Artist> artists;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.artist_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(artists.get(position), null);
    }

    @Override
    public int getItemCount() {
        return artists.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.artist_item_image)
        ImageView artistImage;
        @BindView(R.id.artist_item_name)
        TextView name;
        @BindView(R.id.artist_item_joindate)
        TextView joinDate;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }


        public void bind(Artist artist, Object o) {

        }
    }
}
