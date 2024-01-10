package it.uniroma3.dim.domain;

import it.uniroma3.dim.domain.entity.Job;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface JobRepository extends CrudRepository<Job, Long> {

    List<Job> findAllByName(String name);

}
