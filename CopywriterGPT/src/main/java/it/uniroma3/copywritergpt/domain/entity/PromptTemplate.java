package it.uniroma3.copywritergpt.domain.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class PromptTemplate {

    @Id
    private Long id;

    private String name;

    @ManyToOne(cascade = CascadeType.ALL)
    private PromptCategory category;

    private String content;

}
