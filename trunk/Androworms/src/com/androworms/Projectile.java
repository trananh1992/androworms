package com.androworms;

public class Projectile  {
	// Variable global pour connaitre le type de projectile.
	//missile, grenade
	public static final short EXPLOSIF  = 1;
	//pistolet, mitraillette
	public static final short PERFORANT = 2; 

	
	// Propriete 
	private int degat;
	private int typeExplosion;
	// Rayon d'action pour la degradation de la carte.
	private int rayon;
		
	public int getDegat() {
		return degat;
	}
	public void setDegat(int degat) {
		this.degat = degat;
	}
	public int getTypeExplosion() {
		return typeExplosion;
	}
	public void setTypeExplosion(int typeExplosion) {
		this.typeExplosion = typeExplosion;
	}
	
	public int getRayon() {
		return rayon;
	}
	public void setRayon(int rayon) {
		this.rayon = rayon;
	}
	public void estExplosif() {
		typeExplosion = EXPLOSIF;
	}
	
	public void estPerforant() {
		typeExplosion = PERFORANT;
	}
	

}
