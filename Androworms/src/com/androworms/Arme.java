package com.androworms;

import android.graphics.Point;

public class Arme extends Objet 
{
	private static final int ARME_WIDTH = 40;
	private static final int ARME_HEIGHT = 50;
	
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

	@Override
	public int getIdImage() {
		//TODO mettre bonne image
		return R.drawable.hache;
	}

	@Override
	public Point getTailleImage() {
		// TODO 
		return new Point(ARME_WIDTH, ARME_HEIGHT);
	}

}
