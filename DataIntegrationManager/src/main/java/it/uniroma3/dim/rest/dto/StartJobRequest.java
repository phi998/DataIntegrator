package it.uniroma3.dim.rest.dto;

import lombok.Data;

import java.util.Collection;

@Data
public class StartJobRequest {

    private Collection<Integer> columnsToDrop;

}
