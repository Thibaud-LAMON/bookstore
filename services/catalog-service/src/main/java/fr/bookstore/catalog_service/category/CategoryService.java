package fr.bookstore.catalog_service.category;


import fr.bookstore.catalog_service.category.exception.CategoryInUseException;
import fr.bookstore.catalog_service.category.dto.CategoryRequest;
import fr.bookstore.catalog_service.category.dto.CategoryResponse;
import fr.bookstore.catalog_service.category.exception.CategoryNameAlreadyExistsException;
import fr.bookstore.catalog_service.category.exception.CategoryNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper mapper;

    public Category findEntityOrThrow(Long id) {
        return findOrThrow(id);
    }

    private Category findOrThrow(Long id){
        return categoryRepository
                .findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
    }

    public Page<CategoryResponse> search(String q, Pageable pageable) {
        Page<Category> page = (q == null || q.isBlank())
                ? categoryRepository.findAll(pageable)
                : categoryRepository.findByNameContainingIgnoreCase(q, pageable);
        return page.map(mapper::toResponse);
    }

    public CategoryResponse get(Long id) {
        return mapper.toResponse(findOrThrow(id));
    }

    @Transactional
    public CategoryResponse create(CategoryRequest request) {
        if (categoryRepository.existsByNameIgnoreCase(request.name())) {
            throw new CategoryNameAlreadyExistsException(request.name());
        }
        Category category = mapper.toEntity(request);
        return mapper.toResponse(categoryRepository.save(category));
    }

    @Transactional
    public CategoryResponse update(Long id, CategoryRequest request) {
        Category category = findOrThrow(id);
        if (categoryRepository.existsByNameIgnoreCaseAndIdNot(request.name(), id)) {
            throw new CategoryNameAlreadyExistsException(request.name());
        }
        mapper.update(request, category);
        return mapper.toResponse(category);
    }

    @Transactional
    public void delete(Long id) {
        Category category = findOrThrow(id);
        long bookCount = categoryRepository.countBooksByCategoryId(id);
        if (bookCount > 0) {
            throw new CategoryInUseException(id, bookCount);
        }
        categoryRepository.delete(category);
    }
}
