package com.androworms;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PointF;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.androworms.ui.ClavierDirectionnel;
import com.androworms.utile.Informations;

public class ActiviteJeu extends Activity implements OnClickListener {
	
	private static final String TAG = "Androworms.ActiviteJeu";
	
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
	private MoteurGraphique moteurGraph;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v(TAG,"Start");
		
		/* Affiche la vue */
		setContentView(R.layout.activite_jeu);
		
		/* Récupération du layout de fond */
		moteurGraph = (MoteurGraphique)findViewById(R.id.mg_carte);
		
		/* Récupération des paramètres envoyé à l'activity */
		Bundle bundle = this.getIntent().getExtras();
		
		/* Création du noyau */
		noyau = new Noyau(getBaseContext(), moteurGraph, bundle);
		moteurGraph.setNoyau(noyau);
		
		/* Mode TIR */
		ToggleButton tgb = (ToggleButton) findViewById(R.id.tg_mode_tir);
		tgb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (!isChecked && (mode == TIR || mode == TIR_EN_COURS)) {
					setMode(RIEN);
					findViewById(R.id.tg_clavier).setEnabled(true);
				} else if (isChecked && mode != SELECTION_ARME) {
					setMode(TIR);
					findViewById(R.id.tg_clavier).setEnabled(false);
				}
			}
		});
		
		/* Mode déplacement */
		ToggleButton tgb2 = (ToggleButton)findViewById(R.id.tg_clavier);
		tgb2.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					findViewById(R.id.clavier).setVisibility(View.VISIBLE);
					findViewById(R.id.tg_mode_tir).setEnabled(false);
				} else {
					findViewById(R.id.clavier).setVisibility(View.INVISIBLE);
					findViewById(R.id.tg_mode_tir).setEnabled(true);
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
		
		ImageButton hache, pistolet, bazooka, grenade, mine;
		hache = (ImageButton) findViewById(R.id.hache);
		pistolet = (ImageButton) findViewById(R.id.pistolet);
		bazooka = (ImageButton) findViewById(R.id.bazooka);
		grenade = (ImageButton) findViewById(R.id.grenade);
		mine = (ImageButton) findViewById(R.id.mine);
		hache.setOnClickListener(this);
		pistolet.setOnClickListener(this);
		bazooka.setOnClickListener(this);
		grenade.setOnClickListener(this);
		mine.setOnClickListener(this);
		
		
		/* Affichage du mode de jeu */
		tv = (TextView)findViewById(R.id.tv_mode_jeu);
		updateAffichageMode();

		/* Gestion du clavier*/
		ClavierDirectionnel clavier = (ClavierDirectionnel) findViewById(R.id.clavier);
		clavier.setOnTouchListener(new EvenementClavier(noyau));
		
		/* Recentrer sur le joueur */
		ImageView ivRecentrer = (ImageView)findViewById(R.id.iv_recentrer);
		ivRecentrer.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				centrerCarte();
			}
		});
		// On centre le joueur au premier chargement
		centrerCarte();
		
		/* Gestion de la pause */
		ImageView ivPause = (ImageView)findViewById(R.id.iv_pause);
		ivPause.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// FIXME : juste un test pour ajouter des sons dans le projet.
				if (noyau.getParametresApplication().getBoolean(ActiviteParametres.PARAMETRE_SONS_CLE, ActiviteParametres.PARAMETRE_SONS_DEFAUT)) {
					MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.sound_ringer_vibrate);
					mp.start();
				}
			}
		});
	}
	
	/** Fonction qui affiche ou cache le bouton de recentrage */
	public void afficheBoutonCentrerCarte(boolean estAffiche) {
		ImageView ivRecentrer = (ImageView)findViewById(R.id.iv_recentrer);
		if (estAffiche) {
			ivRecentrer.setVisibility(View.VISIBLE);
		}
		else {
			ivRecentrer.setVisibility(View.INVISIBLE);
			
			// FIXME : à supprimer quand ça marchera bien !
			ivRecentrer.setVisibility(View.VISIBLE);
		}
	}
	
	/** Fonction qui centre le joueur au milieu de la carte */
	public void centrerCarte() {
		// Taille de l'écran
		int width = Informations.getWidthPixels();
		int height = Informations.getHeightPixels();
		// Position du joueur sur la carte de fond
		PointF positionJoueur = noyau.getMonde().getPersonnagePrincipal().getPosition();
		// Position du joueur par rapport au coin supérieur gauche de l'écran du téléphone et du coin supérieur gauche de l'image
		// Négatif sur "x", si l'image est plus à gauche que le bord gauche de l'écran,
		//      négatif sur "y" si l'image est plus haute que le haut de l'écran
		PointF positionJoueurSurEcran = moteurGraph.transpositionPointSurEcran(positionJoueur);
		// Calcul de la translation à faire pour que l'image soit au centre de l'image
		float transX = - positionJoueurSurEcran.x + width / (float)2 - Noyau.TAILLE_IMAGE_JOUEUR.x / (float)2;
		float transY = -positionJoueurSurEcran.y + height / (float)2 - Noyau.TAILLE_IMAGE_JOUEUR.y / (float)2;
		// Application de la translation de l'image et de tous les objets
		moteurGraph.getMatrice().postTranslate(transX,transY);
		moteurGraph.getEvtJeu().fixTrans();
		// Actualisation des graphismes
		moteurGraph.invalidate();
		// On affiche ou on cache
		afficheBoutonCentrerCarte(false);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.v(TAG, "ActiviteJeu destroy");
		moteurGraph = (MoteurGraphique)findViewById(R.id.mg_carte);
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
	
	@Override
	public void onBackPressed() {	
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Voulez-vous vraiment quitter la partie ?");
		builder.setCancelable(false);
		builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				finish();
			}
		});
		builder.setNegativeButton("Non", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	@Override
	public void onClick(View arg0) {
		ImageButton b = (ImageButton)arg0;
		Personnage persoPrincipal = this.noyau.getMonde().getPersonnagePrincipal();
		switch(b.getId()) {
		case R.id.hache:
			moteurGraph.ajouterArme(persoPrincipal, R.drawable.bras_hache);
			break;
		case R.id.pistolet:
			moteurGraph.ajouterArme(persoPrincipal, R.drawable.bras_pistolet);
			break;
		case R.id.bazooka:
			moteurGraph.ajouterArme(persoPrincipal, R.drawable.bras_bazooka);
			break;
		case R.id.grenade:
			moteurGraph.ajouterArme(persoPrincipal, R.drawable.bras_grenade);
			break;
		case R.id.mine:
			moteurGraph.ajouterArme(persoPrincipal, R.drawable.bras_mine);
			break;
		default :
			break;
		}
	}
}