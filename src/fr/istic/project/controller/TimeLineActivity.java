package fr.istic.project.controller;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import fr.istic.project.R;
import fr.istic.project.utils.UIUtils;

public class TimeLineActivity extends Activity {
	
	private LinearLayout rlItem1;
	
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
	}
	
	/**
	 * Load data on the fields
	 */
	private void loadData() {
		LayoutInflater factory = LayoutInflater.from(this);
		View child = factory.inflate(R.layout.item_timeline, null);
		
		this.rlItem1.addView(child);
	}

}
