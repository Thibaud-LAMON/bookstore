package fr.bookstore.catalog_service.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record BookResponse(Long id,
                           String isbn,
                           String title,
                           String description,
                           BigDecimal price,
                           LocalDate publishedOn,
                           int stock,
                           String category,
                           List<String> authors) {}

