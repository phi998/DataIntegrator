package it.uniroma3.dim.domain;

import it.uniroma3.di.common.api.dto.FileUploadedResponse;
import it.uniroma3.di.common.api.dto.RetrieveFileResponse;
import it.uniroma3.dim.domain.entity.Job;
import it.uniroma3.dim.domain.entity.JobResult;
import it.uniroma3.dim.domain.entity.JobTable;
import it.uniroma3.dim.domain.entity.OntologyItem;
import it.uniroma3.dim.domain.enums.JobStatus;
import it.uniroma3.dim.domain.enums.JobType;
import it.uniroma3.dim.event.JobStartedEvent;
import it.uniroma3.dim.eventpublisher.impl.JobCreatedEventPublisher;
import it.uniroma3.dim.proxy.FileStorageClient;
import it.uniroma3.dim.rest.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
@Slf4j
public class JobService {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private JobCreatedEventPublisher publisher;

    public CreateNewJobResponse createNewJob(String jobName, Collection<String> ontologyItems, String jobType) {
        log.info("createNewJob(): jobName={}", jobName);

        Job job = new Job();
        job.setName(jobName);
        job.setStatus(JobStatus.CREATED);
        job.setJobType(JobType.valueOf(jobType));
        job.addOntologyItems(ontologyItems);

        job = jobRepository.save(job);

        return Converter.toCreateNewJobResponse(job);
    }

    public GetJobInfoResponse getJobInfo(Long jobId) {
        Job job = jobRepository.findById(jobId).get();

        return Converter.toGetJobInfoResponse(job);
    }

    public TableAddedToJobResponse addNewTableToJob(Long jobId, String tableName, String tableContent) {
        log.info("addNewTableToJob(): jobId={}, tableName={}", jobId, tableName);

        Job job = jobRepository.findById(jobId).get();

        JobTable jobTable = new JobTable();
        jobTable.setName(tableName);
        jobTable.setTableData(tableContent.getBytes(StandardCharsets.UTF_8));

        job.addJobTable(jobTable);

        job = jobRepository.save(job);

        return Converter.toTableAddedToJobResponse(job);
    }

    public JobStartedResponse startJob(Long jobId) {
        log.info("addNewTableToJob(): jobId={}", jobId);

        Job job = jobRepository.findById(jobId).get();
        job.setStatus(JobStatus.STARTED);

        JobStartedEvent jobStartedEvent = new JobStartedEvent();
        jobStartedEvent.setJobId(job.getId());
        jobStartedEvent.setJobName(job.getName());
        jobStartedEvent.getData().getOntology().addAll(job.getJobData().getOntology().stream().map(OntologyItem::getItem).toList());

        for(JobTable jt : job.getJobData().getTables()) {
            String tableResourceUrl = this.uploadFile(jt.getName(), jt.getTableData());
            jobStartedEvent.getData().addTableData(jt.getName(), tableResourceUrl);
        }

        publisher.publish(jobStartedEvent);

        job = jobRepository.save(job);

        return Converter.toJobStartedResponse(job);
    }

    public void setJobEnded(Long jobId, String jobResultUrl) {
        Job job = jobRepository.findById(jobId).get();
        String resourceAssignedName = this.getLastPartOfUrl(jobResultUrl);

        RetrieveFileResponse retrieveFileResponse = new FileStorageClient(new RestTemplate()).retrieveFile(resourceAssignedName);

        job.setStatus(JobStatus.COMPLETED);


        JobResult jobResult = new JobResult();


        job = jobRepository.save(job);


    }

    /**
     * @return table content url on dis service
     * */
    private String uploadFile(String tableName, byte[] tableData) {
        FileStorageClient fileStorageClient = new FileStorageClient(new RestTemplate());
        FileUploadedResponse fileUploadedResponse = fileStorageClient.uploadFile(tableName, tableData, true);

        return fileUploadedResponse.getResourceUrl();
    }

    private String getLastPartOfUrl(String urlString) {
        String lastSegment = "";

        try {
            URL url = new URL(urlString);
            String path = url.getPath();

            String[] pathSegments = path.split("/");
            lastSegment = pathSegments[pathSegments.length - 1];
        } catch (MalformedURLException e) {
            log.error("getLastPartOfUrl(): Error={}", e.getMessage());
        }

        return lastSegment;
    }


    static class Converter {

        public static DataIntegrationJobCreatedResponse toJobCreatedResponse(Job job) {
            DataIntegrationJobCreatedResponse dataIntegrationJobCreatedResponse = new DataIntegrationJobCreatedResponse();
            dataIntegrationJobCreatedResponse.setJobId(job.getId());
            dataIntegrationJobCreatedResponse.setJobName(job.getName());
            dataIntegrationJobCreatedResponse.setJobStatus(job.getStatus().name());

            return dataIntegrationJobCreatedResponse;
        }

        public static JobStartedResponse toJobStartedResponse(Job job) {
            JobStartedResponse jobStartedResponse = new JobStartedResponse();
            jobStartedResponse.setJobId(job.getId());
            jobStartedResponse.setJobName(job.getName());
            jobStartedResponse.setJobStatus(job.getStatus().name());

            return jobStartedResponse;
        }

        public static CreateNewJobResponse toCreateNewJobResponse(Job job) {
            CreateNewJobResponse createNewJobResponse = new CreateNewJobResponse();
            createNewJobResponse.setJobId(job.getId());
            createNewJobResponse.setJobName(job.getName());
            createNewJobResponse.setJobStatus(job.getStatus().name());
            return createNewJobResponse;
        }

        public static GetJobInfoResponse toGetJobInfoResponse(Job job) {
            GetJobInfoResponse jobInfoResponse = new GetJobInfoResponse();
            jobInfoResponse.setId(job.getId());
            jobInfoResponse.setName(job.getName());
            jobInfoResponse.setJobStatus(job.getStatus().name());

            return jobInfoResponse;
        }

        public static TableAddedToJobResponse toTableAddedToJobResponse(Job job) {
            TableAddedToJobResponse tableAddedToJobResponse = new TableAddedToJobResponse();
            tableAddedToJobResponse.setJobName(job.getName());
            tableAddedToJobResponse.setJobId(job.getId());

            return tableAddedToJobResponse;
        }

    }

}
