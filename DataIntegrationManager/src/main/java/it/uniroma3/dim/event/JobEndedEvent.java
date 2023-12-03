package it.uniroma3.dim.event;

import lombok.Data;

import java.util.List;

@Data
public class JobEndedEvent implements DomainEvent {

    private Long jobId;

    private String jobName;

    private List<JobTableEventData> jobResultTables;

}
