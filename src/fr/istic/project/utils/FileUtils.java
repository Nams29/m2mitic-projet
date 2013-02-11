package fr.istic.project.utils;

import java.io.File;

public class FileUtils {
	
	public static String getFileName(String filepath) {
		int slash = filepath.lastIndexOf(File.separatorChar);
		String name = filepath.substring(slash+1, filepath.length());
		return name;
	}
	
	public static String getFileExtension(String filename) {
		int dot = filename.lastIndexOf('.');
		String extension = filename.substring(dot+1, filename.length()).toLowerCase();		
		return extension;
	}
}
