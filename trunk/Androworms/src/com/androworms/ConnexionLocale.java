package com.androworms;

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
}