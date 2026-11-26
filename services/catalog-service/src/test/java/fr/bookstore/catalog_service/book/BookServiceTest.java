package fr.bookstore.catalog_service.book;

import fr.bookstore.catalog_service.author.Author;
import fr.bookstore.catalog_service.author.AuthorMapperImpl;
import fr.bookstore.catalog_service.author.AuthorService;
import fr.bookstore.catalog_service.book.dto.BookResponse;
import fr.bookstore.catalog_service.book.dto.CreateBookRequest;
import fr.bookstore.catalog_service.book.dto.UpdateBookRequest;
import fr.bookstore.catalog_service.book.exception.BookNotFoundException;
import fr.bookstore.catalog_service.book.exception.IsbnAlreadyExistsException;
import fr.bookstore.catalog_service.category.Category;
import fr.bookstore.catalog_service.category.CategoryMapperImpl;
import fr.bookstore.catalog_service.category.CategoryService;
import fr.bookstore.catalog_service.category.exception.CategoryNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock BookRepository bookRepository;
    @Mock AuthorService authorService;
    @Mock CategoryService categoryService;

    BookService bookService;

    @BeforeEach
    void setUp() {

        BookMapper mapper = new BookMapperImpl(new AuthorMapperImpl(), new CategoryMapperImpl());
        bookService = new BookService(bookRepository, categoryService, authorService, mapper);
    }

    private static CreateBookRequest validCreateRequest() {

        return new CreateBookRequest("9782070360024", "L'Étranger", "Roman de Camus",
                new BigDecimal("8.90"), LocalDate.of(1942, 5, 19), 25, 1L,
                Set.of(1L));
    }

    private static Category roman() {

        Category c = new Category();
        c.setName("Roman");
        return c;
    }

    private static Author camus() {

        Author a = new Author();
        a.setName("Albert Camus");
        return a;
    }


    @Nested
    @DisplayName("create")
    class Create {

        @Test
        @DisplayName("Créer le livre avec sa catégorie et ses auteurs.")
        void createsBookWithRelations() {

            when(bookRepository.existsByIsbn("9782070360024")).thenReturn(false);
            when(categoryService.findEntityOrThrow(1L)).thenReturn(roman());
            when(authorService.findAllByIdsOrThrow(Set.of(1L))).thenReturn(new LinkedHashSet<>(Set.of(camus())));
            when(bookRepository.save(any(Book.class))).thenAnswer(inv -> inv.getArgument(0));

            BookResponse response = bookService.create(validCreateRequest());

            assertThat(response.isbn())
                    .isEqualTo("9782070360024");
            assertThat(response.title())
                    .isEqualTo("L'Étranger");
            assertThat(response.price())
                    .isEqualByComparingTo("8.90");
            assertThat(response.category().name())
                    .isEqualTo("Roman");
            assertThat(response.authors()).extracting("name").containsExactly("Albert Camus");
        }


        @Test
        @DisplayName("Refuse un ISBN déjà existant, n'enregistre pas.")
        void rejectsDuplicateIsbn() {

            when(bookRepository.existsByIsbn("9782070360024")).thenReturn(true);

            assertThatThrownBy(() -> bookService.create(validCreateRequest()))
                    .isInstanceOf(IsbnAlreadyExistsException.class)
                    .hasMessageContaining("9782070360024");

            verify(bookRepository, never()).save(any());
        }

        @Test
        @DisplayName("Refuse une catégorie inexistante, n'enregistre pas.")
        void rejectsUnknownCategory() {

            when(bookRepository.existsByIsbn(any())).thenReturn(false);
            when(categoryService.findEntityOrThrow(1L)).thenThrow(new CategoryNotFoundException(1L));

            assertThatThrownBy(() -> bookService
                    .create(validCreateRequest()))
                    .isInstanceOf(CategoryNotFoundException.class);

            verify(bookRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("get")
    class Get {

        @Test
        @DisplayName("Lève BookNotFoundException si l'id n'existe pas.")
        void throwsWhenMissing() {

            when(bookRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> bookService.get(99L))
                    .isInstanceOf(BookNotFoundException.class)
                    .hasMessageContaining("99");
        }
    }

    @Nested
    @DisplayName("update")
    class Update {

        @Test
        @DisplayName("Modifie l'entité existante sans appeler save().")
        void updatesExistingEntity() {

            Book existing = new Book();
            existing.setIsbn("9782070360024");
            existing.setTitle("Ancien titre");
            existing.setPrice(new BigDecimal("5.00"));
            when(bookRepository.findById(42L)).thenReturn(Optional.of(existing));
            when(authorService.findAllByIdsOrThrow(any())).thenReturn(new LinkedHashSet<>());

            UpdateBookRequest request = new UpdateBookRequest("Nouveau titre", null,
                    new BigDecimal("9.90"), null, 10, null, Set.of());

            BookResponse response = bookService.update(42L, request);

            assertThat(existing.getTitle()).isEqualTo("Nouveau titre");
            assertThat(existing.getIsbn()).isEqualTo("9782070360024");
            assertThat(response.price()).isEqualByComparingTo("9.90");
            verify(bookRepository, never()).save(any());
        }

        @Test
        @DisplayName("Lève BookNotFoundException si l'id n'existe pas.")
        void throwsWhenMissing() {

            /* Book existing = new Book();
            existing.setIsbn("9782070360024");
            existing.setTitle("Ancien titre");
            existing.setPrice(new BigDecimal("5.00"));
            //when(bookRepository.findById(42L)).thenReturn(Optional.of(existing));
            when(authorService.findAllByIdsOrThrow(any())).thenReturn(new LinkedHashSet<>()); */ //inutile
            when(bookRepository.findById(99L)).thenReturn(Optional.empty());

            UpdateBookRequest request = new UpdateBookRequest("Nouveau titre", null,
                    new BigDecimal("9.90"), null, 10, null, Set.of());

            //BookResponse response = bookService.update(42L, request); //inutile

            //testait une entité non préparée (42L préparée mais 99L n'existe pas), corrigé par ligne 179
            assertThatThrownBy(() -> bookService.update(99L, request))
                    .isInstanceOf(BookNotFoundException.class)
                    .hasMessageContaining("99");

            verifyNoInteractions(authorService, categoryService);
        }
    }

    @Nested
    @DisplayName("delete")
    class Delete {

        @Test
        @DisplayName("Supprime le livre.")
        void deleteExistingEntity() {

            Book existing = new Book();
            existing.setIsbn("9782070360024");
            when(bookRepository.findById(99L)).thenReturn(Optional.of(existing));

            //INCORRECT : le test appelle le mock, il doit appeler le service
            //bookRepository.delete(existing);
            //CORRECT : ci-dessous
            bookService.delete(99L);

            verify(bookRepository).delete(existing); // le mock doit vérifier qu'il est appelé par le service.
        }

        @Test
        @DisplayName("Lève BookNotFoundException si l'id n'existe pas.")
        void throwsWhenMissing() {

            when(bookRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> bookService.delete(99L))
                    .isInstanceOf(BookNotFoundException.class)
                    .hasMessageContaining("99");
        }
    }
}
