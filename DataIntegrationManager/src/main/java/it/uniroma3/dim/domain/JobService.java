package it.uniroma3.dim.domain;

import it.uniroma3.di.common.api.dto.dis.FileUploadedResponse;
import it.uniroma3.di.common.api.dto.dis.RetrieveFileResponse;
import it.uniroma3.dim.domain.entity.*;
import it.uniroma3.dim.domain.enums.JobStatus;
import it.uniroma3.dim.domain.enums.JobType;
import it.uniroma3.dim.event.JobStartedEvent;
import it.uniroma3.dim.eventpublisher.impl.JobCreatedEventPublisher;
import it.uniroma3.dim.proxy.DisStorageClient;
import it.uniroma3.dim.proxy.TssStorageClient;
import it.uniroma3.dim.rest.dto.*;
import it.uniroma3.dim.utils.CSVUtils;
import it.uniroma3.dim.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class JobService {

    private static final int ROWS_TO_SAMPLE = 10;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private OntologyService ontologyService;

    @Autowired
    private JobCreatedEventPublisher publisher;

    public CreateNewJobResponse createNewJob(String jobName, String ontologyName, String jobType) {
        log.info("createNewJob(): jobName={}, ontologyName={}, jobType={}", jobName, ontologyName, jobName);

        Job job = new Job();
        job.setName(jobName);
        job.setStatus(JobStatus.CREATED);
        job.setJobType(JobType.valueOf(jobType));
        job.getJobData().setOntology(ontologyService.getOntologyByName(ontologyName));

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

        Ontology ontology = job.getJobData().getOntology();
        jobStartedEvent.getData().setOntologyName(ontology.getName());
        for(OntologyItem oi: ontology.getItems()) {
            jobStartedEvent.getData().addOntologyItem(oi.getLabel(),oi.getImportance(),oi.getType().name(),oi.getNotes());
        }

        for(JobTable jt : job.getJobData().getTables()) {
            String tableResourceUrl = this.uploadFileToDis(jt.getName(), jt.getTableData());
            jobStartedEvent.getData().addTableData(jt.getName(), tableResourceUrl);
        }

        log.info("PUBLISH MESSAGE: jobStartedEvent={}", jobStartedEvent);

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

            RetrieveFileResponse retrieveFileResponse = new DisStorageClient(new RestTemplate()).retrieveFile(tableUrl);
            JobTable jobTable = new JobTable();
            jobTable.setName(tableName);
            jobTable.setTableData(retrieveFileResponse.getFileContent());

            jobResult.addJobTable(jobTable);
        }

        job.setStatus(JobStatus.COMPLETED);
        job.setJobResult(jobResult);

        jobRepository.save(job);
    }

    public void changeJobStatus(Long jobId, String status) {

    }

    public Map<String, String> getEndedJobResultPreview(Long jobId) {
        log.info("getEndedJobResultPreview(): jobId={}", jobId);

        Map<String, String> previews = new HashMap<>();

        Job job = jobRepository.findById(jobId).get();

        for(JobTable jobTable: job.getJobResult().getResultTables()) {
            String tableContent = Utils.convertBinaryToString(jobTable.getTableData());
            tableContent = CSVUtils.sample(tableContent, ROWS_TO_SAMPLE);
            previews.put(jobTable.getName(), tableContent);
        }

        return previews;
    }

    public void push(Long jobId) {
        log.info("push(): jobId={}", jobId);

        Job job = this.jobRepository.findById(jobId).get();

        Map<String, String> ontology = new HashMap<>();
        for(OntologyItem ontologyItem: job.getJobData().getOntology().getItems()) {
            ontology.put(ontologyItem.getLabel(), ontologyItem.getType().name());
        }

        for(JobTable jobTable: job.getJobResult().getResultTables()) {
            String tableName = jobTable.getName();
            byte[] tableContent = jobTable.getTableData();
            String category = job.getName(); // FIXME add category attribute to job

            this.uploadToTss(tableName,category,tableContent,ontology);
        }
    }

    /**
     * @return table content url on dis service
     * */
    private String uploadFileToDis(String tableName, byte[] tableData) {
        DisStorageClient disStorageClient = new DisStorageClient(new RestTemplate());
        FileUploadedResponse fileUploadedResponse = disStorageClient.uploadFile(tableName, tableData, true);

        return fileUploadedResponse.getResourceUrl();
    }


    private void uploadToTss(String tableName, String category, byte[] resultTableContent, Map<String,String> ontology2type) {
        TssStorageClient tssStorageClient = new TssStorageClient(new RestTemplate());
        tssStorageClient.uploadJobResultTable(tableName,category,resultTableContent,ontology2type);
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

            Ontology o = job.getJobData().getOntology();
            for(OntologyItem oi: o.getItems()) {
                createNewJobResponse.addOntologyItemLabel(oi.getLabel());
            }

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
