package it.uniroma3.di.common.api.dto;

import lombok.Data;

@Data
public class UploadFileRequest {

    private String fileName;

    private byte[] fileContent;

    private boolean persist;

}
