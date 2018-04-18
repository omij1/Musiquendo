package com.example.mimo.musiquendo.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mimo.musiquendo.Model.Album;
import com.example.mimo.musiquendo.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {

    interface OnItemClickListener {
        void onItemClick(View view, Album album);
    }

    private List<Album> albums;
    //private final OnItemClickListener listener;

    public AlbumAdapter(List<Album> albums) {
        this.albums = albums;
    }

    @Override
    public AlbumAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.album_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AlbumAdapter.ViewHolder holder, int position) {
        holder.bind(albums.get(position), null);
    }

    @Override
    public int getItemCount() {
        return albums.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.album_item_image)
        ImageView albumImage;
        @BindView(R.id.album_item_name)
        TextView name;
        @BindView(R.id.album_item_autor)
        TextView autor;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(final Album album, final OnItemClickListener listener) {
            name.setText(album.getName());
            autor.setText(album.getArtist_name());
            Picasso.get().load(album.getImage()).into(albumImage);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(itemView, album);
                }
            });
        }
    }

}
