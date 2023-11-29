package it.uniroma3.dis.domain;

import it.uniroma3.di.common.api.dto.FileUploadedResponse;
import it.uniroma3.di.common.api.dto.RetrieveFileResponse;
import it.uniroma3.di.common.utils.Endpoints;
import it.uniroma3.dis.domain.entity.StoredFile;
import it.uniroma3.dis.domain.entity.enums.StoreStatus;
import it.uniroma3.dis.utils.FileUtils;
import it.uniroma3.dis.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class DataStorageService {

    private final static String SERVICE_PREFIX_URL = Endpoints.DIS_ENDPOINT + "/files/";

    @Autowired
    private DataStorageRepository dataStorageRepository;

    public FileUploadedResponse storeFile(String fileName, byte[] fileContent, boolean persist) {
        String assignedName = UUID.randomUUID() + Utils.getFileExtension(fileName);

        StoredFile storedFile = new StoredFile();
        storedFile.setName(fileName);
        storedFile.setAssignedName(assignedName);
        storedFile.setPersist(persist);

        FileUtils.saveStoredFile(storedFile.getAssignedName(), fileContent);

        dataStorageRepository.save(storedFile);

        return Converter.toFileUploadedResponse(storedFile, StoreStatus.STORED);
    }

    public RetrieveFileResponse retrieveFile(String assignedName) {
        StoredFile storedFile = this.dataStorageRepository.findAllByAssignedName(assignedName).get(0);

        byte[] fileContent = FileUtils.getFileContent(assignedName);
        if(!storedFile.isPersist()) {
            FileUtils.deleteStoredFile(assignedName);
        }

        return Converter.toRetrieveFileResponse(storedFile, fileContent);
    }

    static class Converter {

        static FileUploadedResponse toFileUploadedResponse(StoredFile storedFile, StoreStatus storeStatus) {
            FileUploadedResponse fileUploadedResponse = new FileUploadedResponse();
            fileUploadedResponse.setResourceUrl(SERVICE_PREFIX_URL + storedFile.getAssignedName());
            fileUploadedResponse.setResult(storeStatus.name());

            return fileUploadedResponse;
        }

        static RetrieveFileResponse toRetrieveFileResponse(StoredFile storedFile, byte[] fileContent) {
            RetrieveFileResponse retrieveFileResponse = new RetrieveFileResponse();
            retrieveFileResponse.setFileName(storedFile.getName());
            retrieveFileResponse.setFileContent(fileContent);

            return retrieveFileResponse;
        }

    }

}
