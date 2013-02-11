package fr.istic.project.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import fr.istic.project.R;
import fr.istic.project.model.PPicture;
import fr.istic.project.utils.FileUtils;

public class MainActivity extends Activity {
	
    List<PPicture> pictures = new LinkedList<PPicture>(); 
    
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
     
        getPhotos();
    }
    
    
    public void getPhotos() {
    	TextView console = (TextView) findViewById(R.id.console);
    	LinearLayout timeline = (LinearLayout) findViewById(R.id.timeline);
    	
    	
    	/* VERIFICATION DE LA DISPONIBILTE DE LA CARTE */
    	String state = Environment.getExternalStorageState();
    	if (Environment.MEDIA_MOUNTED.equals(state)) {
    	    // We can read and write the media
    	        
    	    
    	    /* PARCOURS DE LA CARTE SD */
    	    File mFile 	= Environment.getExternalStorageDirectory();
    	    browseSD(mFile);
    	    
    	    /* AFFICHAGE */
    	    for(PPicture pic : pictures) {
    	    	console.setText(console.getText() + "\n" +pic.getPath());
    	    	
    	    	ImageView iv = new ImageView(this);
    	    	iv.setBackgroundDrawable(getResources().getDrawable((R.drawable.polaroid_photo_frame)));
    	    	iv.setImageDrawable(Drawable.createFromPath(pic.getPath()));
    	    	iv.setLayoutParams(new LayoutParams(87, 100));
    	    	iv.setPadding(5, 5, 5, 10);
    	    	
    	    	timeline.addView(iv);
    	    	
    	    	
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
    
    
    public void browseSD(File dir) {
    	final List<String> picturesExtensions = new ArrayList<String>();
    	picturesExtensions.add("jpg");
    	picturesExtensions.add("png");    	
    	
    	File[] files = dir.listFiles();

    	
    	for(File f : files) {
    		//System.out.println(""+ f.toString());
    		
    		if (f.isFile()) {
    			// Vérification de l'extension du fichier
    			if (picturesExtensions.contains(FileUtils.getFileExtension(f.getPath())))
    				pictures.add(new PPicture(f));
    			
    		} else {
        		if (f.isDirectory() && !f.isHidden()) browseSD(f); // Récursivité !
    		}
    	}
    }
}