package fr.bookstore.catalog_service.book;

import fr.bookstore.catalog_service.author.AuthorService;
import fr.bookstore.catalog_service.book.dto.*;
import fr.bookstore.catalog_service.book.exception.*;
import fr.bookstore.catalog_service.category.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookService {

    private final BookRepository bookRepository;
    private final CategoryService categoryService;
    private final AuthorService authorService;
    private final BookMapper mapper;

    private Book findOrThrow(Long id) {
        return bookRepository
                .findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
    }

    private void attachRelations(Book book, Long categoryId, Set<Long> authorIds) {
        book.setCategory(categoryService.findEntityOrThrow(categoryId));

        book.getAuthors().clear();
        book.getAuthors().addAll(authorService.findAllByIdsOrThrow(authorIds));
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
