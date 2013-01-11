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
	public static final int TAILLE_DEPLACEMENT_JOUEUR = 2;
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
		Personnage p = monde.getPersonnage(personnage);
		// On peut aller tout a droite mais faut garder de la place pour que le joueur reste dans l'image
		float x = min(p.getPosition().x+TAILLE_DEPLACEMENT_JOUEUR, monde.getTerrain().getWidth()-p.getWidthImageTerrain());
		affecterDeplacementJoueur(p, x, p.getPosition().y);
		gravite(p);
		noyau.actualiserGraphisme();
	}
	
	private void affecterDeplacementJoueur(Personnage p, float x, float y) {
		int decalage = decalagePositionPersonnage( (int)x, (int)(p.getHeightImageTerrain()+y));
		if( decalage < HAUTEUR_DEPLACEMENT_JOUEUR ) {
			p.setPosition(x, y-decalage);
		}
	}
	
	// Cette fonction retoune la position optimal de y,
	private int decalagePositionPersonnage( int x, int y) {
		int decalage =0;
		while(y >= 0 && collision(x, y)) {
			y--;
			decalage ++;
		}	
		return decalage;
	}

	public void deplacementJoueurGauche(String personnage) {
		Personnage p = monde.getPersonnage(personnage);
		// Minimum 0 car il ne fait pas que le personnage sorte de l'image.
		float x = max(p.getPosition().x-TAILLE_DEPLACEMENT_JOUEUR, 0);
		affecterDeplacementJoueur(p, x, p.getPosition().y);
		gravite(p);
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
		return (!collision((int)p.getPosition().x, p.getHeightImageTerrain()+(int)p.getPosition().y));
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
	
	
}