package it.uniroma3.dim.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;

@Entity
@Data
public class Ontology {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Collection<OntologyItem> items;

    public Ontology() {
        this.items = new ArrayList<>();
    }

    public void addItem(OntologyItem item) {
        this.items.add(item);
    }

}
