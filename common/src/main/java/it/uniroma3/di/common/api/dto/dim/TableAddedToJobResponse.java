package it.uniroma3.di.common.api.dto.dim;

import lombok.Data;

@Data
public class TableAddedToJobResponse {

    private Long jobId;

    private String jobName;

}
