// This file is part of the Language Classifier Java library.
// Copyright (C) 2010 Jaouad Mousser
//  				  Jaouad.Mousser@uni-konstanz.de
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

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import ClassLang.Ngrams;

public class Classify_Language{

	public static void main(String[] args){
	int  minGram = Integer.parseInt(args[0]);
	int maxGram = Integer.parseInt(args[1]);
	int profileSize = Integer.parseInt(args[2]);
	String text  = args[3];
    Ngrams ng = new Ngrams(text);
	ArrayList<String> ngs = ng.build_Ngrams(minGram, maxGram);
	NgramRanking ngR = new NgramRanking(ngs);
	Map<String, Integer> ngRMap = ngR.getRankedNgMap();
	Distance_Measure dm = null;
	int dist = 0;
	String language = null;

	Map<String, Integer> langDistance = new LinkedHashMap<String, Integer>(); 
	File Lang_Profiles= new File("Language_profiles");
	File[] files = Lang_Profiles.listFiles();
	for (File langP : files) {
		if(langP.getName().endsWith(".xml")){
		language = langP.getName().substring(0, langP.getName().length()-4);
		dm = new Distance_Measure(ngRMap, langP);
		dist = dm.count_distance(profileSize);
		langDistance.put(language , dist);
		}
	}
	Map<String, Integer> lgDistanceSorted = ngR.sortByValueDesc(langDistance);
	Set<String> entries =  lgDistanceSorted.keySet();
	List<String> languages = new ArrayList<String>();
	languages.addAll(entries);
	
	System.out.println("Your text is written in: " + languages.get(0));
	}

	
}