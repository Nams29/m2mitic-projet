package fr.istic.project.utils;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class FileUtils {
	
	public static final String[] allowedDeviceDirectories = { "DCIM" }; // TODO bouger dans constantes ou strings.xml
	public static final String[] allowedCardDirectories = { "!aaProjet" };
	
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
		String extension = filename.substring(dot+1, filename.length()).toUpperCase(LocaleUtils.getLocaleFR());		
		return extension;
	}
}
