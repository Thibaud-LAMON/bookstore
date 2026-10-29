package fr.bookstore.catalog_service.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "author")
@Getter
@Setter
@NoArgsConstructor
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE) //Le code ne gère pas l'id
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    //Inutile pour la navigation du catalogue, n'aurait pas dû être rédigé
    //@ManyToMany(mappedBy = "authors") //cascade inutiles => auteurs et livres ont des cycles de vie indépendants
    //private List<Book> books;
}
