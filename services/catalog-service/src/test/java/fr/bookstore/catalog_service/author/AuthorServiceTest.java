package fr.bookstore.catalog_service.author;

import fr.bookstore.catalog_service.author.dto.AuthorRequest;
import fr.bookstore.catalog_service.author.dto.AuthorResponse;
import fr.bookstore.catalog_service.author.exception.AuthorInUseException;
import fr.bookstore.catalog_service.author.exception.AuthorNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthorServiceTest {

    @Mock AuthorRepository authorRepository;

    AuthorService authorService;

    @BeforeEach
    void setUp() {

        AuthorMapper mapper = new AuthorMapperImpl();
        authorService = new AuthorService(authorRepository, mapper);
    }


    private static AuthorRequest validCreateRequest() {

        return new AuthorRequest("Albert Camus");
    }

    @Nested
    @DisplayName("create")
    class Create {

        @Test
        @DisplayName("Création d'un auteur.")
        void createAuthor() {

            when(authorRepository.save(any(Author.class))).thenAnswer(inv -> inv.getArgument(0));

            AuthorResponse response = authorService.create(validCreateRequest());

            assertThat(response.name()).isEqualTo("Albert Camus");
        }
    }

    @Nested
    @DisplayName("get")
    class Get {

        @Test
        @DisplayName("Lève AuthorNotFoundException si l'id n'existe pas.")
        void throwsWhenMissing() {

            when(authorRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> authorService.get(99L))
                    .isInstanceOf(AuthorNotFoundException.class)
                    .hasMessageContaining("99");
        }
    }

    @Nested
    @DisplayName("update")
    class Update {

        @Test
        @DisplayName("Modifie l'entité existante sans appeler save().")
        void updatesExistingEntity() {

            Author existing = new Author();
            existing.setName("Albert Camus");
            when(authorRepository.findById(42L)).thenReturn(Optional.of(existing));

            AuthorRequest request = new AuthorRequest("Camus, Albert");

            AuthorResponse response = authorService.update(42L, request);

            assertThat(response.name()).isEqualTo("Camus, Albert");
            verify(authorRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("delete")
    class Delete {

        @Test
        @DisplayName("Lève AuthorInUseException si l'auteur a des livres en ventes.")
        void throwsWhenInUse() {

            Author inUse = new Author();
            inUse.setName("Albert Camus");
            when(authorRepository.findById(42L)).thenReturn(Optional.of(inUse));

            assertThatThrownBy(() -> authorService.delete(42L))
                    .isInstanceOf(AuthorInUseException.class)
                    .hasMessageContaining("42");
        }
    }
}
