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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import ClassLang.NgramRanking;
import ClassLang.Ngrams;

public class ProfileLang{
	//Read a file into a byte array
   public static byte[] getBytesFromFile(File file) throws IOException{
	        InputStream is = new FileInputStream(file);
	        // Get the size of the file
	        long length = file.length();
	        if (length > Integer.MAX_VALUE) {
	            System.err.println("Your file is to large!");
	        }
	        // Create the byte array to hold the data
	        byte[] bytes = new byte[(int)length];
	    
	        // Read in the bytes
	        int offset = 0;
	        int numRead = 0;
	        while (offset < bytes.length
	               && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
	            offset += numRead;
	        }
	        // Ensure all the bytes have been read in
	        if (offset < bytes.length) {
	            throw new IOException("Could not completely read file "+file.getName());
	        }   
	        // Close the input stream and return bytes
	        is.close();
	        return bytes;
	    }
   
  
 @SuppressWarnings("unchecked")
public static void writeLanguageModel(String text, String Languagelabel, int NgMin, int NgMax, int profileSize) throws ParserConfigurationException{
	    Ngrams ng = new Ngrams(text);
	 	ArrayList<String> ngList = ng.build_Ngrams(NgMin, NgMax);
	 	NgramRanking ngR = new NgramRanking(ngList);
	 	Map<String, Integer>  ngMap = ngR.getRankedNgMap();
		List entries = new LinkedList(ngMap.entrySet());
		
			
try{
	 	DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
	 	documentBuilderFactory.setIgnoringElementContentWhitespace(false);
	 	DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
	 	Document doc = documentBuilder.newDocument();
	 	Element rootElement = doc.createElement("LangProfile");
	 	rootElement.setAttribute("ID", Languagelabel);
	 	doc.appendChild(rootElement);
	 	Element nGramsEl = doc.createElement("Ngrams");
	 	rootElement.appendChild(nGramsEl);
	 	for(int i=0; i<=profileSize; i++){
	 		    Map.Entry entry = (Map.Entry)entries.get(i);
	 			Element ngram = doc.createElement("Ngram");
	 			ngram.setAttribute("name", (String) entry.getKey());
	 			ngram.setAttribute("ranking", ""+(Integer) entry.getValue()+"");
	 			nGramsEl.appendChild(ngram);
	 		}
	 		
	 	
	 	     File fil = new File(Languagelabel+".xml");  
	         OutputStreamWriter fr = new OutputStreamWriter(new FileOutputStream(fil), "UTF-8");
	         OutputFormat format = new OutputFormat(doc);
	         format.setLineWidth(100);
	         format.setIndenting(false);
	         format.setIndent(4);
	         XMLSerializer serializer = new XMLSerializer(fr, format);
	         serializer.serialize(doc);
	         System.err.print("Your language profile was build successfully in: \n");
	         System.out.println(fil.getAbsolutePath());
	 	}
catch (FactoryConfigurationError e) {
    System.out.println("Could not locate a JAXP factory class");
  }
  catch (ParserConfigurationException e) {
    System.out.println(
     "Could not locate a JAXP DocumentBuilder class"
    );
  }
  catch (DOMException e) {
   System.err.println(e);
  }
  catch (IOException e) {
   System.err.println(e);
  }
 }
	 public static void main(String[] args) throws ParserConfigurationException{
		String fileName = args[0];
		int minGram = Integer.parseInt(args[1]);
		int maxGram =  Integer.parseInt(args[2]);
		int maxProfileSize =  Integer.parseInt(args[3]);
		File file = new File(fileName);
        try {
			String text = new String(getBytesFromFile(file));
			writeLanguageModel(text, "Language_Profiles/"+file.getName().substring(0, file.getName().length()-4),minGram, maxGram, maxProfileSize);
		} catch (IOException e) {
			System.err.println("No such file or directory...");
		}
		
	 }
}