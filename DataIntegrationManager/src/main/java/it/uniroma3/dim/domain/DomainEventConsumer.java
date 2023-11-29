package it.uniroma3.dim.domain;

import it.uniroma3.dim.event.DomainEvent;
import it.uniroma3.dim.event.JobEndedEvent;
import it.uniroma3.dim.event.ManagerInformEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DomainEventConsumer {

    public void onEvent(DomainEvent event) {
        log.info("DomainEventConsumer - onEvent(): event={}",event.getClass());

        if (event.getClass().equals(ManagerInformEvent.class)) {
            ManagerInformEvent mie = (ManagerInformEvent) event;
            onManagerInformedEvent(mie);
        } else if(event.getClass().equals(JobEndedEvent.class)) {
            JobEndedEvent jee = (JobEndedEvent) event;
            onJobendedEvent(jee);
        } else {
            log.info("UNKNOWN EVENT: " + event);
        }
    }

    private void onManagerInformedEvent(ManagerInformEvent mie) {

    }

    private void onJobendedEvent(JobEndedEvent jee) {

    }

}
