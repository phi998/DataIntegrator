package it.uniroma3.dim.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;

@Entity
@Data
public class JobData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    private Ontology ontology;

    @OneToMany(cascade = CascadeType.ALL)
    private Collection<JobTable> tables;

    public JobData() {
        tables = new ArrayList<>();
    }

}
