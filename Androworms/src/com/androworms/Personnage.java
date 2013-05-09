package com.androworms;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.PointF;

/** Cette classe contient toutes les informations sur le personnage utilisé par un joueur.
 */
public class Personnage extends ElementSurCarte implements Cloneable {
	
	/*
	 * Propriete de personnage. Image du personnage couleur chapeau accessoire
	 */

	private String nom;

	private List<PointF> mouvementGauche;
	private List<PointF> mouvementDroite;
	private List<PointF> mouvementForces;


	public Personnage(String nom, ImageInformation ii) {
		super(new PointF(), ii);
		this.nom = nom;
		mouvementGauche = new ArrayList<PointF>();
		mouvementDroite = new ArrayList<PointF>();
		mouvementForces = new ArrayList<PointF>();
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

	public static int getIdImage() {
		return R.drawable.test_android_face;
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
	
	public void addMouvementForces(PointF p) {
		mouvementForces.add(p);
	}

	public List<PointF> getMouvementGauche() {
		return mouvementGauche;
	}

	public void setMouvementGauche(List<PointF> mouvementGauche) {
		this.mouvementGauche = mouvementGauche;
	}

	public List<PointF> getMouvementDroite() {
		return mouvementDroite;
	}

	public void setMouvementDroite(List<PointF> mouvementDroite) {
		this.mouvementDroite = mouvementDroite;
	}

	public List<PointF> getMouvementForces() {
		return mouvementForces;
	}

	public void setMouvementForces(List<PointF> mouvementForces) {
		this.mouvementForces = mouvementForces;
	}
	
	
}

