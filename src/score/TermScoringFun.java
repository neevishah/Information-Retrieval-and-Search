package score;

import index.Index;

/** This provides the score for a single term in a search query w.r.t. a 
 *  corpus of documents indexed in Index s.  Not all scoring functions
 *  will require access to the Index s.  See TFScoringFun for a simple
 *  instantiation.
 * 
 * @author ssanner@mie.utoronto.ca
 *
 */
public interface TermScoringFun {

	public abstract void init(Index s);
	public abstract double scoreToken(String term, int freq);
}
