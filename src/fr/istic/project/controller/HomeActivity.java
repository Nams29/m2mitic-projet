package fr.istic.project.controller;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import fr.istic.project.R;
import fr.istic.project.utils.UIUtils;

public class HomeActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (UIUtils.isHoneycomb()) {
            ActionBar actionBar = this.getActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        ImageView menuTemps = (ImageView) findViewById(R.id.menuTemps);
        menuTemps.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TimeLineActivity.class);
                startActivity(intent);
            }
        });

        ImageView menuCouleur = (ImageView) findViewById(R.id.menuCouleur);
        menuCouleur.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ColorViewerActivity.class);
                startActivity(intent);
            }
        });

        ImageView menuContenu = (ImageView) findViewById(R.id.menuContenu);
        menuContenu.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ViewerActivity.class);
                Bundle b = new Bundle();
                b.putInt(ViewerActivity.VIEWTYPE, ViewerActivity.VIEWTYPE_CONTENT);
                startActivity(intent);
            }
        });

    }

}
