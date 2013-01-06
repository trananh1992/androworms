package com.androworms;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

public class Monde {
	private static final String TAG= "Androworms.Monde";
	private MoteurPhysique mp;
	private MoteurGraphique mg;
	private List<Personnage> listePersonnage;
	private List<ObjetSurCarte> listeObjetCarte;
	private List<Objet> tousLesObjets;
	
	public Monde(MoteurPhysique mp) {
		super();
		this.mp = mp;
		tousLesObjets = new ArrayList<Objet>();
		listePersonnage = new ArrayList<Personnage>();
		listeObjetCarte = new ArrayList<ObjetSurCarte>();
	}
	
	public Monde(MoteurPhysique mp, MoteurGraphique mg) {
		super();
		this.mp = mp;
		this.mg = mg;
		tousLesObjets = new ArrayList<Objet>();
		listePersonnage = new ArrayList<Personnage>();
		listeObjetCarte = new ArrayList<ObjetSurCarte>();
	}

	public MoteurPhysique getMp() {
		return mp;
	}
	
	

	public MoteurGraphique getMg() {
		return mg;
	}

	public void setMg(MoteurGraphique mg) {
		this.mg = mg;
	}

	public void setMp(MoteurPhysique mp) {
		this.mp = mp;
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
	
	private Personnage getPersonnage(String Nom) {
		for(int i =0; i < listePersonnage.size(); i++) {
			if(Nom.compareTo(listePersonnage.get(i).getNom()) == 0) {
				return listePersonnage.get(i);				
			}
		}
		Log.v(TAG, "Le personnage n'a pas été trouvé.");
		return null;
	}

	public void deplacementJoueurDroite(String personnage) {
		/// Il faudra faire passer ce mouvement à droite dans le moteur physique.
		// La coordonné en y sera affecté si besoin est.
		getPersonnage(personnage).deplacementDroite(getPersonnage(personnage).getPosition().y);
				
	}
	
	public void deplacementJoueurGauche(String personnage) {
		/// Il faudra faire passer ce mouvement à gauche dans le moteur physique.
		// La coordonné en y sera affecté si besoin est.
		getPersonnage(personnage).deplacementGauche(getPersonnage(personnage).getPosition().y);
				
	}
	
}