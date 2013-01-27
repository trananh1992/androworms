package com.androworms;

import java.util.List;

import android.graphics.Color;
import android.graphics.PointF;

/**
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
	
	private float abs(float a) {
		if(a < 0) {
			return -a;
		} else {
			return a;
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
		for(int i = 0; i < abs(addToX); i++) {
			pNew.setPosition(pNew.getPosition().x + (addToX/abs(addToX)), pNew.getPosition().y);
			pOld.setPosition(new PointF(pNew.getPosition().x, pNew.getPosition().y));
			gravite(pOld);
		}
		noyau.actualiserGraphisme();
	}
	
	private boolean estEnCollision(Personnage personnage) {
		boolean result = false;
		for(int i = 0; i < monde.nombrePersonnage() && !result; i++) {
			Personnage p = monde.getListePersonnage().get(i);
			if( p.getNom().compareTo(personnage.getNom()) != 0 && personnage.estEnCollision(p)) {
				result = true;
			}
		}
		if( !result ) {
			List<PointF> enveloppeConvexe = personnage.getEnveloppeConvexe();
			for(int i = 0; i < enveloppeConvexe.size() && !result; i++) {
				result = result || collision(enveloppeConvexe.get(i)) ;				
			}
		}
		return result;
	}
	
	private void ajusterY( Personnage p) {
		int decalage = decalagePositionPersonnage((int)p.getPosition().x,(int)(p.getHeightImageTerrain()+p.getPosition().y));
		p.setPosition(p.getPosition().x, p.getPosition().y - decalage);
	}
	
	/** Cette fonction retoune la position optimal de y. */
	private int decalagePositionPersonnage( int x, int y ) {
		int decalage =0;
		while(y >= 0 && collision(x, y)) {
			y--;
			decalage ++;
		}	
		return decalage;
	}

	
	/** Cette fonction verifie que la gravite est respectee. */
	public void gravite() {
		for(int i = 0; i < monde.nombrePersonnage(); i++) {
			Personnage p = monde.getListePersonnage().get(i);
			while(personnageVolant(p)) {
				p.addMouvementForces(p.getPosition());
				p.setPosition(p.getPosition().x, p.getPosition().y+1);
			}
		}
		noyau.mouvementForces();
		//noyau.actualiserGraphisme();
	}
	
	/** Cette fonction verifie que toutes les regles de la physique implementees sont respectees. */

	public void gravite(Personnage p) {
		while(personnageVolant(p)) {
			p.setPosition(p.getPosition().x, p.getPosition().y+1);
		}
		noyau.actualiserGraphisme();
	}		
	
	/** On teste si le personnage n'a rien sous les pieds.
	    Renvoie vrai si le personnage vole et faux si non. */
	public boolean personnageVolant(Personnage p) {
		return (!collision((int)p.getPosition().x, p.getHeightImageTerrain()+(int)p.getPosition().y))
				//&& estDansTerrain(p))
				;
}
	
	public boolean collision(int x, int y) {
		return Color.alpha(monde.getTerrain().getPixel(x, y)) > 0;
	}
	
	public boolean collision(PointF p) {
		return collision((int)p.x, (int)p.y);
	}
	
	public boolean collision(Personnage p) {
		return collision(p.getPosition());
	}
	
	public void sautJoueurDroite() {
		
	}
	
	public void sautJoueurGauche() {
		
	}
	
	public boolean estDansTerrain(Personnage p) {
		return monde.getTerrain().getHeight() - (p.getHeightImageTerrain()+(int)p.getPosition().y) > 1
				&& monde.getTerrain().getWidth() - (p.getWidthImageTerrain()+(int)p.getPosition().x) > 1
				&& p.getPosition().y >= 0 
				&& p.getPosition().x >= 0
				;
	}
}