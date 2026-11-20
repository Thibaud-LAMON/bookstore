package fr.bookstore.catalog_service.book;

import fr.bookstore.catalog_service.author.AuthorMapper;
import fr.bookstore.catalog_service.book.dto.*;
import fr.bookstore.catalog_service.author.Author;
import fr.bookstore.catalog_service.category.CategoryMapper;
import org.mapstruct.*;

@Mapper(uses = {AuthorMapper.class, CategoryMapper.class})
public interface BookMapper {

    BookResponse toResponse(Book book);

    default String authorName(Author author) { return author.getName(); } //permettra de récupérer le nom de l'auteur

    @Mapping(target = "category", ignore = true)
    @Mapping(target = "authors", ignore = true)
    Book toEntity(CreateBookRequest request);

    @Mapping(target = "isbn", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "authors", ignore = true)
    void update(UpdateBookRequest request, @MappingTarget Book book);
}
