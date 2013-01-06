package com.androworms;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.androworms.ui.Paddle;

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
	
	private Monde monde;
	
	/* Gestion du déplacement du joueur avec le Paddle */
	private static final int DEPLACEMENT_JOUEUR = 20;
	private static final int TEMPS_APPUIE = 60;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v(TAG,"Start");
		
		/* Affiche la vue */
		setContentView(R.layout.terrain_de_jeu);
		
		/* Récupération du layout de fond */
		MoteurGraphique moteurGraph = (MoteurGraphique)findViewById(R.id.trlCarte);
		
		// ZONE DE TEST |--> TODO : a déplacer ailleurs !
		monde = new Monde(null, moteurGraph);
		List<Personnage> persos = new ArrayList<Personnage>();
		Personnage p = new Personnage("John Doe");
		p.setPosition(new Point(820, 470));
		persos.add(p);
		p = new Personnage("Tux");
		p.setPosition(new Point(120, 450));
		persos.add(p);
		monde.setListePersonnage(persos);
		List<ObjetSurCarte> objs = new ArrayList<ObjetSurCarte>();
		Objet o = new Arme("Hache", null, 0);
		ObjetSurCarte obj = new ObjetSurCarte(o, new PointF(250,500));
		objs.add(obj);
		obj = new ObjetSurCarte(o, new PointF(1000, 500));
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
					findViewById(R.id.paddle1).setVisibility(View.VISIBLE);
					findViewById(R.id.toggleButton1).setEnabled(false);
				} else {
					findViewById(R.id.paddle1).setVisibility(View.INVISIBLE);
					findViewById(R.id.toggleButton1).setEnabled(true);
				}
			}
		});
		// par défaut, le Paddle est caché
		findViewById(R.id.paddle1).setVisibility(View.INVISIBLE);
		
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
		
		/* Gestion du Paddle */
		Paddle paddle = (Paddle)findViewById(R.id.paddle1);
		paddle.setOnTouchListener(new OnTouchListener() {
			
			private Handler mHandler;
			private int idBtnCourrant;
			/* Action lorsque l'on touche un des boutons du Paddle */
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
					/* Début d'appuie d'un boutton du Paddle */
					case MotionEvent.ACTION_DOWN:
						
						idBtnCourrant = v.getId();
						/* Je crée un Handler qui va bouger le joueur tous les x secondes s'il reste appuyer dessus */
						if (mHandler != null) {
							return true;
						}
						mHandler = new Handler();
						// TODO : le lancement du Thread n'est pas rentable pour faire juste 1 déplacement.
						// il faudrait mettre le code du déplacement ici, et la création du Thread avec postDelayed sur TEMPS_APPUIE
						// mais ce code est assez gros, et donc il faudrait faire une fonction qui est appellé ici et depuis le Runnable.
						// (et mettre un commentaire !)
						mHandler.post(mAction);
						break;
					/* Fin d'appuie d'un boutton du Paddle */
					case MotionEvent.ACTION_UP:
						
						idBtnCourrant = -1;
						/* Je supprime le Handler */
						if (mHandler == null) {
							return true;
						}
						mHandler.removeCallbacks(mAction);
						mHandler = null;
						break;
					default:
						break;
				}
				return true;
			}
			
			/* Thread pour gérer toutes les x secondes le déplacement du joueur lorsque l'on reste appuyé */
			Runnable mAction = new Runnable() {
				public void run() {
					switch (idBtnCourrant) {
						// Déplacement vers la droite
						case Paddle.BOUTON_DROITE:
							Log.v(TAG,"Déplacement vers la droite");
							Point p1 = monde.getPersonnagePrincipal().getPosition();
							p1.offset(DEPLACEMENT_JOUEUR, 0);
							monde.getPersonnagePrincipal().setPosition(p1);
							break;
						// Déplacement vers le haut
						case Paddle.BOUTON_HAUT:
							Log.v(TAG,"Déplacement vers la haut");
							Point p3 = monde.getPersonnagePrincipal().getPosition();
							p3.offset(0, -DEPLACEMENT_JOUEUR);
							monde.getPersonnagePrincipal().setPosition(p3);
							break;
						// Déplacement vers la gauche
						case Paddle.BOUTON_GAUCHE:
							Log.v(TAG,"Déplacement vers la gauche");
							Point p2 = monde.getPersonnagePrincipal().getPosition();
							p2.offset(-DEPLACEMENT_JOUEUR, 0);
							monde.getPersonnagePrincipal().setPosition(p2);
							break;
						default:
							Log.v(TAG,"Déplacement default");
							break;
					}
					monde.getMg().actualiserGraphisme();
					mHandler.postDelayed(this, TEMPS_APPUIE);
				}
			};
		});
	}
	
	
	/* Appelée quand l'activité n'est plus affichée
	 */
	protected void onStop() {
		super.onStop();
		Log.v(TAG, "GameActivity stop");
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