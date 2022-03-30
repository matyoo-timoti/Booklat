package com.example.booklat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.ViewHolder> {
    Context context;
    List<Book> bookList;
    RecyclerView recyclerView;
    final View.OnClickListener onClickListener = new CustomOnClickListener();

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView author;
        TextView year;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textViewEntryTitle);
            author = itemView.findViewById(R.id.textViewEntryAuthor);
            year = itemView.findViewById(R.id.textViewEntryYear);
        }
    }

    public BooksAdapter(Context context, List<Book> bookList, RecyclerView recyclerView) {
        this.context = context;
        this.bookList = bookList;
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public BooksAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.single_item, parent, false);
        view.setOnClickListener(onClickListener);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BooksAdapter.ViewHolder holder, int position) {
        Book book = bookList.get(position);
        holder.title.setText(book.getTitle());
        holder.author.setText(book.getAuthor());
        holder.year.setText(book.getYear());
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    private class CustomOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            int itemPosition = recyclerView.getChildLayoutPosition(view);
            String item = bookList.get(itemPosition).getTitle();
            Toast.makeText(context,item, Toast.LENGTH_SHORT).show();
        }
    }
}
