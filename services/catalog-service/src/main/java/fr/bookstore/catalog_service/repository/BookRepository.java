package fr.bookstore.catalog_service.repository;

import fr.bookstore.catalog_service.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Pageable;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    Page<Book> findByTitleContainingIgnoreCase(String q, Pageable pageable);
    Optional<Book> findByIsbn(String isbn);
}
