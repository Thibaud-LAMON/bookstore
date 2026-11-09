package fr.bookstore.catalog_service.service;

import fr.bookstore.catalog_service.api.dto.*;
import fr.bookstore.catalog_service.api.mappers.BookMapper;
import fr.bookstore.catalog_service.exception.*;
import fr.bookstore.catalog_service.model.Author;
import fr.bookstore.catalog_service.model.Book;
import fr.bookstore.catalog_service.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookService {

    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final AuthorRepository authorRepository;
    private final BookMapper mapper;

    private Book findOrThrow(Long id) {
        return bookRepository
                .findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
    }

    private void attachRelations(Book book, Long categoryId, Set<Long> authorIds) {
        book.setCategory(categoryId == null ? null
                : categoryRepository
                .findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId)));

        book.getAuthors().clear();
        if (authorIds != null && !authorIds.isEmpty()) {
            List<Author> authors = authorRepository.findAllById(authorIds);
            if (authors.size() != authorIds.size()) throw new AuthorNotFoundException(authorIds);
            book.getAuthors()
                .addAll(authors);
        }
    }

    public Page<BookResponse> search(String q, Pageable pageable){
        Page<Book> page = (q == null || q.isBlank())
                ? bookRepository.findAll(pageable)
                : bookRepository.findByTitleContainingIgnoreCase(q, pageable);
        return page.map(mapper::toResponse);
    }

    public BookResponse get(Long id) {
        return mapper.toResponse(findOrThrow(id));
    }

    @Transactional
    public BookResponse create(CreateBookRequest request){
        if (bookRepository.existsByIsbn(request.isbn())){
            throw new IsbnAlreadyExistsException(request.isbn());
        }
        Book book = mapper.toEntity(request);
        attachRelations(book, request.categoryId(), request.authorIds());
        return mapper.toResponse(bookRepository.save(book));
    }

    @Transactional
    public BookResponse update(Long id, UpdateBookRequest request) {
        Book book = findOrThrow(id);
        mapper.update(request, book);
        attachRelations(book, request.categoryId(), request.authorIds());
        return mapper.toResponse(book);
    }

    @Transactional
    public void delete(Long id) {
        bookRepository.delete(findOrThrow(id));
    }

}
