package it.uniroma3.di.common.api.dto.dim;

import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;

@Data
public class GetJobInfoResponse {

    private Long id;

    private String name;

    private String jobStatus;

    private GetOntologyResponse ontology;

    private Collection<EndedJobTable> endedJobTables;

    public GetJobInfoResponse() {
        this.endedJobTables = new ArrayList<>();
    }

}
