package io;

import java.io.File;
import java.util.ArrayList;

public class FileFinder {
	
	/** Returns all non-directory files under a given directory src 
	 *  (and recursively for all of its subdirectories)  
	 * 
	 * @param src source directory
	 * @return ArrayList of Files  
	 */
	public static ArrayList<File> GetAllFiles(String src) {
		
		ArrayList<File> ret_files = new ArrayList<File>();
		File[] files = new File(src).listFiles();

		for (File f : files) {			
			if (f.isDirectory()) {
				ret_files.addAll(GetAllFiles(f.getPath()));
			} else {
				ret_files.add(f);
			}
		}
		
		return ret_files;
	}

}
