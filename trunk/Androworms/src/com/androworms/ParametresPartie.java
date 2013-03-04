package com.androworms;

import java.util.ArrayList;
import java.util.List;

import android.bluetooth.BluetoothSocket;

public final class ParametresPartie {
	
	// Mode de jeu
	public static final int MODE_SOLO = 1;
	public static final int MODE_2JOUEURS = 2;
	public static final int MODE_BLUETOOTH_SERVEUR = 3;
	public static final int MODE_BLUETOOTH_CLIENT = 4;
	public static final int MODE_WIFI_SERVEUR = 5;
	public static final int MODE_WIFI_CLIENT = 6;
	private int modeJeu;
	
	// Carte
	private boolean estCartePerso;
	private String nomCarte;
	
	// Mode solo
	private int difficuluteIA;
	
	// Mode 2 joueurs
	private String nomJoueur2;
	
	// Mode Bluetooth Serveur
	private List<BluetoothSocket> socketsClients;
	
	// Mode Bluetooth Client
	private BluetoothSocket socketServeur;
	
	// Singleton de ParametresPartie
	private static ParametresPartie parametres;
	
	private ParametresPartie() {
		// Mode de jeu
		setModeJeu(MODE_SOLO);
		
		// Carte
		estCartePerso = false;
		nomCarte = "terrain_jeu_defaut_1.png";
		
		// Mode solo
		difficuluteIA = -1;
		
		// Mode 2 joueurs
		nomJoueur2 = "Joueur 2";
		
		// Mode Bluetooth Serveur
		socketsClients = new ArrayList<BluetoothSocket>();
		
		// Mode Bluetooth Client
		socketServeur = null;
	}
	
	/** Singleton des ParamètresParties */
	public static ParametresPartie getParametresPartie() {
		if (parametres == null) {
			parametres = new ParametresPartie();
		}
		return parametres;
	}
	
	/** Savoir le mode de jeu */
	public int getModeJeu() {
		return modeJeu;
	}
	
	/** Définir le mode de jeu */
	public void setModeJeu(int modeJeu) {
		this.modeJeu = modeJeu;
	}
	
	/** Savoir si c'est une carte personnalisé ou une carte par défaut */
	public boolean isEstCartePerso() {
		return estCartePerso;
	}
	
	/** Définir si c'est une carte personnalisé ou une carte par défaut */
	public void setEstCartePerso(boolean estCartePerso) {
		this.estCartePerso = estCartePerso;
	}

	public String getNomCarte() {
		return nomCarte;
	}

	public void setNomCarte(String nomCarte) {
		this.nomCarte = nomCarte;
	}

	public int getDifficuluteIA() {
		return difficuluteIA;
	}

	public void setDifficuluteIA(int difficuluteIA) {
		this.difficuluteIA = difficuluteIA;
	}

	public String getNomJoueur2() {
		return nomJoueur2;
	}

	public void setNomJoueur2(String nomJoueur2) {
		this.nomJoueur2 = nomJoueur2;
	}

	public List<BluetoothSocket> getSocketsClients() {
		return socketsClients;
	}

	public void setSocketsClients(List<BluetoothSocket> socketsClients) {
		this.socketsClients = socketsClients;
	}
	
	public void addSocketsClients(BluetoothSocket socketClient) {
		this.socketsClients.add(socketClient);
	}

	public BluetoothSocket getSocketServeur() {
		return socketServeur;
	}

	public void setSocketServeur(BluetoothSocket socketServeur) {
		this.socketServeur = socketServeur;
	}
}