package fr.istic.project.controller;

import java.io.File;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import fr.istic.project.R;
import fr.istic.project.data.ApplicationDB;
import fr.istic.project.data.ApplicationSQLiteOpenHelper;
import fr.istic.project.data.FindPhotosTask;
import fr.istic.project.model.OContext;
import fr.istic.project.model.OPhoto;
import fr.istic.project.utils.FileUtils;

public class MainActivity extends Activity {

    private TextView console;
    private transient ApplicationDB applicationDB;
    private List<OPhoto> newPhotos = null;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.console = (TextView) findViewById(R.id.main_console);

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
        // Faut pas faire un switch parce qu'à partir d'une certaine version d'Android
        // tu peux que faire des switchs sur des variables d'application (genre les R.machin)
        // et avec item.getItemId() ça plante... Oui c'pas logique
        if (id == R.id.menu_home) {
            Intent i = new Intent(this, HomeActivity.class);
            this.startActivity(i);
            this.finish();
            return true;
        } 
        else if (id == R.id.menu_db_reset) {
            deleteDatabase(ApplicationSQLiteOpenHelper.DATABASE_NAME);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
    
    
    public void findPhotos() {

        /* VERIFICATION DE LA DISPONIBILTE DES MEDIAS */
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // We can read and write the media 

			boolean useApplicationDirectory = false;
			boolean useExternalDirectoriesDevice = true;
	      	boolean useExternalDirectoriesRemovable = true;
	
            /* PARCOURS DES MEDIAS - PREPARATION */
            File[] directories = FileUtils.getAllowedDirectories(useApplicationDirectory, useExternalDirectoriesDevice, useExternalDirectoriesRemovable); // Ajout des dossiers à parcourir
            for (File dir : directories) {
                console.append("\n " + dir.toString());
            }

            /* PARCOURS DES MEDIAS - EXECUTION */
            FindPhotosTask findPhotosTask = new FindPhotosTask(MainActivity.this);
            findPhotosTask.execute(directories);

            /* AFFICHAGE */
            // voir méthode processPhotos() déclenchée par FindPhotosTask.onPostExecute() 
            

        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // We can only read the media
            //Toast.makeText(getApplicationContext(), "Erreur : Mémoire interne et/ou carte microSD en lecture seule.", Toast.LENGTH_LONG).show();
        	dialogContinuer();
        	
        } else {
            // Something else is wrong. We can neither read nor write
            //Toast.makeText(getApplicationContext(), "Erreur : Mémoire interne et/ou carte microSD manquante.", Toast.LENGTH_LONG).show();
        	dialogContinuer();
        }

    }
    
    
    /**
     * Dialog qui réclame un média pour commencer
     */
    public boolean dialogContinuer() {
    	
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Média(s) inaccessible(s) !");
        builder.setMessage("Veuillez insérer une carte microSD (ou déconnecter l'appareil de votre ordinateur) puis cliquer sur Commencer.")

        /* Clic sur Commencer */
        .setNeutralButton("Commencer.", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
            	findPhotos();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
        return false;
        
    }
    
    
    /**
     * Dialog qui demande si l'application doit ajouter les nouvelles photos trouvées
     */
    public boolean dialogAjouter() {
    	
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Ajouter les nouvelles photos ?");
        builder.setMessage("De nouvelles photos ont été trouvées. Souhaitez-vous les ajouter à l'application ?")

        /* Clic sur Ajouter */
        .setPositiveButton("Ajouter !", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                processPhotos(true);
            }
        })

        /* Clic sur Non */
        .setNegativeButton("Non, merci.", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                processPhotos(false);
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        return false;
    }


	public void onPostExecuteFindPhotosTask(List<OPhoto> newPhotos) {
    	
		this.newPhotos = newPhotos;
		
		if (!newPhotos.isEmpty()) { // Si il y a de nouvelles photos
			dialogAjouter(); // Affichage du dialog pour l'ajout de photos	
		} else {
			processPhotos(false);
		}

    }
	
	
	public void processPhotos(boolean ajouter) {
		
		console.append("\n\nNouveau contenu trouvé  :\n" + newPhotos.size() + " photo(s).\n");
		
		console.append("\nDétail des photos présentes dans la carte :");
        for (OPhoto newPhoto : newPhotos) {
    		//if (ajouter) applicationDB.addPhoto(newPhoto); // Si l'utilisateur souhaite ajouter les nouvelles photos
            console.append("\n - " + newPhoto.getName() + "\n   " + newPhoto.getIdentifier());
        }
    	
        
    	List<OPhoto> photos = applicationDB.getAllPhotos();
    	List<OContext> contexts = applicationDB.getAllContexts();
       
        console.append("\n\nContenu sauvegardé dans la base de données :\n" + photos.size() + " photo(s).\n"
                + contexts.size() + " contexte(s).\n");
        
        console.append("\nDétail des photos présentes dans la BDD :");        
    	for(OPhoto photo : photos) {
    		console.append("\n - " + photo.getName() + "\n   " + photo.getIdentifier());
    	}
	}
	

    public ApplicationDB getApplicationDB() {
        return applicationDB;
    }

}