package fr.bookstore.catalog_service.category.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryRequest(
        @Schema(example = "Roman")
        @NotBlank @Size(max = 255) String name
) {
}
