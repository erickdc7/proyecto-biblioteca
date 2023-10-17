package com.erickdiaz.proyectobiblioteca;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import androidx.fragment.app.FragmentManager;


public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {
    private List<Book> books;
    private Context context;

    public BookAdapter(Context context, List<Book> books) {
        this.context = context;
        this.books = books;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View bookView = inflater.inflate(R.layout.book_item, parent, false);
        return new BookViewHolder(bookView);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = books.get(position);
        holder.titleTextView.setText(book.getTitle());
        holder.authorTextView.setText(book.getAuthor());
        holder.categoryTextView.setText(book.getCategory());
        holder.availabilityTextView.setText("Availability: " + book.getAvailability());

        // Configura la imagen de la portada del libro usando Glide
        Glide.with(context)
                .load(book.getCoverImageUrl())
                .into(holder.coverImageView);

        holder.btnReservar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Verifica que el contexto sea un AppCompatActivity antes de obtener el FragmentManager
                if (context instanceof AppCompatActivity) {
                    FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.fragment_container, new PrestamoFragment());
                    transaction.addToBackStack(null);  // Opcional: permite volver al fragmento anterior
                    transaction.commit();
                }
            }
        });




    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public class BookViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView;
        public TextView authorTextView;
        public TextView categoryTextView;
        public TextView availabilityTextView;
        public ImageView coverImageView; // Agrega una referencia al ImageView

        public Button btnReservar;

        public BookViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            authorTextView = itemView.findViewById(R.id.authorTextView);
            categoryTextView = itemView.findViewById(R.id.categoryTextView);
            availabilityTextView = itemView.findViewById(R.id.availabilityTextView);
            coverImageView = itemView.findViewById(R.id.bookCover);

            // Agrega referencias a otros elementos de book_item.xml aquí

            btnReservar = itemView.findViewById(R.id.btnReservar);

        }
    }
}
