package com.androworms;

import java.util.ArrayList;
import java.util.List;

/*
 * Le noyau gere tous les interactions.
 * Echange entre IHM/reseau/moteurGraphique.
 */
public class Noyau {
	private MoteurPhysique mp;
	private MoteurGraphique mg;
	private Machine machine;
	// private ihm...
	private List<Joueur> listJoueur;
	private List<ObjetSurCarte> listObjetCarte;
	
	public Noyau()
	{
		mg = new MoteurGraphique();
		mp = new MoteurPhysique(this);
		machine = new Machine(this);
		listJoueur = new ArrayList<Joueur>();
		listObjetCarte = new ArrayList<ObjetSurCarte>();
	}
	
	public MoteurGraphique getMg() {
		return mg;
	}
	public void setMg(MoteurGraphique mg) {
		this.mg = mg;
	}
	public Machine getMachine() {
		return machine;
	}
	public void setMachine(Machine machine) {
		this.machine = machine;
	}
	public List<Joueur> getListJoueur() {
		return listJoueur;
	}
	public void setListJoueur(List<Joueur> listJoueur) {
		this.listJoueur = listJoueur;
	}
	public List<ObjetSurCarte> getListObjetCarte() {
		return listObjetCarte;
	}
	public void setListObjetCarte(List<ObjetSurCarte> listObjetCarte) {
		this.listObjetCarte = listObjetCarte;
	}

	public MoteurPhysique getMp() {
		return mp;
	}

	public void setMp(MoteurPhysique mp) {
		this.mp = mp;
	}
	
	
	
}
