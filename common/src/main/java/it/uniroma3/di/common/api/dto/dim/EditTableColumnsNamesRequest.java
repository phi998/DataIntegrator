package it.uniroma3.di.common.api.dto.dim;

import lombok.Data;

import java.util.Collection;
import java.util.Map;

@Data
public class EditTableColumnsNamesRequest {

    private Map<String, Collection<String>> table2colnames;

}
