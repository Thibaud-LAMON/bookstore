package fr.bookstore.catalog_service.exception;

public class IsbnAlreadyExistsException extends RuntimeException {
    public IsbnAlreadyExistsException(String isbn) {
        super("Un livre avec l'ISBN " + isbn + " existe déjà");
    }
}
