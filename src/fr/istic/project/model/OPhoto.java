package fr.istic.project.model;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Picture;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.media.ExifInterface;
import android.provider.MediaStore.Files;
import fr.istic.project.utils.BitmapUtils;
import fr.istic.project.utils.FileUtils;

public class OPhoto { // Photo est déjà utilisé par Android...	
	
	private final SimpleDateFormat SDF = new SimpleDateFormat("yyyy:MM:dd hh:mm:ss");
		
	private File file;
	private String path;
	private String name;
	private ExifInterface exif;
	
	private String _id;
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
			System.out.println(name);
			this.exif = new ExifInterface(path);
			
			String exifDateTime = exif.getAttribute(ExifInterface.TAG_DATETIME);
			String fileDateTime = SDF.format(file.lastModified());
			//System.out.println((exifDateTime != null ? "exif" : "file"));
			if (exifDateTime == null) exifDateTime = fileDateTime;
			this.date = exifDateTime;
			
			this.location = ""; // Voir aussi processLocation()
			this.note = -1f;
			this.description = "";
			
			this.context = null;
			
			this.identifier = this.name;
			process_id();
			//System.out.println(identifier);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * Constructeur côté SQLite
	 * @param _id
	 */
	public OPhoto(String _id) {
		this._id = _id;
	}
	
	
	/**
	 * Récupération de la localité avec Geocoder
	 * @param geocoder
	 * @return
	 */
	public boolean processLocation(Geocoder geocoder) {
		try {
			List<Address> addresses;
			
			float[] latlon = new float[2]; exif.getLatLong(latlon);
			if (latlon[0] != 0.0f) {
				addresses = geocoder.getFromLocation(latlon[0], latlon[1], 1);
				this.location = addresses.get(0).getLocality() + ", " + addresses.get(0).getCountryName();
				System.out.println(this.path + " location:"+this.location);
			}			
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	public void process_id() {
		// TODO check que ça change pas avec la density
		// http://stackoverflow.com/questions/7929280/android-bitmap-getpixel-depends-on-density
		Bitmap bitmap = BitmapUtils.decodeSampledBitmapFromResource(this.path, 512, 512);
//		Bitmap bitmap = BitmapFactory.decodeFile(this.path); // Pleine taille, plante avec les grosses photos
		
		// UTILISATION DE TOUS LES PIXELS DE L'IMAGE
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.JPEG, 0, stream); // TODO tester en lossless (PNG, 0)
		byte[] bitmapByteArray = stream.toByteArray();


		// MD5
		MessageDigest md5;
		try {
			md5 = java.security.MessageDigest.getInstance("MD5");
			md5.update(bitmapByteArray);
			byte messageDigest[] = md5.digest();
			
//			// TODO a tester pour performances
//			// Create Hex String
//	        StringBuffer hexString = new StringBuffer();
//	        for (int i = 0; i < messageDigest.length; i++) {
//	            String h = Integer.toHexString(0xFF & messageDigest[i]);
//	            while (h.length() < 2)
//	                h = "0" + h;
//	            hexString.append(h);
//	        }
//	        this.identifier = hexString.toString();
			this.identifier = new BigInteger(1, messageDigest).toString(16);
	        
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		// Pour aider le Garbage Collector...
		bitmap.recycle();
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
	
	public String getDate() {
		return date;
	}


	public String getLocation() {
		return location;
	}


	public Float getNote() {
		return note;
	}

	public String get_id() {
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


	public void set_id(String _id) {
		this._id = _id;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public void setContext(OContext context) {
		this.context = context;
		this.context.addPicture(this);
	}
	
}
