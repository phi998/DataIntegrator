package it.uniroma3.tss.domain.model;

import it.uniroma3.tss.domain.model.enums.AttributeType;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class CategoryAttribute {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @Enumerated
    private AttributeType type;

}
