MolecularLucene
===============

Lucene is exceptionally good in texts search. Other kinds of query (dates/numbers ranges, geospatial e.t.c) are also supported.
Here is attempt to bring chemcal structures search into Lucene world. 

This project introduces special kind of lucene analyzer for searching/indexing chemical structures.

In order to be indexed and/or searched
by MolecularLucene chemical structures should be provided as text representation ([SMILES](http://en.wikipedia.org/wiki/Simplified_molecular-input_line-entry_system) is the only supported format now, 
but I am going to add [InChi](http://en.wikipedia.org/wiki/International_Chemical_Identifier) ).

This allows to create full text search and similar chemical structures search in one common "canvas".  

For example lucene index contains documents having fields "description" and "smiles" 
Field "description" is free-text description of chemical compound and "smiles" contais
chemical structure information. A query to index looks like this:

`description:"amino acid" AND smiles:c1ccc2c\(c1\)cc\[nH\]2`

Note that characters (,),[ and ] are escaped becase they have special meaning in Lucene query syntax.

Literally this means: Show me compounds having phrase "amino acid" in description and chemical structure similar to [indole](http://en.wikipedia.org/wiki/Indole) 
(smiles:c1ccc2c(c1)cc[nH]2).

See [autotsests source code](https://github.com/AlexanderSavochkin/MolecularLucene/blob/master/lucenechemistry/src/test/java/org/molecularlucene/ChemicalStructureSearchTest.java) for basic example of usage.
