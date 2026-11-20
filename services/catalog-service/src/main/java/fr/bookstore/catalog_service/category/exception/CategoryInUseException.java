package fr.bookstore.catalog_service.category.exception;

import fr.bookstore.catalog_service.common.ConflictException;

public class CategoryInUseException extends ConflictException {
    public CategoryInUseException(Long id, long bookCount) {
        super("La catégorie " + id + " est utilisée par " + bookCount
                + " livre(s). Reclassez-les avant de la supprimer.");
    }
}
