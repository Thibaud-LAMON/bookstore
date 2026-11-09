package fr.bookstore.catalog_service.api.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

public record UpdateBookRequest(@NotBlank @Size(max = 255) String title,
                                @Size(max = 4000) String description,
                                @NotNull @PositiveOrZero BigDecimal price,
                                LocalDate publishedOn,
                                @PositiveOrZero int stock,
                                Long categoryId,
                                Set<Long> authorIds) {}
