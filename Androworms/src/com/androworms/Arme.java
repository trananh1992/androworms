package com.androworms;

public class Arme extends Objet 
{
	private int portee;
	private Munition munition;
	
	public Arme (String nom, Munition m, int p)
	{
		super(nom);
		portee = p;
		munition = m;
	}
	
	
	public int getPortee() {
		return portee;
	}
	public void setPortee(int portee) {
		this.portee = portee;
	}
	public Munition getMunition() {
		return munition;
	}
	public void setMunition(Munition munition) {
		this.munition = munition;
	}

	
	
	

}
