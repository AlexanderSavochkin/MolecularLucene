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
import org.apache.lucene.store.*;
import org.apache.lucene.util.Version;
import org.junit.Assert;
import org.junit.Test;
import org.molecularlucene.tokenizer.SmilesAnalyzer;

import java.io.File;
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

    final static String benzeneDescription = "It is an aromatic hydrocarbon. A cyclic hydrocarbon with a continuous pi bond.";
    final static String benzeneSMILES = "c1ccccc1";

    final static String tolueneDescription = "It is an aromatic hydrocarbon. It is a mono-substituted benzene derivative. Methylbenzene.";
    final static String tolueneSMILES = "Cc1ccccc1";

    final static String phenolDescription = "An aromatic organic compound. The molecule consists of a phenyl group (-C6H5) bonded to a hydroxyl group (-OH).";
    final static String phenolSMILES = "c1ccc(cc1)O";

    final static String trinitrotolueneDescription = "TNT. Common expolsive. Nitrated toluene.";
    final static String trinitrotolueneSMILES = "O=[N+]([O-])c1c(c(ccc1C)[N+]([O-])=O)[N+]([O-])=O";

    final static String indoleDescription = "An aromatic heterocyclic organic compound. Indole is a common component of fragrances and the precursor to many pharmaceuticals.";
    final static String indoleSMILES = "c1ccc2c(c1)cc[nH]2";

    final static String tryptophanDescription="Essential amino acid. It is encoded in the standard genetic code as the codon UGG.";
    final static String tryptophanSMILES="c1ccc2c(c1)c(c[nH]2)C[C@@H](C(=O)O)N";

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

    Document getBenzeneDocument() {
        Document doc = new Document();
        doc.add( new TextField( NAME_FIELD, "benzene", Field.Store.YES ) );
        doc.add( new TextField( FREE_TEXT_FIELD, benzeneDescription, Field.Store.YES ) );
        doc.add( new TextField( SMILES_FIELD, benzeneSMILES, Field.Store.YES ) );
        return doc;
    }

    Document getTolueneDocument() {
        Document doc = new Document();
        doc.add( new TextField( NAME_FIELD, "toluene", Field.Store.YES ) );
        doc.add( new TextField( FREE_TEXT_FIELD, tolueneDescription, Field.Store.YES ) );
        doc.add( new TextField( SMILES_FIELD, tolueneSMILES, Field.Store.YES ) );
        return doc;
    }

    Document getPhenolDocument() {
        Document doc = new Document();
        doc.add( new TextField( NAME_FIELD, "phenol", Field.Store.YES ) );
        doc.add( new TextField( FREE_TEXT_FIELD, phenolDescription, Field.Store.YES ) );
        doc.add( new TextField( SMILES_FIELD, phenolSMILES, Field.Store.YES ) );
        return doc;
    }

    Document getTrinitrotolueneDocument() {
        Document doc = new Document();
        doc.add( new TextField( NAME_FIELD, "trinitrotoluene", Field.Store.YES ) );
        doc.add( new TextField( FREE_TEXT_FIELD, trinitrotolueneDescription, Field.Store.YES ) );
        doc.add( new TextField( SMILES_FIELD, trinitrotolueneSMILES, Field.Store.YES ) );
        return doc;
    }

    Document getIndoleDocument() {
        Document doc = new Document();
        doc.add( new TextField( NAME_FIELD, "indole", Field.Store.YES ) );
        doc.add( new TextField( FREE_TEXT_FIELD, indoleDescription, Field.Store.YES ) );
        doc.add( new TextField( SMILES_FIELD, indoleSMILES, Field.Store.YES ) );
        return doc;
    }

    Document getTryptophanDocument() {
        Document doc = new Document();
        doc.add( new TextField( NAME_FIELD, "tryptophan", Field.Store.YES ) );
        doc.add( new TextField( FREE_TEXT_FIELD, tryptophanDescription, Field.Store.YES ) );
        doc.add( new TextField( SMILES_FIELD, tryptophanSMILES, Field.Store.YES ) );
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

    Directory getIndexWithCompounds(Document[] docs) {
        Directory directory = null;
        IndexWriter indexWriter = null;
        try {
            directory = new RAMDirectory();
            //directory = new SimpleFSDirectory( new File("D:\\work\\ttt\\") );  //For index structure debugging
            IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_40, getAnalyzer());
            indexWriter = new IndexWriter( directory, config);
            for (Document doc: docs) {
                indexWriter.addDocument(doc);
            }
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
        Document documents[] = {getButhaneDocument(), getAceticAcidDocument() };
        Directory dir = getIndexWithCompounds(documents);

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

            Document foundDoc = searcher.doc(hits[0].doc);
            String foundName = foundDoc.getField(NAME_FIELD).stringValue();
            Assert.assertEquals( foundName, "buthane"  );

        } catch (Exception e) {
            throw new RuntimeException( "Exception during parsing query.", e );
        }
    }

    @Test
    public void testChemicalStructureSearch2() {
        Document documents[] = {getButhaneDocument(), getAceticAcidDocument(),getBenzeneDocument(),
        getTolueneDocument(), getPhenolDocument(), getTrinitrotolueneDocument(),
        getIndoleDocument(), getTryptophanDocument()  };
        Directory dir = getIndexWithCompounds(documents);

        IndexReader reader = null;
        try {
            reader = IndexReader.open(dir);
            IndexSearcher searcher = new IndexSearcher(reader);

            String querystr = "smiles:c1ccc\\(cc1\\)O";
            Query q = null;
            q = new QueryParser(Version.LUCENE_40, FREE_TEXT_FIELD, getAnalyzer()).parse(querystr);

            TopScoreDocCollector collector = TopScoreDocCollector.create(5, true);
            searcher.search(q, collector);
            ScoreDoc[] hits = collector.topDocs().scoreDocs;

            Document foundDoc = searcher.doc(hits[0].doc);
            String foundName = foundDoc.getField(NAME_FIELD).stringValue();
            Assert.assertEquals( foundName, "phenol"  );

            foundDoc = searcher.doc(hits[1].doc);
            foundName = foundDoc.getField(NAME_FIELD).stringValue();
            Assert.assertEquals( foundName, "benzene"  );

        } catch (Exception e) {
	    e.printStackTrace();
            throw new RuntimeException( "Exception during parsing query.", e );
        }
    }



}
