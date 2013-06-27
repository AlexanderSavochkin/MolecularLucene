package org.molecularlucene.tokenizer;

import org.apache.lucene.util.AttributeSource;
import org.openscience.cdk.DefaultChemObjectBuilder;

import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.smiles.SmilesParser;

import java.io.IOException;
import java.io.Reader;

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
 * This tokenizer processes smiles
 */
public class SmilesTokenizer extends StructureTokenizer {
    public SmilesTokenizer(Reader input) {
        super(input);
    }

    public SmilesTokenizer(AttributeFactory factory, Reader input) {
        super(factory, input);
    }

    public   SmilesTokenizer(AttributeSource source, Reader input) {
        super(source, input);
    }

    @Override
    protected void setStructure(Reader input) {
        SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());
        StringBuffer smilesBuffer = new StringBuffer();

        try {
            char[] buf = new char[1024];
            int numRead=0;
            while((numRead=input.read(buf)) != -1){
                String readData = String.valueOf(buf, 0, numRead);
                smilesBuffer.append(readData);
            }
        }
        catch (IOException e) {
            throw new RuntimeException("Cant read similes string from Reader:" + e.toString() );
        }

        try {
            IAtomContainer structure = sp.parseSmiles( smilesBuffer.toString() );
            setStructure(structure);
        } catch (InvalidSmilesException e) {
            throw new RuntimeException("Cant parse similes string:" + e.toString() );
        }

    }
}
