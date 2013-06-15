package com.androworms;

import android.util.Log;

public class ConnexionLocale extends Connexion {
	
	public ConnexionLocale(Noyau n) {
		super(n);
	}
	
	public void deplacementJoueurDroite(String nomPersonnage) {
		getNoyau().deplacementJoueurDroite(nomPersonnage);
	}

	public void deplacementJoueurGauche(String nomPersonnage) {
		getNoyau().deplacementJoueurGauche(nomPersonnage);
	} 
	
	public void deplacementJoueurSaut(String nomPersonnage) {
		
	}

	@Override
	public void finDuTourJoueur() {
		chronoStop();
		getNoyau().prochainJoueur();
		String nomJoueurCourant = getNoyau().getMonde().getPersonnagePrincipal().getNom();
		chronoInit(nomJoueurCourant);
		chronoReprise();
	}

	@Override
	public void tempsEcoule() {
		finDuTourJoueur();
	}

	@Override
	public void effectuerTir(Vector2D vd) {
		chronoStop();
		getNoyau().effectuerTir(vd);
	}

}