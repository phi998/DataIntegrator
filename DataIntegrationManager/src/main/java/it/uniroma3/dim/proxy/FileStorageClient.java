package it.uniroma3.dim.proxy;

import it.uniroma3.di.common.api.FileStorager;
import it.uniroma3.di.common.api.dto.FileUploadedResponse;
import it.uniroma3.di.common.api.dto.RetrieveFileResponse;
import it.uniroma3.di.common.api.dto.UploadFileRequest;
import it.uniroma3.di.common.utils.Endpoints;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@Slf4j
public class FileStorageClient implements FileStorager {

    private final RestTemplate restTemplate;
    private final static String DIS_ENDPOINT_FILES = Endpoints.DIS_ENDPOINT + "/files";

    // TODO autowiring
    public FileStorageClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public FileUploadedResponse uploadFile(String fileName, byte[] fileContent, boolean persist) {
        UploadFileRequest uploadFileRequest = new UploadFileRequest();
        uploadFileRequest.setFileName(fileName);
        uploadFileRequest.setFileContent(fileContent);
        uploadFileRequest.setPersist(persist);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UploadFileRequest> requestEntity = new HttpEntity<>(uploadFileRequest, headers);

        ResponseEntity<FileUploadedResponse> responseEntity = restTemplate.postForEntity(DIS_ENDPOINT_FILES, requestEntity, FileUploadedResponse.class);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            log.info("POST request successful. Response: {}", responseEntity.getBody());
        } else {
            log.error("POST request failed. Response: {}", responseEntity.getStatusCode());
        }

        return responseEntity.getBody();
    }

    @Override
    public RetrieveFileResponse retrieveFile(String assignedName) {
        String apiUrl = DIS_ENDPOINT_FILES + "/" + assignedName;

        // TODO manage errors
        return restTemplate.getForObject(apiUrl, RetrieveFileResponse.class);
    }

    @Override
    public RetrieveFileResponse retrieveFileFromUrl(String url) {
        return restTemplate.getForObject(url, RetrieveFileResponse.class);
    }
}
