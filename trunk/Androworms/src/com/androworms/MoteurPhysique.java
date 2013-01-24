package com.androworms;

import android.graphics.Color;
import android.graphics.PointF;

/*
 * Cette classe génère la physique du jeu.
 * C'est a dire les chutes, projections --> les trajectoires.
 */
public class MoteurPhysique {
	
	private Noyau noyau;
	private Monde monde;
	public static final int TAILLE_DEPLACEMENT_JOUEUR = 1;
	public static final int HAUTEUR_DEPLACEMENT_JOUEUR = 20;
	
	public MoteurPhysique(Noyau n, Monde monde) {
		this.noyau = n;
		this.monde = monde;
	}
	
	private float max(float a, float b) {
		if( a > b ) {
			return a;
		}
		else {
			return b;
		}
	}
	
	private float min(float a, float b) {
		if( a < b ) {
			return a;
		}
		else {
			return b;
		}
	}

	public void deplacementJoueurDroite(String personnage) {
		deplacementJoueur(personnage, TAILLE_DEPLACEMENT_JOUEUR);
	}
	
	public void deplacementJoueurGauche(String personnage) {
		deplacementJoueur(personnage, -TAILLE_DEPLACEMENT_JOUEUR);
	}
	
	public void deplacementJoueur(String personnage, int addToX) {
		Personnage pOld = monde.getPersonnage(personnage);
		Personnage pNew = pOld.clone();
		pNew.setPosition(pNew.getPosition().x + addToX, pNew.getPosition().y);
		ajusterY(pNew);
		pNew = validationDeplacement(pOld, pNew);
		pOld.setPosition(new PointF(pNew.getPosition().x, pNew.getPosition().y));
		gravite(pOld);
		noyau.actualiserGraphisme();
	}
	
	private Personnage validationDeplacement(Personnage pOld, Personnage pNew) {
		float decalage = pOld.getPosition().y - pNew.getPosition().y;
		if( decalage < HAUTEUR_DEPLACEMENT_JOUEUR && !estEnCollision(pNew)) {
			return pNew;
		}
		return pOld;
	}
	
	private boolean estEnCollision(Personnage personnage) {
		for(int i = 0; i < monde.nombrePersonnage(); i++) {
			Personnage p = monde.getListePersonnage().get(i);
			if( p.getNom().compareTo(personnage.getNom()) != 0 && personnage.estEnCollision(p)) {
				return true;
			}
		}
		return false;
	}
	
	private void ajusterY( Personnage p) {
		int decalage = decalagePositionPersonnage((int)p.getPosition().x,(int)(p.getHeightImageTerrain()+p.getPosition().y));
		p.setPosition(p.getPosition().x, p.getPosition().y - decalage);
	}
	
	// Cette fonction retoune la position optimal de y,
	private int decalagePositionPersonnage( int x, int y ) {
		int decalage =0;
		while(y >= 0 && collision(x, y)) {
			y--;
			decalage ++;
		}	
		return decalage;
	}


	
	// Cette fonction verifie que la gravite est respectee.
	public void gravite() {
		for(int i = 0; i < monde.nombrePersonnage(); i++) {
			Personnage p = monde.getListePersonnage().get(i);
			while(personnageVolant(p)) {
				p.setPosition(p.getPosition().x, p.getPosition().y+1);

			}
		}
		noyau.actualiserGraphisme();
	}
	// Cette fonction verifie que toutes les regles de la physique implementees
	// sont respectees.
	public void gravite(Personnage p) {
		while(personnageVolant(p)) {
			p.setPosition(p.getPosition().x, p.getPosition().y+1);
		}
		noyau.actualiserGraphisme();
	}		

	// On teste si le personnage n'a rien sous les pieds.
	// Renvoie vrai si le personnage vole et faux si non.
	// TODO : la fonction ne prends qu'un point de reference
	public boolean personnageVolant(Personnage p) {
		return (!collision((int)p.getPosition().x, p.getHeightImageTerrain()+(int)p.getPosition().y)
				&& monde.getTerrain().getHeight() - (p.getHeightImageTerrain()+(int)p.getPosition().y) > 1);
	}
	
	public boolean collision(int x, int y) {
		return Color.alpha(monde.getTerrain().getPixel(x, y)) > 0;	
	}
	
	
	public boolean collision(PointF p) {
		return collision((int)p.x, (int)p.y);
	}
	
	public void sautJoueurDroite() {
		
	}
	
	public void sautJoueurGauche() {
		
	}
	
	public boolean sortieDuTerrain(Personnage p) {
		return false;
	}
	
	
}