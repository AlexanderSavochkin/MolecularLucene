package org.molecularlucene.tokenizer;

import java.util.HashMap;
import java.util.Map;

/**
 * Copyright 2015 Alexander Savochkin
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

class ElementaryGroupChains {
    private final static Map<String, Map<String, Integer>> groupToChainMap = new HashMap<String, Map<String, Integer>>() {{
        put("OH", new HashMap<String, Integer>() {{
            put("O", 1);
        }});
        put("CO3", new HashMap<String, Integer>() {{
            put("O=C-O", 2);
            put("C", 1);
            put("O-C", 2);
            put("C-O", 2);
            put("C=O", 1);
            put("O=C", 1);
            put("O-C-O", 2);
            put("O-C=O", 2);
            put("O", 3);
        }});
        put("NO2", new HashMap<String, Integer>() {{
            put("N=O", 1);
            put("N-O", 1);
            put("O=N", 1);
            put("O-N", 1);
            put("O=N-O", 1);
            put("O-N=O", 1);
            put("N", 1);
            put("O", 2);
        }});
        put("NO3", new HashMap<String, Integer>() {{
            put("O-N", 2);
            put("N-O", 2);
            put("N=O", 1);
            put("O=N", 1);
            put("O=N-O", 2);
            put("O-N-O", 2);
            put("O-N=O", 2);
            put("N", 1);
            put("O", 3);
        }});
        put("SiO3", new HashMap<String, Integer>() {{
            put("O-Si", 2);
            put("O=Si", 1);
            put("O-Si-O", 2);
            put("O-Si=O", 2);
            put("Si", 1);
            put("O=Si-O", 2);
            put("O", 3);
            put("Si-O", 2);
            put("Si=O", 1);
        }});
        put("PO3", new HashMap<String, Integer>() {{
            put("P", 1);
            put("O=P-O", 3);
            put("O-P", 3);
            put("P-O", 3);
            put("P=O", 1);
            put("O=P", 1);
            put("O-P=O", 3);
            put("O-P-O", 6);
            put("O", 4);
        }});
        put("PO4", new HashMap<String, Integer>() {{
            put("P", 1);
            put("O=P-O", 3);
            put("O-P", 3);
            put("P-O", 3);
            put("P=O", 1);
            put("O=P", 1);
            put("O-P-O", 6);
            put("O-P=O", 3);
            put("O", 4);
        }});
        put("SO3", new HashMap<String, Integer>() {{
            put("O=S-O", 2);
            put("S", 1);
            put("O-S", 2);
            put("S-O", 2);
            put("S=O", 1);
            put("O=S", 1);
            put("O-S=O", 2);
            put("O-S-O", 2);
            put("O", 3);
        }});
        put("SO4", new HashMap<String, Integer>() {{
            put("O=S-O", 4);
            put("O=S=O", 2);
            put("S", 1);
            put("O-S", 2);
            put("S-O", 2);
            put("S=O", 2);
            put("O=S", 2);
            put("O-S=O", 4);
            put("O-S-O", 2);
            put("O", 4);
        }});
        put("ClO", new HashMap<String, Integer>() {{
            put("O-Cl", 1);
            put("Cl", 1);
            put("Cl-O", 1);
            put("O", 1);
        }});
        put("ClO2", new HashMap<String, Integer>() {{
            put("O-Cl=O", 1);
            put("O-Cl", 1);
            put("O=Cl", 1);
            put("O=Cl-O", 1);
            put("Cl", 1);
            put("Cl-O", 1);
            put("Cl=O", 1);
            put("O", 2);
        }});
        put("ClO3", new HashMap<String, Integer>() {{
            put("O-Cl=O", 2);
            put("O-Cl", 1);
            put("O=Cl", 2);
            put("O=Cl-O", 2);
            put("O=Cl=O", 2);
            put("Cl", 1);
            put("Cl-O", 1);
            put("Cl=O", 2);
            put("O", 3);
        }});
        put("ClO4", new HashMap<String, Integer>() {{
            put("O-Cl=O", 3);
            put("O-Cl", 1);
            put("O=Cl", 3);
            put("O=Cl-O", 3);
            put("O=Cl=O", 6);
            put("Cl", 1);
            put("Cl-O", 1);
            put("Cl=O", 3);
            put("O", 4);
        }});
        put("BrO", new HashMap<String, Integer>() {{
            put("Br", 1);
            put("O-Br", 1);
            put("Br-O", 1);
            put("O", 1);
        }});
        put("BrO2", new HashMap<String, Integer>() {{
            put("Br", 1);
            put("O-Br=O", 1);
            put("O-Br", 1);
            put("O=Br", 1);
            put("O=Br-O", 1);
            put("Br-O", 1);
            put("Br=O", 1);
            put("O", 2);
        }});
        put("BrO3", new HashMap<String, Integer>() {{
            put("Br", 1);
            put("O-Br=O", 2);
            put("O-Br", 1);
            put("O=Br", 2);
            put("O=Br-O", 2);
            put("O=Br=O", 2);
            put("Br-O", 1);
            put("Br=O", 2);
            put("O", 3);
        }});
        put("BrO4", new HashMap<String, Integer>() {{
            put("Br", 1);
            put("O-Br=O", 3);
            put("O-Br", 1);
            put("O=Br", 3);
            put("O=Br-O", 3);
            put("O=Br=O", 6);
            put("Br-O", 1);
            put("Br=O", 3);
            put("O", 4);
        }});
        put("IO", new HashMap<String, Integer>() {{
            put("I", 1);
            put("O-I", 1);
            put("I-O", 1);
            put("O", 1);
        }});
        put("IO2", new HashMap<String, Integer>() {{
            put("O-I=O", 1);
            put("I", 1);
            put("O-I", 1);
            put("I-O", 1);
            put("I=O", 1);
            put("O=I", 1);
            put("O=I-O", 1);
            put("O", 2);
        }});
        put("IO3", new HashMap<String, Integer>() {{
            put("O-I=O", 2);
            put("I", 1);
            put("O-I", 1);
            put("I-O", 1);
            put("I=O", 2);
            put("O=I", 2);
            put("O=I-O", 2);
            put("O=I=O", 2);
            put("O", 3);
        }});
        put("IO4", new HashMap<String, Integer>() {{
            put("O-I=O", 3);
            put("I", 1);
            put("O-I", 1);
            put("I-O", 1);
            put("I=O", 3);
            put("O=I", 3);
            put("O=I-O", 3);
            put("O=I=O", 6);
            put("O", 4);
        }});
        put("IO4", new HashMap<String, Integer>() {{
            put("Mn", 1);
            put("Mn-O", 1);
            put("Mn=O", 3);
            put("O-Mn", 1);
            put("O=Mn", 3);
            put("O-Mn=O", 3);
            put("O=Mn-O", 3);
            put("O=Mn=O", 6);
            put("O", 4);
        }});
        put("CH3", new HashMap<String, Integer>() {{
            put("C", 1);
        }});
        put("CH2", new HashMap<String, Integer>() {{
            put("C", 1);
        }});
        put("C2H5", new HashMap<String, Integer>() {{
            put("C", 2);
            put("C-C", 2);
        }});
        put("C6H5", new HashMap<String, Integer>() {{
            put("C", 6);
            put("C:C:C:C:C", 12);
            put("C:C:C:C", 12);
            put("C:C", 12);
            put("C:C:C", 12);
        }});
        put("COOH", new HashMap<String, Integer>() {{
            put("O=C-O", 1);
            put("C", 1);
            put("O=C", 1);
            put("C=O", 1);
            put("C-O", 1);
            put("O-C", 1);
            put("O-C=O", 1);
            put("O", 2);
        }});
        put("NH2", new HashMap<String, Integer>() {{
            put("N", 1);
        }});
        put("NH4", new HashMap<String, Integer>() {{
            put("N", 1);
        }});
    }};

    String[] get(String group) {
        return new String[]{};
    }
}
