package fr.bookstore.catalog_service.book;

import fr.bookstore.catalog_service.book.dto.*;
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

@Tag(name = "Livres", description = "Consultation et gestion du catalogue de livres.")
@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @Operation(summary = "Recherche les livres",
            description = "Recherche sur le titre de façon paginée et insensible à la casse. " +
                    "Renvoie tout le catalogue en cas de recherche vide.")
    @GetMapping
    public PagedModel<BookResponse> search(
            @Parameter(description = "Texte recherché dans le titre", example = "dune")
            @RequestParam(required = false) String q,
            @ParameterObject @PageableDefault(size = 20, sort = "title") Pageable pageable) {
        return new PagedModel<>(bookService.search(q, pageable));
    }

    @Operation(summary = "Obtenir un livre par identifiant")
    @ApiResponse(responseCode = "200", description = "Livre trouvé")
    @ApiResponse(responseCode = "404", description = "Livre introuvable",
            content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    @GetMapping("/{id}")
    public BookResponse get(@PathVariable Long id) {
        return bookService.get(id);
    }

    @Operation(summary = "Créer un livre")
    @ApiResponse(responseCode = "201", description = "Livre créé")
    @ApiResponse(responseCode = "400", description = "Données incorrectes",
            content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "409", description = "ISBN déjà existant",
            content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    @PostMapping
    public ResponseEntity<BookResponse> create(@Valid @RequestBody CreateBookRequest request) {
        BookResponse created = bookService.create(request);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.id())
                .toUri();
        return ResponseEntity
                .created(location)
                .body(created);
    }

    @Operation(summary = "Modifier les données d'un livre")
    @ApiResponse(responseCode = "200", description = "Livre modifié")
    @ApiResponse(responseCode = "400", description = "Données incorrectes",
            content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "404", description = "Livre introuvable",
            content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "409", description = "Modification annulée, une autre est déjà en cours.",
            content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    @PutMapping("/{id}")
    public BookResponse update(@PathVariable Long id, @Valid @RequestBody UpdateBookRequest request) {
        return bookService.update(id, request);
    }

    @Operation(summary = "Supprimer un livre")
    @ApiResponse(responseCode = "204", description = "Livre supprimé")
    @ApiResponse(responseCode = "404", description = "Livre introuvable",
            content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "409", description = "Suppression impossible, une opération l'en empêche.",
            content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        bookService.delete(id);
    }
}
