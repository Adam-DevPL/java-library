package org.example.Model;

import java.util.UUID;

public class BookDto {
    private final String title;
    private final String author;

    public BookDto(String title, String author) {
        this.title = title;
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }
}
