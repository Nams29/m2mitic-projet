package fr.istic.project.controller;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.RelativeLayout;
import fr.istic.project.R;

public class TimeLineActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.timeline);

		// Set main background.
		RelativeLayout rLayout = (RelativeLayout) findViewById(R.id.timeline);
		Resources res = getResources();
		Drawable drawable = res.getDrawable(R.drawable.bg_frise); 
		rLayout.setBackgroundDrawable(drawable);

	}
}
