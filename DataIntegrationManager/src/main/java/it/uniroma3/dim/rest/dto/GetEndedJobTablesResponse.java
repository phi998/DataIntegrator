package it.uniroma3.dim.rest.dto;

import lombok.Data;

import java.util.Collection;

@Data
public class GetEndedJobTablesResponse {

    private Long jobId;

    private Collection<EndedJobTable> endedJobTables;

}
