package com.example.booklat;

public class Book {

    private final long id;
    private final String title;
    private final String author;
    private final Integer year;

    public Book(long id, String title, String author, Integer year) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.year = year;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public Integer getYear() {
        return year;
    }
}
