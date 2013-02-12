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
import fr.istic.project.R;
import fr.istic.project.model.FindPhotosTask;
import fr.istic.project.model.OPhoto;

public class MainActivity extends Activity {
	
	TextView console;
	LinearLayout timeline;
    List<OPhoto> photos = new LinkedList<OPhoto>(); 
    
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        console = (TextView) findViewById(R.id.main_console);
    	timeline = (LinearLayout) findViewById(R.id.main_timeline);
     
        getPhotos();
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
    
	public void getPhotos() {
    	/* VERIFICATION DE LA DISPONIBILTE DE LA CARTE */
    	String state = Environment.getExternalStorageState();
    	if (Environment.MEDIA_MOUNTED.equals(state)) {
    	    // We can read and write the media
    	        
    	    
    	    /* PARCOURS DE LA CARTE SD */
    	    File mFile 	= Environment.getExternalStorageDirectory();
    	    
    	    FindPhotosTask task = new FindPhotosTask(this);
    	    task.execute(mFile);

    	    
    	    /* AFFICHAGE */
    	    for(OPhoto photo : photos) {
    	    	console.setText(console.getText() + "\n" +photo.getPath());
    	    	
//    	    	ImageView iv = new ImageView(this);
//    	    	iv.setBackgroundDrawable(getResources().getDrawable((R.drawable.polaroid_photo_frame)));
//    	    	iv.setImageDrawable(Drawable.createFromPath(pic.getPath()));
//    	    	iv.setLayoutParams(new LayoutParams(87, 100));
//    	    	iv.setPadding(5, 5, 5, 10);
//    	    	
//    	    	timeline.addView(iv);
    	    	
    	    	
//    	    	/* EDITION DES DONNEES EXIF */
//    	    	try {
//    	    		Log.d("tag", "file:"+f.getPath().substring(4));
//    	    		
//    	    		ExifInterface mExifTool = new ExifInterface(f.getPath().substring(4));
//    	    		Log.d("tag", "model:"+mExifTool.getAttribute(ExifInterface.TAG_MODEL));
////    	    		mExifTool.setAttribute(ExifInterface.TAG_MODEL, "sdf2");
////    	    		mExifTool.saveAttributes();
////    	    		Log.d("tag", "model:"+mExifTool.getAttribute(ExifInterface.TAG_MODEL));
//    	    	} catch (IOException e) {
//    	    		// TODO Auto-generated catch block
//    	    		e.printStackTrace();
//    	    	}
    	    }
    	    	    
    	    
    	    
    	    
    	    
    	} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
    	    // We can only read the media
    	    
    	} else {
    	    // Something else is wrong. We can neither read nor write

    	}       	
    	
    }
    
    
    
    
    
    
//    static class ProgressTask extends AsyncTask<Void, Integer, Boolean> {
//    	private final OPhoto picture;
//    	private final TextView console;
//    	
//    	public ProgressTask(OPhoto picture, TextView console) {
//    		this.picture = picture;
//    		this.console = console;
//    	}
//    	
//        @Override
//        protected void onPreExecute () {
//        }
//     
//        @Override
//        protected void onPostExecute (Boolean result) {
//        }
//     
//        @Override
//        protected Boolean doInBackground (Void... arg0) {
//          console.setText(console.getText() + "\n" +picture.getPath());
//          return true;
//        }
//     
//        @Override
//        protected void onProgressUpdate (Integer... prog) {
//        }
//     
//        @Override
//        protected void onCancelled () {
//        }
//
//      }
}