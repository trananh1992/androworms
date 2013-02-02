package com.androworms;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

public class ConnexionDistante extends Connexion {
	
	// L'item 1 designe le client et l'item 2 le serveur (si il y a)
	private static final int SERVEUR_POSITION = 1;
	private static final int CLIENT_SERVEUR = 0;
	
	private static final String TAG_COM = "Androworms.ConnexionDistante";
	
	private List<Communication> communications;

	public ConnexionDistante(Noyau n) {
		super(n);
		communications = new ArrayList<Communication>();
	}

	private Client getClient() {
		return (Client)communications.get(CLIENT_SERVEUR);
	}

	private Serveur getServeur() {
		return (Serveur)communications.get(SERVEUR_POSITION);
	}
	
	public void lancementServeur() {
		Log.v(TAG_COM, "Lancement de lancementServeur");
		Serveur serveur = new Serveur(this);
		communications.set(SERVEUR_POSITION, serveur);
		new Thread(serveur).start();
	}

	public List<String> listeDesServeursPrimaires() {
		// On recherche l'ensemble des appareils dans le voisinage (bluetooth)
		// qui ont lance un serveurPrimaire.
		// Le joueur pourra alors se connecter a l'un d'entre eux si besoin.
		return new ArrayList<String>();
	}
	
	public void deplacementJoueurDroite(String nomPersonnage) {
		getClient().deplacementJoueurDroite(nomPersonnage);
	}

	public void deplacementJoueurGauche(String nomPersonnage) {
		getNoyau().deplacementJoueurGauche(nomPersonnage);
	} 

	public void deplacementJoueurSaut(String nomPersonnage) {
		
	}
	
	public void arretServeur() {
		getServeur().arret();
	}
}