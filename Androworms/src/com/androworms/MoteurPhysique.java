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
	public static final int HAUTEUR_DEPLACEMENT_JOUEUR = 5;
	
	public MoteurPhysique(Noyau n, Monde monde) {
		this.noyau = n;
		this.monde = monde;
	}
	
	public void deplacementJoueurDroite(String personnage) {
		Personnage p = monde.getPersonnage(personnage);
		noyau.animerAndroidDroite(p);
		deplacementJoueur(p, 1, p.getMouvementDroite());
	}
	
	public void deplacementJoueurGauche(String personnage) {
		Personnage p = monde.getPersonnage(personnage);
		noyau.animerAndroidGauche(p);
		deplacementJoueur(p, -1, p.getMouvementGauche());
	}
	
	public void deplacementJoueur(Personnage pOld, int addToX, List<PointF> path) {
		Personnage pNew = pOld.clone();
		for(int i = 0; i < TAILLE_DEPLACEMENT_JOUEUR; i++) {
			pNew.setPosition(pNew.getPosition().x + addToX, 
					pNew.getPosition().y-HAUTEUR_DEPLACEMENT_JOUEUR);
			if( !estEnCollision(pNew) && estDansTerrain(pNew)) {
				//pOld.setPosition(new PointF(pNew.getPosition().x, pNew.getPosition().y));
				gravite(pNew);
				//path.add(pNew.getPosition());
			}
		}
		pOld.setPosition(pNew.getPosition());
		//noyau.actualiserGraphisme()
		//noyau.mouvementForces();
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
	
	/** Cette fonction verifie que la gravite est respectee. */
	public void gravite() {
		for(int i = 0; i < monde.nombrePersonnage(); i++) {
			Personnage p = monde.getListePersonnage().get(i);
			Personnage pNew = p.clone();
			while(personnageVolant(pNew)) {
				p.addMouvementForces(new PointF(pNew.getPosition().x, pNew.getPosition().y));
				pNew.setPosition(pNew.getPosition().x, pNew.getPosition().y+1);
			}
		}
		noyau.mouvementForces();
		//noyau.actualiserGraphisme();
	}
	
	/** Cette fonction verifie que toutes les regles de la physique implementees sont respectees. */
	public void gravite(Personnage p) {
		//Personnage pNew = p.clone();
		while(personnageVolant(p)) {
			//p.addMouvementForces(new PointF(pNew.getPosition().x, pNew.getPosition().y));
			p.setPosition(p.getPosition().x, p.getPosition().y+1);
		}
		//noyau.actualiserGraphisme();
		//noyau.mouvementForces();

	}		
	
	/** On teste si le personnage n'a rien sous les pieds.
	    Renvoie vrai si le personnage vole et faux si non. */
	public boolean personnageVolant(Personnage p) {
		List<PointF> tmp = p.getEnveloppeConvexeBas();
		for(int i =0; i < tmp.size(); i ++) {
			if(collision(tmp.get(i))) {
				return false;
			}
		}
		return true;
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
	
	public void sautJoueurDroite(String nomPersonnage) {
		Personnage personnage = monde.getPersonnage(nomPersonnage);
		Personnage p = personnage.clone();
		for(int i = 0; i < 30 && !estDansTerrain(p); i++) {
			p.setPosition(p.getPosition().x +1, p.getPosition().y+1);
			if(!estDansTerrain(p)) {
				personnage.addMouvementForces(p.getPosition());
			}
		}
		gravite(p);
	}
	
	public void sautJoueurGauche(String nomPersonnage) {
		sautJoueurDroite(nomPersonnage);
	}
	
	public boolean estDansTerrain(Personnage p) {
		return monde.getTerrain().getHeight() - (p.getHeightImageTerrain()+(int)p.getPosition().y) > 1
				&& monde.getTerrain().getWidth() - (p.getWidthImageTerrain()+(int)p.getPosition().x) > 1
				&& p.getPosition().y >= 1 
				&& p.getPosition().x >= 1;
	}
}