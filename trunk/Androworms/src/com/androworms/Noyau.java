package com.androworms;

import java.util.ArrayList;
import java.util.List;

import android.graphics.PointF;
import android.util.Log;


/*
 * Le noyau gere tous les interactions.
 * Echange entre IHM/reseau/moteurGraphique.
 */
public class Noyau {

	// private ihm...
	public final static int DEPLACEMENT_DROITE = 0;
	public final static int DEPLACEMENT_GAUCHE = 1;
	public final static int DEPLACEMENT_HAUT = 2;
	
	private static final String TAG_NOYAU = "Androworms.Noyau";
	private Connexion connexion;
	private String nomPersonnage;
	private Monde monde;

	private MoteurPhysique physique;
	private MoteurGraphique graphique;

	
	public Noyau(MoteurGraphique mg) {
		// ZONE DE TEST |--> TODO : a déplacer ailleurs !
		monde = new Monde(null, mg);
		this.graphique = mg;
		List<Personnage> persos = new ArrayList<Personnage>();
		Personnage p = new Personnage("John Doe");
		p.setPosition(new PointF(820, 470));
		persos.add(p);
		p = new Personnage("Tux");
		p.setPosition(new PointF(120, 450));
		persos.add(p);
		monde.setListePersonnage(persos);
		List<ObjetSurCarte> objs = new ArrayList<ObjetSurCarte>();
		Objet o = new Arme("Hache", null, 0);
		ObjetSurCarte obj = new ObjetSurCarte(o, new PointF(250,500));
		objs.add(obj);
		obj = new ObjetSurCarte(o, new PointF(1000, 500));
		objs.add(obj);
		monde.setListeObjetCarte(objs);
		//FIN ZONE DE TEST
		
		connexion = new ConnexionLocale(this);
		this.nomPersonnage = "Tux";
	}
	
	public String getNomPersonnage() {
		return nomPersonnage;
	}

	public void setNomPersonnage(String nomPersonnage) {
		this.nomPersonnage = nomPersonnage;
	}

	public void creationPartieLocale() {
		connexion = new ConnexionLocale(this);
		physique = new MoteurPhysique(this);
		monde = new Monde(physique);
		
	}
	
	public void creationPartieDistante() {
		connexion = new ConnexionDistante(this);
	}
	
	public void deplacementJoueurFromIHM(int move) {
		switch(move) {
			case DEPLACEMENT_DROITE : 
				connexion.deplacementJoueurDroite(nomPersonnage);
				break;
			case DEPLACEMENT_GAUCHE : 
				connexion.deplacementJoueurGauche(nomPersonnage);
				break;
			case DEPLACEMENT_HAUT : 
				//connexion.deplacementJoueurHaut(nomPersonnage);
				break;
			default :
				break;
		}
	}
	
	public void deplacementJoueurDroite(String personnage) {
		// Impacter le monde de ce changement
		monde.deplacementJoueurDroite(personnage);
		this.graphique.actualiserGraphisme();
	}
	public void deplacementJoueurGauche(String personnage) {
		// Impacter le monde de ce changement
		monde.deplacementJoueurGauche(personnage);
		this.graphique.actualiserGraphisme();
	}
	
	public void testReseau() {
		Log.v(TAG_NOYAU, "TestReseau n'est plus valide !");
	}
	
	public Monde getMonde() {
		return monde;
	}
	
	
}
