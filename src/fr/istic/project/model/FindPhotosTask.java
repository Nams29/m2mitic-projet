package fr.istic.project.model;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.widget.Toast;
import fr.istic.project.controller.MainActivity;
import fr.istic.project.utils.FileUtils;
import fr.istic.project.utils.LocaleUtils;

public class FindPhotosTask extends AsyncTask<File, Integer, Void> {
	
	private MainActivity activity;
	private ProgressDialog progressDialog;
	private List<OPhoto> photos;

	private Geocoder geocoder;
	private boolean geocoderError;
	
	private ApplicationDB applicationDB;
	
	private long start;
	private boolean cancelTask;

	
	public FindPhotosTask(MainActivity activity) {
		super();
		
		this.activity = activity;
		this.progressDialog = new ProgressDialog(activity); 
				progressDialog.setTitle("Recherche des photos");
				progressDialog.setCancelable(true);
				progressDialog.setCanceledOnTouchOutside(true);
			    progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
				    @Override
				    public void onCancel(DialogInterface dialog) {
				    	cancelTask = true;
				    }
				});
		this.cancelTask = false;
		this.photos = new LinkedList<OPhoto>();
		
		this.geocoder = new Geocoder(activity, LocaleUtils.LOCALE_FR);
		this.geocoderError = false;
		
		this.applicationDB = activity.getApplicationDB();
		if (applicationDB.getContext(0) == null)
			OContext.defaultContext = applicationDB.addContext(OContext.defaultContext); // Ajout du contexte par défaut
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		start = System.currentTimeMillis();
		progressDialog.show();
	}
	
	
	@Override
	protected Void doInBackground(File... params) {
		
		for(File dir : params) { // Pour chaque dossier à parcourir
			//activity.getConsole().append("\n b"+dir.toString());
			File[] files = dir.listFiles();

			if (files != null) {
				//activity.getConsole().append("\n c"+dir.toString());
		    	for(File file : files) { // Pour chaque fichier du dossier
		    		//System.out.println(""+ file.toString());
		    		
		    		if (photos.size() >= 500) break;
		    		if (cancelTask) break;
		    		
		    		if (file.isFile()) {
		    			// Vérification de l'extension du fichier
		    			if (FileUtils.ALLOWED_FILE_EXTENSIONS.contains(FileUtils.getFileExtension(file.getPath()))) {
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
		long duration = System.currentTimeMillis() - start;
		
		
		Toast.makeText(activity, "duree : "+ (duration/1000), Toast.LENGTH_LONG).show();
		activity.processPhotos(photos); // Retourne dans l'activity        
    }
}
