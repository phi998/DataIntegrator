package it.uniroma3.di.common.api.dto.tss;

import lombok.Data;

import java.util.Collection;

@Data
public class QueryResponse {

    private Collection<ResultEntryResponse> documents;

}
