package org.molecularlucene.elementarygroupchains;


import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.molecularlucene.tokenizer.SmilesTokenizer;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/*
        "NO3" |
        "SiO3" | "PO3" | "PO4" | "SO4" | "ClO" | "ClO2" | "ClO3" | "ClO4" |
        "SeO3" | "SeO4" | "BrO" | "BrO2" | "BrO3" | "BrO4" |
        "IO" | "IO2" | "IO3" | "IO4" |
        "CH3" | "CH2" | "C2H5" | "C6H5" |
        "COOH" | "NH2" | "NH4"import java.io.StringReader;
*/

public class Main {

    private static Map<String, Integer> extractChains(String elementaryGroup, String smiles, String boundAtom) throws IOException {
        SmilesTokenizer smilesTokenizer = new SmilesTokenizer(new StringReader(smiles));

        Map<String, Integer> chain2Count = new HashMap<String, Integer>();

        while ( smilesTokenizer.incrementToken() ) {
            CharTermAttribute att = smilesTokenizer.getAttribute(CharTermAttribute.class);
            String chain = att.toString();

            if (boundAtom != null && chain.contains(boundAtom)) {
                continue;
            }

            String cleanChain = chain.replaceAll("_\\d+", "");

            int count = chain2Count.containsKey(cleanChain) ? chain2Count.get(cleanChain) : 0;
            chain2Count.put(cleanChain, count + 1);
        }

        return chain2Count;
    }

    private static void printResults(String elementaryGroup, Map<String, Integer> fragments) {
        //put("OH", new String[]{});
        System.out.format("put(\"%s\", new HashMap<String, Integer>() {{", elementaryGroup);
        for (String key : fragments.keySet()) {
            System.out.format("\tput(\"%s\", %d);",key, fragments.get(key));
        }
        System.out.println("}});");
    }

    private static void processGroup(String elementaryGroup, String smiles, String boundAtom) throws IOException {
        Map<String, Integer> fragments = extractChains(elementaryGroup, smiles, boundAtom);
        printResults(elementaryGroup, fragments);
    }

    public static void main(String[] args) throws IOException {
        String[] atomChains = null;

        processGroup("OH", "O[Na]", "Na");
        processGroup("CO3","[Na+].[Na+].[O-]C([O-])=O","Na");
        processGroup("NO2","C[N+](=O)[O-]","C");
        processGroup("NO3","[Na+].[O-][N+]([O-])=O","Na");
        processGroup("SiO3","[Na+].[Na+].[O-][Si]([O-])=O","Na");
        processGroup("PO3","[O-]P(=O)([O-])[O-]",null);
        processGroup("PO4","[O-]P([O-])([O-])=O",null);
        processGroup("SO3","[O-]S(=O)[O-].[Na+].[Na+]","Na");
        processGroup("SO4","[O-]S(=O)(=O)[O-]",null);
        processGroup("ClO","[O-]Cl",null);
        processGroup("ClO2","[O-]Cl=O",null);
        processGroup("ClO3","OCl(=O)=O",null);
        processGroup("ClO4","[O-][Cl](=O)(=O)=O",null);
        processGroup("BrO","[O-]Br",null);
        processGroup("BrO2","[O-]Br=O",null);
        processGroup("BrO3","OBr(=O)=O",null);
        processGroup("BrO4","[O-][Br](=O)(=O)=O",null);
        processGroup("IO","[O-]I",null);
        processGroup("IO2","[O-]I=O",null);
        processGroup("IO3","OI(=O)=O",null);
        processGroup("IO4","[O-][I](=O)(=O)=O",null);
        processGroup("IO4","[O-][Mn](=O)(=O)=O",null);
        processGroup("CH3","CCl","Cl");
        processGroup("CH2","CCl","Cl");
        processGroup("C2H5","CCO","O");
        processGroup("C6H5","c1ccc(cc1)O","O");
        processGroup("COOH","O=CO",null);
        processGroup("NH2","NO","O");
        processGroup("NH4","[Cl-].[NH4+]","Cl");

    }
}
