package fr.istic.project.controller;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import fr.istic.project.R;
import fr.istic.project.data.ApplicationDB;
import fr.istic.project.hac.HAC;
import fr.istic.project.hac.HAC.PictInfo;
import fr.istic.project.utils.BitmapUtils;
import fr.istic.project.utils.UIUtils;

public class TimeLineActivity extends Activity {

	private static final int TIMELINE_ITEM_SIZE = 230;
	
	ArrayList<ArrayList<PictInfo>> groups;
	
	private LinearLayout rlItem1;
	private LinearLayout rlItem2;
	private LinearLayout rlItem3;
	private LinearLayout rlItem4;
	private LinearLayout rlItem5;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);

		this.initLayout();
		this.loadData();
		
		if (UIUtils.isHoneycomb()) {
			ActionBar actionBar = this.getActionBar();
			actionBar.setDisplayHomeAsUpEnabled(true);
		}

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			Intent i = new Intent(this, MainActivity.class);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			this.startActivity(i);
			return true;
		}
		else {
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * Initializes the graphic elements
	 */
	private void initLayout() {
		this.rlItem1 = (LinearLayout) this.findViewById(R.id.timeline_item1);
		this.rlItem2 = (LinearLayout) this.findViewById(R.id.timeline_item2);
		this.rlItem3 = (LinearLayout) this.findViewById(R.id.timeline_item3);
		this.rlItem4 = (LinearLayout) this.findViewById(R.id.timeline_item4);
		this.rlItem5 = (LinearLayout) this.findViewById(R.id.timeline_item5);

		final float scale = this.getResources().getDisplayMetrics().density;
		int pixels = (int) (TIMELINE_ITEM_SIZE * scale + 0.5f);

		LayoutInflater factory = LayoutInflater.from(this);

		View childUp = factory.inflate(R.layout.item_timeline_up, null);
		childUp.setLayoutParams(new LayoutParams(pixels, LayoutParams.MATCH_PARENT));
		this.rlItem1.addView(childUp);

		View childDown = factory.inflate(R.layout.item_timeline_down, null);
		childDown.setLayoutParams(new LayoutParams(pixels, LayoutParams.MATCH_PARENT));
		this.rlItem2.addView(childDown);

		childUp = factory.inflate(R.layout.item_timeline_up, null);
		childUp.setLayoutParams(new LayoutParams(pixels, LayoutParams.MATCH_PARENT));
		this.rlItem3.addView(childUp);

		childDown = factory.inflate(R.layout.item_timeline_down, null);
		childDown.setLayoutParams(new LayoutParams(pixels, LayoutParams.MATCH_PARENT));
		this.rlItem4.addView(childDown);

		childUp = factory.inflate(R.layout.item_timeline_up, null);
		childUp.setLayoutParams(new LayoutParams(pixels, LayoutParams.MATCH_PARENT));
		this.rlItem5.addView(childUp);
	}

	/**
	 * Load data on the fields
	 */
	private void loadData() {
		HAC hac = new HAC();

		ApplicationDB database = ApplicationDB.getInstance();
		database.openDb();
		
		hac.addPhotos(database.getAllPhotos());
		database.closeDb();
		
		hac.setNbClusters(5);
		hac.findMiddle();
		
		groups = hac.getResults();
		
		BitmapWorkerTask task = new BitmapWorkerTask(groups);
		task.execute(0);
	}
	
	private void onLoadFinished(List<Bitmap> data) {
		ImageView ivCenter = (ImageView) this.rlItem1.findViewById(R.id.timeline_photo_center);
		ImageView ivLeft = (ImageView) this.rlItem1.findViewById(R.id.timeline_photo_left);
		ImageView ivRight = (ImageView) this.rlItem1.findViewById(R.id.timeline_photo_right);
		
		if (data.size()>0) this.setImageBitmap(ivCenter, data.get(0));
		if (data.size()>1) this.setImageBitmap(ivLeft, data.get(1));
		if (data.size()>2) this.setImageBitmap(ivRight, data.get(2));
		this.rlItem1.setTag(groups.get(0));
		
		ivCenter = (ImageView) this.rlItem2.findViewById(R.id.timeline_photo_center);
		ivLeft = (ImageView) this.rlItem2.findViewById(R.id.timeline_photo_left);
		ivRight = (ImageView) this.rlItem2.findViewById(R.id.timeline_photo_right);

		if (data.size()>3) this.setImageBitmap(ivCenter, data.get(3));
		if (data.size()>4) this.setImageBitmap(ivLeft, data.get(4));
		if (data.size()>5) this.setImageBitmap(ivRight, data.get(5));
		this.rlItem2.setTag(groups.get(1));
		
		ivCenter = (ImageView) this.rlItem3.findViewById(R.id.timeline_photo_center);
		ivLeft = (ImageView) this.rlItem3.findViewById(R.id.timeline_photo_left);
		ivRight = (ImageView) this.rlItem3.findViewById(R.id.timeline_photo_right);

		if (data.size()>6) this.setImageBitmap(ivCenter, data.get(6));
		if (data.size()>7) this.setImageBitmap(ivLeft, data.get(7));
		if (data.size()>8) this.setImageBitmap(ivRight, data.get(8));
		this.rlItem3.setTag(groups.get(2));
		
		ivCenter = (ImageView) this.rlItem4.findViewById(R.id.timeline_photo_center);
		ivLeft = (ImageView) this.rlItem4.findViewById(R.id.timeline_photo_left);
		ivRight = (ImageView) this.rlItem4.findViewById(R.id.timeline_photo_right);

		if (data.size()>9) this.setImageBitmap(ivCenter, data.get(9));
		if (data.size()>10) this.setImageBitmap(ivLeft, data.get(10));
		if (data.size()>11) this.setImageBitmap(ivRight, data.get(11));
		this.rlItem4.setTag(groups.get(3));
		
		ivCenter = (ImageView) this.rlItem5.findViewById(R.id.timeline_photo_center);
		ivLeft = (ImageView) this.rlItem5.findViewById(R.id.timeline_photo_left);
		ivRight = (ImageView) this.rlItem5.findViewById(R.id.timeline_photo_right);

		if (data.size()>12) this.setImageBitmap(ivCenter, data.get(12));
		if (data.size()>13) this.setImageBitmap(ivLeft, data.get(13));
		if (data.size()>14) this.setImageBitmap(ivRight, data.get(14));
		this.rlItem5.setTag(groups.get(4));
	}
	
	private void setImageBitmap(ImageView iv, Bitmap b) {
		if (b != null) {
			iv.setImageBitmap(b);
		}
	}
	
	/**
	 * Class BitmapWorkerTask
	 */
	class BitmapWorkerTask extends AsyncTask<Integer, Void, List<Bitmap>> {
		private List<ArrayList<PictInfo>> data;
		private List<Bitmap> results;

		public BitmapWorkerTask(ArrayList<ArrayList<PictInfo>> data) {
			this.results = new ArrayList<Bitmap>();
			this.data = data;
		}

		// Decode image in background.
		@Override
		protected List<Bitmap> doInBackground(Integer... params) {
			int i = 0;
			for (ArrayList<PictInfo> group : data) {
				for (i=0; i<3; i++) {
					if (i < group.size()) {
						results.add(BitmapUtils.decodeSampledBitmapFromResource(group.get(i).getName(), 100, 100));
					}
					else {
						results.add(null);
					}
				}
			}
			
			return results;
		}

		// Once complete, see if ImageView is still around and set bitmap.
		@Override
		protected void onPostExecute(List<Bitmap> bitmap) {
			onLoadFinished(results);
		}
	}

}
