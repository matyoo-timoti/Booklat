package com.example.booklat;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class BookViewHolder extends RecyclerView.ViewHolder {
    TextView tvTitle, tvAuthor, tvYear;
    ImageButton btnMenu;
    ImageView bookIcon;


    public BookViewHolder(View itemView) {
        super(itemView);
        tvTitle = itemView.findViewById(R.id.textViewEntryTitle);
        tvAuthor = itemView.findViewById(R.id.textViewEntryAuthor);
        tvYear = itemView.findViewById(R.id.textViewEntryYear);
        btnMenu = itemView.findViewById(R.id.btnMenu);
        bookIcon = itemView.findViewById(R.id.icBook);
    }
}
