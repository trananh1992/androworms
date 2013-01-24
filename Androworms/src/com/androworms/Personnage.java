package com.androworms;

import java.util.ArrayList;
import java.util.List;

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
	

	private String nom;


	
	public Personnage(String nom, ImageInformation ii) {
		super(new PointF(), ii);
		this.nom = nom;
	}
	
	public Personnage clone() {
		Personnage result= new Personnage(getNom(), getImageInformation());
		result.setPosition(getPosition().x, getPosition().y);
		return result;
	}

	
	
	public List<PointF> getEnveloppeConvexeHaut() {
		List<PointF> result = new ArrayList<PointF>();
		result.add(getPosition());
		result.add(new PointF(getPosition().x+getWidthImageTerrain(), getPosition().y));
		return result;
	}
	
	public List<PointF> getEnveloppeConvexeBas() {
		List<PointF> result = new ArrayList<PointF>();
		result.add(new PointF(getPosition().x+getWidthImageTerrain(), getPosition().y+getHeightImageTerrain()));
		result.add(new PointF(getPosition().x, getPosition().y+getHeightImageTerrain()));
		return result;
	}
	
	public List<PointF> getEnveloppeConvexeDroite() {
		List<PointF> result = new ArrayList<PointF>();
		result.add(new PointF(getPosition().x+getWidthImageTerrain(), getPosition().y));
		result.add(new PointF(getPosition().x+getWidthImageTerrain(), getPosition().y+getHeightImageTerrain()));
		return result;
	}
	
	public List<PointF> getEnveloppeConvexeGauche() {
		List<PointF> result = new ArrayList<PointF>();
		result.add(getPosition());
		result.add(new PointF(getPosition().x, getPosition().y+getHeightImageTerrain()));
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
		if( getPosition().x + pas > max ) {
			setPosition(max, getPosition().y);
		}
		else {
			setPosition(getPosition().x + pas, getPosition().y);
		}
	}

	public void deplacementGauche(int pas, int min) {
		if(getPosition().x - pas < min) {
			setPosition(min, getPosition().y);
		}
		else {
			setPosition(getPosition().x - pas, getPosition().y);
		}

	}
	
	
	// Test si le personnage courant est en dessous du personnage p.
	public boolean estEnDessous(Personnage p) {
		List<PointF> reference = getEnveloppeConvexeHaut();
		List<PointF> comparateur = p.getEnveloppeConvexeBas();
		for(int i = 0; i < reference.size(); i++) {
			for(int j= 0; j < comparateur.size(); j++) {
				if(reference.get(i).y < comparateur.get(i).y) {
					return false;
				}
			}
		}
		return true;
	}
	
	// Test si le personnage courant est en dessus du personnage p.
	public boolean estAuDessus(Personnage p) {
		List<PointF> reference = getEnveloppeConvexeBas();
		List<PointF> comparateur = p.getEnveloppeConvexeHaut();
		for(int i = 0; i < reference.size(); i++) {
			for(int j= 0; j < comparateur.size(); j++) {
				if(reference.get(i).y > comparateur.get(i).y) {
					return false;
				}
			}
		}
		return true;
	}
	
	//Test si le personnage courant est a gauche du personnage p.
	public boolean estAGauche(Personnage p) {
		List<PointF> reference = getEnveloppeConvexeDroite();
		List<PointF> comparateur = p.getEnveloppeConvexeGauche();
		for(int i = 0; i < reference.size(); i++) {
			for(int j= 0; j < comparateur.size(); j++) {
				if(reference.get(i).x > comparateur.get(i).x) {
					return false;
				}
			}
		}
		return true;
	}
	//Test si le personnage courant est a droite du personnage p.
	public boolean estADroite(Personnage p) {
		List<PointF> reference = getEnveloppeConvexeGauche();
		List<PointF> comparateur = p.getEnveloppeConvexeDroite();
		for(int i = 0; i < reference.size(); i++) {
			for(int j= 0; j < comparateur.size(); j++) {
				if(reference.get(i).x < comparateur.get(i).x) {
					return false;
				}
			}
		}
		return true;
	}
	
	// Test si le personnage est en collision avec un autre.
	public boolean estEnCollision(Personnage p) {
		return !(estEnDessous(p) || estAuDessus(p) || estADroite(p) || estAGauche(p));
	}
}
