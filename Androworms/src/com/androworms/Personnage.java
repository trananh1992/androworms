package com.androworms;

import android.graphics.Point;



/*
 * Cette classe contient toutes les informations
 * sur le personnage utiliser par un joueur.
 */
public class Personnage {
	/*
	 * Propriete de personnage.
	 * Image du personnage
	 * couleur 
	 * chapeau 
	 * accessoire
	 */
	private String nom;
	private Point position;

	
	public Personnage(String nom) {
		this.nom = nom;
		position = new Point();
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public Point getPosition() {
		return position;
	}

	public void setPosition(Point position) {
		this.position = position;
	}
}
