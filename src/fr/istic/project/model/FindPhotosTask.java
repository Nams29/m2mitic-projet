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
	private boolean geocoderError;
	
	private ApplicationDB applicationDB;

	
	public FindPhotosTask(MainActivity activity) {
		super();
		
		this.activity = activity;
		this.progressDialog = new ProgressDialog(activity); progressDialog.setTitle("Recherche des photos");
		this.photos = new LinkedList<OPhoto>();
		
		this.geocoder = new Geocoder(activity, FileUtils.locale);
		this.geocoderError = false;
		
		this.applicationDB = activity.getApplicationDB();
		if (applicationDB.getContext(0) == null)
			OContext.defaultContext = applicationDB.addContext(OContext.defaultContext); // Ajout du contexte par défaut
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		progressDialog.show();
	}
	
	
	@Override
	protected Void doInBackground(File... params) {
		
		for(File dir : params) { // Pour chaque répertoire à parcourir
			activity.getConsole().append("\n b"+dir.toString());
			File[] files = dir.listFiles();

			if (files != null) {
				activity.getConsole().append("\n c"+dir.toString());
		    	for(File file : files) {
		    		//System.out.println(""+ file.toString());
		    		
		    		//if (photos.size() > 50) break;
		    		
		    		if (file.isFile()) {
		    			// Vérification de l'extension du fichier
		    			if (FileUtils.allowedPhotosExtensions.contains(FileUtils.getFileExtension(file.getPath()))) {
		    				OPhoto photo = new OPhoto(file); // Création de la photo
		    				photo.setContext(OContext.defaultContext);
		    				if (photo.processLocation(geocoder) == false) geocoderError = true; // Récupération de la localité avec Geocoder
		    				applicationDB.addPhoto(photo);
		    				//if (applicationDB.addPhoto(photo) != -1) { // TODO prendre en compte différement
		    					photo.setIdentifier(photo.getIdentifier());
		    				//}
		    				photos.add(photo);
		    				System.out.println(photos.size());
		    				publishProgress(photos.size());
		    			}
		    		} else { // C'est un répertoire
		        		if (file.isDirectory() && !file.isHidden()) doInBackground(file); // Récursivité !
		    		}
		    	}
			}
		}
    	
		return null;
	}
	
	
	@Override
    protected void onProgressUpdate(Integer... progress) {
		super.onProgressUpdate(progress);
		progressDialog.setMessage("Photos trouvées : "+progress[0].toString());		
    }

	
	@Override
    protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		
		progressDialog.dismiss();
		if (geocoderError) {
			Toast.makeText(activity, "Erreur : connexion Internet indisponible, les localisations des photos n'ont pu être calculées avec Geocoder.", Toast.LENGTH_LONG).show();
		}
        activity.processPhotos(photos); // Retourne dans l'activity        
    }
}
