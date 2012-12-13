package com.androworms;

public class Munition {
	
	private Projectile projectile;
	private int nombre;
	
	public Munition(Projectile p)
	{
		this.projectile = p;
		nombre = 0;
	}
	
	public Projectile getProjectile() {
		return projectile;
	}
	public void setProjectile(Projectile projectile) {
		this.projectile = projectile;
	}
	public int getNombre() {
		return nombre;
	}
	public void setNombre(int nombre) {
		this.nombre = nombre;
	}
	
	public boolean aEncoreProjectile()
	{
		return (nombre != 0);
	}
	
	public Projectile prendreUnProjectile()
	{
		if(nombre > 0)
		{	
			nombre--;	
		}
		return projectile;
	}
	
	public void ajouter(int valeur)
	{
		if( nombre >= 0)
		{	
			nombre += valeur;	
		}
	}
	
	public void projectileInfini()
	{
		nombre = -1;
	}
	

}
