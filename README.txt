Welcome to ClassLang - Language Classification for Java
=======================================================

ClassLang is a Program which implements the classification algorithm described in :

Cavnar, W. B. and J. M. Trenkle, ''N-Gram-Based Text Categorization''
In Proceedings of Third Annual Symposium on Document Analysis and
Information Retrieval, Las Vegas, NV, UNLV Publications/Reprographics,
pp. 161-175, 11-13 April 1994.


The algorithm: "is based on calculating and comparing profiles of N-gram frequencies" of the source document and the input text.
 
 
In the directory Language_Corpra you will find the documents for languages the program is able to identify.  It contains text from wikipedia and BBC-news.

The directory Language_Profiles contains xml files with ngram profiles for each language. Each xml file contains the 400 most frequent ngrams of each language. You can of course build larger profiles, but Canvar et al. concider the number 400 to be the optimal size to provide the best results.
===============
To train a new profile:

cd ClassLang
java -jar ProfileLang.jar [File Name][minNgram][maxNgram][Profile size]

The first argument is the document. The optimal size is between 10k and 20k (Canvar et al.).
The 2d and 3Th arguments correspond to the ngrams size (ex. 1 5 for one to five grams). Note that you cannot build ngrams longer than 5 grams.
The 4Th argument is the length of the language profile you want to build (The optimal size is 400)

Example:

java -jar ProfileLang.jar  someTextDoc.txt 1 5 400 

The produced profile will be put in /Language_Profiles
==============

To classify a language text:

java -jar ClassLang.jar [minNgram][maxNgram][Profile size] [text]

Example:

java -jar ClassLang.jar 1 3 300 "Wolfgang Schäuble ist hart zu sich selbst - und zu seinen Mitarbeitern."


1 3 300  are tested to be the optimal arguments providing near 98% right decisions.
Of Course, as in most language classifiers, the longer the input text the better the results. One word input like "in" or "aucain" are likely to return the wrong decision, since this words are found in more than one languages. Other more profiled words like "Weisheit", "soeure", "health" etc are correctly attributed to their respective language.

======================
Jaouad Mousser
Jaouad.Mousser@uni-konstanz.de
