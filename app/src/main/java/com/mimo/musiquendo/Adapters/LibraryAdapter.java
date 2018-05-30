package com.mimo.musiquendo.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mimo.musiquendo.Model.DataBase.DownloadItem;
import com.mimo.musiquendo.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Adaptador que sirve para mostrar las canciones descargadas por el usuario
 */

public class LibraryAdapter extends RecyclerView.Adapter<LibraryAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onDownloadItemClick(View view, DownloadItem track, int position);
        boolean onLongItemClick(View view, DownloadItem track, int position);
    }


    private List<DownloadItem> itemList;
    private final OnItemClickListener listener;


    public LibraryAdapter(OnItemClickListener clickListener) {
        this.listener = clickListener;
    }


    @Override
    public LibraryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.library_item, parent, false);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(LibraryAdapter.ViewHolder holder, int position) {
        holder.bind(itemList.get(position), listener, position);
    }


    @Override
    public int getItemCount() {
        int itemCount = 0;
        if (itemList != null) {
            itemCount = itemList.size();
        }
        return itemCount;
    }


    public void setData(List<DownloadItem> downloadItems) {
        this.itemList = downloadItems;
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.library_item_cover)
        ImageView cover;
        @BindView(R.id.library_item_name)
        TextView name;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }


        public void bind(DownloadItem downloadItem, OnItemClickListener listener, int position) {
            name.setText(downloadItem.getTrackName());
            if (!downloadItem.getCover().equals("")) {
                Picasso.get().load(downloadItem.getCover()).into(cover);
            } else {
                cover.setImageDrawable(itemView.getResources().getDrawable(R.drawable.no_image));
            }
            itemView.setOnClickListener(v -> listener.onDownloadItemClick(itemView, downloadItem, position));
            itemView.setOnLongClickListener(v -> listener.onLongItemClick(itemView, downloadItem, position));
        }
    }
}
