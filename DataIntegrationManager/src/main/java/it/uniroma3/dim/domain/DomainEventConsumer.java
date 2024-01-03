package it.uniroma3.dim.domain;

import it.uniroma3.dim.event.DomainEvent;
import it.uniroma3.dim.event.JobEndedEvent;
import it.uniroma3.dim.event.JobTableEventData;
import it.uniroma3.dim.event.ManagerInformEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class DomainEventConsumer {

    @Autowired
    private JobService jobService;

    public void onEvent(DomainEvent event) {
        log.info("DomainEventConsumer - onEvent(): event={}",event.getClass());

        if (event.getClass().equals(ManagerInformEvent.class)) {
            ManagerInformEvent mie = (ManagerInformEvent) event;
            onManagerInformedEvent(mie);
        } else if(event.getClass().equals(JobEndedEvent.class)) {
            JobEndedEvent jee = (JobEndedEvent) event;
            onJobEndedEvent(jee);
        } else {
            log.info("UNKNOWN EVENT: " + event);
        }
    }

    private void onManagerInformedEvent(ManagerInformEvent mie) {
        log.info("onManagerInformedEvent(): mie={}", mie);

        jobService.changeJobStatus(mie.getData().getJobId(), mie.getEvent());
    }

    private void onJobEndedEvent(JobEndedEvent jee) {
        log.info("onJobEndedEvent(): jee={}", jee);

        Map<String,String> tableName2tableUrl = new HashMap<>();
        for(JobTableEventData jted: jee.getJobResultTables()) {
            tableName2tableUrl.put(jted.getTableName(), jted.getTableUrl());
        }

        jobService.setJobEnded(jee.getJobId(), tableName2tableUrl);
    }

}
