package fr.bookstore.catalog_service.book.exception;

import fr.bookstore.catalog_service.common.ConflictException;

//ConflictException permet à GlobalExceptionHandler de ne plus dépendre de cette classe
public class IsbnAlreadyExistsException extends ConflictException {
    public IsbnAlreadyExistsException(String isbn) {
        super("Un livre avec l'ISBN " + isbn + " existe déjà");
    }
}
