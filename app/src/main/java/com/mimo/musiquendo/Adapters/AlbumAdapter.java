package com.mimo.musiquendo.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mimo.musiquendo.Model.Album;
import com.mimo.musiquendo.Model.SharedPreferences.PreferencesManager;
import com.mimo.musiquendo.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Adaptador que rellena el RecyclerView con los álbumes
 */

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onAlbumClick(View view, Album album);
    }


    private List<Album> albums;
    private final OnItemClickListener listener;
    private Context mContext;

    public AlbumAdapter(List<Album> albums, OnItemClickListener listener, Context context) {
        this.albums = albums;
        this.listener = listener;
        this.mContext = context;
    }


    /**
     * Método que permite actualizar el contenido del recycler view
     * @param items Nuevos elementos que deben mostrarse
     */
    public void swapItems(List<Album> items) {
        this.albums = items;
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
    public void onBindViewHolder(AlbumAdapter.ViewHolder holder, int position) {
        holder.bind(albums.get(position), listener);
    }


    @Override
    public int getItemCount() {
        return albums.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_image)
        ImageView albumImage;
        @BindView(R.id.item_name)
        TextView name;
        @BindView(R.id.item_surname)
        TextView autor;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }


        void bind(final Album album, final OnItemClickListener listener) {
            name.setText(album.getName());
            autor.setText(album.getArtist_name());
            Picasso.get().load(album.getImage()).into(albumImage);
            itemView.setOnClickListener(v -> listener.onAlbumClick(itemView, album));
        }
    }
}
