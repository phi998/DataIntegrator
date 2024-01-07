package it.uniroma3.dim.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Data
public class JobTable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @OneToMany(cascade = CascadeType.ALL)
    private List<JobTableColumn> columns;

    private byte[] tableData;

    public void editColumnNames(Collection<String> columnNames) {
        this.columns = new ArrayList<>();

        int columnIndex = 0;
        for(String columnName : columnNames) {
            this.columns.add(new JobTableColumn(columnIndex, columnName));
            columnIndex++;
        }
    }

}
