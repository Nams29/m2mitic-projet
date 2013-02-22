package fr.istic.project.model;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import fr.istic.project.R;
import fr.istic.project.data.ApplicationDB;

public class ColorTraitment {

    private final Context context;

    public ColorTraitment(Context context) {
        this.context = context;
    }

    public List<OPhoto> process() {

        String chaine = "";
        List<OPhoto> photosToReturn = new ArrayList<OPhoto>();

        // Lecture du fichier texte.  
        try {

            // Préparation de la lecture.
            InputStream raw = context.getResources().openRawResource(R.raw.matrice);
            InputStreamReader ipsr = new InputStreamReader(raw);
            BufferedReader br = new BufferedReader(ipsr);

            String ligne;
            while ((ligne = br.readLine()) != null) {
                chaine += ligne;
            }
            br.close();
        } catch (Exception e) {
            System.out.println(e.toString());
        }

        // Suppression des crochets.
        chaine = chaine.substring(0, chaine.length() - 1);

        // Split par crochet.
        String[] lignes = chaine.split("]");

        // Appel à la base.
        ApplicationDB database = ApplicationDB.getInstance();
        database.openDb();

        for (int i = 0; i < lignes.length; i++) {
            String[] vectorPhoto = lignes[i].split(",");

            for (int j = 0; j < vectorPhoto.length; j++) {
                String photoName = vectorPhoto[j].replace("'", "");
                photoName = photoName.replace("[", "");

                OPhoto photo;

                if (photoName.equals("0")) {
                    photo = database.getPhotoByName("noir.jpg");
                } else {

                    if (photoName.contains("|")) {
                        String[] temp = photoName.split("\\|");
                        photo = database.getPhotoByName(temp[0].trim());
                    } else {
                        photo = database.getPhotoByName(photoName);
                    }
                }

                if (photo != null) {
                    photosToReturn.add(photo);
                } else {
                    System.out.println("Photo vide !!! " + photoName);
                }

            }
        }

        database.closeDb();

        return photosToReturn;
    }

}
