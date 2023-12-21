package it.uniroma3.dim.domain.entity;

import it.uniroma3.dim.domain.enums.JobStatus;
import it.uniroma3.dim.domain.enums.JobType;
import it.uniroma3.dim.domain.enums.OntologyItemType;
import jakarta.persistence.*;
import lombok.Data;

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

    public void addOntologyItem(String item, String ontologyItemType, int importance) {
        OntologyItem oi = new OntologyItem();
        oi.setLabel(item);
        oi.setType(OntologyItemType.valueOf(ontologyItemType));
        oi.setImportance(importance);
        jobData.getOntology().addItem(oi);
    }

}
