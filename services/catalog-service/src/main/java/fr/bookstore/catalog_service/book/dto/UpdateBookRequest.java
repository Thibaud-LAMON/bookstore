package fr.bookstore.catalog_service.book.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

public record UpdateBookRequest(
        @Schema(example = "L'Étranger")
        @NotBlank @Size(max = 255) String title,

        @Schema(description = "Résumé ; null si non renseigné", example = "Meursault, employé de bureau à Alger…")
        @Size(max = 4000) String description,

        @Schema(description = "Prix TTC en euros", example = "8.90")
        @NotNull @PositiveOrZero BigDecimal price,

        @Schema(example = "1942-05-19")
        LocalDate publishedOn,

        @Schema(example = "30")
        @PositiveOrZero int stock,

        @Schema(description = "Identifiant d'une catégorie existante", example = "1")
        Long categoryId,

        @Schema(description = "Identifiants d'auteurs existants", example = "[1]")
        Set<Long> authorIds) {}
