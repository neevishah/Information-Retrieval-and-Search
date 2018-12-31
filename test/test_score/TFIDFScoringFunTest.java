package test_score;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import score.TFIDFScoringFun;
import index.*;
import io.DocSource;
import tokenizer.Tokenizer;

public class TFIDFScoringFunTest {

	@Test
	public void TFIDFScoring() {
		
		//expected index and score (using solution)
		Index e = new soln.index.InvertedIndex(new soln.io.FileDocSource("files/Part1/awards_1994"),new soln.tokenizer.IndexingTokenizer(), new soln.score.TFIDFScoringFun());
		e.buildIndex();
		ArrayList<DocScore> doc_scores = e.getSortedSearchResults("computer");
		DocScore expected = doc_scores.get(0);
		
		//actual index and score
		Index a = new index.InvertedIndex(new io.FileDocSource("files/Part1/awards_1994"),new tokenizer.IndexingTokenizer(), new score.TFIDFScoringFun());
		a.buildIndex();
		ArrayList<DocScore> doc_scoresA = a.getSortedSearchResults("computer");
		DocScore actual = doc_scoresA.get(0);
		
		assertEquals("Scoring test", expected, actual); //scores of both should be equal for the same query and directory
		
		}
}

