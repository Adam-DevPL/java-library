package org.example.service.Utils.exceptions;

public class BookStockBookOutOfStock extends RuntimeException{
    public BookStockBookOutOfStock(Throwable cause) {
        super(cause);
    }
}
