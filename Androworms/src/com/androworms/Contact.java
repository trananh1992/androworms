package com.androworms;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

public abstract class Contact {
	
	private static final String TAG = "Androworms.Contact";
	
	// Identifiant (généré aléatoirement) pour Androworms.
	// Il sert à établir la connexion Bluetooth entre 2 appareils
	public static final UUID ANDROWORMS_UUID = UUID.fromString("c2d080da-7610-4dde-96c1-01406eb4b24b");
	
	
	private BluetoothSocket socketServeur;
	// Liste des sockets des clients du jeu. (pour le serveur)
	private List<BluetoothSocket> socketClient;
	
	// variable utile ?
	private BluetoothDevice device;
	
	public Contact() {
		socketServeur = null;
		socketClient = new ArrayList<BluetoothSocket>();
	}

	public BluetoothSocket getSocketServeur() {
		return socketServeur;
	}

	public void setSocketServeur(BluetoothSocket socketServeur) {
		this.socketServeur = socketServeur;
	}
	
	public List<BluetoothSocket> getSocketClient() {
		return socketClient;
	}

	public void setSocketClient(List<BluetoothSocket> socketClient) {
		this.socketClient = socketClient;
	}
	
	public void addSocketClient(BluetoothSocket socketClient) {
		this.socketClient.add(socketClient);
	}

	public BluetoothDevice getDevice() {
		return device;
	}

	public void setDevice(BluetoothDevice device) {
		this.device = device;
	}
	
	public void init(BluetoothAdapter ba, String adresse){
		
	}
	
	public void connect() {
		
	}
}