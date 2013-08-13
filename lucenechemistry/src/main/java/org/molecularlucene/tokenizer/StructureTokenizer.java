package org.molecularlucene.tokenizer;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.AttributeSource;

import org.apache.lucene.util.Version;
import org.openscience.cdk.CDKConstants;
import org.openscience.cdk.aromaticity.CDKHueckelAromaticityDetector;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.*;
import org.openscience.cdk.graph.PathTools;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import org.openscience.cdk.tools.periodictable.PeriodicTable;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 Copyright 2013 Alexander Savochkin

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

/**
 * This tokenizer converts chemical structure to lucene tokens. Each tokens corresponds to
 * "short" path through molecular structure. Implementations must provide conversion
 * from structure text representations to CDK's IAtomContainer class.
 */
public abstract class StructureTokenizer extends Tokenizer {

    IAtomContainer chemicalStructure = null;
    Iterator<IAtom> currentAtomIterator = null;
    List<List<IAtom>> pathesFromCurrentAtom = null;
    Iterator<List<IAtom>> currentPathIterator = null;
    Map<IAtom,Map<IAtom, IBond>> atoms2Bond
            = new HashMap<IAtom, Map<IAtom,IBond>>();

    int maxLength = 4;

    private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);

    protected StructureTokenizer(Reader input) {
        super(input);
        setStructure(input);
    }

    protected StructureTokenizer(AttributeFactory factory, Reader input) {
        super(factory, input);
        setStructure(input);
    }

    protected abstract boolean setStructure(Reader input);

    protected void setStructure(IAtomContainer chemicalStructure) {
        //Initialize iterating through all structure's atoms
        this.chemicalStructure = chemicalStructure;

        //Calculate aromaticity
        try {
            AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(chemicalStructure);
            CDKHueckelAromaticityDetector.detectAromaticity(chemicalStructure);
        }
        catch(CDKException e) {
            throw new RuntimeException(e);
        }

        currentAtomIterator = chemicalStructure.atoms().iterator();
        processNextAtom();
    }

    protected boolean processNextAtom() {
        if (!currentAtomIterator.hasNext())
            return false;
        IAtom currentAtom = currentAtomIterator.next();
        pathesFromCurrentAtom = PathTools.getPathsOfLengthUpto(chemicalStructure, currentAtom, maxLength);
        currentPathIterator = pathesFromCurrentAtom.iterator();
        return true;
    }

    @Override
    final public boolean incrementToken() throws IOException {
        clearAttributes();

        //Check if we can go on with iterations
        while (!currentPathIterator.hasNext()) {
            //Go to next atom
            if (!processNextAtom())
                //It is possible that reader is reused, so try to reinitialize structure from reader
                if ( setStructure( input ) )
                    continue;
                else
                    return false;
        }
        List<IAtom> currentPath = currentPathIterator.next();

        //Convert path to token
        StringBuffer sb = new StringBuffer();
        IAtom previousAtom = currentPath.get(0);
        if (previousAtom instanceof  IPseudoAtom)
            sb.append( "[x]" );
        else {
            Integer atomicNumber = PeriodicTable.getAtomicNumber(previousAtom.getSymbol());
            if (atomicNumber != null)
                sb.append( previousAtom.getSymbol() );
            else
                sb.append( "[x]" );
        }
        for (int i = 1; i < currentPath.size(); ++i) {
            IAtom nextAtom = currentPath.get(i);
            Map<IAtom, IBond> neighbor2Bond = atoms2Bond.get( previousAtom );
            IBond bond = (neighbor2Bond != null) ? neighbor2Bond.get( nextAtom ) : null;
            if ( bond == null ) {
                bond = chemicalStructure.getBond(previousAtom, nextAtom);
                Map<IAtom, IBond> atom2BondRcord = new HashMap<IAtom, IBond>();
                atom2BondRcord.put(nextAtom, bond);
                atoms2Bond.put(previousAtom, atom2BondRcord);
            }
            sb.append(getBondString(bond));
            sb.append(nextAtom.getSymbol());
            previousAtom = nextAtom;
        }

        termAtt.setEmpty().append(sb);

        return true;
    }

    protected String getBondString(IBond b)
    {
        String result = "";
        if (b.getFlag(CDKConstants.ISAROMATIC))
        {
            result = ":";
        }
        else if (b.getOrder() == IBond.Order.SINGLE)
        {
            result = "-";
        }
        else if (b.getOrder() == IBond.Order.DOUBLE)
        {
            result = "=";
        }
        else if (b.getOrder() == IBond.Order.TRIPLE)
        {
            result = "#";
        }
        return result;
    }
}
