package com.androworms;

/*
 * Cette classe génère la physique du jeu.
 * C'est a dire les chutes, projections --> les trajectoires.
 */
public class MoteurPhysique {
	
	private Noyau noyau;
	private Monde monde;
	
	public MoteurPhysique(Noyau n, Monde monde) {
		this.noyau = n;
		this.monde = monde;
	}
	

	public void deplacementJoueurDroite(String personnage) {
		Personnage p = monde.getPersonnage(personnage);
		p.deplacementDroite();		
	}

	public void deplacementJoueurGauche(String personnage) {
		Personnage p = monde.getPersonnage(personnage);
		p.deplacementGauche();
		
	}	
	
	public void sautJoueurDroite() {
		
	}
	
	public void sautJoueurGauche() {
		
	}
	
	
}