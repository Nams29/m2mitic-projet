package fr.istic.project.model;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import fr.istic.project.controller.MainActivity;
import fr.istic.project.utils.FileUtils;

public class FindPhotosTask extends AsyncTask<File, Integer, Void> {
	
	private MainActivity activity;
	private ProgressDialog progressDialog;
	private List<OPhoto> photos;

	
	public FindPhotosTask(MainActivity activity) {
		super();
		
		this.activity = activity;
		this.progressDialog = new ProgressDialog(activity); progressDialog.setTitle("Recherche des photos");
		this.photos = new LinkedList<OPhoto>();
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
	    				OPhoto photo = new OPhoto(file);
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
        activity.processPhotos(photos); // Retourne dans l'activity        
    }
}
