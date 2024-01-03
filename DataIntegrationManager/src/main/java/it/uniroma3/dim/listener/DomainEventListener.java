package it.uniroma3.dim.listener;

import it.uniroma3.dim.channel.JobEndServiceEventChannel;
import it.uniroma3.dim.channel.ManagerInformEventChannel;
import it.uniroma3.dim.domain.DomainEventConsumer;
import it.uniroma3.dim.event.DomainEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DomainEventListener {

    @Autowired
    private DomainEventConsumer domainEventConsumer;

    @KafkaListener(topics = {ManagerInformEventChannel.channel, JobEndServiceEventChannel.channel}, groupId="data-integration-manager")
    public void listen(ConsumerRecord<String, DomainEvent> record) throws Exception {
        log.info("EVENT LISTENER: " + record.toString());
        DomainEvent event = record.value();
        domainEventConsumer.onEvent(event);
    }

}
