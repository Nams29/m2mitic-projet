package fr.istic.project.model;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;
import fr.istic.project.utils.FileUtils;

public class FindPhotosTask extends AsyncTask<File, Integer, Long> {
	
	private Activity activity;
	private ProgressDialog progressDialog;
	private List<OPhoto> photos = new LinkedList<OPhoto>();

	
	public FindPhotosTask(Activity activity) {
		super();
		
		this.activity = activity;
		this.progressDialog = new ProgressDialog(activity);
		progressDialog.setTitle("Recherche des photos");		
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		progressDialog.show();
	}
	
	
	@Override
	protected Long doInBackground(File... params) {
		File dir = params[0];		
		
		final List<String> picturesExtensions = new ArrayList<String>();
    	picturesExtensions.add("jpg");
    	picturesExtensions.add("png");    	
    	
    	File[] files = dir.listFiles();

    	
    	for(File f : files) {
    		//System.out.println(""+ f.toString());
    		
    		if (f.isFile()) {
    			// Vérification de l'extension du fichier
    			if (picturesExtensions.contains(FileUtils.getFileExtension(f.getPath()))) {
    				OPhoto photo = new OPhoto(f);
    				photos.add(photo);
    				publishProgress(photos.size());
    			}
    		} else {
        		if (f.isDirectory() && !f.isHidden()) doInBackground(f); // Récursivité !
    		}
    	}
    	
		return Long.valueOf(photos.size());
	}
	
	
	@Override
    protected void onProgressUpdate(Integer... progress) {
		progressDialog.setMessage("Photos trouvées : "+progress[0].toString());
    }

	@Override
    protected void onPostExecute(Long result) {
        progressDialog.dismiss();
        Toast.makeText(activity.getApplicationContext(), ("Nombre total de photos trouvées : "+result.toString()), Toast.LENGTH_SHORT).show();
    }
}
