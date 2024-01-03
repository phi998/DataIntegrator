package it.uniroma3.dim.domain;

import it.uniroma3.dim.domain.entity.Ontology;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.List;

public interface OntologyRepository extends CrudRepository<Ontology, Long> {

    List<Ontology> findAllByName(String name);

}
