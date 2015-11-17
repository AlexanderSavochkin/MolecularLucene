package org.molecularlucene.tokenizer;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

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

public class SemiStructureTokenizer extends Tokenizer {

    Map<String, Integer> fragmentsCounts = new HashMap<String, Integer>();
    Iterator<Map.Entry<String, Integer>> currentIterator = null;
    Map.Entry<String, Integer> currentEntry = null;
    int currentCount = 0;
    private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);

    private static class DFSStackEntry {
        DFSStackEntry(ChemSubStructure subStructure, int multiplier) {
            this.subStructure = subStructure;
            this.multiplier = multiplier;
        }
        public ChemSubStructure subStructure;
        public int multiplier;
    }

    private void setStructireInfo(Reader input) {
        SemistructuredFormulaParser formulaParser = new SemistructuredFormulaParser(input);
        try {
            ChemSubStructure structure = formulaParser.parse();

            //DFS traverse
            Stack<DFSStackEntry> stack = new Stack<DFSStackEntry>();
            stack.push(new DFSStackEntry(structure,1));
            while (!stack.isEmpty()) {
                DFSStackEntry currentEntry = stack.pop();
                ChemSubStructure current = currentEntry.subStructure;
                int multiplier = currentEntry.multiplier;

                if (current instanceof Element) {
                    mapFragmentsChains(((Element) current).subFormula, multiplier);
                }
                else if (current instanceof ElementaryGroup) {
                    mapFragmentsChains(((ElementaryGroup) current).subFormula, multiplier);                }
                else if (current instanceof Group) {
                    ChemSubStructure containedSubStructure = ((Group)current).containedSubStructure;
                    stack.push(new DFSStackEntry(containedSubStructure, multiplier));
                }
                else if (current instanceof Term) {
                    Term asTerm = ((Term)current);
                    stack.push(new DFSStackEntry(asTerm.subStrucure, multiplier * asTerm.multiplier));
                }
                else if (current instanceof Formula) {
                    for (ChemSubStructure subStructure : ((Formula) current).subStructures) {
                        stack.push(new DFSStackEntry(subStructure, multiplier) );
                    }
                }
            }

            currentIterator = fragmentsCounts.entrySet().iterator();
            currentCount = 0;

        } catch (ParseException e) {
            //e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void mapFragmentsChains(String subFormula, int multiplier) {
        Map<String, Integer> currentFragmentsCount = ElementaryGroupChains.groupToChainMap.get(subFormula);
        if (currentFragmentsCount != null) {
            for (String fragment : currentFragmentsCount.keySet()) {
                int fragmentCount = currentFragmentsCount.get(fragment);
                int count = fragmentsCounts.containsKey(subFormula) ? fragmentsCounts.get(subFormula) : 0;
                fragmentsCounts.put(fragment, count + fragmentCount * multiplier);
            }
        }
    }

    protected SemiStructureTokenizer(Reader input) {
        super(input);
        setStructireInfo(input);
    }

    protected SemiStructureTokenizer(AttributeFactory factory, Reader input) {
        super(factory, input);
        setStructireInfo(input);
    }

    @Override
    final public boolean incrementToken() throws IOException {
        if (currentEntry == null) {
            currentEntry = currentIterator.next();
            currentCount = 0;
            String result = String.format("%s_%d", currentEntry.getKey(), currentCount);
            termAtt.setEmpty().append(result);
            return true;
        }
        else {
            if (currentCount < currentEntry.getValue()) {
                String result = String.format("%s_%d", currentEntry.getKey(), ++currentCount);
                termAtt.setEmpty().append(result);
                return true;
            }
            else if (currentIterator.hasNext()) {
                Map.Entry<String, Integer> currentEntry = currentIterator.next();
                currentCount = 0;
                String result = String.format("%s_%d", currentEntry.getKey(), currentCount);
                termAtt.setEmpty().append(result);
                return true;
            }
            else {
                return false;
            }

        }
    }
}
