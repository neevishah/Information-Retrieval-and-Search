package index;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

import io.DocSource;
import score.TermScoringFun;
import tokenizer.Tokenizer;


public class InvertedIndex extends Index {

	private HashMap <String, HashMap <Integer, Integer>> _index; //from handout - stores term, ID, term freq
	private HashMap <String, Integer> _docFreq; //from handout - stores term, doc freq
	
	public InvertedIndex(DocSource doc_source, Tokenizer tokenizer, TermScoringFun scoring) {
		
		super(doc_source, tokenizer, scoring); //handout mentions that InvertedIndex has the same parameters as superclass Index
		_index = new HashMap<String, HashMap <Integer, Integer>>(); 
		_docFreq = new HashMap<String, Integer>(); 
	}

	@Override
    public void buildIndex() {
		
		for (int index = 0; index < _docSource.getNumDocs(); index++) { //go thru all docs
			
			ArrayList<String> tokens = _tokenizer.tokenize(_docSource.getDoc(index)); //tokenize content from current doc
			
			for (String term : tokens ) { //go thru all tokens
				
				//if term is NOT in index already
				if (_index.containsKey(term)==false) {
					_index.put(term, new HashMap <Integer, Integer>()); //set up ID and freq for that term
					_index.get(term).put(index, 1); //set freq to 1 since this is the first time term showed up in the doc
					_docFreq.put(term, 1); //set doc freq to 1 since this is the first time term showed up across all of our docs
				}
				
				//if term is in index, but a new document is found with it
				else if (_index.get(term).containsKey(index)==false) {
					_index.get(term).put(index, 1); //set freq to 1 since this is the first time term showed up in the current doc
					_docFreq.put(term, _docFreq.get(term)+1); //increase doc freq by 1 since this is another time the term has showed up across all of our docs
				}
				
				//if term is in index and in doc, but is found again
				else if (_index.get(term).containsKey(index)==true) {
					_index.get(term).put(index, _index.get(term).get(index)+1); //increase term freq by 1 since this is another time the term has showed up in the current doc
				}
			}
		}
	}
	
	@Override
	public Integer getDocumentFreq (String term) { // Return document frequency of the term
		if (_docFreq.containsKey(term)) {
			return _docFreq.get(term);
		}
		
		else 
			return null;
	}
	
	@Override
    public ArrayList <DocScore> getSortedSearchResults(String query){
	
	//finds scores of each doc ID for the query and sorts search results.
	//as mentioned in handout, assume buildIndex() runs before this
	
	HashMap <Integer, Double> scoreHM = new HashMap <Integer, Double> (); // maintain HashMap from document IDs to a document's current score
	ArrayList <String> tokens = _tokenizer.tokenize(query); //if query is more than one word, it will have multiple tokens
		
	for (int index = 0; index < _docSource.getNumDocs(); index++) { //iterate through documents (index begins at 0, so index is 1 less than total number of docs)
		for (String token : tokens) { //for each tokenized query term
			if (_index.containsKey(token) && _index.get(token).containsKey(index)) {
				Integer tfreq = _index.get(token).get(index);
				Double score = _scoring.scoreToken(token, tfreq);
				
				//first time doc is present in scoring
				if (scoreHM.containsKey(index)==false && _index.get(token).get(index) !=null) {
					if (score !=null) {
						scoreHM.put(index, score);
					}
				}
				
				//doc exists in scoring
				//as each query term is processed, the score for that term in a document can be added to 
				//that document's current score in the HashMap 
				else if ((scoreHM.containsKey(index)==true) && _index.get(token).get(index) != null) {
					scoreHM.put(index, scoreHM.get(index) + score); 
				}		
			}
		}
	}
	//Once all query terms are processed, create SortedDocScore's for all matching documents and their non-zero scores
	
	TreeSet <DocScore> results = new TreeSet <DocScore> (); //without the treeset, the rankings for each query arent sorted properly
	
	for (Integer i : scoreHM.keySet()) { //for each doc id in scoreHM, if score >0, create sorted doc score 
		if(scoreHM.get(i) > -0.01) { //score must be non zero. setting this to -0.01 because sometimes very small scores in small directories become 0.0 and I do not want them to get removed
			results.add(new SortedDocScore(scoreHM.get(i), i, _docSource.getDoc(i))); //add each SortedDocScore to list of results
		}
	}
	
	return new ArrayList <DocScore> (results); //getSortedSearchResults should return a list of SortedDocScore's
	
	}	
}