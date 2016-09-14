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

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

@SuppressWarnings("unused")
public class Distance_Measure{
	String str = null;
	 private static Map<String, Integer> NG_MAP = new LinkedHashMap<String, Integer>();
	 private static File LANGUAGE_MODEL = null;
	 private static Document doc = null;
	 private static int modelRanking = 0;//Rank of ngram in the model
	 private static int textRanking = 0;//Rank of ngram in the input model
	 
	 public Distance_Measure(Map<String, Integer> nGMap , File LanguageModelFile){
		Distance_Measure.NG_MAP =  nGMap;
		Distance_Measure.LANGUAGE_MODEL = LanguageModelFile;
		getDoc();
	}	 
	 @SuppressWarnings("unchecked")
	public static int count_distance(int MaxModelSize){ 
		 int distance  =0;
			NodeList nodeL = doc.getElementsByTagName("Ngram");
			Map<String, Integer> modelNgMap = new LinkedHashMap<String, Integer>();
			for(int i =0; i<MaxModelSize; i++){
				Element ngramEl  = (Element) nodeL.item(i);
				if(ngramEl!=null){
					String name = ngramEl.getAttribute("name");
					int rank = Integer.parseInt(ngramEl.getAttribute("ranking"));
					modelNgMap.put(name, rank);
				}
				else{
					System.err.println("No nrgam Elements in your XML model Document.");
				}
			}
			List entries = new LinkedList(NG_MAP.entrySet());
			Map.Entry entry = null;
			for (int l = 0; l<entries.size(); l++) {
			     entry = (Map.Entry)entries.get(l);
			     textRanking = (Integer) entry.getValue();
			     if(modelNgMap.containsKey(entry.getKey())){
			    	 modelRanking = modelNgMap.get(entry.getKey());
			    	 distance += Math.abs(textRanking-modelRanking);//Distance between ngram rank of input and ngram rank of Model
			     }
			      
			     else{
			    	distance += Math.abs(textRanking-(MaxModelSize)); //Maximal value when input Ngram are not found in the model
			     }
			}
			return distance;
		 }
		 
	 
	 public static void getDoc(){
	 	   DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	 	   factory.setNamespaceAware(true);
	 	   DocumentBuilder builder = null;
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			System.out.println("A problem ist occured while parsing!");
			e.printStackTrace();
		}
	 	  try {
			doc = builder.parse(LANGUAGE_MODEL);
			
		} catch (SAXException e) {
			 
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      }
}
