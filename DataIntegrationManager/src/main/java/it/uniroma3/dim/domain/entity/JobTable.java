package it.uniroma3.dim.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class JobTable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private byte[] tableData;

}
