package tokenizer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;


public class IndexingTokenizer implements Tokenizer {
	
	public IndexingTokenizer () {
	}

	@Override
	public ArrayList<String> tokenize(String s) {

		//IndexingTokenizer is meant to modify SimpleTokenizer by lowercasing all tokens and preserving hyphenated words
		
		ArrayList <String> ret = new ArrayList <String>();
		
		Pattern p = Pattern.compile("(\\w[\\w-]*)"); //preserves hyphens (* implies 0 or more) //given in lab
		Matcher m = p.matcher(s);
		
		while (m.find()) {
			ret.add(m.group().toLowerCase()); //lowercase token after found
		}
		
		return ret;	
	}
}