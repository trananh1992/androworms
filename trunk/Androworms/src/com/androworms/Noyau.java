package com.androworms;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.util.Log;
import android.graphics.drawable.BitmapDrawable;

/*
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

	
	public Noyau(Context context, MoteurGraphique mg) {

		test(context);
		graphique = mg;

		
	}
	
	// Cette fonction sera à supprimer lorsque l'on en aura plus besoin.
	public void test(Context context) {

		creationPartieLocale();
		ImageInformation ii = new ImageInformation(R.drawable.android_face, 162, 214 );
		Personnage johnDoe = new Personnage("John Doe", ii);
		johnDoe.setPosition(new PointF(240, 0));

		Personnage tux = new Personnage("Tux", ii);
		tux.setPosition(new PointF(120, 200));
		monde.addPersonnage(tux);
		monde.addPersonnage(johnDoe);

		ImageInformation iiObjetCarte = new ImageInformation(R.drawable.arme_hache, 139, 95);
		Objet o = new Arme("Hache");
		o.setImageTerrain(R.drawable.arme_hache, 139, 95);
		ObjetSurCarte obj = new ObjetSurCarte(o, new PointF(250,500), iiObjetCarte);
		monde.addObjetSurCarte(obj);
		obj = new ObjetSurCarte(o, new PointF(1000, 500), iiObjetCarte);
		monde.addObjetSurCarte(obj);
		
		Bitmap b = ((BitmapDrawable)context.getResources().getDrawable(R.drawable.terrain_jeu_defaut_640x360)).getBitmap();
		monde.setTerrain(b, 1280, 720);

		this.nomPersonnage = "Tux";
	}
	
	public String getNomPersonnage() {
		return nomPersonnage;
	}

	public void setNomPersonnage(String nomPersonnage) {
		this.nomPersonnage = nomPersonnage;
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
	}
	
	/*
	 * Gestion des messages venanat de l'IHM 
	 */
	public void sautJoueurDroiteFromIHM() {
		physique.gravite();
	}
	
	public void sautJoueurGaucheFromIHM() {
		
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
		// TODO Auto-generated method stub
		Log.v(TAG_NOYAU, "On tire");
	}
	
	/*
	 * Gestion des tests.
	 */
	
	public void testReseau() {
		Log.v(TAG_NOYAU, "TestReseau n'est plus valide !");
	}
	
	public Monde getMonde() {
		return monde;
	}
	
	
}
