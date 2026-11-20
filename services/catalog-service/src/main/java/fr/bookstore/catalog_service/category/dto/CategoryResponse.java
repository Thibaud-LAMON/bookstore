package fr.bookstore.catalog_service.category.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

public record CategoryResponse(
        @Schema(requiredMode = REQUIRED, example = "1")
        Long id,

        @Schema(requiredMode = REQUIRED, example = "Roman")
        String name
) {
}
