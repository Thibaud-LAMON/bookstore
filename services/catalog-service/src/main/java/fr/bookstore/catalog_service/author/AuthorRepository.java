package fr.bookstore.catalog_service.author;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    Page<Author> findByNameContainingIgnoreCase(String q, Pageable pageable);

    @Query(value = "SELECT COUNT(*) FROM book_author WHERE author_id = :authorId", nativeQuery = true)
    long countBooksByAuthorId(@Param("authorId") Long authorId);
}
