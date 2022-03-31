package com.example.booklat;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BookAdapter extends RecyclerView.Adapter<BookViewHolder> implements Filterable {
    private final Context context;
    private ArrayList<Book> listOfBooks;
    private final ArrayList<Book> mArrayList;
    private final SqLiteDatabase mDatabase;


    BookAdapter(Context context, ArrayList<Book> listOfBooks) {
        this.context = context;
        this.listOfBooks = listOfBooks;
        this.mArrayList = listOfBooks;
        mDatabase = new SqLiteDatabase(context);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    listOfBooks = mArrayList;
                } else {
                    ArrayList<Book> filteredList = new ArrayList<>();
                    for (Book contacts : mArrayList) {
                        if (contacts.getTitle().toLowerCase().contains(charString)) {
                            filteredList.add(contacts);
                        }
                    }
                    listOfBooks = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = listOfBooks;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                listOfBooks = (ArrayList<Book>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BookViewHolder holder, int position) {
        final Book book = listOfBooks.get(position);
        holder.tvTitle.setText(book.getTitle());
        holder.tvAuthor.setText(book.getAuthor());
        holder.tvYear.setText(book.getPublishedYear());
        holder.btnEdit.setOnClickListener(view -> {
            mDatabase.deleteBook(book.getId());
            ((Activity) context).finish();
            context.startActivity(((Activity) context).getIntent());
        });
    }

    @Override
    public int getItemCount() {
        return listOfBooks.size();
    }
}
