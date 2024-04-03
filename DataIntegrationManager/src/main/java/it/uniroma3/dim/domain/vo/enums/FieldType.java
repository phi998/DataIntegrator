package it.uniroma3.dim.domain.vo.enums;

public enum FieldType {

    STRING("String"),
    TEXT("String"),
    NUMERIC("Int32");

    public String sqlName;

    FieldType(String sqlName) {
        this.sqlName = sqlName;
    }

    public static FieldType fromName(String name) {
        // At the moment I work only with strings
        /*for(FieldType type: FieldType.values())
            if(name.equals(type.name()))
                return FieldType.fromName(name);*/

        return STRING;
    }

}
