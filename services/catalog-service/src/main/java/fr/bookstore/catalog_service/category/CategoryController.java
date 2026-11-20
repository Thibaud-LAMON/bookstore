package fr.bookstore.catalog_service.category;

import fr.bookstore.catalog_service.category.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Tag(name = "Catégories", description = "Consultation et gestion de la liste des catégories.")
@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "Rechercher les catégories",
            description = "Recherche la catégorie de façon paginée et insensible à la casse " +
                    "Renvoie toutes les catégories en cas de recherche vide.")
    @GetMapping
    public PagedModel<CategoryResponse> search(
            @Parameter(description = "Tout ou partie du nom de la catégorie recherchée", example = "Roman")
            @RequestParam(required = false) String q,
            @ParameterObject @PageableDefault(size = 20, sort = "name")Pageable pageable) {
        return new PagedModel<>(categoryService.search(q, pageable));
    }

    @Operation(summary = "Trouver une catégorie par identifiant")
    @ApiResponse(responseCode = "200", description = "Catégorie trouvée")
    @ApiResponse(responseCode = "404", description = "Catégorie introuvable",
            content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    @GetMapping("/{id}")
    public CategoryResponse get(@PathVariable Long id) {
        return categoryService.get(id);
    }

    @Operation(summary = "Créer une catégorie")
    @ApiResponse(responseCode = "201", description = "Catégorie créée")
    @ApiResponse(responseCode = "400", description = "Données incorrectes",
            content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "409", description = "Une catégorie porte déjà ce nom",
            content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    @PostMapping
    public ResponseEntity<CategoryResponse> create(@Valid @RequestBody CategoryRequest request) {
        CategoryResponse created = categoryService.create(request);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.id())
                .toUri();
        return ResponseEntity
                .created(location)
                .body(created);
    }

    @Operation(summary = "Modifier une catégorie")
    @ApiResponse(responseCode = "200", description = "Catégorie modifiée")
    @ApiResponse(responseCode = "400", description = "Données incorrectes",
            content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "404", description = "Catégorie introuvable",
            content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "409", description = "Une autre catégorie porte déjà ce nom",
            content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    @PutMapping("/{id}")
    public CategoryResponse update(@PathVariable Long id, @Valid @RequestBody CategoryRequest request) {
        return categoryService.update(id, request);
    }

    @Operation(summary = "Supprimer une catégorie")
    @ApiResponse(responseCode = "204", description = "Catégorie supprimée")
    @ApiResponse(responseCode = "404", description = "Catégorie introuvable",
            content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "409", description = "Suppression impossible, la catégorie est utilisée par des livres.",
            content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        categoryService.delete(id);
    }
}
