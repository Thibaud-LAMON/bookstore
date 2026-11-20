package fr.bookstore.catalog_service.book;

import fr.bookstore.catalog_service.author.AuthorMapper;
import fr.bookstore.catalog_service.book.dto.*;
import fr.bookstore.catalog_service.author.Author;
import org.mapstruct.* ;

@Mapper(uses = AuthorMapper.class, componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface BookMapper {

    @Mapping(target = "category", source = "category.name")
    BookResponse toResponse(Book book);

    default String authorName(Author author) { return author.getName(); } //permettra de récupérer le nom de l'auteur

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
