package fr.bookstore.catalog_service.common;

public abstract class NotFoundException extends RuntimeException {
    protected NotFoundException(String message){
        super(message);
    }
}
