package com.androworms;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.PointF;
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

	
	public Noyau(Context context, MoteurGraphique mg, boolean estDeuxJoueursBluetooth) {
		// Le paramètre "estDeuxJoueursBluetooth" est en test pour le moment, il ne sera peut-être pas définitif
		test(context, estDeuxJoueursBluetooth);
		graphique = mg;
	}
	
	/** Cette fonction sera à supprimer lorsque l'on en aura plus besoin. */
	public void test(Context context, boolean estDeuxJoueursBluetooth) {
		if (estDeuxJoueursBluetooth) {
			creationPartieDistante();
		}
		else {
			creationPartieLocale();
		}
		
		ImageInformation ii = new ImageInformation(R.drawable.test_android_face, 81, 107);
		Personnage johnDoe = new Personnage("John Doe", ii);
		johnDoe.setPosition(new PointF(220, 200));

		SharedPreferences settings = context.getSharedPreferences(ActiviteParametres.PREFS_NAME, 0);
		String pseudo = settings.getString("pseudo", "Joueur1");
		
		Personnage tux = new Personnage(pseudo, ii);
		tux.setPosition(new PointF(120, 200));
		monde.addPersonnage(tux);
		monde.addPersonnage(johnDoe);
		
		Bitmap b = ((BitmapDrawable)context.getResources().getDrawable(R.drawable.terrain_jeu_defaut_4)).getBitmap();
		monde.setTerrain(b, 1280, 720);
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

	public void actualiserGraphisme() {
		graphique.actualiserGraphisme();
	}

	public void creationPartieLocale() {
		connexion = new ConnexionLocale(this);
		monde = new Monde();
		physique = new MoteurPhysique(this, monde);
	}
	
	public void creationPartieDistante() {
		connexion = new ConnexionDistante(this);
		monde = new Monde();
		physique = new MoteurPhysique(this, monde);
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
	

	
	/** Gestion des tests. */
	public void testReseau() {
		Log.v(TAG_NOYAU, "TestReseau n'est plus valide !");
	}
	
	public Monde getMonde() {
		return monde;
	}

	/** Cette fonction informe l'interface graphique 
	 * qu'il y a des mouvements que des joueurs doivent exécuter
	 */
	public void mouvementForces() {
		graphique.affecterMouvementForces(monde.getListePersonnage());
	}
	


	
}