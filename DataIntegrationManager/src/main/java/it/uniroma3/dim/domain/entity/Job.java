package it.uniroma3.dim.domain.entity;

import it.uniroma3.dim.domain.enums.JobStatus;
import it.uniroma3.dim.domain.enums.JobType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.Map;

@Entity
@Data
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private JobStatus status;

    @Enumerated(EnumType.STRING)
    private JobType jobType;

    @OneToOne(cascade = CascadeType.ALL)
    private JobData jobData;

    @OneToOne(cascade = CascadeType.ALL)
    private JobResult jobResult;

    public Job() {
        jobData = new JobData();
    }

    public void addJobTable(JobTable jobTable) {
        this.jobData.getTables().add(jobTable);
    }

    public void addOntologyItems(Collection<String> ontologyItems) {
        for(String ontologyItem : ontologyItems) {
            this.jobData.getOntology().add(new OntologyItem(ontologyItem));
        }
    }

}
