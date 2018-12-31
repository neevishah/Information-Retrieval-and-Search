package test_index;

import static org.junit.Assert.*;

import org.junit.Test;

import index.SortedDocScore;

public class SortedDocScoreTest {

	@Test
	public void SortedDocScore() {
	
	//initialize some different variations of SortedDocScore(score, id, content)
	SortedDocScore sdsE = new SortedDocScore (3, 5, "Yay"); //expected (the instance)
	
	//actuals 
	SortedDocScore sdsA2 = new SortedDocScore (4, 5, "Yay");
	SortedDocScore sdsA4 = new SortedDocScore (2, 5, "yay");
	
	assertEquals("Sorting test", sdsE.compareTo(sdsA2), 1);	//expected has a lower score so it'll go after --> 1
	assertEquals("Sorting test", sdsE.compareTo(sdsA4), -1); //expected has a higher score so it'll go before --> -1
	}
	
	@Test
	public void SortedDocScoreEquality() {
	
	//initialize some different variations of SortedDocScore(score, id, content)
	SortedDocScore sdsE = new SortedDocScore (3, 5, "Yay"); //expected (the instance)
	
	//actuals
	SortedDocScore sdsA = new SortedDocScore (3, 5, "Yay"); 
	SortedDocScore sdsA3 = new SortedDocScore (3, 5, "yay");
	
	assertEquals("Sorting test", sdsE.compareTo(sdsA), 0); //exactly equal, therefore should be 0
	assertEquals("Sorting test", sdsE.compareTo(sdsA3), -32); //score equal, but content is off by -32 (difference between Y and y in ASCII)
	assertEquals("Sorting test", sdsA3.compareTo(sdsE), 32); //score equal, but content is off by 32 (difference between y and Y in ASCII)	
	}
}

