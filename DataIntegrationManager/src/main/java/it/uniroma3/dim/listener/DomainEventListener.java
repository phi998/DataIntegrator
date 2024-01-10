package it.uniroma3.dim.listener;

import com.google.gson.Gson;
import it.uniroma3.dim.channel.JobEndServiceEventChannel;
import it.uniroma3.dim.channel.ManagerInformEventChannel;
import it.uniroma3.dim.domain.DomainEventConsumer;
import it.uniroma3.dim.event.DomainEvent;
import it.uniroma3.dim.event.JobEndedEvent;
import it.uniroma3.dim.event.ManagerInformEvent;
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

    @KafkaListener(topics = {ManagerInformEventChannel.channel, JobEndServiceEventChannel.channel})
    public void listen(ConsumerRecord<String, String> record) {
        log.info("EVENT LISTENER: " + record.toString());
        String eventString = record.value();
        String topic = record.topic();

        domainEventConsumer.onEvent(getEventObject(eventString, topic));
    }

    /** FIXME build a custom json deserializer or study how to adapt default kafka deserializer */
    private DomainEvent getEventObject(String eventStringJson, String topic) {
        DomainEvent event = null;
        Gson gson = new Gson();

        if(topic.equals(ManagerInformEventChannel.channel)) {
            event = gson.fromJson(eventStringJson, ManagerInformEvent.class);
        } else if(topic.equals(JobEndServiceEventChannel.channel)) {
            event = gson.fromJson(eventStringJson, JobEndedEvent.class);
        }

        return event;
    }

}
