package test_tokenizer;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import tokenizer.Tokenizer;
import tokenizer.SimpleTokenizer;

public class SimpleTokenizerTest {

	@Test
	public void testTokenize() {
		Tokenizer tok = new SimpleTokenizer();
		ArrayList<String> tokens = tok.tokenize("A state-of-the-art product.");
		assertEquals("Failed simple token", "product", tokens.get(5)); //there should be 6 tokens -- the last token should be index 5
		assertEquals("hyphen not preserved test", "state", tokens.get(1)); //in SimpleTokenizer, there is no code to preserve hyphens. tokens.get(1) should equal state, not state-of-the-art
	}
}
