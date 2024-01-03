package it.uniroma3.dis.rest;

import it.uniroma3.di.common.api.dto.dis.FileUploadedResponse;
import it.uniroma3.di.common.api.dto.dis.RetrieveFileResponse;
import it.uniroma3.di.common.api.dto.dis.UploadFileRequest;
import it.uniroma3.dis.domain.DataStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class DataStorageController {

    @Autowired
    private DataStorageService dataStorageService;

    @PostMapping("/files")
    public FileUploadedResponse uploadFile(@RequestBody UploadFileRequest uploadFileRequest) {
        log.info("uploadFile(): uploadFileRequest={}", uploadFileRequest.getFileName());

        String fileName = uploadFileRequest.getFileName();
        byte[] fileContent = uploadFileRequest.getFileContent();
        boolean persist = uploadFileRequest.isPersist();

        return dataStorageService.storeFile(fileName, fileContent, persist);
    }

    @GetMapping("/files/{assignedName}")
    public RetrieveFileResponse retrieveFile(@PathVariable("assignedName") String assignedName) {
        log.info("retrieveFile(): assignedName={}", assignedName);
        return dataStorageService.retrieveFile(assignedName);
    }

}
