package org.example.Model.Book;

import java.util.UUID;

public class Book {
    private final String id = UUID.randomUUID().toString();
    private final String title;
    private final String author;

    public Book(BookDto bookDto) {
        this.title = bookDto.title();
        this.author = bookDto.author();
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }
}
