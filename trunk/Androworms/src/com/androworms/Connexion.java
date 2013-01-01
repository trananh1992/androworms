package com.androworms;



public abstract class Connexion {

	private Noyau noyau;
	
	public Connexion(Noyau c) {
		noyau = c;
	}
	
	
	
	public Noyau getNoyau() {
		return noyau;
	}



	public void setNoyau(Noyau noyau) {
		this.noyau = noyau;
	}



	public abstract void deplacementJoueurDroite(String nomPersonnage);
	public abstract void deplacementJoueurGauche(String nomPersonnage);
	public abstract void deplacementJoueurSaut(String nomPersonnage);
	

	

}
