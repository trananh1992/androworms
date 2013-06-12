package com.androworms;

import java.io.File;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.graphics.drawable.BitmapDrawable;

/**
 * Le noyau gere tous les interactions.
 * Echange entre IHM/reseau/moteurGraphique.
 */
public class Noyau {

	// Constantes de jeu
	private static final int DEPLACEMENT_DROITE = 0;
	private static final int DEPLACEMENT_GAUCHE = 1;
	private static final int DEPLACEMENT_HAUT = 2;
	private static final int VITESSE_CHUTE = 40;
	private static final float VITESSE_PROJECTILE = 0.05f;
	private static final int DECALAGE_JOUEUR = 20;
	private static final float VITESSE_RAFRAICHISSEMENT = 100f;
	
	// Constantes de tailles
	public static final Point TAILLE_PROJECTILE = new Point(40, 30);
	
	private static final String TAG_NOYAU = "Androworms.Noyau";
	private Connexion connexion;

	private Monde monde;

	private MoteurPhysique physique;
	private MoteurGraphique graphique;
	
	// Variable de débug
	public static final Point DEBUG_POSITION_JOUEUR_1 = new Point(700, 200);
	public static final Point DEBUG_POSITION_JOUEUR_2 = new Point(500, 200);
	public static final Point TAILLE_IMAGE_JOUEUR = new Point(81, 107);
	public static final Point TAILLE_IMAGE_FOND = new Point(1280, 720);
	
	// Paramètres de l'application configurable dans l'activité "Paramètres" de l'application
	private SharedPreferences parametresApplication;
	
	public Noyau(Context context, MoteurGraphique mg, Bundle bundle) {
		this.graphique = mg;
		this.parametresApplication = context.getSharedPreferences(ActiviteParametres.PARAMETRES_CLE, 0);
		
		// Juste pour le test
		test(context, bundle);
	}
	
	/** Cette fonction sera à supprimer lorsque l'on en aura plus besoin. */
	public void test(Context context, Bundle bundle) {
		ParametresPartie params = ParametresPartie.getParametresPartie();
		Integer paramMode = params.getModeJeu();
		Boolean paramEstCartePerso = params.isEstCartePerso();
		String paramNomCarte = params.getNomCarte();

		monde = new Monde();
		physique = new MoteurPhysique(this, monde);
		
		/*
		switch (paramMode) {
		case ParametresPartie.MODE_BLUETOOTH_SERVEUR:
		case ParametresPartie.MODE_BLUETOOTH_CLIENT:
			creationPartieDistante();
			break;
		case ParametresPartie.MODE_SOLO :
		default :
			creationPartieLocale();
			break;
		}
		*/
		creationPartieLocale();
		
		ImageInformation ii = new ImageInformation(R.drawable.android_face, TAILLE_IMAGE_JOUEUR.x, TAILLE_IMAGE_JOUEUR.y, context);
		Personnage johnDoe = new Personnage("John Doe", ii);
		johnDoe.setPosition(new PointF(DEBUG_POSITION_JOUEUR_1));
		
		//String pseudo = this.parametresApplication.getString(ActiviteParametres.PARAMETRE_PSEUDO_CLE, ActiviteParametres.PARAMETRE_PSEUDO_DEFAUT_2);
		String pseudo = "Tux";
		ImageInformation ii2 = new ImageInformation(R.drawable.android_face, TAILLE_IMAGE_JOUEUR.x, TAILLE_IMAGE_JOUEUR.y, context);
		Personnage tux = new Personnage(pseudo, ii2);
		tux.setPosition(new PointF(DEBUG_POSITION_JOUEUR_2));
		/*
		ImageInformation ii3 = new ImageInformation(R.drawable.missile, 40, 30, context);
		Objet o = new Objet("missile", ii3);
		ObjetSurCarte osc = new ObjetSurCarte(o, new PointF(150,150), ii3);
		*/
		monde.addPersonnage(tux);
		monde.addPersonnage(johnDoe);
		//monde.addObjetSurCarte(osc);

		
		if (paramEstCartePerso != null && paramNomCarte != null) {
			if (paramEstCartePerso) {
				File root = Environment.getExternalStorageDirectory();
				File sd = new File(root, ActiviteAndroworms.DOSSIER_CARTE + paramNomCarte);
				Bitmap b = BitmapFactory.decodeFile(sd.getAbsolutePath());
				//monde.setTerrain(b, TAILLE_IMAGE_FOND.x, TAILLE_IMAGE_FOND.y);
				monde.setPremierPlan(Bitmap.createScaledBitmap(b, TAILLE_IMAGE_FOND.x, TAILLE_IMAGE_FOND.y, true));
			}
			else {
				// TODO : prendre en compte ce paramètre
				// afficher la carte parmi les cartes par défaut de l'application.
				Bitmap b = ((BitmapDrawable)context.getResources().getDrawable(R.drawable.terrain_jeu_defaut_4)).getBitmap();
				//monde.setTerrain(b, TAILLE_IMAGE_FOND.x, TAILLE_IMAGE_FOND.y);
				monde.setPremierPlan(b);
			}
		}

		monde.setArrierePlan(((BitmapDrawable)context.getResources().getDrawable(R.drawable.image_fond)).getBitmap());

		/*
		Log.v(TAG_NOYAU, "Début de l'initialisation de la matrice de collision");
		//physique.initMatriceCollision();
		Log.v(TAG_NOYAU, "Fin de l'initialisation de la matrice de collision");
		*/
		graphique.remetAplusTard(new RunnableGravite(physique, this), 3);
	}

	
	
	public MoteurPhysique getPhysique() {
		return physique;
	}

	public void setPhysique(MoteurPhysique physique) {
		this.physique = physique;
	}
	
	public SharedPreferences getParametresApplication() {
		return parametresApplication;
	}
	
	public void actualiserGraphisme() {
		graphique.actualiserGraphisme();
	}

	public void creationPartieLocale() {
		connexion = new ConnexionLocale(this);
	}
	
	public void creationPartieDistante() {
		connexion = new ConnexionDistante(this);
	}
	
	/** Gestion des messages venanat de l'IHM  et runnable */
	public void sautJoueurDroiteFromIHM() {
		physique.sautJoueurDroite(monde.getPersonnagePrincipal().getNom());
		mouvementForces();
	}
	
	public void finDuTourFromIHM() { 
		connexion.finDuTourJoueur();
	}
	
	public void sautJoueurGaucheFromIHM() {
		physique.sautJoueurGauche(monde.getPersonnagePrincipal().getNom());
		mouvementForces();
	}
	
	public void deplacementJoueurDroiteFromIHM() {
		connexion.deplacementJoueurDroite(monde.getPersonnagePrincipal().getNom());
	}
	
	public void deplacementJoueurGaucheFromIHM() {
		connexion.deplacementJoueurGauche(monde.getPersonnagePrincipal().getNom());
	}
	
	public void effectuerTirFromIHM(Vector2D vd) {
		connexion.effectuerTir(vd);
	}
	
	// FIN des fonctions venant de l'IHM ou des runnables
	
	public void prochainJoueur() {
		monde.setPersonnageSuivant();
		monde.unsetTerrainSansPersonnageSave();
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

	public void effectuerTir(Vector2D vd) {
		Bitmap image = graphique.getImage(R.drawable.missile);
		ImageInformation ii = new ImageInformation(image, R.drawable.missile);
		ii.setHeight(TAILLE_PROJECTILE.y);
		ii.setWidth(TAILLE_PROJECTILE.x);
		float seconde = VITESSE_PROJECTILE;
		PointF position =  monde.getPersonnagePrincipal().clone().getPosition();
		position.set(position.x + DECALAGE_JOUEUR, position.y + DECALAGE_JOUEUR);
		ObjetSurCarte esc = new ObjetSurCarte(new Objet("missile", ii), position, ii);
		physique.effectuerTir(esc, vd, seconde);
		mouvementTir(esc, seconde);
		//graphique.ajouterElementSurCarte(esc);
		Log.v(TAG_NOYAU,"On tire ou pas !");
	}
	
	public void animerAndroidDroite(Personnage p) {
		graphique.animerAndroidDroite(p);
	}
	
	public void animerAndroidGauche(Personnage p) {
		graphique.animerAndroidGauche(p);
	}
	
	public void stopAnimationAndroid() {
		graphique.stopAnimationAndroid();
	}
	
	public Monde getMonde() {
		return monde;
	}

	/** Cette fonction informe l'interface graphique 
	 * qu'il y a des mouvements que des joueurs doivent exécuter
	 */
	public void mouvementForces() {
		graphique.remetAplusTard(new RunnableMouvementForce(graphique, this, (int) (physique.getRafraichissement() * VITESSE_RAFRAICHISSEMENT)), VITESSE_CHUTE);
	}
	
	public void mouvementTir(ObjetSurCarte esc, float seconde) {
		//graphique.remetAplusTard(new RunnableMouvementForce(graphique, this, (int) (physique.getRafraichissement()*100f)), VITESSE_CHUTE);
		graphique.remetAplusTard(new RunnableTir(esc, graphique, this, (int) (physique.getRafraichissement() * VITESSE_RAFRAICHISSEMENT)), (int) (seconde*1000));
	}
}