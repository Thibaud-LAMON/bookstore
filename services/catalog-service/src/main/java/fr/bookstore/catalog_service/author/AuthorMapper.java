package fr.bookstore.catalog_service.author;

import fr.bookstore.catalog_service.author.dto.*;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper
public interface AuthorMapper {

    AuthorResponse toResponse(Author author);

    Author toEntity(AuthorRequest request);

    void update(AuthorRequest request, @MappingTarget Author author);
}
