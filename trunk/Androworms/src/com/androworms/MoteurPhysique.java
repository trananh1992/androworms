package com.androworms;

import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PointF;
import android.util.Log;

/**
 * Cette classe génère la physique du jeu.
 * C'est a dire les chutes, projections --> les trajectoires.
 */
public class MoteurPhysique {
	
	private Noyau noyau;
	private Monde monde;
	public static final int TAILLE_DEPLACEMENT_JOUEUR = 1;
	public static final int HAUTEUR_DEPLACEMENT_JOUEUR = 5;
	public static final String TAG = "Androworms.MOTEURPHYSIQUE";
	private static final float RAFRAICHISSEMENT = 0.2f;
	
	
	public MoteurPhysique(Noyau n, Monde monde) {
		this.noyau = n;
		this.monde = monde;
	}
	
	public float getRafraichissement() {
		return RAFRAICHISSEMENT;
	}
	
	
	
	public void deplacementJoueurDroite(String personnage) {
		Personnage p = monde.getPersonnage(personnage);
		noyau.animerAndroidDroite(p);
		deplacementJoueur(p, 1);
	}
	
	public void deplacementJoueurGauche(String personnage) {
		Personnage p = monde.getPersonnage(personnage);
		noyau.animerAndroidGauche(p);
		deplacementJoueur(p, -1);
	}
	
	public void deplacementJoueur(Personnage pOld, int addToX) {
		Personnage pNew = pOld.clone();
		Bitmap carte = monde.getTerrainSansPersonnageCible(pOld.getNom());
		for(int i = 0; i < TAILLE_DEPLACEMENT_JOUEUR; i++) {
			float x = pOld.getPosition().x + addToX;
			float y = pOld.getPosition().y;
			for(int j = HAUTEUR_DEPLACEMENT_JOUEUR; j > -HAUTEUR_DEPLACEMENT_JOUEUR; j--) {
				pNew.setPosition(x,	y+j);
				if(dessinPossible(pNew, carte)) {
					pOld.setPosition(new PointF(x,y+j));
					return;
				}
			}
		}
	}
	
	/** Cette fonction verifie que la gravite est respectee. */
	public void gravite() {
		for(int i = 0; i < monde.nombrePersonnage(); i++) {
			Log.v(TAG, "Au tour de " + monde.getPersonnage(i).getNom());
			monde.unsetTerrainSansPersonnageSave();
			gravite(monde.getPersonnage(i));
		}
		monde.unsetTerrainSansPersonnageSave();
	}
	
	//TODO a finir
	/** Cette fonction verifie que toutes les regles de la physique implementees sont respectees. */
	public void gravite(ElementSurCarte p) {
		// TODO monde.getTerrainSansPersonnageCible(p.getNom());
		applyForce(p, monde.getPremierPlan(), new Vector2D(0,0));
		/*
		Bitmap carte = monde.getPremierPlan(); 
		// TODO monde.getTerrainSansPersonnageCible(p.getNom());
		ElementSurCarte pNew = p.clone();
		float i = rafraichissement;
		applyAcceleration(pNew, i);
		while( dessinPossible (pNew, carte) ) {
			p.addMouvementForces(new PointF(pNew.getPosition().x, pNew.getPosition().y));
			i += rafraichissement;
			pNew.setPosition(p.getPosition().x, p.getPosition().y);
			applyAcceleration(pNew, i);
		}
		// TODO a tester
		p.addMouvementForces(pNew.getPosition());
		do {
			pNew.getPosition().y++;
		} while(dessinPossible(pNew, carte));
		pNew.getPosition().y--;
		 */
	}
	
	/**
	 * Applique les accelerations sur un element sur carte pour un temps donnee.
	 * @param esc l'element sur carte à modifer
	 * @param temps le temps depuis lequel l'objet subit l'acceleration.
	 * @param carte la carte pour le teste d l'element sur carte.
	 * @return true si l'element a bouge, false si l'element est reste a la meme place.
	 */
	public void applyAcceleration(ElementSurCarte esc, float temps) {
		List<Vector2D> acceleration = monde.getAcceleration();
		for(int i = 0; i < acceleration.size(); i++) {
			esc.setPosition(esc.getPosition().x + acceleration.get(i).getX() * Math.pow(temps,2)
					, esc.getPosition().y + acceleration.get(i).getY() * Math.pow(temps,2) );
		}
	}
	
	public void applyVecteur(ElementSurCarte esc, float temps, Vector2D vd) {
		esc.getPosition().x += (vd.getX() * temps);
		esc.getPosition().y += (vd.getY() * temps);
	}
	
	public void applyForce(ElementSurCarte esc, Bitmap carte, Vector2D vd) { 
		PointF save = new PointF();
		ElementSurCarte pNew = esc.clone();
		float i = RAFRAICHISSEMENT;
		applyAcceleration(pNew, i);
		applyVecteur(pNew, i, vd);
		while( dessinPossible (pNew, carte) ) {
			esc.addMouvementForces(new PointF(pNew.getPosition().x, pNew.getPosition().y));
			save.set(pNew.getPosition().x, pNew.getPosition().y);
			i += RAFRAICHISSEMENT;
			pNew.setPosition(esc.getPosition().x, esc.getPosition().y);
			applyAcceleration(pNew, i);
			applyVecteur(pNew, i, vd);
		}
		if( save.x + save.y != 0.) {
			Vector2D tmp = Vector2D.vecteurAB(save, pNew.getPosition());
			Log.v(TAG, "tmp = " + tmp.getX() + "  " + tmp.getY());
			pNew.setPosition(save.x, save.y);
			if(tmp.size() > 1) {
				esc.addMouvementForces(dernierePositionSuivantVecteur(pNew, carte, tmp));
			}
		}
	}
	
	public PointF dernierePositionSuivantVecteur(ElementSurCarte esc, Bitmap carte, Vector2D vd) {
		float deplacement = Math.abs(1/Math.max(vd.getX(), vd.getY()));
		PointF result = new PointF();
		do {
			result.set(esc.getPosition().x, esc.getPosition().y);
			applyVecteur(esc, deplacement, vd);
		} while(dessinPossible(esc, carte));
		Log.v(TAG, "ajustement = " + result.x + "  " + result.y);
		return result;
	}
	
	public boolean dessinPossible(ElementSurCarte p, Bitmap carte) {
		return dessinPossible(p.getImageView(), (int)p.getPosition().x, (int) p.getPosition().y,
				carte);
	}
	
	public boolean dessinPossible(Personnage p) {
		return dessinPossible(p, monde.getTerrainSansPersonnageCible(p.getNom()));
	}
	
	public boolean dessinPossible(Bitmap petiteImage,  int ox, int oy, Bitmap grandeImage) {
		if(!estDansTerrain(petiteImage, ox, oy, grandeImage)) {
			return false;
		}
		for(int i =0; i < petiteImage.getWidth(); i++) {
			for(int j=0; j < petiteImage.getHeight(); j++) {
				if( !estTransparent(petiteImage, i, j)
						&& !estTransparent(grandeImage, i+ox, j+oy)) {
					return false;
				}
			}
		}
		return true;
	}
	
	private boolean estTransparent(Bitmap b, int x, int y) {
		return Color.alpha(b.getPixel(x, y)) == Color.TRANSPARENT;
	}
	
	public boolean collision(Bitmap bm, int x, int y) {
		return dessinPossible(bm, x, y, monde.getPremierPlan());
	}
	
	public boolean collision(Personnage p) {
		return collision(p.getImageView(), (int)p.getPosition().x, (int)p.getPosition().y);
	}
	
	public void sautJoueurDroite(String nomPersonnage) {
		applyForce(monde.getPersonnage(nomPersonnage), 
				monde.getTerrainSansPersonnageCible(nomPersonnage), 
				new Vector2D(50, -50));
	}
	
	public void sautJoueurGauche(String nomPersonnage) {
		applyForce(monde.getPersonnage(nomPersonnage), 
				monde.getTerrainSansPersonnageCible(nomPersonnage), 
				new Vector2D(-50, -50));
	}
	
	public boolean estDansTerrain(Bitmap petiteImage, int ox, int oy, Bitmap grandeImage) {
		return  0 <= ox 
				&& (ox+petiteImage.getWidth()) < grandeImage.getWidth()
				&& 0 <= oy 
				&& (oy + petiteImage.getHeight()) < grandeImage.getHeight();
	}
}