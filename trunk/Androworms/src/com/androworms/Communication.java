package com.androworms;

import java.util.ArrayList;
import java.util.List;

/*
 * Classe mere pour la classe client et la classe serveur.
 * Les deux classes suivantes permettent de contacter les joueurs.
 */
public class Communication {
	private Client client;
	private Serveur serveur;
	
	public Communication(boolean hote) {
		new ServeurSecondaire();
		if(hote) {
			serveur = new ServeurPrimaire(this);
		}
		else {
			serveur = null;
		}
	}
	
	public void ajouterContactServeur(Contact c) {
		client.setContactServeur(c);
	}
	
	public void deplacementJoueurDroite(String nomPersonnage) {
		client.deplacementJoueurDroite(nomPersonnage);
	}
	
	public List<String> listeDesServeursPrimaires() {
		// On recherche l'ensemble des appareils dans le voisinage (bluetooth)
		// qui ont lance un serveurPrimaire.
		// Le joueur pourra alors se connecter a l'un d'entre eux si besoin.
		return new ArrayList<String>();
	}
}