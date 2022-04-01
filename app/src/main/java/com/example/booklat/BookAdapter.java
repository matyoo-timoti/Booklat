package com.example.booklat;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Objects;

public class BookAdapter extends RecyclerView.Adapter<BookViewHolder> implements Filterable{
    private final Context context;
    private ArrayList<Book> listOfBooks;
    private final ArrayList<Book> mArrayList;
    private final SqLiteDatabase database;

    BookAdapter(Context context, ArrayList<Book> listOfBooks) {
        this.context = context;
        this.listOfBooks = listOfBooks;
        this.mArrayList = listOfBooks;
        database = new SqLiteDatabase(context);
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

    @NonNull
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

        holder.btnEdit.setOnClickListener(view -> editBookDialog(book));

        // Deletes the entry when delete button is tapped. Then restart the main activity.

        holder.btnDelete.setOnClickListener(view -> {
            database.deleteBook(book.getId());
            Toast.makeText(context, "Book: " + book.getTitle() + "has been deleted", Toast.LENGTH_LONG).show();
            ((Activity) context).finish();
            context.startActivity(((Activity) context).getIntent());
        });
    }

    private void editBookDialog(Book book) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View subView = inflater.inflate(R.layout.dialog_add_book_layout, null);

        final EditText titleField = subView.findViewById(R.id.titleField);
        final EditText authorField = subView.findViewById(R.id.authorField);
        final EditText yearPubField = subView.findViewById(R.id.yearPubField);


        // Retrieve data from book object and set as text on edit text.

        if (book != null) {
            titleField.setText(book.getTitle());
            authorField.setText(book.getAuthor());
            yearPubField.setText(book.getPublishedYear());
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Edit Book");
        builder.setView(subView);

        // Dialog positive button
        // Instantiate positive button
        builder.setPositiveButton("SAVE CHANGES", (dialogInterface, i) -> {
            // Do nothing here
        });

        // Dialog negative button

        builder.setNegativeButton("CANCEL", (dialogInterface, i) -> Toast.makeText(context, "Add book cancelled", Toast.LENGTH_SHORT).show());
        final AlertDialog dialog = builder.create();
        dialog.show();


        // Override the button handler as to prevent closing of the dialog if the title field is empty.

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String newTitle = titleField.getText().toString();
            String newAuthor = authorField.getText().toString();
            String newYearPub = yearPubField.getText().toString();

            if (TextUtils.isEmpty(newTitle)) {
                Toast.makeText(context, "Title field cannot be empty.", Toast.LENGTH_LONG).show();
                titleField.setError("This field cannot be empty.");
            } else {
                database.updateBook(new Book(Objects.requireNonNull(book).getId(), newTitle, newAuthor, newYearPub));
                Toast.makeText(context, "Changes saved", Toast.LENGTH_LONG).show();
                ((Activity) context).finish();
                context.startActivity(((Activity) context).getIntent());
                dialog.dismiss();
            }
        });

    }

    @Override
    public int getItemCount() {
        return listOfBooks.size();
    }
}
