package fr.istic.project.controller;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import fr.istic.project.R;
import fr.istic.project.utils.UIUtils;

public class TimeLineActivity extends Activity {
	
	private static final int TIMELINE_ITEM_SIZE = 230;
	
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
	}
	
	/**
	 * Load data on the fields
	 */
	private void loadData() {
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

}
