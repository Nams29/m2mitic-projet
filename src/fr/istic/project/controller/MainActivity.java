package fr.istic.project.controller;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import fr.istic.project.R;
import fr.istic.project.model.ApplicationDB;
import fr.istic.project.model.ApplicationSQLiteOpenHelper;
import fr.istic.project.model.FindPhotosTask;
import fr.istic.project.model.OPhoto;
import fr.istic.project.utils.FileUtils;

public class MainActivity extends Activity {
	
	private TextView console;
	private LinearLayout timeline;
	
	private List<OPhoto> photos;
	private transient ApplicationDB applicationDB;
    
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        this.console = (TextView) findViewById(R.id.main_console);
    	this.timeline = (LinearLayout) findViewById(R.id.main_timeline);
    	
    	this.photos = new LinkedList<OPhoto>();
    	
    	deleteDatabase(ApplicationSQLiteOpenHelper.DATABASE_NAME); // TODO à virer plus tard
    	console.append("Mode debug : BDD vidée à chaque lancement !\n");
    	this.applicationDB = ApplicationDB.getInstance();
    	applicationDB.initialize(this);
    	applicationDB.openDb();    	
		
    	findPhotos();
    }
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if  (id == R.id.menu_timeline) { // TODO faire un switch !!
			Intent i = new Intent(this, TimeLineActivity.class);
			this.startActivity(i);
			return true;
		}
		if  (id == R.id.menu_db_reset) {
			deleteDatabase(ApplicationSQLiteOpenHelper.DATABASE_NAME);
			return true;
		}
		else {
			return super.onOptionsItemSelected(item);
		}
	}
    
	public void findPhotos() {
	
    	/* VERIFICATION DE LA DISPONIBILTE DE LA CARTE */
    	String state = Environment.getExternalStorageState();
    	if (Environment.MEDIA_MOUNTED.equals(state)) {
    	    // We can read and write the media
    	        
    	    
    	    /* PARCOURS DE LA CARTE SD - PREPARATION */
    	    // TODO proposer selection reps / a revoir avec
    		// http://stackoverflow.com/questions/10282457/how-to-access-removable-storage-on-android-devices
    		// http://renzhi.ca/2012/02/03/how-to-list-all-sd-cards-on-android/
    		// http://stackoverflow.com/questions/7450650/how-to-list-additional-external-storage-folders-mount-points
    		
    		File fSDcard;
    	    if (Environment.isExternalStorageRemovable()) {
    	    	fSDcard = Environment.getExternalStorageDirectory();
    	    } else {
    	    	fSDcard = new File("/Removable/MicroSD"); // ASUS Transformer
    	    }
    	    
    	    
    	    File[] directories = new File[FileUtils.ALLOWED_DIRECTORIES_CARD.length];	    
    	    for (int i=0; i<FileUtils.ALLOWED_DIRECTORIES_CARD.length; i++) {
    	    	directories[i] = new File(fSDcard.getPath() + File.separatorChar + FileUtils.ALLOWED_DIRECTORIES_CARD[i]); // Ajout des répertoires à parcourir
    	    	
    	    	console.append("\n a"+directories[i]);
    	    }
    	    
    	    /* PARCOURS DE LA CARTE SD - EXECUTION */
    	    FindPhotosTask findPhotosTask = new FindPhotosTask(this);
	    	findPhotosTask.execute(directories);
	    	
	    	
    	    /* AFFICHAGE */
    	    // voir méthode processPhotos() déclenchée par FindPhotosTask.onPostExecute()
    	    
    	} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
    	    // We can only read the media
    	    Toast.makeText(getApplicationContext(), "Erreur : Carte SD en lecture seule.", Toast.LENGTH_SHORT).show();
    	} else {
    	    // Something else is wrong. We can neither read nor write
    		Toast.makeText(getApplicationContext(), "Erreur : Carte SD manquante.", Toast.LENGTH_SHORT).show();
    	}       	
    	
    }
	
	
	public void processPhotos(List<OPhoto> newPhotos) {
		
		console.append("\nContenu présent dans la carte :\n"
						+ newPhotos.size() + " photo(s).\n");

		console.append("\nContenu sauvegardé dans la base de données :\n"
						+ applicationDB.getAllPhotos().size() + " photo(s).\n"
						+ applicationDB.getAllContexts().size() + " contexte(s).\n");
		
		console.append("\nDétail des photos présentes dans la carte :");
		
		for(OPhoto newPhoto : newPhotos) {
			this.photos.add(newPhoto);
			console.append("\n - " + newPhoto.getName() + "\n   " + newPhoto.getIdentifier());
			
//	    	ImageView iv = new ImageView(this);
//	    	iv.setBackgroundDrawable(getResources().getDrawable((R.drawable.polaroid_photo_frame)));
//	    	iv.setImageDrawable(Drawable.createFromPath(pic.getPath()));
//	    	iv.setLayoutParams(new LayoutParams(87, 100));
//	    	iv.setPadding(5, 5, 5, 10);
//	    	
//	    	timeline.addView(iv);
    	}
		
	}
	
	
	public ApplicationDB getApplicationDB() {
		return applicationDB;
	}
	
	public TextView getConsole() {
		return this.console;
	}
 
}