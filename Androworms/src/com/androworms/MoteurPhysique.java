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
	
	public MoteurPhysique(Noyau n, Monde monde) {
		this.noyau = n;
		this.monde = monde;
	}
	

	public void deplacementJoueurDroite(String personnage) {
		Personnage p = monde.getPersonnage(personnage);
		p.deplacementDroite();	
		while(collision((int)p.getPosition().x, p.getHeightImageTerrain()+(int)p.getPosition().y)) {
			p.setPosition(p.getPosition().x, p.getPosition().y-1);			
		}
		gravite(p);
	}

	public void deplacementJoueurGauche(String personnage) {
		Personnage p = monde.getPersonnage(personnage);
		p.deplacementGauche();
		while(collision(p.getPosition())) {
			p.setPosition(p.getPosition().x, p.getPosition().y-1);			
		}	
		gravite(p);
	}
	
	// Cette fonction verifie que la gravite est respectee.
	public void gravite() {
		for(int i = 0; i < monde.nombrePersonnage(); i++) {
			Personnage p = monde.getListePersonnage().get(i);
			while(personnageVolant(p))
			{
				p.setPosition(p.getPosition().x, p.getPosition().y+1);
				noyau.actualiserGraphisme();
			}
		}
	}
	// Cette fonction verifie que toutes les regles de la physique implementees
	// sont respectees.
	public void gravite(Personnage p) {
		while(personnageVolant(p))
		{
			p.setPosition(p.getPosition().x, p.getPosition().y+1);
			noyau.actualiserGraphisme();
		}
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