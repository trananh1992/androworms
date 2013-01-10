package com.androworms;

import android.graphics.PointF;

/*
 * Cette classe contient toutes les informations
 * sur le personnage utiliser par un joueur.
 */
public class Personnage extends ElementSurCarte {
	/*
	 * Propriete de personnage.
	 * Image du personnage
	 * couleur 
	 * chapeau 
	 * accessoire
	 */
	
	public static final int TAILLE_DEPLACEMENT_JOUEUR = 10;
	private String nom;

	
	public Personnage(String nom, ImageInformation ii) {
		super(new PointF(), ii);
		this.nom = nom;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}
	
	public static int getIdImage() {
		return R.drawable.android_face;
	}

	public void deplacementDroite() {
		setPosition(getPosition().x + TAILLE_DEPLACEMENT_JOUEUR, getPosition().y);
	}

	public void deplacementGauche() {
		setPosition(getPosition().x - TAILLE_DEPLACEMENT_JOUEUR, getPosition().y);
	}
}
