package fr.bookstore.catalog_service.repository;

import fr.bookstore.catalog_service.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
