package it.uniroma3.di.common.api;

import it.uniroma3.di.common.api.dto.dis.FileUploadedResponse;
import it.uniroma3.di.common.api.dto.dis.RetrieveFileResponse;

public interface FileStorager {

    FileUploadedResponse uploadFile(String fileName, byte[] fileContent, boolean persist);

    RetrieveFileResponse retrieveFile(String assignedName);

    RetrieveFileResponse retrieveFileFromUrl(String url);

}
