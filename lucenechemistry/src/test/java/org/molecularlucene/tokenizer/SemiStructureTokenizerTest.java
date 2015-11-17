package org.molecularlucene.tokenizer;

import org.junit.Test;

import java.io.Reader;
import java.io.StringReader;

import static org.junit.Assert.*;

/**
 Copyright 2015 Alexander Savochkin

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


public class SemiStructureTokenizerTest {

    @Test
    public void testSetStructireInfo() {
        Reader reader = new StringReader("C2H5OH");
        SemiStructureTokenizer tokenizer = new SemiStructureTokenizer(reader);
        assertTrue(tokenizer.fragmentsCounts.containsKey("C"));
        assertTrue(tokenizer.fragmentsCounts.containsKey("C-C"));
        assertTrue(tokenizer.fragmentsCounts.containsKey("O"));
        assertEquals(tokenizer.fragmentsCounts.size(),3);
    }

    @Test
    public void testIncrementToken() throws Exception {

    }
}