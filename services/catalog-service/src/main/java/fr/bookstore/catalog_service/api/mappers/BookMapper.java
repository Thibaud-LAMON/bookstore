package fr.bookstore.catalog_service.api.mappers;

import fr.bookstore.catalog_service.api.dto.BookResponse;
import fr.bookstore.catalog_service.api.dto.CreateBookRequest;
import fr.bookstore.catalog_service.api.dto.UpdateBookRequest;
import fr.bookstore.catalog_service.model.Author;
import fr.bookstore.catalog_service.model.Book;
import org.mapstruct.* ;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface BookMapper {
    @Mapping(target = "category", source = "category.name")
    BookResponse toResponse(Book book);

    default String authorName(Author author) { return author.getName(); } //permettra de récupérer le nom de l'auteur

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "authors", ignore = true)
    Book toEntity(CreateBookRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isbn", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "authors", ignore = true)
    void update(UpdateBookRequest request, @MappingTarget Book book);
}
