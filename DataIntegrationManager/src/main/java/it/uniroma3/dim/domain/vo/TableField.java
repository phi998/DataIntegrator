package it.uniroma3.dim.domain.vo;

import it.uniroma3.dim.domain.vo.enums.FieldType;
import lombok.Data;

@Data
public class TableField {

    private String name;

    private String symbolicName;

    private FieldType type;

}
