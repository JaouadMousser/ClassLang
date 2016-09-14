// This file is part of the Language Classifier Java library.
// Copyright (C) 2010 Jaouad Mousser
//					  Jaouad.Mousser@uni-konstanz.de
//                    Univeristy of Konstanz
//                    Departement of Linguistics
//
// ClassLang is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public License
// as published by the Free Software Foundation; either version 2
// of the License, or (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.

package ClassLang;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
public class NgramRanking{
	private  ArrayList<String> listOfNgrams = new ArrayList<String>();
	private  Set<String> NgramSet = new HashSet<String>();//ngrams one time
	private   Map<String, Integer> unsortedNgs = new LinkedHashMap<String, Integer>();
	private static List<ArrayList<String>> rankedNgramsLists = new ArrayList<ArrayList<String>>();//List of Ngram Lists
	private Map<String, Integer> sortedRankedNgMap = new LinkedHashMap<String, Integer>();
	

	public NgramRanking(ArrayList<String> NgramsUnsorted){
		this.listOfNgrams = NgramsUnsorted;
		 NgramSet.addAll(listOfNgrams);
		 for (Iterator<String> it = NgramSet.iterator(); it.hasNext();){
			String s = it.next();
			unsortedNgs.put(s, Collections.frequency(listOfNgrams, s));
		}
	} 

//Return a map with ngrams and their frequency sorted by values (frequency)
@SuppressWarnings("unchecked")
public Map<String, Integer> getfreqHash(){
	return (Map<String, Integer>) sortByValueAsc(unsortedNgs);
}

//Return a list of ranked ngram lists (For case where more ngrams has the same frequence).
public List<ArrayList<String>> getRankedNgramsLists(){
	getfreqHash();
	return buildRankingLists(unsortedNgs);
}
    
public Map<String, Integer> getRankedNgMap(){//Return a map with ng as key and their ranking as value
	getRankedNgramsLists();
	for(int i = 0;i<rankedNgramsLists.size(); i++){
 		List<String> listN = rankedNgramsLists.get(i);
 		for(int e =0; e<listN.size(); e++){
 			String nG = listN.get(e);
 			int rank =i+1;
 			sortedRankedNgMap.put(nG, rank);
 		
 		}
 	}
	return sortedRankedNgMap;
}

//outputs the elements of the map sorted by value ascending
@SuppressWarnings("unchecked")
public Map sortByValueAsc(Map map) {
    List list = new LinkedList(map.entrySet());
    Collections.sort(list, new Comparator() {
         public int compare(Object o2, Object o1) {
              return ((Comparable) ((Map.Entry) (o1)).getValue())
             .compareTo(((Map.Entry) (o2)).getValue());
         }
    });
//logger.info(list);
Map result = new LinkedHashMap();
for (Iterator it = list.iterator(); it.hasNext();) {
    Map.Entry entry = (Map.Entry)it.next();
    result.put(entry.getKey(), entry.getValue());
    }
return result;
}

//outputs the elements of the map sorted by value descending
@SuppressWarnings("unchecked")
public Map sortByValueDesc(Map map) {
    List list = new LinkedList(map.entrySet());
    Collections.sort(list, new Comparator() {
         public int compare(Object o1, Object o2) {
              return ((Comparable) ((Map.Entry) (o1)).getValue())
             .compareTo(((Map.Entry) (o2)).getValue());
         }
    });
//logger.info(list);
Map result = new LinkedHashMap();
for (Iterator it = list.iterator(); it.hasNext();) {
    Map.Entry entry = (Map.Entry)it.next();
    result.put(entry.getKey(), entry.getValue());
    }
return result;
}
//Returns a List of lists. Each of the contained list populates ngrams with the same ranking
@SuppressWarnings("unchecked")
public static List<ArrayList<String>> buildRankingLists(Map map){	
	 int value1=0;
	 int value2 =0;
	//List of ranked ngram lists
	//Entries of ngram map (ngram; frequencyRANKED);
	List entries = new LinkedList(map.entrySet());
	//Values int (unduplicated).
	Set<Integer> UnsortedValues = new HashSet<Integer>();
	//Sorted list of in values
	List<Integer> sortedValues = new ArrayList<Integer>(); 
	//Put map values into set
	for (Iterator it = entries.iterator(); it.hasNext();) {
	     Map.Entry entry = (Map.Entry)it.next();
	     UnsortedValues.add((Integer) entry.getValue());
	     }
	//Put set content into list, sort and reverse
     sortedValues.addAll(UnsortedValues);
	 Collections.sort(sortedValues);
	 Collections.reverse(sortedValues );
	 //Put ngrams with the same frequence in one list and than put the list in the big container
     for(int in = 0; in<sortedValues .size(); in++){
    	 List ngsInList = new ArrayList();
    	 value1 = sortedValues.get(in);
    	 for (Iterator it = entries.iterator(); it.hasNext();) {
    		 Map.Entry entry = (Map.Entry)it.next();
    		 value2 = (Integer) entry.getValue();
    		 if(value2==value1){
    			 ngsInList.add(entry.getKey());
    		 }
    	 }
    	 rankedNgramsLists.add((ArrayList<String>) ngsInList);
     } 
     return rankedNgramsLists; 
     }
}

