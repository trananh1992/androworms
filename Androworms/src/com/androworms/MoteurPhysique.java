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
	private final float rafraichissement = 0.2f;
	
	
	public MoteurPhysique(Noyau n, Monde monde) {
		this.noyau = n;
		this.monde = monde;
	}
	
	public float getRafraichissement() {
		return rafraichissement;
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
	;
	public void deplacementJoueur(Personnage pOld, int addToX, List<PointF> path) {
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
	public void gravite(Personnage p) {
		Bitmap carte = monde.getPremierPlan();
		Personnage pNew = p.clone();
		float i = rafraichissement;
		while( applyAcceleration(pNew, i, carte) && dessinPossible (pNew, carte) ) {
			p.addMouvementForces(new PointF(pNew.getPosition().x, pNew.getPosition().y));
			i += rafraichissement;
			pNew.setPosition(p.getPosition().x, p.getPosition().y);
		}
		
	}		
	
	/**
	 * Applique les accelerations sur un element sur carte pour un temps donnee.
	 * @param esc l'element sur carte à modifer
	 * @param temps le temps depuis lequel l'objet subit l'acceleration.
	 * @param carte la carte pour le teste d l'element sur carte.
	 * @return true si l'element a bouge, false si l'element est reste a la meme place.
	 */
	public boolean applyAcceleration(ElementSurCarte esc, float temps, Bitmap carte) {
		List<Vector2D> acceleration = monde.getAcceleration();
		for(int i = 0; i < acceleration.size(); i++) {
			esc.setPosition(esc.getPosition().x + acceleration.get(i).x * Math.pow(temps,2)
					, esc.getPosition().y + acceleration.get(i).y * Math.pow(temps,2) );
		}
		if(dessinPossible(esc, carte)) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean dessinPossible(ElementSurCarte p, Bitmap carte) {
		return dessinPossible(p.getImageView(), (int)p.getPosition().x, (int) p.getPosition().y,
				carte);
	}
	
	public boolean dessinPossible(Personnage p) {
		return dessinPossible(p, monde.getTerrainSansPersonnageCible(p.getNom()));
	}
	
	public boolean dessinPossible(Bitmap petiteImage,  int Ox, int Oy, Bitmap grandeImage) {
		if(!estDansTerrain(petiteImage, Ox, Oy, grandeImage)) {
			return false;
		}
		for(int i =0; i < petiteImage.getWidth(); i++) {
			for(int j=0; j < petiteImage.getHeight(); j++) {
				if( !estTransparent(petiteImage, i, j)
						&& !estTransparent(grandeImage, i+Ox, j+Oy)) {
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
	}
	
	public void sautJoueurGauche(String nomPersonnage) {
		sautJoueurDroite(nomPersonnage);
	}
	
	public boolean estDansTerrain(Bitmap petiteImage, int Ox, int Oy, Bitmap grandeImage) {
		return  0 <= Ox 
				&& (Ox+petiteImage.getWidth()) < grandeImage.getWidth()
				&& 0 <= Oy 
				&& (Oy + petiteImage.getHeight()) < grandeImage.getHeight();
	}
}