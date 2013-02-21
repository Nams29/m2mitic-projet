package fr.istic.project.controller;

import java.util.HashMap;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;
import fr.istic.project.R;
import fr.istic.project.utils.OnFlingGestureListener;
import fr.istic.project.utils.UIUtils;

public class ColorViewerActivity extends Activity {

	private TableLayout imagesTable;
	private ImageView fullScreen;
	
	private final HashMap<String, Integer> imagesDeTests = new HashMap<String, Integer>();
	private int tailleMatrice;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_color_viewer);

		//-------------------------------------------------------------------------
		// TODO : partie à adapter.
		//-------------------------------------------------------------------------
		
		tailleMatrice = 3;

		imagesTable = (TableLayout) findViewById(R.id.visionneuse);
		fullScreen = (ImageView) findViewById(R.id.fullview);
		
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
		
		if (UIUtils.isHoneycomb()) {
			ActionBar actionBar = this.getActionBar();
			actionBar.setDisplayHomeAsUpEnabled(true);
		}

	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent i = new Intent(this, HomeActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            this.startActivity(i);
            return true;
        } 
        else {
            return super.onOptionsItemSelected(item);
        }
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
				iv.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						onClickImage((ImageView)v);
					}
				});
				iv.setTag(cle);
				row.addView(iv);

			}

			imagesTable.addView(row);
		}
	}

	/**
	 * Called when the user click on an image
	 * @param v
	 */
	private void onClickImage(ImageView v) {
		fullScreen.setTag(v.getTag());
		
		fullScreen.setImageResource(this.imagesDeTests.get(v.getTag()));
		
		fullScreen.setVisibility(View.VISIBLE);
		
		fullScreen.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				fullScreen.setVisibility(View.GONE);
				fullScreen.setImageDrawable(null);
			}
		});
		
		fullScreen.setOnTouchListener(new MyFlingGestureListener(this, fullScreen));

	}
	
	@Override
	public void onBackPressed() {
		if (fullScreen.getVisibility() == View.VISIBLE) {
			fullScreen.setVisibility(View.GONE);
			fullScreen.setImageDrawable(null);
		}
		else {
			super.onBackPressed();
		}
	}
	
	/**
	 * Class MyFlingGestureListener
	 */
	class MyFlingGestureListener extends OnFlingGestureListener {

		Context c;
		ImageView iv;

		public MyFlingGestureListener(Context c, ImageView iv) {
			super(c);
			this.iv = iv;
		}

		@Override
		public void onRightToLeft() {
			String currentId = String.valueOf(iv.getTag());
			String str[] = currentId.split("#");
			Integer firstId = Integer.valueOf(str[0]);
			Integer secondId = Integer.valueOf(str[1]);

			if (secondId == tailleMatrice) {
				Toast.makeText(getApplicationContext(), "On est déjà au bord l'ami !", Toast.LENGTH_SHORT).show();
			} else {
				String newTag = firstId + "#" + (secondId + 1);
				iv.setTag(newTag);
				iv.setImageResource(imagesDeTests.get(newTag));
			}
		}

		@Override
		public void onLeftToRight() {
			String currentId = String.valueOf(iv.getTag());
			String str[] = currentId.split("#");
			Integer firstId = Integer.valueOf(str[0]);
			Integer secondId = Integer.valueOf(str[1]);

			if (secondId == 1) {
				Toast.makeText(getApplicationContext(), "On est déjà au bord l'ami !", Toast.LENGTH_SHORT).show();
			} else {
				String newTag = firstId + "#" + (secondId - 1);
				iv.setTag(newTag);
				iv.setImageResource(imagesDeTests.get(newTag));
			}
		}

		@Override
		public void onBottomToTop() {
			String currentId = String.valueOf(iv.getTag());
			String str[] = currentId.split("#");
			Integer firstId = Integer.valueOf(str[0]);
			Integer secondId = Integer.valueOf(str[1]);

			if (firstId == tailleMatrice) {
				Toast.makeText(getApplicationContext(), "On est déjà au bord l'ami !", Toast.LENGTH_SHORT).show();
			} else {
				String newTag = (firstId + 1) + "#" + secondId;
				iv.setTag(newTag);
				iv.setImageResource(imagesDeTests.get(newTag));
			}
		}

		@Override
		public void onTopToBottom() {
			String currentId = String.valueOf(iv.getTag());
			String str[] = currentId.split("#");
			Integer firstId = Integer.valueOf(str[0]);
			Integer secondId = Integer.valueOf(str[1]);

			if (firstId == 1) {
				Toast.makeText(getApplicationContext(), "On est déjà au bord l'ami !", Toast.LENGTH_SHORT).show();
			} else {
				String newTag = (firstId - 1) + "#" + secondId;
				iv.setTag(newTag);
				iv.setImageResource(imagesDeTests.get(newTag));
			}
		}

	}
}