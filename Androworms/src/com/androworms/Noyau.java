package com.androworms;

import android.util.Log;


/*
 * Le noyau gere tous les interactions.
 * Echange entre IHM/reseau/moteurGraphique.
 */
public class Noyau {

	// private ihm...
	private static final String TAG_NOYAU = "Androworms.Noyau";
	private Connexion connexion;
	private String nomPersonnage;
	private Monde monde;
	private MoteurPhysique physique;
	private MoteurGraphique graphique;

	
	public Noyau() {
	}
	
	public String getNomPersonnage() {
		return nomPersonnage;
	}

	public void setNomPersonnage(String nomPersonnage) {
		this.nomPersonnage = nomPersonnage;
	}

	public void creationPartieLocale() {
		connexion = new ConnexionLocale(this);
		physique = new MoteurPhysique(this);
		monde = new Monde(physique);
		
	}
	
	public void creationPartieDistante() {
		connexion = new ConnexionDistante(this);
	}
	
	public void deplacementJoueurFromIHM(int move) {
		if( move == 0 ) {
			connexion.deplacementJoueurDroite(nomPersonnage);
		}
		else {
			connexion.deplacementJoueurGauche(nomPersonnage);
		}

	}
	
	public void deplacementJoueurDroite(String personnage) {
		// Impacter le monde de ce changement
		monde.deplacementJoueurDroite(personnage);
	}
	public void deplacementJoueurGauche(String personnage) {
		// Impacter le monde de ce changement
		monde.deplacementJoueurGauche(personnage);
	}
	
	public void testReseau() {
		Log.v(TAG_NOYAU, "TestReseau n'est plus valide !");
	}
	

	
	
}
