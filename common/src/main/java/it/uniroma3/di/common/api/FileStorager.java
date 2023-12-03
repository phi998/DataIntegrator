package it.uniroma3.di.common.api;

import it.uniroma3.di.common.api.dto.FileUploadedResponse;
import it.uniroma3.di.common.api.dto.RetrieveFileResponse;

public interface FileStorager {

    FileUploadedResponse uploadFile(String fileName, byte[] fileContent, boolean persist);

    RetrieveFileResponse retrieveFile(String assignedName);

    RetrieveFileResponse retrieveFileFromUrl(String url);

}
