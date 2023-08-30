package org.example.Model;

import java.util.UUID;

public class BookStock {
    private final String id = UUID.randomUUID().toString();
    private final Book book;
    private int quantity;

    public BookStock(Book book) {
        this.book = book;
        this.quantity = 1;
    }

    public String getId() {
        return id;
    }

    public Book getBook() {
        return book;
    }

    public int getQuantity() {
        return quantity;
    }

    public void increaseQuantity() {
        this.quantity++;
    }

    public void decreaseQuantity() {
        this.quantity--;
    }
}
