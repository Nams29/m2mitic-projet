package fr.istic.project.controller;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import fr.istic.project.R;
import fr.istic.project.utils.UIUtils;

public class TimeLineActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);

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

}
