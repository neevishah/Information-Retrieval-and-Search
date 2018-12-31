package test_io;

import static org.junit.Assert.*;

import org.junit.Test;

import io.FileDocSource;

public class FileDocSourceTest {

	@Test
	public void FileDocSource() {
		FileDocSource test = new FileDocSource ("files/Part1/awards_1994"); //actual
		soln.io.FileDocSource expectedF = new soln.io.FileDocSource("files/Part1/awards_1994"); //expected (using solution)
		String expected = expectedF.getDoc(0); //testing using document text of document with index 0
		assertEquals("Content Output Test", expected, test.getDoc(0)); //document text of document(index 0) for solution and my code match!
	}
}

