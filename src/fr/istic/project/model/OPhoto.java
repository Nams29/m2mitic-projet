package fr.istic.project.model;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

import fr.istic.project.utils.FileUtils;

import android.media.ExifInterface;

public class OPhoto { // Photo est déjà utilisé par Android...	
	
	private final SimpleDateFormat SDF = new SimpleDateFormat("yyyy:MM:dd hh:mm:ss");
		
	private File file;
	private String path;
	private String name;
	private ExifInterface exif;
	
	private Long _id;
	private String identifier;
	private String date;
	private String location;
	private Float note;
	private String description;
	private OContext context;
	
	
	/**
	 * Constructeur côté objet
	 * @param file
	 */
	public OPhoto(File file) {
		try {
			this.file = file;
			this.path = file.getPath();
			this.name = FileUtils.getFileName(this.path);	
			this.exif = new ExifInterface(path);
			
			String exifDateTime = exif.getAttribute(ExifInterface.TAG_DATETIME);
			String fileDateTime = SDF.format(file.lastModified());
			//System.out.println((exifDateTime != null ? "exif" : "file"));
			if (exifDateTime == null) exifDateTime = fileDateTime;
			this.date = exifDateTime;
			
			this.location = "";
			this.note = 0.0f;
			this.description = "";
			
			this.identifier = this.date + "--" + fileDateTime + "--" + this.name; // TODO confirmer datecreation + datemodif + nomfichier
			//System.out.println(identifier);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * Constructeur côté SQLite
	 * @param _id
	 */
	public OPhoto(Long _id) {
		this._id = _id;
	}
	
	
	/* GETTERS */
	
	public File getFile() {
		return file;
	}

	public String getPath() {
		return path;
	}

	
	public String getDate() {
		return date;
	}


	public String getLocation() {
		return location;
	}


	public Float getNote() {
		return note;
	}

	public long get_id() {
		return _id;
	}
	
	public String getIdentifier() {
		return identifier;
	}

	public String getDescription() {
		return description;
	}

	public OContext getContext() {
		return context;
	}


	/* SETTERS */
	
	public void setDate(String date) {
		this.date = date;
	}

	public void setNote(Float note) {
		this.note = note;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
	public void setPath(String path) {
		try {
			this.path = path;
			this.path = path;
			this.name = FileUtils.getFileName(this.path);
			this.file = new File(this.path);
			this.exif = new ExifInterface(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setName(String name) {
		this.name = name;
	}


	public void setExif(ExifInterface exif) {
		this.exif = exif;
	}


	public void set_id(long _id) {
		this._id = _id;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public void setContext(OContext context) {
		this.context = context;
	}
	
}
