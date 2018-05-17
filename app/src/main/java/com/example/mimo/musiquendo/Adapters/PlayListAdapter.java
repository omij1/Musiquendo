package com.example.mimo.musiquendo.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mimo.musiquendo.Model.PlayList;
import com.example.mimo.musiquendo.Model.SharedPreferences.PreferencesManager;
import com.example.mimo.musiquendo.Provider.JamendoProvider;
import com.example.mimo.musiquendo.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Adaptador que rellena el RecyclerView de las listas de reproducción
 */

public class PlayListAdapter extends RecyclerView.Adapter<PlayListAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onPlayListClick(View view, PlayList playList);
        void onDownloadItemClick(PlayList playList);
    }

    public interface CoverCallback {
        void onCoversSuccess(PlayList playlistWithCover);
    }

    private List<PlayList> playLists;
    private final OnItemClickListener listener;
    private Context mContext;

    public PlayListAdapter(List<PlayList> playLists, OnItemClickListener listener, Context context) {
        this.playLists = playLists;
        this.listener = listener;
        this.mContext = context;
    }

    /**
     * Método que permite actualizar el contenido del recycler view
     * @param items Nuevos elementos que deben mostrarse
     */
    public void swapItems(List<PlayList> items) {
        this.playLists = items;
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
        holder.bind(playLists.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return playLists.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_image)
        ImageView playListImage;
        @BindView(R.id.item_name)
        TextView playListName;
        @BindView(R.id.item_surname)
        TextView playListCreation;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(PlayList playList, OnItemClickListener listener) {

            JamendoProvider jamendo = new JamendoProvider(itemView.getContext());

            playListName.setText(playList.getName());
            playListCreation.setText(playList.getCreation());

            /*Después de obtener las listas de reproducción hay que coger de cada una su foto de portada ya que el api no la proporciona en la misma ruta.
            Para esto se debe realizar una petición por cada lista y coger la foto de su primera canción. Es decir:
            Para obtener las listas se llama a /playlist pero como no hay imagen se debe poner la portada de la primera canción de esa lista para lo cual hay que
            irse a /playlist/tracks y buscar la portada de la primera canción.
            Ante esto tenía dos opciones:
            La primera era cargar todos los datos incluyendo imágenes antes de llamar al adaptador lo que provoca un retardo de más de diez segundos
            hasta que aparecen los datos ya que se debe coger las listas y después de cada una su foto llamando a la otra ruta.
            Opción dos obtener las listas de reproducción y que los datos se carguen rápido en el recycler y posteriormente según el usuario va haciendo
            scroll que se vayan cogiendo las imágenes. De esta forma no hay retardo y no se penaliza el rendimiento de la aplicación.*/

            jamendo.getALbumCover(playList, playlistWithCover -> {
                if (!playlistWithCover.getCover().equals("")){
                    Picasso.get().load(playlistWithCover.getCover()).into(playListImage);
                }
                else {
                    playListImage.setImageDrawable(itemView.getResources().getDrawable(R.drawable.no_image));

                }
            });
            itemView.setOnClickListener(view -> listener.onPlayListClick(itemView, playList));
        }
    }
}
