package com.androworms;

import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

public class Serveur extends Communication implements Runnable {
	private BluetoothServerSocket socketPublique;
	private static final String TAG_SERVEUR = "Androworms.Serveur";
	
	public Serveur(ConnexionDistante c) {
		super(c);
		Log.v(TAG_SERVEUR, "Création de la socket publique");
		try {
			// MY_UUID is the app's UUID string, also used by the client code
			socketPublique = BluetoothAdapter.getDefaultAdapter().listenUsingRfcommWithServiceRecord("AndroWorms", UUID.randomUUID());
		} catch (Exception e) {
			Log.v(TAG_SERVEUR, "Erreur sur la creation de la socket publique ");
			Log.v(TAG_SERVEUR, "\t"+e.getMessage());
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
				Log.v(TAG_SERVEUR, "Le thread est toujours là !");
				
				socket = socketPublique.accept();
				// If a connection was accepted
				if (socket != null) {
					// Do work to manage the connection (in a separate thread)
					socketPublique.close();
				}
			}
		} catch (Exception e) {
			Log.v(TAG_SERVEUR, "Gestion de la socketPublique inefficace!");
			//Log.v("Serveur", e.getMessage());
		}
	}
	
	public void arret() {
		
	}
}