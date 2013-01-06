package com.androworms;

/*
 * Cette classe génère la physique du jeu.
 * C'est a dire les chutes, projections --> les trajectoires.
 */
public class MoteurPhysique {
	
	private Noyau noyau;
	
	public MoteurPhysique(Noyau n) {
		this.noyau = n;
	}
	
	public MoteurPhysique(MoteurGraphique mg, Noyau noyau) {
		super();
		this.noyau = noyau;
	}	
}