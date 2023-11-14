package it.uniroma3.webpipe.dataintegration.model;

import java.util.Collection;

public class NewDataIntegrationJobRequest {

    private String name;

    private String context;

    private Collection<String> keywords;

    private TablesCollection tablesCollection;

}
