package fr.bookstore.catalog_service.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "category")
@Getter
@Setter
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE) //Le code ne gère pas l'id
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    //Inutile pour la navigation du catalogue, aurait pu être supprimé
    //@OneToMany(mappedBy = "category")
    //private List<Book> books;
}
