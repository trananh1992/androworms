package com.androworms;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.androworms.ui.TouchRelativeLayout;

public class GameActivity extends Activity {
	
	private static final String TAG = "TESTAndroworms.MainActivity";
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v(TAG,"Start");
		
		/* Affiche la vue */
		setContentView(R.layout.terrain_de_jeu);
		
		/* Récupération du layout de fond */
		TouchRelativeLayout trl = (TouchRelativeLayout)findViewById(R.id.trlCarte);
		
		/** Actions post-construction **/
		trl.init(this);
	}
}
