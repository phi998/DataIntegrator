package it.uniroma3.di.common.api.dto;

import lombok.Data;

@Data
public class RetrieveFileResponse {

    private String fileName;

    private byte[] fileContent;

}
