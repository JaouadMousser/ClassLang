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
import java.util.StringTokenizer;

public class Ngrams {
private ArrayList<String> ngrams = new ArrayList<String>();
private static String text ="";

//Constructor. Class taking a single word text or a complex line as argument.
public Ngrams(String TEXT){
	   this.text = TEXT;
}

//building ngrams out of single tokens
public void token_Ngrams(String tes, int j){
	String text = "";
//Clean up the tokens. Only letter characters are allowed
	char[] characters = tes.toCharArray();
	for(int i = 0; i<characters.length; i++){
	if(Character.isLetter(characters[i])||Character.toString(characters[i]).equals("'")){
		text += characters[i];
	}
	}
	text = text.toLowerCase();
//Set boudaries to grams to produce ngrams (for the token "text") like: _t te ex xt t_, _te tex ext xt_ etc
	if(j==2&text.length()>1){
		text  = "_"+text+"_";
	}
	else if(j==3&text.length()>=3){
		text  = "_"+text+"__";
	}
	else if(j==4&text.length()>=4){
		text = "_"+text+"___";
	}
	else if(j==5&text.length()>=5){
		text  = "_"+text+"____";
	}
//build ngrams 
	for(int i = 0; i<text.length(); i++, j++){
		String fs = null;
		if(j<=text.length()){
			fs = text.substring(i, j);
			ngrams.add(fs);
		}
}			
		}

public void NgramLength(String text, int min, int max){
	for(int e = min; e<=max; e++){
		token_Ngrams(text, e);
		
	}
}

public ArrayList<String> build_Ngrams(int min, int max){
	StringTokenizer stk = new StringTokenizer(text);
	while(stk.hasMoreTokens()){
		NgramLength(stk.nextToken(), min, max);
	}
	return ngrams;
}

}

