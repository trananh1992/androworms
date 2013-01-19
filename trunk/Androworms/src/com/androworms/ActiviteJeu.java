package com.androworms;

import java.io.File;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.androworms.ui.ClavierDirectionnel;

public class ActiviteJeu extends Activity {
	
	private static final String TAG = "TESTAndroworms.GameActivity";
	
	/* Etats possible pour le mode de gestion des doigts */
	private static int mode;
	public static final int RIEN = 0;
	public static final int DEPLACEMENT = 1;
	public static final int ZOOM = 2;
	public static final int SELECTION_ARME = 3;
	public static final int TIR = 4;
	public static final int TIR_EN_COURS = 5;
	
	private static TextView tv;
	private Noyau noyau;


	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v(TAG,"Start");
		
		/* Affiche la vue */
		setContentView(R.layout.terrain_de_jeu);
		
		/* Récupération du layout de fond */
		MoteurGraphique moteurGraph = (MoteurGraphique)findViewById(R.id.trlCarte);
		
		noyau = new Noyau(getBaseContext(), moteurGraph);
		Bundle bundle = this.getIntent().getExtras();
		if(bundle != null)
		{
			String map = (String) bundle.get("map");
			if(map!=null && map.length()>0)
			{
				Log.e("started activite jeu","with map "+map);
				Monde m = noyau.getMonde();
				File root = Environment.getExternalStorageDirectory();
				File sd = new File(root,"Androworms/"+map);
				Bitmap b = BitmapFactory.decodeFile(sd.getAbsolutePath());
				m.setTerrain(b, 1280, 720);
			}
		}
		moteurGraph.setNoyau(noyau);
		
		/* Mode TIR */
		ToggleButton tgb = (ToggleButton) findViewById(R.id.toggleButton1);
		tgb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (!isChecked && (mode == TIR || mode == TIR_EN_COURS)) {
					setMode(RIEN);
					findViewById(R.id.toggleButton2).setEnabled(true);
				} else if (isChecked && mode != SELECTION_ARME) {
					setMode(TIR);
					findViewById(R.id.toggleButton2).setEnabled(false);
				}
			}
		});
		
		/* Mode déplacement */
		ToggleButton tgb2 = (ToggleButton)findViewById(R.id.toggleButton2);
		tgb2.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					findViewById(R.id.clavier).setVisibility(View.VISIBLE);
					findViewById(R.id.toggleButton1).setEnabled(false);
				} else {
					findViewById(R.id.clavier).setVisibility(View.INVISIBLE);
					findViewById(R.id.toggleButton1).setEnabled(true);
				}
			}
		});
		// par défaut, le clavier est caché
		findViewById(R.id.clavier).setVisibility(View.INVISIBLE);

		/* Sélecteur d'armes */
		SlidingDrawer sd = (SlidingDrawer)findViewById(R.id.selecteur_arme);
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
		TableLayout tl = (TableLayout)findViewById(R.id.selecteur_arme_contenu);
		tl.setBackgroundColor(Color.WHITE);
		tl.getBackground().setAlpha(80);
		
		
		/* Affichage du mode de jeu */
		tv = (TextView)findViewById(R.id.mode_jeu);
		updateAffichageMode();

		/* Gestion du clavier*/
		ClavierDirectionnel clavier = (ClavierDirectionnel) findViewById(R.id.clavier);
		clavier.setOnTouchListener(new EvenementClavier(noyau));
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.v(TAG, "ActiviteJeu destroy");
		MoteurGraphique moteurGraph = (MoteurGraphique)findViewById(R.id.trlCarte);
		moteurGraph.nettoyer();
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
			case TIR_EN_COURS:
				tv.setText("Mode : TIR_EN_COURS");
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
		ActiviteJeu.mode = mode;
		updateAffichageMode();
	}
}