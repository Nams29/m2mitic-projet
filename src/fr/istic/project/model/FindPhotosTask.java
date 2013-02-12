package fr.istic.project.model;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import android.app.ProgressDialog;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.widget.Toast;
import fr.istic.project.controller.MainActivity;
import fr.istic.project.utils.FileUtils;

public class FindPhotosTask extends AsyncTask<File, Integer, Void> {
	
	private MainActivity activity;
	private ProgressDialog progressDialog;
	private List<OPhoto> photos;

	private Geocoder geocoder;
	private static boolean geocoderError = false;

	
	public FindPhotosTask(MainActivity activity) {
		super();
		
		this.activity = activity;
		this.progressDialog = new ProgressDialog(activity); progressDialog.setTitle("Recherche des photos");
		this.photos = new LinkedList<OPhoto>();
		
		this.geocoder = new Geocoder(activity, FileUtils.locale);
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		progressDialog.show();
	}
	
	
	@Override
	protected Void doInBackground(File... params) {
		
		for(File dir : params) { // Pour chaque répertoire à parcourir
			File[] files = dir.listFiles();

	    	
	    	for(File file : files) {
	    		//System.out.println(""+ file.toString());
	    		
	    		if (file.isFile()) {
	    			// Vérification de l'extension du fichier
	    			if (FileUtils.allowedPhotosExtensions.contains(FileUtils.getFileExtension(file.getPath()))) {
	    				OPhoto photo = new OPhoto(file); // Création de la photo
	    				if (photo.processLocation(geocoder) == false) geocoderError = true; // Récupération de la localité avec Geocoder
	    				photos.add(photo);
	    				publishProgress(photos.size());
	    			}
	    		} else { // C'est un répertoire
	        		if (file.isDirectory() && !file.isHidden()) doInBackground(file); // Récursivité !
	    		}
	    	}
		}
    	
		return null;
	}
	
	
	@Override
    protected void onProgressUpdate(Integer... progress) {
		progressDialog.setMessage("Photos trouvées : "+progress[0].toString());		
    }

	
	@Override
    protected void onPostExecute(Void result) {
		progressDialog.dismiss();
		if (geocoderError) {
			Toast.makeText(activity, "Erreur : connexion Internet indisponible, les localisations des photos n'ont pas pu être calculées avec Geocoder.", Toast.LENGTH_LONG).show();
		}
        activity.processPhotos(photos); // Retourne dans l'activity        
    }
}
