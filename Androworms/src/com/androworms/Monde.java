package com.androworms;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.util.Log;

public class Monde {
	
	private static final String TAG = "Androworms.Monde";
	
	private Bitmap terrain;
	private MoteurGraphique mg;
	private List<Personnage> listePersonnage;
	private List<ObjetSurCarte> listeObjetCarte;
	private List<Objet> tousLesObjets;
	
	public Monde() {
		super();
		tousLesObjets = new ArrayList<Objet>();
		listePersonnage = new ArrayList<Personnage>();
		listeObjetCarte = new ArrayList<ObjetSurCarte>();
	}
	
	public void setTerrain(Bitmap b, int width, int height){
		terrain = Bitmap.createScaledBitmap(b, width, height, true);
	}
	
	public Bitmap getTerrain() {
		return terrain;
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
	
	public Personnage getPersonnagePrincipal() {
		if (this.listePersonnage == null || this.listePersonnage.size() == 0) {
			return null;
		} else {
			return this.listePersonnage.get(0);
		}
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
	
}