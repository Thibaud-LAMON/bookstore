package fr.bookstore.catalog_service.author.exception;

import fr.bookstore.catalog_service.common.NotFoundException;

import java.util.Set;

public class AuthorNotFoundException extends NotFoundException {
    public AuthorNotFoundException(Long id) {
        super("Auteur introuvable (id=" + id + ")");
    }

    public AuthorNotFoundException(Set<Long> ids) {
        super("Un ou plusieurs auteurs introuvables parmi " + ids);
    }
}
