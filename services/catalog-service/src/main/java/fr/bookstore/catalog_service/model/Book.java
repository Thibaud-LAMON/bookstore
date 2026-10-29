package fr.bookstore.catalog_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "book")
@Getter
@Setter
@NoArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE) //Le code ne gère pas l'id
    @Column(nullable = false) //Généré automatiquement, mais redondant, peut être supprimé
    private Long id;

    @Column(name = "isbn", unique = true, nullable = false, length = 13) //correction longueur, 30 = excès de prudence
    private String isbn;

    @Column(name = "title", nullable = false) //name peut être retiré pour chaque colonne car redondant
    private String title;

    @Column(name = "description", length = 4000)
    private String description;

    @Column(name = "price", nullable = false, precision = 10, scale = 2) //2 décimales, 10 chiffres au total
    private BigDecimal price;

    @Column(name = "published_on")
    private LocalDate publishedOn; //LocalDate pour ne pas avoir l'heure dans la valeur

    @Column(name = "stock", nullable = false)
    private Integer stock; //le stock tiens sur un Integer

    @Version //évite que plusieurs commandes achètent le dernier exemplaire
    @Setter(AccessLevel.NONE) // géré par Hibernate
    private Long version;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToMany
    @JoinTable(name = "book_author", //fixer les noms est essentiel pour les migrations via Flyway
            joinColumns = @JoinColumn(name = "book_id"), //sans fixation les noms seraient générés de manière imprévisible
            inverseJoinColumns = @JoinColumn(name = "author_id"))
    private Set<Author> authors = new HashSet<>(); //Empêche les manip de collections null
}
