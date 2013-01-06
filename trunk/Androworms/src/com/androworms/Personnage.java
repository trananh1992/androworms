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

	public static final int JOUEUR_WIDTH = 180;
	public static final int JOUEUR_HEIGHT = 173;
	
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
	
	public static int getIdImage() {
		return R.drawable.android_face;
	}

	public void deplacementDroite(int y) {
		position.set(position.x+1, y);
	}

	public void deplacementGauche(int y) {
		position.set(position.x-1, y);
		
	}
}
