package it.uniroma3.di.common.api.dto.dim;

import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;

@Data
public class GetEndedJobTablesResponse {

    private Long jobId;

    private Collection<EndedJobTable> endedJobTables;

    public GetEndedJobTablesResponse() {
        this.endedJobTables = new ArrayList<>();
    }

    public void addResultTable(String name, String content) {
        EndedJobTable endedJobTable = new EndedJobTable();
        endedJobTable.setTableName(name);
        endedJobTable.setTableContent(content);

        this.endedJobTables.add(endedJobTable);
    }

}
