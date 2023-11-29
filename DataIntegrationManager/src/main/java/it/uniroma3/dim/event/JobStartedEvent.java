package it.uniroma3.dim.event;

import lombok.Data;

@Data
public class JobStartedEvent implements DomainEvent {

    private Long jobId;

    private String jobName;

    private JobStartedEventData data;

    public JobStartedEvent() {
        data = new JobStartedEventData();
    }

}
