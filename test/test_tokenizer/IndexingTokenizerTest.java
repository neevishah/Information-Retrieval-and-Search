package test_tokenizer;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import tokenizer.Tokenizer;
import tokenizer.IndexingTokenizer;

public class IndexingTokenizerTest {

	@Test
	public void testIndexingTokenizer() {
		Tokenizer tok = new IndexingTokenizer();
		ArrayList<String> tokens = tok.tokenize("A state-of-the-art product.");
		assertEquals("Failed lowercase", "a", tokens.get(0)); //check for lowercase -- I coded IndexingTokenizer so that each character would be lowercase
		assertEquals("Failed hyphen test", "state-of-the-art", tokens.get(1)); //check for hyphens -- I coded IndexingTokenizer so that hyphens would be preserved and 'state-of-the-art' would be considered 1 token instead of 4
		assertEquals("Failed simple token", "product", tokens.get(2)); //there should be 3 tokens -- product should be index 2
	}
}