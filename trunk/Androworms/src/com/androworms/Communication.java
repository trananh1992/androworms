package com.androworms;

/*
 * Classe mere pour la classe client et la classe serveur.
 * Les deux classes suivantes permettent de contacter les joueurs.
 */
public abstract class Communication {
	
	private ConnexionDistante connexionDistante;
	
	public Communication(ConnexionDistante cd) {
		connexionDistante = cd;
	}

	public ConnexionDistante getConnexionDistante() {
		return connexionDistante;
	}

	public void setConnexionDistante(ConnexionDistante connexionDistante) {
		this.connexionDistante = connexionDistante;
	}
	
	
}