package fr.bookstore.catalog_service.exception;

public class BookNotFoundException extends NotFoundException {
    public BookNotFoundException(Long id) {
        super("Livre introuvable (id=" + id + ")");
    }
}
