package com.mimo.musiquendo.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mimo.musiquendo.Model.Artist;
import com.mimo.musiquendo.Model.SharedPreferences.PreferencesManager;
import com.mimo.musiquendo.R;
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
    private Context mContext;


    public ArtistAdapter(List<Artist> artists, OnItemClickListener listener, Context context) {
        this.artists = artists;
        this.listener = listener;
        this.mContext = context;
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
        View view;
        PreferencesManager manager = new PreferencesManager(mContext);

        //Miramos el layout que hay que inflar dependiendo del modo de vista elegido

        view = manager.getDisplayMode().equals(mContext.getString(R.string.grid)) ?
                LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, parent, false)
                : LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);

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

        @BindView(R.id.item_image)
        ImageView artistImage;
        @BindView(R.id.item_name)
        TextView name;
        @BindView(R.id.item_surname)
        TextView joindate;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }


        public void bind(final Artist artist, final OnItemClickListener listener) {
            name.setText(artist.getName());
            joindate.setText(artist.getJoindate());
            if (!artist.getImage().equals("")) {
                Picasso.get().load(artist.getImage()).into(artistImage);
            } else {
                artistImage.setImageDrawable(itemView.getResources().getDrawable(R.drawable.no_image));
            }
            itemView.setOnClickListener(v -> listener.onArtistClick(itemView, artist));
        }
    }
}
