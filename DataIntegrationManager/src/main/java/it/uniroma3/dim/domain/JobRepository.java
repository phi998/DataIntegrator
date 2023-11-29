package it.uniroma3.dim.domain;

import it.uniroma3.dim.domain.entity.Job;
import org.springframework.data.repository.CrudRepository;

public interface JobRepository extends CrudRepository<Job, Long> {

}
