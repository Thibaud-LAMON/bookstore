package fr.bookstore.catalog_service.category.exception;

import fr.bookstore.catalog_service.common.ConflictException;

public class CategoryNameAlreadyExistsException extends ConflictException {
    public CategoryNameAlreadyExistsException(String name) {
        super("Une catégorie nommée « " + name + " » existe déjà");
    }
}
