package com.example.mimo.musiquendo.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mimo.musiquendo.Model.Artist;
import com.example.mimo.musiquendo.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Adaptador que rellena el RecyclerView con la lista de artistas
 */

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onArtistClick(View view, Artist artist);
    }

    private List<Artist> artists;
    private final OnItemClickListener listener;

    public ArtistAdapter(List<Artist> artists, OnItemClickListener listener) {
        this.artists = artists;
        this.listener = listener;
    }

    /**
     * Método que permite actualizar el contenido del recycler view
     * @param items Nuevos elementos que deben mostrarse
     */
    public void swapItems(List<Artist> items) {
        this.artists = items;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.artist_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(artists.get(position), listener);
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

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }


        public void bind(final Artist artist, final OnItemClickListener listener) {
            name.setText(artist.getName());
            if (!artist.getImage().equals("")) {
                Picasso.get().load(artist.getImage()).into(artistImage);
            } else {
                artistImage.setImageDrawable(itemView.getResources().getDrawable(R.drawable.no_image));
            }
            itemView.setOnClickListener(v -> listener.onArtistClick(itemView, artist));
        }
    }
}
