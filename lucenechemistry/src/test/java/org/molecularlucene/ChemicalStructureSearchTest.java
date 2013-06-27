package org.molecularlucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.junit.Assert;
import org.junit.Test;
import org.molecularlucene.tokenizer.SmilesAnalyzer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Unit test for lucene structures search.
 */
public class ChemicalStructureSearchTest
{
    final static String NAME_FIELD = "name";
    final static String FREE_TEXT_FIELD = "text";
    final static String SMILES_FIELD = "smiles";

    final static String buthaneDescription = "Normal buthane. Alkane with 4 carbon atoms in. Unbranched isomer. Gas. at room temperature and atmospheric pressure.";
    final static String buthaneSMILES = "CCCC";

    final static String aceticAcidDescription = "Acetic acid is one of the simplest carboxylic acids. Liquid.";
    final static String aceticAcidSMILES = "CC(O)=O";

    Document getButhaneDocument() {
        Document doc = new Document();
        doc.add( new TextField( NAME_FIELD, "buthane", Field.Store.YES ) );
        doc.add( new TextField( FREE_TEXT_FIELD, buthaneDescription, Field.Store.YES ) );
        doc.add( new TextField( SMILES_FIELD, buthaneSMILES, Field.Store.YES ) );
        return doc;
    }

    Document getAceticAcidDocument() {
        Document doc = new Document();
        doc.add( new TextField( NAME_FIELD, "acetic acid", Field.Store.YES ) );
        doc.add( new TextField( FREE_TEXT_FIELD, aceticAcidDescription, Field.Store.YES ) );
        doc.add( new TextField( SMILES_FIELD, aceticAcidSMILES, Field.Store.YES ) );
        return doc;
    }

    Analyzer getAnalyzer() {
        Map<String,Analyzer> analyzerPerField = new HashMap<String,Analyzer>();

        analyzerPerField.put(FREE_TEXT_FIELD, new StandardAnalyzer(Version.LUCENE_40) );
        analyzerPerField.put(SMILES_FIELD, new SmilesAnalyzer() );

        PerFieldAnalyzerWrapper analyzerWrapper =
                new PerFieldAnalyzerWrapper(new StandardAnalyzer(Version.LUCENE_40), analyzerPerField);

        return analyzerWrapper;
    }

    Directory getIndexWithButhaneAndAceticAcid() {
        RAMDirectory directory = null;
        IndexWriter indexWriter = null;
        try {
            directory = new RAMDirectory();
            IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_40, getAnalyzer());
            indexWriter = new IndexWriter( directory, config);
            indexWriter.addDocument(getButhaneDocument());
            indexWriter.addDocument(getAceticAcidDocument());
        } catch (IOException e) {
            throw new RuntimeException( "Exception during creating index.", e );
        } finally {
            if (indexWriter != null)
                try {
                    indexWriter.close();
                } catch (IOException e) {
                    throw new RuntimeException( "Exception during closing index.", e );
                }
        }
        return directory;
    }

    @Test
    public void testChemicalStructureSearch1() {
        Directory dir = getIndexWithButhaneAndAceticAcid();

        IndexReader reader = null;
        try {
            reader = IndexReader.open(dir);
            IndexSearcher searcher = new IndexSearcher(reader);

            String querystr = "smiles:CCC";
            Query q = null;
            q = new QueryParser(Version.LUCENE_40, FREE_TEXT_FIELD, getAnalyzer()).parse(querystr);

            TopScoreDocCollector collector = TopScoreDocCollector.create(5, true);
            searcher.search(q, collector);
            ScoreDoc[] hits = collector.topDocs().scoreDocs;

            Assert.assertEquals( hits.length, 1 );
            Document foundDoc = searcher.doc(hits[0].doc);
            String foundName = foundDoc.getField(NAME_FIELD).stringValue();
            Assert.assertEquals( foundName, "buthane"  );

        } catch (Exception e) {
            throw new RuntimeException( "Exception during parsing query.", e );
        }


    }
}
