package it.uniroma3.dim.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;

@Entity
@Data
public class JobResult {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL)
    private Collection<JobTable> resultTables;

    public JobResult() {
        this.resultTables = new ArrayList<>();
    }

    public void addJobTable(JobTable jobTable) {
        this.getResultTables().add(jobTable);
    }

}
