package it.uniroma3.tss.domain.vo;

import it.uniroma3.tss.domain.vo.enums.FieldType;
import lombok.Data;

@Data
public class TableField {

    private String name;

    private String symbolicName;

    private FieldType type;

}
