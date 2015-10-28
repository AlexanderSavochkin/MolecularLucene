package org.molecularlucene.tokenizer;

import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;

import static org.junit.Assert.*;

public class SemistructuredFormulaParserTest {

    @Test
    public void testParse() throws Exception {
        SemistructuredFormulaParser formulaParser = new SemistructuredFormulaParser(new StringReader("Al2(SO4)3"));
        ChemSubStructure subStructure = formulaParser.parse();
        Assert.assertTrue(subStructure instanceof Formula);
        Assert.assertTrue(((Formula) subStructure).subStructures.size() == 2);
        Assert.assertTrue(((Formula) subStructure).subStructures.get(0) instanceof Term);
        Assert.assertTrue(((Formula) subStructure).subStructures.get(1) instanceof Term);

        Term term0 = (Term)((Formula) subStructure).subStructures.get(0);
        Term term1 = (Term)((Formula) subStructure).subStructures.get(1);

        Assert.assertEquals(term0.multiplier, 2);
        Assert.assertEquals(term1.multiplier, 3);

        Assert.assertTrue(term0.subStrucure instanceof Element);
        Element term0Element = (Element)term0.subStrucure;
        Assert.assertEquals( term0Element.subFormula, "Al");

        Assert.assertTrue(term1.subStrucure instanceof Group);
        Group term1Group = (Group)term1.subStrucure;

        Assert.assertTrue(term1Group.containedSubStructure instanceof Formula);
        Formula term1GroupFormula = (Formula)term1Group.containedSubStructure;
        Assert.assertEquals(term1GroupFormula.subStructures.size(),1);
        //Assert.assertEquals(term1ElementaryGroup.subFormula,"SO4");

        Term term1GroupFormulaTerm = (Term)term1GroupFormula.subStructures.get(0);
        Assert.assertEquals(term1GroupFormulaTerm.multiplier,1);
        Assert.assertTrue(term1GroupFormulaTerm.subStrucure instanceof ElementaryGroup);
        Assert.assertEquals(((ElementaryGroup)term1GroupFormulaTerm.subStrucure).subFormula,"SO4");

        System.out.println("Ok");
    }
}