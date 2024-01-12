package it.uniroma3.di.common.api.dto.dim;

import lombok.Data;

import java.util.Collection;

@Data
public class StartJobRequest {

    private Collection<Integer> columnsToDrop;

}
