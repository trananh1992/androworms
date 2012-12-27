package com.androworms;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import android.widget.TextView;
import android.widget.ToggleButton;

public class GameActivity extends Activity {
	
	private static final String TAG = "TESTAndroworms.GameActivity";
	
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
		MoteurGraphique moteurGraph = (MoteurGraphique)findViewById(R.id.trlCarte);
		
		// ZONE DE TEST
		Monde monde = new Monde(null, moteurGraph);
		List<Personnage> persos = new ArrayList<Personnage>();
		Personnage p = new Personnage("toto");
		p.setPosition(new Point(1500, 980));
		persos.add(p);
		p = new Personnage("tux");
		p.setPosition(new Point(240, 1100));
		persos.add(p);
		monde.setListePersonnage(persos);
		List<ObjetSurCarte> objs = new ArrayList<ObjetSurCarte>();
		Objet o = new Arme("truc", null, 0);
		ObjetSurCarte obj = new ObjetSurCarte(o, new PointF(100,200));
		objs.add(obj);
		obj = new ObjetSurCarte(o, new PointF(500, 1000));
		objs.add(obj);
		monde.setListeObjetCarte(objs);
		moteurGraph.setMonde(monde);
		//FIN ZONE DE TEST
		
		/* Mode TIR */
		ToggleButton tgb = (ToggleButton)findViewById(R.id.toggleButton1);
		tgb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (!isChecked && mode == TIR) {
					setMode(RIEN);
				} else if (isChecked && mode != SELECTION_ARME) {
					setMode(TIR);
				}
			}
		});
		
		/* Selecteur d'armes */
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