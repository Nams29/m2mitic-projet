package fr.istic.project.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

public class FileUtils {
	
	public static final SimpleDateFormat DATE_FORMAT_EXIF = new SimpleDateFormat("yyyy:MM:dd hh:mm:ss");
	
	public static final String[] ALLOWED_DIRECTORIES_DEVICE = { "DCIM" };
	public static final String[] ALLOWED_DIRECTORIES_CARD = { "!aaProjet" };
	
	public static final List<String> ALLOWED_FILE_EXTENSIONS;	
	static { // Bloc d'initialisation statique !
		ALLOWED_FILE_EXTENSIONS = new LinkedList<String>();
		ALLOWED_FILE_EXTENSIONS.add("JPG");
		ALLOWED_FILE_EXTENSIONS.add("PNG");
	}
	
	
	public static String getFileName(String filepath) {
		int slash = filepath.lastIndexOf(File.separatorChar);
		String name = filepath.substring(slash+1, filepath.length());
		return name;
	}
	
	public static String getFileExtension(String filename) {
		int dot = filename.lastIndexOf('.');
		String extension = filename.substring(dot+1, filename.length()).toUpperCase(LocaleUtils.LOCALE_FR);		
		return extension;
	}
}
