package it.uniroma3.dim.eventpublisher;

import it.uniroma3.dim.event.DomainEvent;

public interface EventPublisher {
    void publish(DomainEvent event);
}
