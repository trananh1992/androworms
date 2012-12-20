package com.androworms;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.androworms.ui.TouchRelativeLayout;

public class GameActivity extends Activity {
	
	private static final String TAG = "TESTAndroworms.MainActivity";
	
	/* Etats possible pour le mode de gestion des doigts */
	public static int mode;
	public static final int RIEN = 0;
	public static final int DEPLACEMENT = 1;
	public static final int ZOOM = 2;
	public static final int TIR = 3;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v(TAG,"Start");
		
		/* Affiche la vue */
		setContentView(R.layout.terrain_de_jeu);
		
		/* Récupération du layout de fond */
		TouchRelativeLayout trl = (TouchRelativeLayout)findViewById(R.id.trlCarte);
		
		Button btnTir = (Button)findViewById(R.id.button1);
		btnTir.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				if (mode == TIR) {
					mode = RIEN;
				} else {
					mode = TIR;
				}
			}
		});
		
		/** Actions post-construction **/
		trl.init(this);
	}
}
