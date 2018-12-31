package test;
import io.FileDocSource;
// import soln.io.FileDocSource;
//given in lab

import io.FileFinder;

public class TextFileDocSource {
	
	public static void main(String args[]) {
		FileDocSource fds = new FileDocSource("files");
		System.out.println(FileFinder.GetAllFiles("files"));
		System.out.println("FileDocSource found " + fds.getNumDocs() + " files");
		System.out.println("Example document ");
		System.out.println(fds.getDoc(0));
	}
}