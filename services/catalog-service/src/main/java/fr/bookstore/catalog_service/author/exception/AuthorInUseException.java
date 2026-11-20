package fr.bookstore.catalog_service.author.exception;

import fr.bookstore.catalog_service.common.ConflictException;

public class AuthorInUseException extends ConflictException {
    public AuthorInUseException(Long id, long bookCount) {
        super("L'auteur " + id + " est associé à " + bookCount
                + " livre(s). Retirez-le de ces livres avant de le supprimer.");
    }
}
