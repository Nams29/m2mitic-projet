package fr.istic.project.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;

public class ColorTraitment {

    private final Context context;

    public ColorTraitment(Context context) {
        this.context = context;
    }

    private List<OPhoto> process() {

        String chaine = "";
        List<OPhoto> photosToReturn = new ArrayList<OPhoto>();

        // Lecture du fichier texte.  
        try {

            // Préparation de la lecture.
            InputStream raw = context.getAssets().open("filename.ext");
            InputStreamReader ipsr = new InputStreamReader(raw);
            BufferedReader br = new BufferedReader(ipsr);

            String ligne;
            while ((ligne = br.readLine()) != null) {
                System.out.println(ligne);
                chaine += ligne;
            }
            br.close();
        } catch (Exception e) {
            System.out.println(e.toString());
        }

        // Suppression des crochets.
        chaine = chaine.substring(0, chaine.length() - 1);
        chaine.replace("[", "");

        // Split par crochet.
        String[] lignes = chaine.split("]");

        for (int i = 0; i < lignes.length; i++) {
            String[] vectorPhoto = lignes[i].split(",");

            for (int j = 0; j < vectorPhoto.length; j++) {
                String photoName = vectorPhoto[j].replace("'", "");
                File photoFile = new File(photoName);
                OPhoto photo = new OPhoto(photoFile);
                photosToReturn.add(photo);
            }
        }

        return photosToReturn;
    }

}
