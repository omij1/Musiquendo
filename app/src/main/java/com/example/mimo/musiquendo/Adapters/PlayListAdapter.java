package com.example.mimo.musiquendo.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mimo.musiquendo.Model.PlayList;
import com.example.mimo.musiquendo.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Adaptador que rellena el RecyclerView de las listas de reproducción
 */

public class PlayListAdapter extends RecyclerView.Adapter<PlayListAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onPlayListClick(View view, PlayList playList);
    }

    private List<PlayList> playLists;
    private final OnItemClickListener listener;

    public PlayListAdapter(List<PlayList> playLists, OnItemClickListener listener) {
        this.playLists = playLists;
        this.listener = listener;
    }

    public void swapItems(List<PlayList> items) {
        this.playLists = items;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.playlist_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(playLists.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return playLists.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {



        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(PlayList playList, OnItemClickListener listener) {

        }
    }
}
