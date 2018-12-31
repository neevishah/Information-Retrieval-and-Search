package score;

import index.*; //index is passed in TermScoringFun.java and TF and DF require access to Index
import java.lang.Math; //for log_10

public class TFIDFScoringFun implements TermScoringFun {

	private Index _index;
	private double _nDocs; //will be used as N in the scoring formula (number of documents)
	
	public TFIDFScoringFun() {
	}

	@Override
	public void init (Index s) { //initialization - giving values to private variables above
		//cannot move nDocs to public TFIDFScoringFun() because s is initialized as the index here
	    _index = s;
	    _nDocs = s.getDocSource().getNumDocs();
	}

	@Override
	public double scoreToken (String term, int freq) throws ArithmeticException { //create a score for each term using TF-IDF
	    double score = 0; //score cannot be an integer based on the formula
	   
	    //scoring formula from lecture slides is (1 + log_10 (TF))*log_10(N/DF). TF and DF are integers so they must be casted.
	    score = (1+Math.log10((double)freq))* Math.log10(_nDocs/(double)_index.getDocumentFreq(term));
	    
	    return score;
	}
}