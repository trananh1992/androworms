package com.androworms;

import android.graphics.Point;

public abstract class Objet  {
	private String nom;

	public Objet(String nom) {
		super();
		this.nom = nom;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}
	
	public abstract int getIdImage();
	
	public abstract Point getTailleImage();
}
