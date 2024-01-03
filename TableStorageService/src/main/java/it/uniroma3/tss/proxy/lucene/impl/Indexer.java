package it.uniroma3.tss.proxy.lucene.impl;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;


public class Indexer {

    private static final String STOPWORDS_FILE = "stopwords.txt";

    private Directory indexDirectory;

    public Indexer(String indexDir) throws IOException {
        this.indexDirectory = FSDirectory.open(Paths.get(indexDir));
    }

    public void init() {

    }
    /*
    public void buildIndex(Collection<TableField> fields) throws IOException {
        IndexWriter writer = null;

        Analyzer defaultAnalyzer = new StandardAnalyzer();
        Map<String, Analyzer> perFieldAnalyzers = new HashMap<>();

        for(TableField field: fields) {
            perFieldAnalyzers.put(field.getSymbolicName(), CustomAnalyzer.builder().withTokenizer(WhitespaceTokenizerFactory.class).addTokenFilter(WordDelimiterGraphFilterFactory.class).build());
        }

        CustomAnalyzer.Builder contentAnalyzerBuilder = null;
        contentAnalyzerBuilder = CustomAnalyzer.builder().withTokenizer(this.tokenizerFactoryClass, PropertiesReader.readTokenizerFactoryParams());

        for(Class<TokenFilterFactory> tokenFilterFactory : this.tokenFilterFactoryClasses) {
            contentAnalyzerBuilder.addTokenFilter(tokenFilterFactory);
        }

        contentAnalyzerBuilder.addTokenFilter(StopFilterFactory.NAME, "words", STOPWORDS_FILE);
        CustomAnalyzer ca = contentAnalyzerBuilder.build();

        /*for(TableField field: fields) {
            perFieldAnalyzers.put(field.getSymbolicName(), ca);
        }

        Analyzer analyzer = new PerFieldAnalyzerWrapper(defaultAnalyzer, perFieldAnalyzers);
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        config.setCodec(new SimpleTextCodec());

        writer = new IndexWriter(this.indexDirectory, config);
        writer.deleteAll();

        writer.commit();
        writer.close();
    }
    */

    public void addDocuments() {

    }


}
