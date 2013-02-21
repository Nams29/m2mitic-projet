package fr.istic.project.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;
import fr.istic.project.R;
import fr.istic.project.data.ApplicationDB;
import fr.istic.project.model.OPhoto;
import fr.istic.project.utils.BitmapUtils;
import fr.istic.project.utils.OnFlingGestureListener;
import fr.istic.project.utils.UIUtils;

public class ViewerActivity extends Activity {
	
	public static final String GROUP_FIRSTDATE = "firstdate";
	public static final String GROUP_LASTDATE = "lastdate";
	
	public static final String VIEWTYPE = "viewtype";
	public static final int VIEWTYPE_GROUP = 0;
	public static final int VIEWTYPE_COLOR = 1;
	public static final int VIEWTYPE_CONTENT = 2;
	
	private GridView gridView;
	private ImageView fullScreen;
	
	private MyFlingGestureListener flingListener;
	
	private final int PADDING = 0;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_viewer);
		
		this.initLayout();
		this.loadData(this.getIntent().getIntExtra(VIEWTYPE, VIEWTYPE_CONTENT));
		
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
	
	/**
	 * Called when the user click on an image
	 * @param v
	 */
	private void onClickImage(ImageView v, int position) {
		BitmapWorkerTask task = new BitmapWorkerTask(position);
		task.execute(0);

		flingListener.setPosition(position);
		
		fullScreen.setVisibility(View.VISIBLE);

		fullScreen.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				v.setVisibility(View.GONE);
				fullScreen.setImageBitmap(null);
			}
		});
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
	
	private void initLayout() {
		this.gridView = (GridView) this.findViewById(R.id.gridview);
		this.fullScreen = (ImageView) findViewById(R.id.fullview);

		DisplayMetrics ecran = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(ecran);

		int gridSize = ecran.widthPixels;
		int count = gridSize / 200; // image has 200x200 px
		int colWidth = (gridSize / count) - PADDING;

		this.gridView.setColumnWidth(colWidth);
		this.gridView.setNumColumns(count);
		
		this.gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				onClickImage((ImageView)view, position);
			}
		});
		
		this.flingListener = new MyFlingGestureListener(this);
		this.fullScreen.setOnTouchListener(flingListener);
	}
	
	private void loadData(int viewtype) {
		List<OPhoto> data = new ArrayList<OPhoto>();
		
		if (viewtype == VIEWTYPE_GROUP) {
			Bundle b = this.getIntent().getExtras();
			Date firstDate = (Date) b.get(GROUP_FIRSTDATE);
			Date lastDate = (Date) b.get(GROUP_LASTDATE);
			
			ApplicationDB database = ApplicationDB.getInstance();
			database.openDb();
			data = database.getPhotosFromDateInterval(firstDate, lastDate);
			database.closeDb();
		}
		else if (viewtype == VIEWTYPE_COLOR) {
			ApplicationDB database = ApplicationDB.getInstance();
			database.openDb();
			data = database.getSomePhotos(50);
			database.closeDb();
		}
		else if (viewtype == VIEWTYPE_CONTENT) {
			ApplicationDB database = ApplicationDB.getInstance();
			database.openDb();
			data = database.getSomePhotos(50);
			database.closeDb();
		}
		
		this.gridView.setAdapter(new ViewerLoader(this, data));
	}
	
	/**
	 * Class MyFlingGestureListener
	 */
	class MyFlingGestureListener extends OnFlingGestureListener {
		
		private int position;

		public MyFlingGestureListener(Context c) {
			super(c);
		}
		
		@Override
		public void onRightToLeft() {
			position++;

			this.move(position);
		}

		@Override
		public void onLeftToRight() {
			position--;

			this.move(position);
		}

		@Override
		public void onBottomToTop() {
			int nbCols = gridView.getNumColumns();
			position += nbCols;

			this.move(position);
			
		}

		@Override
		public void onTopToBottom() {
			int nbCols = gridView.getNumColumns();
			position -= nbCols;

			this.move(position);
		}
		
		/**
		 * Display the picture found at the given position
		 * @param position the position of the picture to display
		 */
		private void move(int position) {
			if (position >= 0 && position < gridView.getAdapter().getCount()) { 
				BitmapWorkerTask task = new BitmapWorkerTask(position);
				task.execute(0);
			}
			else {
                Toast.makeText(getApplicationContext(), "On est déjà au bord l'ami !", Toast.LENGTH_SHORT).show();
			}
		}
		
		/**
		 * Set the current picture position
		 * @param p
		 */
		public void setPosition(int p) {
			this.position = p;
		}

	}
	
	/**
	 * Class BitmapWorkerTask
	 */
	class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap> {
		private int position;

		public BitmapWorkerTask(int position) {
			this.position = position;
		}

		// Decode image in background.
		@Override
		protected Bitmap doInBackground(Integer... params) {
			return BitmapUtils.decodeSampledBitmapFromResource(
					((OPhoto)gridView.getAdapter().getItem(position)).getPath(), 200, 200);
		}

		// Once complete, see if ImageView is still around and set bitmap.
		@Override
		protected void onPostExecute(Bitmap bitmap) {
			fullScreen.setImageBitmap(bitmap);
		}
	}
}
