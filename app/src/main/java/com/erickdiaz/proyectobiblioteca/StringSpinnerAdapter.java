package com.erickdiaz.proyectobiblioteca;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

public class StringSpinnerAdapter extends ArrayAdapter<String> {
    public StringSpinnerAdapter(Context context, List<String> strings) {
        super(context, android.R.layout.simple_spinner_item, strings);
        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        TextView textView = (TextView) view;
        String item = getItem(position);
        if (item != null) {
            textView.setText(item);
        } else {
            textView.setText(""); // Evita un elemento nulo en el adaptador
        }
        return view;
    }
}
