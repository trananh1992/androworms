package com.androworms;

import java.io.File;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.graphics.drawable.BitmapDrawable;

/**
 * Le noyau gere tous les interactions.
 * Echange entre IHM/reseau/moteurGraphique.
 */
public class Noyau {

	// private ihm...
	public static final int DEPLACEMENT_DROITE = 0;
	public static final int DEPLACEMENT_GAUCHE = 1;
	public static final int DEPLACEMENT_HAUT = 2;
	
	private static final String TAG_NOYAU = "Androworms.Noyau";
	private Connexion connexion;
	private String nomPersonnage;
	private Monde monde;

	private MoteurPhysique physique;
	private MoteurGraphique graphique;
	
	// Variable de débug
	public static final PointF DEBUG_POSITION_JOUEUR_1 = new PointF(220, 200);
	public static final PointF DEBUG_POSITION_JOUEUR_2 = new PointF(620, 200);
	public static final Point TAILLE_IMAGE_JOUEUR = new Point(81, 107);
	public static final Point TAILLE_IMAGE_FOND = new Point(1280, 720);
	
	// Paramètres de l'application configurable dans l'activité "Paramètres" de l'application
	private SharedPreferences parametresApplication;
	
	public Noyau(Context context, MoteurGraphique mg, Bundle bundle) {
		this.graphique = mg;
		this.parametresApplication = context.getSharedPreferences(ActiviteParametres.PARAMETRES_CLE, 0);
		
		// Juste pour le test
		test(context, bundle);
	}
	
	/** Cette fonction sera à supprimer lorsque l'on en aura plus besoin. */
	public void test(Context context, Bundle bundle) {
		Integer paramMode = (Integer) bundle.get("mode");
		Boolean paramEstCartePerso = (Boolean) bundle.get("estCartePerso");
		String paramNomCarte = (String) bundle.get("nomCarte");

		monde = new Monde();
		physique = new MoteurPhysique(this, monde);
		
		switch (paramMode) {
		case ParametresPartie.MODE_BLUETOOTH_SERVEUR:
		case ParametresPartie.MODE_BLUETOOTH_CLIENT:
			creationPartieDistante();
			break;
		case ParametresPartie.MODE_SOLO :
		default :
			creationPartieLocale();
			break;
		}
		
		ImageInformation ii = new ImageInformation(R.drawable.android_face, TAILLE_IMAGE_JOUEUR.x, TAILLE_IMAGE_JOUEUR.y);
		Personnage johnDoe = new Personnage("John Doe", ii);
		johnDoe.setPosition(DEBUG_POSITION_JOUEUR_1);
		
		String pseudo = this.parametresApplication.getString(ActiviteParametres.PARAMETRE_PSEUDO_CLE, ActiviteParametres.PARAMETRE_PSEUDO_DEFAUT_2);
		
		Personnage tux = new Personnage(pseudo, ii);
		tux.setPosition(DEBUG_POSITION_JOUEUR_2);
		monde.addPersonnage(tux);
		monde.addPersonnage(johnDoe);
		
		if (paramEstCartePerso != null && paramNomCarte != null) {
			if (paramEstCartePerso) {
				File root = Environment.getExternalStorageDirectory();
				File sd = new File(root, "Androworms/" + paramNomCarte);
				Bitmap b = BitmapFactory.decodeFile(sd.getAbsolutePath());
				monde.setTerrain(b, TAILLE_IMAGE_FOND.x, TAILLE_IMAGE_FOND.y);
			}
			else {
				// TODO : prendre en compte ce paramètre
				// afficher la carte parmi les cartes par défaut de l'application.
				Bitmap b = ((BitmapDrawable)context.getResources().getDrawable(R.drawable.terrain_jeu_defaut_4)).getBitmap();
				monde.setTerrain(b, TAILLE_IMAGE_FOND.x, TAILLE_IMAGE_FOND.y);
			}
		}
		
		//physique.gravite();
		//mouvementForces();

		this.nomPersonnage = pseudo;
	}
	
	public String getNomPersonnage() {
		return nomPersonnage;
	}

	public void setNomPersonnage(String nomPersonnage) {
		this.nomPersonnage = nomPersonnage;
	}
	
	public MoteurPhysique getPhysique() {
		return physique;
	}

	public void setPhysique(MoteurPhysique physique) {
		this.physique = physique;
	}
	
	public SharedPreferences getParametresApplication() {
		return parametresApplication;
	}
	
	public void actualiserGraphisme() {
		graphique.actualiserGraphisme();
	}

	public void creationPartieLocale() {
		connexion = new ConnexionLocale(this);
	}
	
	public void creationPartieDistante() {
		connexion = new ConnexionDistante(this);
	}
	
	/** Gestion des messages venanat de l'IHM */
	public void sautJoueurDroiteFromIHM() {
		//physique.sautJoueurDroite(nomPersonnage);
		//physique.gravite();
		graphique.setGravityInFuture();
	}
	
	public void sautJoueurGaucheFromIHM() {
		//physique.sautJoueurGauche(nomPersonnage);
		//physique.gravite();
		graphique.setGravityInFuture();
	}
	
	public void deplacementJoueurDroiteFromIHM() {
		connexion.deplacementJoueurDroite(nomPersonnage);
	}
	
	public void deplacementJoueurGaucheFromIHM() {
		connexion.deplacementJoueurGauche(nomPersonnage);
	}
	
	public void deplacementJoueurDroite(String personnage) {
		// Impacter le monde de ce changement
		physique.deplacementJoueurDroite(personnage);
		this.graphique.actualiserGraphisme();
	}
	public void deplacementJoueurGauche(String personnage) {
		// Impacter le monde de ce changement
		physique.deplacementJoueurGauche(personnage);
		this.graphique.actualiserGraphisme();
	}

	public void effectuerTir(float puissance, float angle) {
		Log.v(TAG_NOYAU, "On tire");
		this.graphique.setGraviteInFuture(3);
	}
	
	
	public Monde getMonde() {
		return monde;
	}

	/** Cette fonction informe l'interface graphique 
	 * qu'il y a des mouvements que des joueurs doivent exécuter
	 */
	public void mouvementForces() {
	
	}
}