package fr.bookstore.catalog_service.exception;

public class CategoryNotFoundException extends NotFoundException {
    public CategoryNotFoundException(Long id) {
        super("Catégorie introuvable (id=" + id + ")");
    }
}
