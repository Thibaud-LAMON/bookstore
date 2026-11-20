package fr.bookstore.catalog_service.author.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

public record AuthorResponse(
        @Schema(requiredMode = REQUIRED, example = "1")
        Long id,

        @Schema(requiredMode = REQUIRED, example = "Albert Camus")
        String name
) {
}
