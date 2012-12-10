package com.androworms;

/*
 * Cette classe genere la physique du jeu.
 * C'est a dire les chutes, projections --> les trajectoires.
 */
public class MoteurPhysique
{
	private MoteurGraphique mg;
	private Noyau noyau;
	
	public MoteurPhysique(Noyau n)
	{
		this.noyau = n;
		this.mg = n.getMg();		
	}
	
	public MoteurPhysique(MoteurGraphique mg, Noyau noyau) {
		super();
		this.mg = mg;
		this.noyau = noyau;
	}
	
	

}
