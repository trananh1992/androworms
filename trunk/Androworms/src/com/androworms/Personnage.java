package com.androworms;

import android.graphics.Bitmap;
import android.graphics.PointF;

/** Cette classe contient toutes les informations sur le personnage utilisé par un joueur.
 */
public class Personnage extends ElementSurCarte implements Cloneable {
	
	/*
	 * Propriete de personnage. Image du personnage couleur chapeau accessoire
	 */

	private String nom;


	public Personnage(String nom, ImageInformation ii) {
		super(new PointF(), ii);
		this.nom = nom;
	}

	public Personnage clone() {
		Personnage result = new Personnage(getNom(), getImageInformation());
		result.setPosition(getPosition().x, getPosition().y);
		return result;
	}


	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public void deplacementDroite(int pas, int max) {
		if (getPosition().x + pas > max) {
			setPosition(max, getPosition().y);
		} else {
			setPosition(getPosition().x + pas, getPosition().y);
		}
	}
	
	public void deplacementGauche(int pas, int min) {
		if (getPosition().x - pas < min) {
			setPosition(min, getPosition().y);
		} else {
			setPosition(getPosition().x - pas, getPosition().y);
		}
	}

	public Bitmap getImageView() {
		return super.getImageView();
	}
}