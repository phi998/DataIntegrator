package it.uniroma3.dim.proxy.storage.solr;

import org.apache.solr.client.solrj.SolrQuery;

import java.util.ArrayList;
import java.util.List;

public class SolrQueryBuilder {

    private List<String> conditions;

    public SolrQueryBuilder() {
        this.conditions = new ArrayList<>();
    }

    public SolrQueryBuilder addCondition(String field, String value) {
        conditions.add(field + ":" + value);
        return this;
    }

    public SolrQueryBuilder addOrCondition(String field, String... values) {
        StringBuilder orCondition = new StringBuilder("(");
        for (String value : values) {
            orCondition.append(field).append(":").append(value).append(" OR ");
        }
        orCondition.setLength(orCondition.length() - 4); // Remove the last " OR "
        orCondition.append(")");
        conditions.add(orCondition.toString());
        return this;
    }

    public SolrQuery build() {
        SolrQuery query = new SolrQuery();
        query.set("q", String.join(" AND ", conditions));
        return query;
    }
}
