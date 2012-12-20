package com.androworms;

import java.util.ArrayList;

/*
 * Classe mere pour la classe client et la classe serveur.
 * Les deux classes suivantes permettent de contacter les joueurs.
 */
public class Communication {
	private Client client;
	private Serveur serveurSecondaire;
	private Serveur serveurPrimaire;
	
	public Communication(boolean hote) {
		client  = new Client();
		serveurSecondaire = new ServeurSecondaire();
		if(hote) {
			serveurPrimaire = new ServeurPrimaire(this);
		}
		else {
			serveurPrimaire = null;
		}
	}
	
	public void ajouterContactServeurPrimaire(Contact c) {
		client.setContactServeurPrimaire(c);
	}
	
	public void deplacementJoueurDroite(String nomPersonnage) {
		client.deplacementJoueurDroite(nomPersonnage);
	}
	
	public ArrayList<String> listeDesServeursPrimaires() {
		// On recherche l'ensemble des appareils dans le voisinage (bluetooth)
		// qui ont lance un serveurPrimaire.
		// Le joueur pourra alors se connecter a l'un d'entre eux si besoin.
		return new ArrayList<String>();
	}
}