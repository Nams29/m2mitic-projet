package fr.istic.project.model;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.location.Address;
import android.location.Geocoder;
import android.media.ExifInterface;
import fr.istic.project.utils.BitmapUtils;
import fr.istic.project.utils.FileUtils;
import fr.istic.project.utils.FormatUtils;

public class OPhoto { // Photo est déjà utilisé par Android...	
	
	private File file;
	private String path;
	private String name;
		
	private String identifier;
	private Date date;
	private String location;
	private Float note;
	private String description;
	private Boolean available;
	private OContext context;
		
	
	/**
	 * Constructeur côté objet
	 * @param file
	 */
	public OPhoto(File file) {		
		this.file = file;
		this.path = file.getPath();
		this.name = FileUtils.getFileName(path);	
		//System.out.println(name);
		
		this.identifier = name;
		this.date = null;
		this.location = "";
		this.note = -1f;
		this.description = "";
		this.available = true;
		this.context = null;
		
		// Voir aussi : processProperties() appelé de manière asynchrone
	}

	
	/**
	 * Constructeur côté SQLite
	 * @param identifier
	 */
	public OPhoto(String identifier) {
		this.identifier = identifier;
	}
	
	
	/**
	 * Traite les propriétés de la photo 
	 */
	public boolean processProperties(Geocoder geocoder) {
		try {			
			ExifInterface exif = new ExifInterface(path);
			
			/* Date */
			String exifDateTime = exif.getAttribute(ExifInterface.TAG_DATETIME);
			String fileDateTime = FileUtils.DATE_FORMAT_EXIF.format(file.lastModified());
			//System.out.println((exifDateTime != null ? "exif" : "file"));
			if (exifDateTime == null) exifDateTime = fileDateTime;
			this.date = FormatUtils.stringHacToDate(exifDateTime);
						
			/* Note */
			//this.note = -1f;
			
			/* Description */
			//this.description = "";
			
			/* Context */
			//this.context = null;
			
			/* Identifier */
			processIdentifier(exifDateTime);
			//System.out.println(identifier);
			
			/* Location */
			return processLocation(exif, geocoder);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	
	/**
	 * Traite la propriété identifiant
	 */
	private void processIdentifier(String exifDateTime) {
		
		if (exifDateTime != null) { // Si la photo possède une date Exif
			this.identifier = this.name+" "+exifDateTime+" "+file.length();
			
		} else { // Sinon on fait un hash MD5 des pixels de l'image
			System.out.println(this.name + ": MD5");
			
			// Récupération de tous les pixels de l'image
			Bitmap bitmap = BitmapUtils.decodeSampledBitmapFromResource(OPhoto.this.path, 256, 256);
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			bitmap.compress(CompressFormat.JPEG, 0, stream);
			byte[] bitmapByteArray = stream.toByteArray();

			// Calcul du MD5 à partir des pixels
			MessageDigest md5;
			try {
				md5 = java.security.MessageDigest.getInstance("MD5");
				md5.update(bitmapByteArray);
				byte messageDigest[] = md5.digest();

				this.identifier = new BigInteger(1, messageDigest).toString(16);
		        
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			
			// Pour aider le Garbage Collector...
			bitmap.recycle();
		}    

	}
	
	
	/**
	 * Traite la propriété localité avec Geocoder
	 * @param geocoder
	 * @return
	 */
	private boolean processLocation(ExifInterface exif, Geocoder geocoder) {
		try {
			List<Address> addresses;
			
			float[] latlon = new float[2]; exif.getLatLong(latlon);
			if (latlon[0] != 0.0f) {
				addresses = geocoder.getFromLocation(latlon[0], latlon[1], 1);
				if (!addresses.isEmpty()) {
					this.location = addresses.get(0).getLocality() + ", " + addresses.get(0).getCountryName();
					
					System.out.println(name + ": GPS - " + location);
					return true;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println(name + ": GPS - KO");
		return false;
	}
	
	
	/* GETTERS */
	
	public File getFile() {
		return file;
	}
	
	public String getPath() {
		return path;
	}

	public String getName() {
		return name;
	}
	
	public Date getDate() {
		return date;
	}


	public String getLocation() {
		return location;
	}


	public Float getNote() {
		return note;
	}
	
	public String getIdentifier() {
		return identifier;
	}
	
	public String getAvailable() {
		return available.toString();
	}

	public String getDescription() {
		return description;
	}

	public OContext getContext() {
		return context;
	}
	

	/* SETTERS */
	public void setPath(String path) {
		this.path = path;
		this.name = FileUtils.getFileName(this.path);
		this.file = new File(this.path);
	}
	
	
	public void setDate(Date date) {
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
	
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	
	public void setAvailable(String s) {
		this.available = Boolean.valueOf(s);
	}

	public void setContext(OContext context) {
		this.context = context;
		this.context.addPicture(this);
	}
	
}
