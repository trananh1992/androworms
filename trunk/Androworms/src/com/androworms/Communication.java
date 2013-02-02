package com.androworms;

/**
 * Classe mere pour la classe client est la classe serveur.
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
	
	/**
	 * Cette fonction envoie un message vers un contact distant.
	 * Elle devra surement retourner un booleen qui indique si l'envoie a fonctionné
	 */
	public boolean sendMessage ( /* Proposition d'argument (Contact c, "Objet" message)*/ ) {
		return false;
	}
	
	/**
	 * Cette objet recoit un message.
	 * Cette classe devra surement être threader pour pouvoir recevoir des messages
	 */
	public void receptionMessage() {
		// La fonction dechiffre le message ou je ne sais quoi.
		traitementMessage(/*Le message sous la forme d'objet qui va bien*/);
	}
	
	/**
	 * Fonction a definir chez les classes filles (client et serveur)
	 * Il n'y aura pas le meme traitement sur les deux.
	 */
	public abstract void traitementMessage(/*Arguments qui vont bien*/);
	
	
}