package index;

public class SortedDocScore extends DocScore implements Comparable {
	
	public SortedDocScore(double score, int doc_id, String content) {
		super(score, doc_id, content); //super constructor undefined without this
	}

	
	@Override //override compareTo that is defined in Comparable
	public int compareTo (Object o) { //from Oracle docs: "Compares this object with the specified object for order. 
		//Returns a negative integer, zero, or a positive integer if this object is 
		//less than, equal to, or greater than the specified object."
		
		if (o instanceof SortedDocScore) {
			SortedDocScore sds = (SortedDocScore)o; // cast (allowed bc o is of type SortedDocScore)
			
			if (this._score < sds._score) { //if _score of instance is less, it would come after
				return 1;
			}
			
			else if (this._score == sds._score) { //if _score of instance is equal to sds, compare content (string)
				return this.getContent().compareTo(sds.getContent()); //will return value after alphabetical comparison
				//negative value if it comes before, 0 if they are exactly equal (case-sensitive), positive if it comes after
				//compareTo is a common method that can be used with strings (alphabetical comparison)
			}
			
			else if (this._score > sds._score) { //if _score of instance is higher, it would come before in ranking
				return -1;
			}
		}
		
		 //else
		return -1;
	}
}
