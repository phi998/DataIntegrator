package it.uniroma3.dim.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class JobTableColumn {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private int index;

    private String name;

    public JobTableColumn() {

    }

    public JobTableColumn(int index, String name) {
        this.index = index;
        this.name = name;
    }

}
