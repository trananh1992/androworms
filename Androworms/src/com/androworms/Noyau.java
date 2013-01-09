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
		ImageInformation ii = new ImageInformation(R.drawable.android_face, 162, 214 );
		Personnage johnDoe = new Personnage("John Doe", ii);
		johnDoe.setPosition(new PointF(820, 470));
		monde.addPersonnage(johnDoe);
		
		Personnage tux = new Personnage("Tux", ii);
		tux.setPosition(new PointF(120, 450));
		monde.addPersonnage(tux);

		ImageInformation iiObjetCarte = new ImageInformation(R.drawable.hache, 139, 95);
		Objet o = new Arme("Hache");
		o.setImageTerrain(R.drawable.hache, 139, 95);
		ObjetSurCarte obj = new ObjetSurCarte(o, new PointF(250,500), iiObjetCarte);
		monde.addObjetSurCarte(obj);
		obj = new ObjetSurCarte(o, new PointF(1000, 500), iiObjetCarte);
		monde.addObjetSurCarte(obj);
		//FIN ZONE DE TEST
		
		connexion = new ConnexionLocale(this);
		this.nomPersonnage = "Tux";
		physique = new MoteurPhysique(this, monde);
	}
	
	public String getNomPersonnage() {
		return nomPersonnage;
	}

	public void setNomPersonnage(String nomPersonnage) {
		this.nomPersonnage = nomPersonnage;
	}

	public void creationPartieLocale() {
		connexion = new ConnexionLocale(this);
		monde = new Monde();
		physique = new MoteurPhysique(this, monde);

		
	}
	
	public void creationPartieDistante() {
		connexion = new ConnexionDistante(this);
	}
	
	/*
	 * Gestion des messages venanat de l'IHM 
	 */
	public void sautJoueurDroiteFromIHM() {
		
	}
	
	public void sautJoueurGaucheFromIHM() {
		
	}
	
	public void deplacementJoueurDroiteFromIHM() {
		connexion.deplacementJoueurDroite(nomPersonnage);
	}
	
	public void deplacementJoueurGaucheFromIHM() {
		connexion.deplacementJoueurGauche(nomPersonnage);
	}
	
	public void deplacementJoueurDroite(String personnage) {
		// Impacter le monde de ce changement
		physique.deplacementJoueurDroite(personnage);
		this.graphique.actualiserGraphisme();
	}
	public void deplacementJoueurGauche(String personnage) {
		// Impacter le monde de ce changement
		physique.deplacementJoueurGauche(personnage);
		this.graphique.actualiserGraphisme();
	}
	
	/*
	 * Gestion des tests.
	 */
	
	public void testReseau() {
		Log.v(TAG_NOYAU, "TestReseau n'est plus valide !");
	}
	
	public Monde getMonde() {
		return monde;
	}
	
	
}
