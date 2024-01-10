package it.uniroma3.dim.domain;

import it.uniroma3.dim.domain.entity.JobTable;
import org.springframework.data.repository.CrudRepository;

public interface JobTableRepository extends CrudRepository<JobTable, Long> {
}
