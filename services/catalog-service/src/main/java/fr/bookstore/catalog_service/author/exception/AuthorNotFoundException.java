package fr.bookstore.catalog_service.author.exception;

import fr.bookstore.catalog_service.common.NotFoundException;

import java.util.Set;

public class AuthorNotFoundException extends NotFoundException {
    public AuthorNotFoundException(Set<Long> ids) {
        super("Un de ces auteurs est introuvables : " + ids);
    }
}
