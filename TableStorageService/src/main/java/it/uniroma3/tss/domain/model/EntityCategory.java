package it.uniroma3.tss.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class EntityCategory {

    @Id
    private Long id;

    private String name;

    @OneToMany
    private List<CategoryAttribute> attributes;

}
