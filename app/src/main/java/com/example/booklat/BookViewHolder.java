package com.example.booklat;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class BookViewHolder extends RecyclerView.ViewHolder {
    TextView tvTitle, tvAuthor, tvYear;
    ImageButton btnEdit, btnDelete;

    public BookViewHolder(View itemView) {
        super(itemView);
        tvTitle = itemView.findViewById(R.id.textViewEntryTitle);
        tvAuthor = itemView.findViewById(R.id.textViewEntryAuthor);
        tvYear = itemView.findViewById(R.id.textViewEntryYear);
        btnEdit = itemView.findViewById(R.id.btnEdit);
        btnDelete = itemView.findViewById(R.id.btnDelete);
    }
}
