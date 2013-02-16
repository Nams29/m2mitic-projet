package fr.istic.project.controller;

import java.util.HashMap;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;
import fr.istic.project.R;
import fr.istic.project.utils.OnFlingGestureListener;
import fr.istic.project.utils.UIUtils;

public class ViewerActivity extends Activity {

    protected TableLayout imagesTable;
    protected final HashMap<String, Integer> imagesDeTests = new HashMap<String, Integer>();
    protected int tailleMatrice;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewer);

        if (UIUtils.isHoneycomb()) {
            ActionBar actionBar = this.getActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //-------------------------------------------------------------------------
        // TODO : partie à adapter.
        //-------------------------------------------------------------------------

        tailleMatrice = 3;

        imagesTable = (TableLayout) findViewById(R.id.visionneuse);
        imagesDeTests.put("1#1", R.drawable.test_image1);
        imagesDeTests.put("1#2", R.drawable.test_image2);
        imagesDeTests.put("1#3", R.drawable.test_image3);
        imagesDeTests.put("2#1", R.drawable.test_image4);
        imagesDeTests.put("2#2", R.drawable.test_image5);
        imagesDeTests.put("2#3", R.drawable.test_image6);
        imagesDeTests.put("3#1", R.drawable.test_image7);
        imagesDeTests.put("3#2", R.drawable.test_image8);
        imagesDeTests.put("3#3", R.drawable.test_image9);

        fillImagesTable();
    }

    private void fillImagesTable() {

        //-------------------------------------------------------------------------        
        // Calcul du rapport largeur / hauteur.
        //-------------------------------------------------------------------------

        // Déclaration des variables.
        DisplayMetrics ecran = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(ecran);
        int hauteurEcran = ecran.heightPixels;
        int largeurEcran = ecran.widthPixels;

        // Affichage optimisé pour la tablette.
        int hauteurPourUnePhoto = hauteurEcran / tailleMatrice;
        int largeurPourUnePhoto = largeurEcran / tailleMatrice;

        for (int i = 1; i <= tailleMatrice; i++) {

            TableRow row = new TableRow(this);

            for (int j = 1; j <= tailleMatrice; j++) {
                String cle = i + "#" + j;
                ImageView iv = new ImageView(this);
                iv.setImageResource(imagesDeTests.get(cle));
                iv.setLayoutParams(new TableRow.LayoutParams(largeurPourUnePhoto, hauteurPourUnePhoto));
                iv.setOnClickListener(new ClickListener());
                iv.setTag(cle);
                row.addView(iv);

            }

            imagesTable.addView(row);
        }
    }

    class ClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {

            ImageView fullScreen = (ImageView) findViewById(R.id.fullview);
            fullScreen.setImageResource(ViewerActivity.this.imagesDeTests.get(v.getTag()));
            fullScreen.setVisibility(View.VISIBLE);
            fullScreen.setOnClickListener(new ClickListenerOut());
            fullScreen.setOnTouchListener(new MyFlingGestureListener(v, fullScreen));
        }
    }

    class MyFlingGestureListener extends OnFlingGestureListener {

        View v;
        ImageView iv;

        public MyFlingGestureListener(View v, ImageView iv) {
            this.v = v;
            this.iv = iv;
        }

        @Override
        public void onRightToLeft() {
            String currentId = String.valueOf(v.getTag());
            String str[] = currentId.split("#");
            Integer firstId = Integer.valueOf(str[0]);
            Integer secondId = Integer.valueOf(str[1]);

            if (secondId == ViewerActivity.this.tailleMatrice) {
                Toast.makeText(getApplicationContext(), "On est déjà au bord l'ami !", Toast.LENGTH_SHORT).show();
            } else {
                String newTag = firstId + "#" + (secondId + 1);
                iv.setTag(newTag);
                iv.setImageResource(ViewerActivity.this.imagesDeTests.get(newTag));

                Toast.makeText(getApplicationContext(), iv.getTag().toString(), Toast.LENGTH_SHORT).show();

                iv.setOnTouchListener(new MyFlingGestureListener(v, iv));
            }
        }

        @Override
        public void onLeftToRight() {
            String currentId = String.valueOf(v.getTag());
            String str[] = currentId.split("#");
            Integer firstId = Integer.valueOf(str[0]);
            Integer secondId = Integer.valueOf(str[1]);

            if (secondId == 1) {
                Toast.makeText(getApplicationContext(), "On est déjà au bord l'ami !", Toast.LENGTH_SHORT).show();
            } else {
                String newTag = firstId + "#" + (secondId - 1);
                iv.setTag(newTag);
                iv.setImageResource(ViewerActivity.this.imagesDeTests.get(newTag));
                Toast.makeText(getApplicationContext(), iv.getTag().toString(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onBottomToTop() {
            String currentId = String.valueOf(v.getTag());
            String str[] = currentId.split("#");
            Integer firstId = Integer.valueOf(str[0]);
            Integer secondId = Integer.valueOf(str[1]);

            if (firstId == ViewerActivity.this.tailleMatrice) {
                Toast.makeText(getApplicationContext(), "On est déjà au bord l'ami !", Toast.LENGTH_SHORT).show();
            } else {
                String newTag = (firstId + 1) + "#" + secondId;
                iv.setTag(newTag);
                iv.setImageResource(ViewerActivity.this.imagesDeTests.get(newTag));
                Toast.makeText(getApplicationContext(), iv.getTag().toString(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onTopToBottom() {
            String currentId = String.valueOf(v.getTag());
            String str[] = currentId.split("#");
            Integer firstId = Integer.valueOf(str[0]);
            Integer secondId = Integer.valueOf(str[1]);

            if (firstId == 1) {
                Toast.makeText(getApplicationContext(), "On est déjà au bord l'ami !", Toast.LENGTH_SHORT).show();
            } else {
                String newTag = (firstId - 1) + "#" + secondId;
                iv.setTag(newTag);
                iv.setImageResource(ViewerActivity.this.imagesDeTests.get(newTag));
                Toast.makeText(getApplicationContext(), iv.getTag().toString(), Toast.LENGTH_SHORT).show();
            }
        }

    }

    class ClickListenerOut implements OnClickListener {

        @Override
        public void onClick(View v) {
            v.setVisibility(View.GONE);
        }
    }
}
