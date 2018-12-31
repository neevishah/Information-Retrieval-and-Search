package index;

import java.util.ArrayList;

import io.DocSource;
import score.TermScoringFun;
import tokenizer.Tokenizer;

/** The primary class that supports indexing and search.
 * 
 * @author ssanner@mie.utoronto.ca
 *
 */

public abstract class Index {
	
	protected Tokenizer _tokenizer;
	protected DocSource _docSource;
	protected TermScoringFun _scoring;
	
	/** An index must have a source of documents, a way to tokenize them, and a way
	 *  to score terms in order to rank search results.  These are all passed in the
	 *  constructor.
	 * 
	 * @param doc_source
	 * @param tokenizer
	 * @param scoring
	 */
	public Index(DocSource doc_source, Tokenizer tokenizer, TermScoringFun scoring) {
		_docSource = doc_source;
		_tokenizer = tokenizer;
		_scoring = scoring;
		_scoring.init(this);
	}
	
	public Tokenizer getTokenizer() {
		return _tokenizer;
	}

	public DocSource getDocSource() {
		return _docSource;
	}
	
	public TermScoringFun getTermScoringFun() {
		return _scoring;
	}
		
	public abstract void buildIndex(); // Index all files in DocSource
	
	public abstract Integer getDocumentFreq(String term); // Return document frequency of the term
	
	public abstract ArrayList<DocScore> getSortedSearchResults(String query); // Return a ranked list of search results for the provided query
}
