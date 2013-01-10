package com.androworms;

import android.content.Context;
import android.graphics.PointF;
import android.util.Log;


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
		// ZONE DE TEST |--> TODO : a déplacer ailleurs !
		monde = new Monde();
		this.graphique = mg;
		ImageInformation ii = new ImageInformation(R.drawable.android_face, 162, 214 );
		Personnage johnDoe = new Personnage("John Doe", ii);
		johnDoe.setPosition(new PointF(820, 470));
		monde.addPersonnage(johnDoe);
		
		Personnage tux = new Personnage("Tux", ii);
		tux.setPosition(new PointF(120, 450));
		monde.addPersonnage(tux);

		ImageInformation iiObjetCarte = new ImageInformation(R.drawable.hache, 139, 95);
		Objet o = new Arme("Hache");
		o.setImageTerrain(R.drawable.hache, 139, 95);
		ObjetSurCarte obj = new ObjetSurCarte(o, new PointF(250,500), iiObjetCarte);
		monde.addObjetSurCarte(obj);
		obj = new ObjetSurCarte(o, new PointF(1000, 500), iiObjetCarte);
		monde.addObjetSurCarte(obj);
		
		monde.setTerrain(context.getResources().getDrawable(R.drawable.terrain_jeu_defaut_640x360), 1280, 720);
		//FIN ZONE DE TEST
		
		connexion = new ConnexionLocale(this);
		this.nomPersonnage = "Tux";
		physique = new MoteurPhysique(this, monde);
		
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
