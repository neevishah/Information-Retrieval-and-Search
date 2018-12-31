package io;

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;


public class FileDocSource extends DocSource {

	private ArrayList <File> _files = new ArrayList <File>(); //"FileDocSource should not load all files into memory at the same time; it simply stores an array of all filenames to be loaded"
	
	public FileDocSource (String directory) { //"FileDocSource should load all files from a directory name provided in the constructor as a String"
		for (File f : FileFinder.GetAllFiles(directory)) {
			_files.add(f);
		}
	}

	@Override
	public int getNumDocs() { //filling in from DocSource. gets number of documents.
		return _files.size();
	}
	
	@Override
	public String getDoc (int id) { //filling in from DocSource. gets document name/content (as seen in StaticDocSource) from document id (Array index of filename)
		
		StringBuilder sb = new StringBuilder();
		
		try {
			BufferedReader fin = new BufferedReader (new FileReader (_files.get(id)));
						
			String line = fin.readLine();
		
			while (line != null) { //until end of file
				sb.append(line + " "); //append lines and add space between consecutive lines
				line = fin.readLine();
			}
			
			fin.close(); //close reader
		}
		
		catch (FileNotFoundException e) { //exception. this one is more specific than IOException so we put it first.
			System.out.println("File was not found.");
		}
		
		catch (IOException e) { //exception
			System.out.println("IO Exception.");
		}
		
		return sb.toString(); //doc content
	}
}

