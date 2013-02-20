package fr.istic.project.controller;

import java.io.File;
import java.util.LinkedList;
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
import android.widget.Toast;
import fr.istic.project.R;
import fr.istic.project.data.ApplicationDB;
import fr.istic.project.data.ApplicationSQLiteOpenHelper;
import fr.istic.project.data.FindPhotosTask;
import fr.istic.project.model.OPhoto;
import fr.istic.project.utils.FileUtils;

public class MainActivity extends Activity {

    private TextView console;

    private List<OPhoto> photos;
    private transient ApplicationDB applicationDB;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.console = (TextView) findViewById(R.id.main_console);
        this.photos = new LinkedList<OPhoto>();

        console.append("Vidage de la BDD : voir dans le menu de l'application.\n");
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

    /**
     * Dialog qui demande si l'application doit rechercher de nouvelles photos
     */
    public boolean dialogRecherche() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Rechercher de nouvelles photos ?");
        builder.setMessage("Souhaitez-vous rechercher la présence de nouvelles photos ?\nCeci peut-être... vâchement long...")

        /* Clic sur Oui */
        .setPositiveButton("Oh oui !", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialogSelection();
            }
        })

        /* Clic sur Non */
        .setNegativeButton("Non, merci.", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                processPhotos(new LinkedList<OPhoto>());
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        return false;
    }

    /**
     * Dialog qui propose de sélectionner les dossiers concernés par la
     * recherche
     */
    public boolean dialogSelection() {
        // Détection des médias disponibles
        final boolean externalDirectoryDeviceAvailable = ((FileUtils.getExternalDirectoryDevice() != null) ? true : false);
        final boolean externalDirectoryRemovableAvailable = ((FileUtils.getExternalDirectoryRemovable() != null) ? true : false);

        // Construction du dialog
        List<CharSequence> items = new LinkedList<CharSequence>();
        final boolean[] itemsPreChecked = { false, false, true }; // Choix par défaut du dialog
        final boolean[] resultats = itemsPreChecked;

        if (externalDirectoryDeviceAvailable) {
            items.add("Répertoire de l'application  \n" + "(dossier \"" + FileUtils.APPLICATION_DIRECTORY_DEVICE + "\" dans la mémoire de la tablette)");
            items.add("Mémoire de la tablette");
        }
        if (externalDirectoryRemovableAvailable) {
            items.add("Mémoire microSD");

            // Répercute le choix par défaut dans la première case si elle est vide
            if (!externalDirectoryDeviceAvailable)
                itemsPreChecked[0] = itemsPreChecked[2];
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Rechercher dans...");
        builder.setMultiChoiceItems(items.toArray(new CharSequence[items.size()]), itemsPreChecked, new DialogInterface.OnMultiChoiceClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                resultats[which] = isChecked;
            }
        })

        /* Clic sur OK */
        .setPositiveButton("OK !", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                boolean useApplicationDirectory = false;
                boolean useExternalDirectoriesDevice = false;
                boolean useExternalDirectoriesRemovable = false;

                if (externalDirectoryDeviceAvailable) {
                    useApplicationDirectory = resultats[0];
                    useExternalDirectoriesDevice = resultats[1];
                }
                if (externalDirectoryRemovableAvailable) {
                    if (externalDirectoryDeviceAvailable)
                        useExternalDirectoriesRemovable = resultats[2];
                    else
                        useExternalDirectoriesRemovable = resultats[0];
                }

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

            }
        });

        AlertDialog alert = builder.create();
        alert.show();

        return false;
    }

    public void findPhotos() {

        /* VERIFICATION DE LA DISPONIBILTE DES MEDIAS */
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // We can read and write the media
            dialogRecherche();

        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // We can only read the media
            Toast.makeText(getApplicationContext(), "Erreur : Mémoire interne et/ou carte microSD en lecture seule.", Toast.LENGTH_LONG).show();

        } else {
            // Something else is wrong. We can neither read nor write
            Toast.makeText(getApplicationContext(), "Erreur : Mémoire interne et/ou carte microSD manquante.", Toast.LENGTH_LONG).show();
        }

    }

    public void processPhotos(List<OPhoto> newPhotos) {

        console.append("\nContenu présent dans la carte :\n" + newPhotos.size() + " photo(s).\n");

        console.append("\nContenu sauvegardé dans la base de données :\n" + applicationDB.getAllPhotos().size() + " photo(s).\n"
                + applicationDB.getAllContexts().size() + " contexte(s).\n");

        console.append("\nDétail des photos présentes dans la carte :");

        for (OPhoto newPhoto : newPhotos) {
            this.photos.add(newPhoto);
            console.append("\n - " + newPhoto.getName() + "\n   " + newPhoto.getIdentifier());
        }

    }

    public ApplicationDB getApplicationDB() {
        return applicationDB;
    }

}