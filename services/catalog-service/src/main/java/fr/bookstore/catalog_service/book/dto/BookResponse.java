package fr.bookstore.catalog_service.book.dto;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

public record BookResponse(
        @Schema(requiredMode = REQUIRED, example = "42")
        Long id,

        @Schema(requiredMode = REQUIRED, description = "ISBN-13", example = "9782070360024")
        String isbn,

        @Schema(requiredMode = REQUIRED, example = "L'Étranger")
        String title,
        @Schema(description = "Résumé ; null si non renseigné",
                example = "Meursault, employé de bureau à Alger…")
        String description,

        @Schema(requiredMode = REQUIRED, description = "Prix TTC en euros", example = "8.90")
        BigDecimal price,

        @Schema(description = "Date de première publication ; null si inconnue", example = "1942-05-19")
        LocalDate publishedOn,

        @Schema(requiredMode = REQUIRED, description = "Exemplaires disponibles", example = "30")
        int stock,

        @Schema(description = "Nom de la catégorie ; null si le livre n'est pas classé", example = "Roman")
        String category,

        @ArraySchema(schema = @Schema(example = "Albert Camus"),
                arraySchema = @Schema(requiredMode = REQUIRED,
                        description = "Noms des auteurs ; liste vide si aucun"))
        List<String> authors) {}


