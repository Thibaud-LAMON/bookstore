package fr.bookstore.catalog_service.book.exception;

import fr.bookstore.catalog_service.common.NotFoundException;

public class BookNotFoundException extends NotFoundException {
    public BookNotFoundException(Long id) {
        super("Livre introuvable (id=" + id + ")");
    }
}
