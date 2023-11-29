package it.uniroma3.dim.eventpublisher.impl;

import it.uniroma3.dim.channel.JobServiceEventChannel;
import it.uniroma3.dim.event.DomainEvent;
import it.uniroma3.dim.eventpublisher.EventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JobCreatedEventPublisher implements EventPublisher {

    @Autowired
    private KafkaTemplate<String, DomainEvent> template;

    private final String channel = JobServiceEventChannel.channel;

    @Override
    public void publish(DomainEvent event) {
        log.info("PUBLISHING EVENT {} ON CHANNEL {}", event, channel);
        template.send(channel, event);
    }
}
