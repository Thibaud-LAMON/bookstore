package fr.bookstore.catalog_service.category;

import fr.bookstore.catalog_service.category.dto.*;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper
public interface CategoryMapper {

    CategoryResponse toResponse(Category category);

    Category toEntity(CategoryRequest request);

    void update(CategoryRequest request, @MappingTarget Category category);
}
