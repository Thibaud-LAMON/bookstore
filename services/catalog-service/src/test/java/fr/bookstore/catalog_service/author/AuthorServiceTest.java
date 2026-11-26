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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

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

    private static Author author(String name) {
        Author a = new Author();
        a.setName(name);
        return a;
    }

    @Nested
    @DisplayName("findAllByIdsOrThrow")
    class FindAllByIdsOrThrow{

        @ParameterizedTest(name = "ids = {0}")
        @NullAndEmptySource
        @DisplayName("Renvoie un ensemble vide sans interroger la base.")
        void returnsEmptyWithoutQuerying(Set<Long> ids) {

            Set<Author> result = authorService.findAllByIdsOrThrow(ids);

            assertThat(result).isEmpty();
            verifyNoInteractions(authorRepository);
        }

        @Test
        @DisplayName("Renvoie tous les auteurs quand ils existent tous.")
        void returnsAllWhenAllExist() {

            Author camus = author("Albert Camus");
            Author sartre = author("Jean-Paul Sartre");
            when(authorRepository.findAllById(Set.of(1L, 2L))).thenReturn(List.of(camus, sartre));

            Set<Author> result = authorService.findAllByIdsOrThrow(Set.of(1L, 2L));

            assertThat(result).containsExactlyInAnyOrder(camus, sartre);
        }

        @Test
        @DisplayName("Lève AuthorNotFoundException si un ou plusieurs auteurs n'existe(nt) pas.")
        void throwsWhenSomeAreMissing() {

            when(authorRepository.findAllById(Set.of(1L,2L,3L)))
                    .thenReturn(List.of(author("Albert Camus"), author("Jean-Paul Sartre")));

            assertThatThrownBy(() -> authorService.findAllByIdsOrThrow(Set.of(1L,2L,3L)))
                    .isInstanceOf(AuthorNotFoundException.class)
                    .hasMessageContaining("1")
                    .hasMessageContaining("2")
                    .hasMessageContaining("3");
        }

        @Test
        @DisplayName("Lève AuthorNotFoundException si aucun auteur n'existe.")
        void throwsWhenNoneExist() {

            when(authorRepository.findAllById(Set.of(7L))).thenReturn(List.of());

            assertThatThrownBy(() -> authorService.findAllByIdsOrThrow(Set.of(7L)))
                    .isInstanceOf(AuthorNotFoundException.class)
                    .hasMessageContaining("7");
        }
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
            when(authorRepository.countBooksByAuthorId(42L)).thenReturn(3L);

            assertThatThrownBy(() -> authorService.delete(42L))
                    .isInstanceOf(AuthorInUseException.class)
                    .hasMessageContaining("42")
                    .hasMessageContaining("3 livre(s)");

            verify(authorRepository, never()).delete(any());
        }
    }
}
