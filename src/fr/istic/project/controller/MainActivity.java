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
import fr.istic.project.model.FindPhotosTask;
import fr.istic.project.model.OPhoto;
import fr.istic.project.utils.FileUtils;

public class MainActivity extends Activity {
	
	private TextView console;
	private LinearLayout timeline;
	
	private List<OPhoto> photos = new LinkedList<OPhoto>();
    
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        this.console = (TextView) findViewById(R.id.main_console);
    	this.timeline = (LinearLayout) findViewById(R.id.main_timeline);
    	
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
		if  (id == R.id.menu_timeline) {
			Intent i = new Intent(this, TimeLineActivity.class);
			this.startActivity(i);
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
    	    File fSDcard = Environment.getExternalStorageDirectory();
    	    File[] directories = new File[FileUtils.allowedSDcardDirectories.length];	    
    	    for (int i=0; i<FileUtils.allowedSDcardDirectories.length; i++) {
    	    	directories[i] = new File(fSDcard.getPath() + File.separatorChar + FileUtils.allowedSDcardDirectories[i]); // Ajout des répertoires à parcourir
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

		for(OPhoto newPhoto : newPhotos) {
			this.photos.add(newPhoto);
			console.append("\n" +newPhoto.getPath());
			
//	    	ImageView iv = new ImageView(this);
//	    	iv.setBackgroundDrawable(getResources().getDrawable((R.drawable.polaroid_photo_frame)));
//	    	iv.setImageDrawable(Drawable.createFromPath(pic.getPath()));
//	    	iv.setLayoutParams(new LayoutParams(87, 100));
//	    	iv.setPadding(5, 5, 5, 10);
//	    	
//	    	timeline.addView(iv);
    	}
		
	}
 
}