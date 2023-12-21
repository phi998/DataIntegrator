package it.uniroma3.dim.domain.entity;

import it.uniroma3.dim.domain.enums.OntologyItemType;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class OntologyItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String label;

    @Enumerated(EnumType.STRING)
    private OntologyItemType type;

    private Integer importance;

    private String notes;

    public OntologyItem() {

    }

    public OntologyItem(String label) {
        this.label = label;
        this.type = OntologyItemType.UNKNOWN;
        this.importance = 0;
    }

}
