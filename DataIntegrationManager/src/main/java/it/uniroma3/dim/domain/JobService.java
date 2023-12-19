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
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Map;

@Service
@Slf4j
public class JobService {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private JobCreatedEventPublisher publisher;

    public CreateNewJobResponse createNewJob(String jobName, Collection<OntologyItemInput> ontologyItemInputs, String jobType) {
        log.info("createNewJob(): jobName={}, ontology={}, jobType={}", jobName, ontologyItemInputs, jobName);

        Job job = new Job();
        job.setName(jobName);
        job.setStatus(JobStatus.CREATED);
        job.setJobType(JobType.valueOf(jobType));

        for(OntologyItemInput oii: ontologyItemInputs) {
            job.addOntologyItem(oii.getItem(), oii.getType(), oii.getImportance());
        }

        job = jobRepository.save(job);

        log.info("createNewJob(): persisted job={}", job);

        return Converter.toCreateNewJobResponse(job);
    }

    public GetJobInfoResponse getJobInfo(Long jobId, boolean showTables) {
        Job job = jobRepository.findById(jobId).get();

        return Converter.toGetJobInfoResponse(job, showTables);
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

    public JobStartedResponse startJob(Long jobId, Collection<Integer> columnsToDrop) {
        log.info("addNewTableToJob(): jobId={}", jobId);

        Job job = jobRepository.findById(jobId).get();
        job.setStatus(JobStatus.STARTED);

        JobStartedEvent jobStartedEvent = new JobStartedEvent();
        jobStartedEvent.setJobId(job.getId());
        jobStartedEvent.setJobName(job.getName());
        jobStartedEvent.getData().setColumnsToDrop(columnsToDrop);
        jobStartedEvent.getData().getOntology().addAll(job.getJobData().getOntology().stream().map(OntologyItem::getItem).toList());

        for(JobTable jt : job.getJobData().getTables()) {
            String tableResourceUrl = this.uploadFile(jt.getName(), jt.getTableData());
            jobStartedEvent.getData().addTableData(jt.getName(), tableResourceUrl);
        }

        publisher.publish(jobStartedEvent);

        job = jobRepository.save(job);

        return Converter.toJobStartedResponse(job);
    }

    public void setJobEnded(Long jobId, Map<String,String> tableName2Url) {
        log.info("addNewTableToJob(): jobId={}, tableName2Url={}", jobId, tableName2Url);

        Job job = jobRepository.findById(jobId).get();

        JobResult jobResult = new JobResult();

        for(Map.Entry<String,String> tn2u: tableName2Url.entrySet()) {
            String tableName = tn2u.getKey();
            String tableUrl = tn2u.getValue();

            RetrieveFileResponse retrieveFileResponse = new FileStorageClient(new RestTemplate()).retrieveFile(tableUrl);
            JobTable jobTable = new JobTable();
            jobTable.setName(tableName);
            jobTable.setTableData(retrieveFileResponse.getFileContent());

            jobResult.addJobTable(jobTable);
        }

        job.setStatus(JobStatus.COMPLETED);
        job.setJobResult(jobResult);

        jobRepository.save(job);
    }

    /**
     * @return table content url on dis service
     * */
    private String uploadFile(String tableName, byte[] tableData) {
        FileStorageClient fileStorageClient = new FileStorageClient(new RestTemplate());
        FileUploadedResponse fileUploadedResponse = fileStorageClient.uploadFile(tableName, tableData, true);

        return fileUploadedResponse.getResourceUrl();
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

        public static GetJobInfoResponse toGetJobInfoResponse(Job job, boolean showTables) {
            GetJobInfoResponse jobInfoResponse = new GetJobInfoResponse();
            jobInfoResponse.setId(job.getId());
            jobInfoResponse.setName(job.getName());
            jobInfoResponse.setJobStatus(job.getStatus().name());

            if(showTables) {
                for(JobTable jt: job.getJobData().getTables()) {
                    EndedJobTable endedJobTable = new EndedJobTable();
                    endedJobTable.setTableName(jt.getName());
                    endedJobTable.setTableContent(new String(jt.getTableData(), StandardCharsets.UTF_8));
                    jobInfoResponse.getEndedJobTables().add(endedJobTable);
                }
            }

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
