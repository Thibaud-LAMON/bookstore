package fr.bookstore.catalog_service.exception;

public abstract class NotFoundException extends RuntimeException {
    protected NotFoundException(String message){
        super(message);
    }
}
