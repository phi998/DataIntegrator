package it.uniroma3.dim.rest.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;

@Data
public class GetJobInfoResponse {

    private Long id;

    private String name;

    private String jobStatus;

    private Collection<EndedJobTable> endedJobTables;

    public GetJobInfoResponse() {
        this.endedJobTables = new ArrayList<>();
    }

}
