package it.uniroma3.dim.event;

import lombok.Data;

@Data
public class ManagerInformEvent implements DomainEvent {

    private String event;

    private ManagerInformEventData data;

    @Data
    public class ManagerInformEventData {

        private Long jobId;

    }

}
