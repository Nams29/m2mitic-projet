package fr.istic.project.controller;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import fr.istic.project.R;
import fr.istic.project.utils.BitmapUtils;
import fr.istic.project.utils.OnFlingGestureListener;
import fr.istic.project.utils.UIUtils;

public class ViewerActivity extends Activity {

	//protected TableLayout imagesTable;

	private GridView gridView;
	private ImageView fullScreen;
	
	private final int PADDING = 0;

	//protected final HashMap<String, Integer> imagesDeTests = new HashMap<String, Integer>();
	//protected int tailleMatrice;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_viewer);

		this.gridView = (GridView) this.findViewById(R.id.gridview);
		this.fullScreen = (ImageView) findViewById(R.id.fullview);

		DisplayMetrics ecran = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(ecran);

		int gridSize = ecran.widthPixels;
		int count = gridSize / 200; // image has 200x200 px
		int colWidth = (gridSize / count) - PADDING;

		this.gridView.setColumnWidth(colWidth);
		this.gridView.setNumColumns(count);

		this.gridView.setAdapter(new ViewerLoader(this));
		this.gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				onClickImage((ImageView)view);
			}
		});

		if (UIUtils.isHoneycomb()) {
			ActionBar actionBar = this.getActionBar();
			actionBar.setDisplayHomeAsUpEnabled(true);
		}

	}

	/**
	 * Called when the user click on an image
	 * @param v
	 */
	private void onClickImage(ImageView v) {
		BitmapWorkerTask task = new BitmapWorkerTask(v);
		task.execute(0);

		fullScreen.setVisibility(View.VISIBLE);

		fullScreen.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				v.setVisibility(View.GONE);
				fullScreen.setImageBitmap(null);
			}
		});

		fullScreen.setOnTouchListener(
				new MyFlingGestureListener(this, fullScreen));
	}
	
	@Override
	public void onBackPressed() {
		if (fullScreen.getVisibility() == View.VISIBLE) {
			fullScreen.setVisibility(View.GONE);
			fullScreen.setImageBitmap(null);
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
			/*String currentId = String.valueOf(iv.getTag());
            String str[] = currentId.split("#");
            Integer firstId = Integer.valueOf(str[0]);
            Integer secondId = Integer.valueOf(str[1]);

            if (secondId == ViewerActivity.this.tailleMatrice) {
                Toast.makeText(getApplicationContext(), "On est déjà au bord l'ami !", Toast.LENGTH_SHORT).show();
            } else {
                String newTag = firstId + "#" + (secondId + 1);
                iv.setTag(newTag);
                iv.setImageResource(ViewerActivity.this.imagesDeTests.get(newTag));
            }*/
		}

		@Override
		public void onLeftToRight() {
			/*String currentId = String.valueOf(iv.getTag());
            String str[] = currentId.split("#");
            Integer firstId = Integer.valueOf(str[0]);
            Integer secondId = Integer.valueOf(str[1]);

            if (secondId == 1) {
                Toast.makeText(getApplicationContext(), "On est déjà au bord l'ami !", Toast.LENGTH_SHORT).show();
            } else {
                String newTag = firstId + "#" + (secondId - 1);
                iv.setTag(newTag);
                iv.setImageResource(ViewerActivity.this.imagesDeTests.get(newTag));
            }*/
		}

		@Override
		public void onBottomToTop() {
			/*String currentId = String.valueOf(iv.getTag());
            String str[] = currentId.split("#");
            Integer firstId = Integer.valueOf(str[0]);
            Integer secondId = Integer.valueOf(str[1]);

            if (firstId == ViewerActivity.this.tailleMatrice) {
                Toast.makeText(getApplicationContext(), "On est déjà au bord l'ami !", Toast.LENGTH_SHORT).show();
            } else {
                String newTag = (firstId + 1) + "#" + secondId;
                iv.setTag(newTag);
                iv.setImageResource(ViewerActivity.this.imagesDeTests.get(newTag));
            }*/
		}

		@Override
		public void onTopToBottom() {
			/*String currentId = String.valueOf(iv.getTag());
            String str[] = currentId.split("#");
            Integer firstId = Integer.valueOf(str[0]);
            Integer secondId = Integer.valueOf(str[1]);

            if (firstId == 1) {
                Toast.makeText(getApplicationContext(), "On est déjà au bord l'ami !", Toast.LENGTH_SHORT).show();
            } else {
                String newTag = (firstId - 1) + "#" + secondId;
                iv.setTag(newTag);
                iv.setImageResource(ViewerActivity.this.imagesDeTests.get(newTag));
            }*/
		}

	}
	
	/**
	 * Class BitmapWorkerTask
	 */
	class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap> {
		private ImageView view;

		public BitmapWorkerTask(ImageView view) {
			this.view = view;
		}

		// Decode image in background.
		@Override
		protected Bitmap doInBackground(Integer... params) {
			return BitmapUtils.decodeSampledBitmapFromResource(view.getTag().toString(), 200, 200);
		}

		// Once complete, see if ImageView is still around and set bitmap.
		@Override
		protected void onPostExecute(Bitmap bitmap) {
			fullScreen.setImageBitmap(bitmap);
		}
	}
}
