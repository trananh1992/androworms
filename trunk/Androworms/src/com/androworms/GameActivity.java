package com.androworms;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import android.widget.SlidingDrawer.OnDrawerScrollListener;
import android.widget.TextView;


public class GameActivity extends Activity {
	
	private static final String TAG = "TESTAndroworms.MainActivity";
	
	/* Etats possible pour le mode de gestion des doigts */
	private static int mode;
	public static final int RIEN = 0;
	public static final int DEPLACEMENT = 1;
	public static final int ZOOM = 2;
	public static final int SELECTION_ARME = 3;
	public static final int TIR = 4;
	
	private static TextView tv;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v(TAG,"Start");
		
		/* Affiche la vue */
		setContentView(R.layout.terrain_de_jeu);
		
		/* Récupération du layout de fond */
		MoteurGraphique trl = (MoteurGraphique)findViewById(R.id.trlCarte);
		
		Button btnTir = (Button)findViewById(R.id.button1);
		btnTir.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				if (mode == TIR) {
					setMode(RIEN);
				} else if (mode != SELECTION_ARME) {
					setMode(TIR);
				}
			}
		});
		
		SlidingDrawer sd = (SlidingDrawer)findViewById(R.id.slidingDrawer1);
		sd.setOnDrawerOpenListener(new OnDrawerOpenListener() {
			public void onDrawerOpened() {
				setMode(SELECTION_ARME);
			}
		});
		sd.setOnDrawerCloseListener(new OnDrawerCloseListener() {
			public void onDrawerClosed() {
				setMode(RIEN);
			}
		});
		
		tv = (TextView)findViewById(R.id.mode_jeu);
		updateAffichageMode();
		
		/** Actions post-construction **/
		trl.init(this);
	}
	
	public static void updateAffichageMode() {
		if (tv != null) {
			switch (mode) {
			case RIEN:
				tv.setText("Mode : RIEN");
				break;
			case DEPLACEMENT:
				tv.setText("Mode : DEPLACEMENT");
				break;
			case ZOOM:
				tv.setText("Mode : ZOOM");
				break;
			case SELECTION_ARME:
				tv.setText("Mode : SELECTION_ARME");
				break;
			case TIR:
				tv.setText("Mode : TIR");
				break;
			default:
				break;
			}
		}
	}

	public static int getMode() {
		return mode;
	}

	public static void setMode(int mode) {
		GameActivity.mode = mode;
		updateAffichageMode();
	}
}
