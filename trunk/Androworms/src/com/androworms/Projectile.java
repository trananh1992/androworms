package com.androworms;

public class Projectile 
{
	// Variable global pour connaitre le type de projectile.
	public static final short EXPLOSIF  = 1; //missile, grenade
	public static final short PERFORANT = 2; //pistolet, mitraillette
	
	// Propriete 
	private int degat;
	private int typeExplosion;
		
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
	
	public void estExplosif()
	{
		typeExplosion = EXPLOSIF;
	}
	
	public void estPerforant()
	{
		typeExplosion = PERFORANT;
	}
	

}
