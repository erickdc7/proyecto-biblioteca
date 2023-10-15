package com.erickdiaz.proyectobiblioteca;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

public class BookSpinnerAdapter extends ArrayAdapter<Book> {
    public BookSpinnerAdapter(Context context, List<Book> books) {
        super(context, android.R.layout.simple_spinner_item, books);
        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        TextView textView = (TextView) view;
        textView.setText(getItem(position).getTitle());
        return view;
    }
}
