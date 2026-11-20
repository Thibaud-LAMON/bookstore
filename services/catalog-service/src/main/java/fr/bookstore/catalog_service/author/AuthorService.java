package fr.bookstore.catalog_service.author;

import fr.bookstore.catalog_service.author.dto.*;
import fr.bookstore.catalog_service.author.exception.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorMapper mapper;

    private Author findOrThrow(Long id) {
        return authorRepository
                .findById(id)
                .orElseThrow(() -> new AuthorNotFoundException(id));
    }

    public Set<Author> findAllByIdsOrThrow(Set<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return new LinkedHashSet<>();
        }
        List<Author> authors = authorRepository.findAllById(ids);
        if (authors.size() != ids.size()) {
            throw new AuthorNotFoundException(ids);
        }
        return new LinkedHashSet<>(authors);
    }

    public Page<AuthorResponse> search(String q, Pageable pageable) {
        Page<Author> page = (q == null || q.isBlank())
                ? authorRepository.findAll(pageable)
                : authorRepository.findByNameContainingIgnoreCase(q, pageable);
        return page.map(mapper::toResponse);
    }

    public AuthorResponse get(Long id) {
        return mapper.toResponse(findOrThrow(id));
    }

    @Transactional
    public AuthorResponse create(AuthorRequest request) {
        Author author = mapper.toEntity(request);
        return mapper.toResponse(authorRepository.save(author));
    }

    @Transactional
    public AuthorResponse update(Long id, AuthorRequest request) {
        Author author = findOrThrow(id);
        mapper.update(request, author);
        return mapper.toResponse(author);
    }

    @Transactional
    public void delete(Long id) {
        Author author = findOrThrow(id);
        long bookCount = authorRepository.countBooksByAuthorId(id);
        if (bookCount > 0) {
            throw new AuthorInUseException(id, bookCount);
        }
        authorRepository.delete(author);
    }
}
