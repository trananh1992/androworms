package com.androworms;

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
	private BluetoothDevice device;
	
	public Contact() {
	}

	public BluetoothSocket getSocket() {
		return socketServeur;
	}

	public void setSocket(BluetoothSocket socket) {
		this.socketServeur = socket;
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