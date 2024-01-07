package it.uniroma3.copywritergpt.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class PromptTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @ManyToOne(cascade = CascadeType.ALL)
    private PromptCategory category;

    private String content;

}
