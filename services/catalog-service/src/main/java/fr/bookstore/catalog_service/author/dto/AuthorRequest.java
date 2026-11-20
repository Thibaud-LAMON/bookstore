package fr.bookstore.catalog_service.author.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AuthorRequest(
        @Schema(example = "Albert Camus")
        @NotBlank @Size(max = 255) String name
) {
}
