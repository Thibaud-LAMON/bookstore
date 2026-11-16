package fr.bookstore.catalog_service.category.exception;

import fr.bookstore.catalog_service.common.NotFoundException;

public class CategoryNotFoundException extends NotFoundException {
    public CategoryNotFoundException(Long id) {
        super("Catégorie introuvable (id=" + id + ")");
    }
}
