package it.uniroma3.dim.domain;

import it.uniroma3.di.common.api.dto.dim.*;
import it.uniroma3.di.common.api.dto.dis.FileUploadedResponse;
import it.uniroma3.di.common.api.dto.dis.RetrieveFileResponse;
import it.uniroma3.dim.domain.entity.*;
import it.uniroma3.dim.domain.enums.JobStatus;
import it.uniroma3.dim.domain.enums.JobType;
import it.uniroma3.dim.domain.vo.TableInfo;
import it.uniroma3.dim.event.JobStartedEvent;
import it.uniroma3.dim.eventpublisher.impl.JobCreatedEventPublisher;
import it.uniroma3.dim.proxy.DisStorageClient;
import it.uniroma3.dim.proxy.TssStorageClient;
import it.uniroma3.dim.utils.CSVUtils;
import it.uniroma3.dim.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static it.uniroma3.di.common.utils.Constants.CSV_SEPARATOR;

@Service
@Slf4j
public class JobService {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private OntologyService ontologyService;

    @Autowired
    private JobTableRepository jobTableRepository;

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

    public GetJobInfoResponse getJobInfoByName(String name, boolean showTables) {
        Job job = jobRepository.findAllByName(name).get(0);

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

    public void setJobEnded(Long jobId, Map<String, TableInfo> tableName2Info) {
        log.info("addNewTableToJob(): jobId={}, tableName2Info={}", jobId, tableName2Info);

        Job job = jobRepository.findById(jobId).get();

        JobResult jobResult = new JobResult();

        for(Map.Entry<String,TableInfo> tn2i: tableName2Info.entrySet()) {
            String tableName = tn2i.getKey();
            TableInfo tableInfo = tn2i.getValue();

            RetrieveFileResponse retrieveFileResponse = new DisStorageClient(new RestTemplate()).retrieveFileFromUrl(tableInfo.getUrl());
            JobTable jobTable = new JobTable();
            jobTable.setName(tableName);
            jobTable.editColumnNames(tableInfo.getColumnNames());

            byte[] tableContent = retrieveFileResponse.getFileContent();
            jobTable.setTableData(tableContent);

            jobResult.addJobTable(jobTable);
        }

        job.setStatus(JobStatus.COMPLETED);
        job.setJobResult(jobResult);

        jobRepository.save(job);
    }

    public void changeJobStatus(Long jobId, String status) {
        log.info("changeJobStatus(): jobId={}, status={}", jobId, status);

        Job job = this.jobRepository.findById(jobId).get();
        job.setStatus(JobStatus.valueOf(status));

        this.jobRepository.save(job);
    }

    public TablesPreviewResponse getEndedJobResultPreview(Long jobId, int rows) {
        log.info("getEndedJobResultPreview(): jobId={}", jobId);

        TablesPreviewResponse tpr = new TablesPreviewResponse();

        Job job = jobRepository.findById(jobId).get();

        for(JobTable jobTable: job.getJobResult().getResultTables()) {
            String tableName = jobTable.getName();
            String tableContent = Utils.convertBinaryToString(jobTable.getTableData());
            tableContent = CSVUtils.sample(tableContent, true, rows);
            Map<String, Collection<String>> structuredTablePreview = CSVUtils.convertCsvStringToStructure(tableContent);

            tpr.addTable(tableName, structuredTablePreview);
        }

        return tpr;
    }

    public void renameColumns(Long jobId, Map<String,Collection<String>> tableName2columnsNames) {
        log.info("renameColumns(): jobId={}, tableName2columnsNames={}", jobId, tableName2columnsNames);

        Job job = this.jobRepository.findById(jobId).get();

        for(JobTable jobTable: job.getJobResult().getResultTables()) {
            String tableName = jobTable.getName();
            String tableContent = Utils.convertBinaryToString(jobTable.getTableData());

            Collection<String> columnsNames = tableName2columnsNames.get(tableName);
            String newCsvHeader = String.join(CSV_SEPARATOR, columnsNames);
            tableContent = tableContent.replaceFirst(".*\n", newCsvHeader + "\n");

            jobTable.setTableData(tableContent.getBytes(StandardCharsets.UTF_8));
            jobTable.editColumnNames(columnsNames);
            this.jobTableRepository.save(jobTable);
        }

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

            String tableContent = "";
            try {
                tableContent = CSVUtils.mergeColumnsWithSameName(Utils.convertBinaryToString(jobTable.getTableData()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
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


    private void uploadToTss(String tableName, String category, String resultTableContent, Map<String,String> ontology2type) {
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
