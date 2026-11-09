package fr.bookstore.catalog_service.repository;

import fr.bookstore.catalog_service.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}
