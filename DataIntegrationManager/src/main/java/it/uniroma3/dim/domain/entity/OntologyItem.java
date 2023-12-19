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

    private String item;

    @Enumerated(EnumType.STRING)
    private OntologyItemType type;

    private Integer importance;

    public OntologyItem() {

    }

    public OntologyItem(String item) {
        this.item = item;
        this.type = OntologyItemType.UNKNOWN;
        this.importance = 0;
    }

}
