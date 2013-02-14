package fr.istic.project.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

import android.os.Environment;

public class FileUtils {
	
	public static final SimpleDateFormat DATE_FORMAT_EXIF = new SimpleDateFormat("yyyy:MM:dd hh:mm:ss");
	
	public static final String APPLICATION_DIRECTORY_DEVICE = "!aaPROJET";
	
	public static final String[] ALLOWED_EXTERNAL_DIRECTORIES_DEVICE = { "DCIM" };
	public static final String[] ALLOWED_EXTERNAL_DIRECTORIES_REMOVABLE = { "/" };
	
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
	
	
	public static File getApplicationDirectory() {
		if (getExternalDirectoryDevice() != null) {
			return new File(getExternalDirectoryDevice().getPath() + File.separatorChar + APPLICATION_DIRECTORY_DEVICE);			
		} else {
			return null;
		}		
	}
	
	public static File getExternalDirectoryDevice() {
		if (!Environment.isExternalStorageRemovable()) {
	    	return Environment.getExternalStorageDirectory();
	    } else {
	    	return null;
	    }
	}
	
	public static File getExternalDirectoryRemovable() {
		if (Environment.isExternalStorageRemovable()) {
	    	return Environment.getExternalStorageDirectory(); // Autre appareil avec mémoire amovible
	    } else {
	    	return new File("/Removable/MicroSD"); // ASUS Transformer
	    }
	}
	
	
	/**
	 * Définition des répertoires à parcourir
	 * @param useApplicationDirectory
	 * @param useExternalDirectoriesDevice
	 * @param useExternalDirectoriesRemovable
	 * @return
	 */
	public static File[] getAllowedDirectories(boolean useApplicationDirectory, boolean useExternalDirectoriesDevice, boolean useExternalDirectoriesRemovable) {
		List<File> allowedDirectories = new LinkedList<File>();		
				
		// Dossier de l'application
		if (useApplicationDirectory) {
			allowedDirectories.add(getApplicationDirectory());
		}
		
		// Dossiers de la mémoire externe, interne à la tablette (ceci n'est pas une blague)
		if (useExternalDirectoriesDevice) {
			for (String s : ALLOWED_EXTERNAL_DIRECTORIES_DEVICE) {
				if (s == "/") {
					allowedDirectories.add(getExternalDirectoryDevice());
				} else {
					allowedDirectories.add(new File(getExternalDirectoryDevice().getPath() + File.separatorChar + s));
				}
			}
		}
		
		// Dossiers de la mémoire externe, amovible
		if (useExternalDirectoriesRemovable) {
			for (String s : ALLOWED_EXTERNAL_DIRECTORIES_REMOVABLE) {
				if (s == "/") {
					allowedDirectories.add(getExternalDirectoryRemovable());
				} else {
					allowedDirectories.add(new File(getExternalDirectoryRemovable().getPath() + File.separatorChar + s));
				}
			}
		}		
		
		return (File[]) allowedDirectories.toArray(new File[allowedDirectories.size()]);
	}
}
