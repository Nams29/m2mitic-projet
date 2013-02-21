package fr.istic.project.data;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.widget.Toast;
import fr.istic.project.controller.MainActivity;
import fr.istic.project.model.OContext;
import fr.istic.project.model.OPhoto;
import fr.istic.project.utils.FileUtils;
import fr.istic.project.utils.LocaleUtils;

public class FindPhotosTask extends AsyncTask<File, Integer, Void> {
	
	private MainActivity activity;
	private ProgressDialog progressDialog;
	private List<OPhoto> newPhotos;

	private Geocoder geocoder;
	private boolean geocoderError;
	
	private ApplicationDB applicationDB;
	
	private long start;
	private boolean cancelTask;

	
	/* Multi threading */	
	final int cores = Runtime.getRuntime().availableProcessors();
	final int maxThreads = cores * 4; // 4 threads par core
	ExecutorService executorService =
		new ThreadPoolExecutor(
			maxThreads,
			maxThreads,
			1,
			TimeUnit.MINUTES,
			new ArrayBlockingQueue<Runnable>(maxThreads, true),
			new ThreadPoolExecutor.CallerRunsPolicy()
	);
	
	
	public FindPhotosTask(MainActivity activity) {
		super();
		
		this.activity = activity;
		this.progressDialog = new ProgressDialog(activity); 
				progressDialog.setTitle("Chargement en cours...");
				progressDialog.setCancelable(true);
				progressDialog.setCanceledOnTouchOutside(true);
			    progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
				    @Override
				    public void onCancel(DialogInterface dialog) {
				    	cancelTask = true;
				    }
				});
		this.cancelTask = false;
		this.newPhotos = new LinkedList<OPhoto>();
		
		this.geocoder = new Geocoder(activity, LocaleUtils.LOCALE_FR);
		this.geocoderError = false;
		
		this.applicationDB = activity.getApplicationDB();
		applicationDB.setAllPhotosUnavailable();
		if (applicationDB.getContext(0) == null) { // TODO a corriger ?
			OContext.defaultContext = applicationDB.addContext(OContext.defaultContext); // Ajout du contexte par défaut
		}
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		start = System.currentTimeMillis();
		progressDialog.show();
		publishProgress(0);
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
		    		
		    		if (cancelTask) break;
		    		
		    		if (file.isFile()) {
		    			// Vérification de l'extension du fichier
		    			if (FileUtils.ALLOWED_FILE_EXTENSIONS.contains(FileUtils.getFileExtension(file.getPath()))) {
		    				final OPhoto photo = new OPhoto(file); // Création de la photo
		    				photo.setContext(OContext.defaultContext);
		    				
		    				
		    				/* Multi threading */
		    				executorService.submit(new Runnable() {
								@Override
								public void run() {
									try {

										if (photo.processProperties(geocoder) == false) geocoderError = true; // Traitement des propriétés (identifiant et localisation)
										newPhotos.add(photo);	
										applicationDB.setPhotoAvailable(photo.getIdentifier());
					    				publishProgress(newPhotos.size());
					    				
									} catch (Exception e) {
										e.printStackTrace();
								    }
							    }
							});

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
		
		/* Multi threading */
		executorService.shutdown();			         
		try {
			// pool didn't terminate after the first try
			if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
				executorService.shutdownNow();
			}

			// pool didn't terminate after the second try
			if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) { }
		} catch (InterruptedException e) {
			executorService.shutdownNow();
			Thread.currentThread().interrupt();
		}
		
		progressDialog.dismiss();
		if (geocoderError) {
			Toast.makeText(activity, "Erreur : connexion Internet indisponible, les localisations des photos n'ont pu être traitées avec Geocoder.", Toast.LENGTH_LONG).show();
		}
		
		//long duration = System.currentTimeMillis() - start;
		//Toast.makeText(activity, "duree : "+ (duration/1000), Toast.LENGTH_LONG).show();
		
		activity.onPostExecuteFindPhotosTask(newPhotos); // Retourne dans l'activity        
    }
}
