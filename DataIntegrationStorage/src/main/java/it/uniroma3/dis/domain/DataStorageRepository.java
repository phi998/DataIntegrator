package it.uniroma3.dis.domain;

import it.uniroma3.dis.domain.entity.StoredFile;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.List;

public interface DataStorageRepository extends CrudRepository<StoredFile, Long> {

    List<StoredFile> findAllByAssignedName(String assignedName);

}
