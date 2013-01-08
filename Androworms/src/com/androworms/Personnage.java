package com.androworms;

import android.graphics.Point;
import android.graphics.PointF;

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

	public static final int JOUEUR_WIDTH = 162;
	public static final int JOUEUR_HEIGHT = 214;
	
	private String nom;
	private PointF position;
	private int id;
	
	public Personnage(String nom) {
		this.nom = nom;
		position = new PointF();
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public PointF getPosition() {
		return position;
	}

	public void setPosition(PointF position) {
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
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
