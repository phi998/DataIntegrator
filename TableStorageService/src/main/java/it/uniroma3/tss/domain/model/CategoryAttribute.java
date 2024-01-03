package it.uniroma3.tss.domain.model;

import it.uniroma3.tss.domain.model.enums.AttributeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class CategoryAttribute {

    @Id
    private Long id;

    private String name;

    @Enumerated
    private AttributeType type;

}
