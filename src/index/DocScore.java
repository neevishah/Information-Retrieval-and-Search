package index;

/** Stores a triple of a document score, a doc ID, and the String content.
 *  A class instance represents one search result.
 * 
 * @author ssanner@mie.utoronto.ca
 *
 */
public abstract class DocScore {

	protected double _score;
	protected int    _docID;
	protected String _content;
	
	public DocScore(double score, int doc_id, String content) {
		_score = score;
		_docID = doc_id;
		_content = content;
	}

	public DocScore(DocScore ds) {
		_score = ds._score;
		_content = ds._content;
	}

	public double getScore() {
		return _score;
	}

	public int getDocID() {
		return _docID;
	}
	
	public String getContent() {
		return _content;
	}
	
	@Override
	public String toString() {
		return "Score=" + _score + ": " + _content.substring(0, Math.min(100, _content.length()));
	}
	
}
