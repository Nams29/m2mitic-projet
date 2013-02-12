package fr.istic.project.utils;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class FileUtils {
	
	public static final Locale locale = new Locale("fr", "FR");
	public static final String[] allowedSDcardDirectories = { "DCIM" }; // TODO hors sdcard ?
	public static final List<String> allowedPhotosExtensions;	
	static { // Bloc d'initialisation statique !
		allowedPhotosExtensions = new LinkedList<String>();
		allowedPhotosExtensions.add("JPG");
		allowedPhotosExtensions.add("PNG");
	}
	
	
	public static String getFileName(String filepath) {
		int slash = filepath.lastIndexOf(File.separatorChar);
		String name = filepath.substring(slash+1, filepath.length());
		return name;
	}
	
	public static String getFileExtension(String filename) {
		int dot = filename.lastIndexOf('.');
		String extension = filename.substring(dot+1, filename.length()).toUpperCase(locale);		
		return extension;
	}
}
