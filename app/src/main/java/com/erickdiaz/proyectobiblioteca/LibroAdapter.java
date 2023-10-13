package com.erickdiaz.proyectobiblioteca;

import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LibroAdapter extends RecyclerView.Adapter<LibroAdapter.LibroViewHolder> {

    private ArrayList<Libro> listaLibros;

    public LibroAdapter(ArrayList<Libro> listaLibros) {
        this.listaLibros = listaLibros;
    }

    @Override
    public LibroViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_libro, parent, false);
        return new LibroViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(LibroViewHolder holder, int position) {
        Libro libro = listaLibros.get(position);
        holder.tituloTextView.setText(libro.getTitulo());
        holder.autorTextView.setText(libro.getAutor());

    }



    @Override
    public int getItemCount() {
        return listaLibros.size();
    }

    public class LibroViewHolder extends RecyclerView.ViewHolder {
        public TextView tituloTextView;
        public TextView autorTextView;

        public LibroViewHolder(View itemView) {
            super(itemView);
            tituloTextView = itemView.findViewById(R.id.tituloTextView);
            autorTextView = itemView.findViewById(R.id.autorTextView);
            // Configura otras vistas si es necesario
        }
    }
}
