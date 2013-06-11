package com.androworms;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PointF;
import android.util.Log;

public class Monde {
	
	private static final String TAG = "Androworms.Monde";
	
	private TerrainMonde tm;
	private MoteurGraphique mg;
	private Personnage personnageEnTrainDeJouer;
	private List<Personnage> listePersonnage;
	private List<ObjetSurCarte> listeObjetCarte;
	private List<Objet> tousLesObjets;
	private List<Vector2D> acceleration;
	
	private Bitmap terrainSansPersonnageSave;
	
	public Monde() {
		super();
		tousLesObjets = new ArrayList<Objet>();
		listePersonnage = new ArrayList<Personnage>();
		listeObjetCarte = new ArrayList<ObjetSurCarte>();
		tm = new TerrainMonde();
		acceleration = new ArrayList<Vector2D>();
		acceleration.add(new Vector2D(0, 10));
	}
	
	

	public List<Vector2D> getAcceleration() {
		return acceleration;
	}



	public void setAcceleration(List<Vector2D> acceleration) {
		this.acceleration = acceleration;
	}



	public void setTerrain(Bitmap b, int width, int height){
		tm.setArrierePlan(Bitmap.createScaledBitmap(b, width, height, true));
	}
	
	public void setPremierPlan(Bitmap b) {
		tm.setPremierPlan(b);
	}
	
	public void setArrierePlan(Bitmap b) {
		tm.setArrierePlan(b);
	}
	
	public Bitmap getTerrain() {
		return tm.getTerrain();
	}
	
	public Bitmap getPremierPlan() {
		return tm.getPremierPlan();
	}
	
	public void unsetTerrainSansPersonnageSave() {
		terrainSansPersonnageSave = null;
	}
	
	public Bitmap getTerrainSansPersonnageCible(String nomPer) {
		if( terrainSansPersonnageSave != null ) {
			return terrainSansPersonnageSave;
		} else {
			Bitmap terrain = getPremierPlan().copy(getPremierPlan().getConfig(), true);
			for(int i = 0; i < listePersonnage.size(); i++) {
				Personnage p = listePersonnage.get(i);
				if( p.getNom().compareTo(nomPer) != 0 ) {
					tm.dessinPersonnageSurCarte(p.getImageView(), p.getPosition(), terrain);
				}
			}
			terrainSansPersonnageSave = terrain;
			return terrain;
		}
	}

	
	public MoteurGraphique getMg() {
		return mg;
	}

	public void setMg(MoteurGraphique mg) {
		this.mg = mg;
	}

	public List<Personnage> getListePersonnage() {
		return listePersonnage;
	}
	
	public Personnage getPersonnage(int i) {
		return listePersonnage.get(i);
	}

	public void setListePersonnage(List<Personnage> listePersonnage) {
		this.listePersonnage = listePersonnage;
	}

	public List<ObjetSurCarte> getListeObjetCarte() {
		return listeObjetCarte;
	}

	public void setListeObjetCarte(List<ObjetSurCarte> listeObjetCarte) {
		this.listeObjetCarte = listeObjetCarte;
	}
	
	public void addPersonnage(String nom, ImageInformation ii) {
		addPersonnage(new Personnage(nom, ii));
	}
	
	public void addPersonnage(Personnage p) {
		listePersonnage.add(p);
	}
	
	public void addObjetSurCarte(ObjetSurCarte osc) {
		listeObjetCarte.add(osc);
	}
	
	public int nombrePersonnage() {
		return listePersonnage.size();
	}
	
	public void ajouterMunition(String nomObjet, int nombre) {
		for(int i = 0; i < tousLesObjets.size(); i++) {
			if(nomObjet.compareTo(tousLesObjets.get(i).getNom()) == 0
					&& tousLesObjets.get(i) instanceof Arme) {
				((Arme)tousLesObjets.get(i)).getMunition().ajouter(nombre);
			}
		}
	}
	
	public void setPersonnageEnTrainDeJouer(int location) {
		if( location < listePersonnage.size()) {
			personnageEnTrainDeJouer = listePersonnage.get(location);
		}
	}
	
	public void setPersonnageSuivant() {
		personnageEnTrainDeJouer = listePersonnage.get((joueurNumero(getPersonnagePrincipal())+1) % listePersonnage.size());
	}
	
	public int joueurNumero(Personnage p) {
		for(int i = 0; i < listePersonnage.size(); i++) {
			if(p == listePersonnage.get(i)) {
				return i;
			}
		}
		return -123;
	}
	
	public Personnage getPersonnagePrincipal() {
		if(personnageEnTrainDeJouer == null) {
			personnageEnTrainDeJouer = listePersonnage.get(1);
		}
		return personnageEnTrainDeJouer;
	}
	
	public Personnage getPersonnage(String nom) {
		for(int i =0; i < listePersonnage.size(); i++) {
			if(nom.compareTo(listePersonnage.get(i).getNom()) == 0) {
				return listePersonnage.get(i);				
			}
		}
		Log.v(TAG, "Le personnage n'a pas été trouvé.");
		return null;
	}
	
	public Bitmap getMondeView() {
		Bitmap terrain = getTerrain();
		for(Personnage p : listePersonnage) {
			dessineSurBitmap(terrain, p.getImageView(), p.getPosition());
		}
		return terrain;
	}
	
	public void dessineSurBitmap(Bitmap fond, Bitmap dessin, PointF centre) {
		for(int i = 0; i < dessin.getWidth(); i++) {
			for(int j = 0; j < dessin.getHeight(); j++) {
				if(Color.alpha(dessin.getPixel(i, j)) != Color.TRANSPARENT) {
					fond.setPixel(((int)centre.x)+i, ((int)centre.y)+j, dessin.getPixel(i, j));
				}
			}
		}
	}
	
}