package com.androworms;

import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

public class Serveur extends Connexion implements Runnable {
	private BluetoothServerSocket socketPublique;
	
	public Serveur(Communication c) {
		super(c);
		Log.v("Serveur", "Création de la socket publique");
		try {
			// MY_UUID is the app's UUID string, also used by the client code
			socketPublique = BluetoothAdapter.getDefaultAdapter().listenUsingRfcommWithServiceRecord("AndroWorms", UUID.randomUUID());
		} catch (Exception e) {
			Log.v("Serveur", "Erreur sur la creation de la socket publique ");
			Log.v("Serveur", "\t"+e.getMessage());
		}
	}
	
	public void run() {
		BluetoothSocket socket = null;
		// Keep listening until exception occurs or a socket is returned
		// TODO Auto-generated method stub
		int count =0 ;
		try {
			while (count < 20) {
				count ++;
				Thread.sleep(1000);
				Log.v("Serveur", "Le thread est toujours là !");
				
				socket = socketPublique.accept();
				// If a connection was accepted
				if (socket != null) {
					// Do work to manage the connection (in a separate thread)
					socketPublique.close();
				}
			}
		} catch (Exception e) {
			Log.v("Serveur", "Gestion de la socketPublique inefficace!");
			//Log.v("Serveur", e.getMessage());
		}
	}
}