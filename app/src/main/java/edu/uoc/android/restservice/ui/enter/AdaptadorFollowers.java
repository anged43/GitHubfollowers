package edu.uoc.android.restservice.ui.enter;

import android.app.Application;
import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.PicassoProvider;

import java.util.ArrayList;
import java.util.List;

import edu.uoc.android.restservice.R;
import edu.uoc.android.restservice.rest.model.Followers;
import edu.uoc.android.restservice.rest.model.Owner;

/**
 * Created by edgardopanchana on 4/29/18.
 */

public class AdaptadorFollowers extends RecyclerView.Adapter<AdaptadorFollowers.ViewHolderFollowers> {

    List<Followers> listaFollowers;  // Lista con los followers
    Context context;

    public AdaptadorFollowers(List<Followers> listaFollowers) { // Constructor para asignar los datos a una variable
        this.listaFollowers = listaFollowers;
    }

    @NonNull
    @Override
    public ViewHolderFollowers onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, null, false);
        context = parent.getContext(); // Obtenemos el context
        return new ViewHolderFollowers(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderFollowers holder, int position) {
        holder.etiNombre.setText(listaFollowers.get(position).getLogin());  // inserción de nombres en un textview
        Picasso.get().load(listaFollowers.get(position).getAvatarUrl()).into(holder.imagen); // la imagen se carga en el imageview
    }

    @Override
    public int getItemCount() {
        return listaFollowers.size();
    }

    public class ViewHolderFollowers extends RecyclerView.ViewHolder {

        TextView etiNombre;
        ImageView imagen; // definimos variables

        public ViewHolderFollowers(View itemView) {
            super(itemView);

            etiNombre = (TextView) itemView.findViewById(R.id.textViewLista);
            imagen = (ImageView) itemView.findViewById(R.id.imageViewLista);  // enlazamos elementos mediante su id

        }
    }
}
