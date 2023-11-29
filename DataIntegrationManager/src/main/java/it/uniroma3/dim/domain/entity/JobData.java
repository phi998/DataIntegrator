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

    @OneToMany(cascade = CascadeType.ALL)
    private Collection<OntologyItem> ontology;

    @OneToMany(cascade = CascadeType.ALL)
    private Collection<JobTable> tables;

    public JobData() {
        tables = new ArrayList<>();
        ontology = new ArrayList<>();
    }

}
