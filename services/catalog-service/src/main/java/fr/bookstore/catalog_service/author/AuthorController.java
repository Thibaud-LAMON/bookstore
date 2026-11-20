package fr.bookstore.catalog_service.author;

import fr.bookstore.catalog_service.author.dto.*;
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

@Tag(name = "Auteurs", description = "Consultation et gestion de la listes des auteurs.")
@RestController
@RequestMapping("/api/catalog_service/v1/authors")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @Operation(summary = "Rechercher les auteurs",
            description = "Recherche l'auteur de façon paginée et insensible à la case" +
                    "Renvoie tous les auteur en cas de recherche vide.")
    @GetMapping
    public PagedModel<AuthorResponse> search(
            @Parameter(description = "Tout ou partie du nom de l'auteur cherché", example = "Camus")
            @RequestParam(required = false) String q,
            @ParameterObject @PageableDefault(size = 20, sort = "name") Pageable pageable) {
        return new PagedModel<>(authorService.search(q, pageable));
    }

    @Operation(summary = "Trouver un auteur par identifiant")
    @ApiResponse(responseCode = "200", description = "Auteur Trouvé")
    @ApiResponse(responseCode = "404", description = "Auteur Introuvable",
            content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    @GetMapping("/{id}")
    public AuthorResponse get(@PathVariable Long id) {
        return authorService.get(id);
    }

    @Operation(summary = "Créer un auteur")
    @ApiResponse(responseCode = "201", description = "Auteur Créé")
    @ApiResponse(responseCode = "400", description = "Données incorrectes",
            content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    @PostMapping
    public ResponseEntity<AuthorResponse> create(@Valid @RequestBody AuthorRequest request) {
        AuthorResponse created = authorService.create(request);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.id())
                .toUri();
        return ResponseEntity
                .created(location)
                .body(created);
    }

    @Operation(summary = "Modifier un auteur")
    @ApiResponse(responseCode = "200", description = "Auteur Modifié")
    @ApiResponse(responseCode = "400", description = "Données incorrectes",
            content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "404", description = "Auteur introuvable",
            content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "409", description = "Modification annulée, une autre est déjà en cours.",
            content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    @PutMapping("/{id}")
    public AuthorResponse update(@PathVariable Long id, @Valid @RequestBody AuthorRequest request) {
        return authorService.update(id, request);
    }

    @Operation(summary = "Modifier un auteur")
    @ApiResponse(responseCode = "204", description = "Auteur Supprimé")
    @ApiResponse(responseCode = "404", description = "Auteur introuvable",
            content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "409", description = "Suppression impossible, une opération l'en empêche.",
            content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        authorService.delete(id);
    }
}
