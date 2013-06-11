package com.androworms;

public abstract class Connexion {

	private Noyau noyau;
	private RunnableTemps chrono;
	
	public Connexion(Noyau c) {
		noyau = c;
		chrono = new RunnableTemps(this);
		chrono.sleep();
	}
	
	public Noyau getNoyau() {
		return noyau;
	}
	
	public void setNoyau(Noyau noyau) {
		this.noyau = noyau;
	}
	
	public void chronoInit() {
		chrono.re_initialise();
	}

	public void chronoReprise() {
		chrono.reprise();
	}
	
	public void chronoStop() {
		chrono.stop();
	}
	
	public abstract void deplacementJoueurDroite(String nomPersonnage);
	public abstract void deplacementJoueurGauche(String nomPersonnage);
	public abstract void deplacementJoueurSaut(String nomPersonnage);
	public abstract void finDuTourJoueur();
	public abstract void tempsEcoule();
	
}