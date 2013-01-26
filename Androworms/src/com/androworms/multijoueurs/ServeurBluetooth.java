package com.androworms.multijoueurs;

import java.io.IOException;

import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

public class ServeurBluetooth extends Thread {
	
	//TODO : valider la visibilité de ces paramètres et leur utilité
	private static final String TAG = "ServeurBluetooth";
	
	private final BluetoothServerSocket mmServerSocket;
	
	public static BluetoothSocket socket = null;
	ActiviteMultiJoueur activiteMultiJoueur;
	
	public ServeurBluetooth(ActiviteMultiJoueur activiteMultiJoueur) {
		//TODO : valider cette fonction
		
		// Use a temporary object that is later assigned to mmServerSocket,
		// because mmServerSocket is final
		BluetoothServerSocket tmp = null;
		try {
			tmp = ActiviteMultiJoueur.mBluetoothAdapter.listenUsingRfcommWithServiceRecord("Androworms", ActiviteMultiJoueur.ANDROWORMS_UUID);
		} catch (IOException e) {
			Log.e(TAG, "Erreur sur la creation de la socket publique ");
			Log.e(TAG, e.getMessage());
		}
		mmServerSocket = tmp;
		
		this.activiteMultiJoueur = activiteMultiJoueur;
	}
	
	public void run() {//TODO : valider cette fonction
		// BluetoothSocket socket = null;
		// Keep listening until exception occurs or a socket is returned
		while (true) {
			try {
				socket = mmServerSocket.accept();
				Log.d(TAG,"Le serveur a accepté une connexion !");
				
				if (socket.getRemoteDevice() == null) {
					Log.d(TAG,"device=null");
				}
				Log.d(TAG,"device="+socket.getRemoteDevice().getName());
				activiteMultiJoueur.getFonctions_IHM().addNewPlayer(socket.getRemoteDevice());
				
			} catch (IOException e) {
				break;
			}
			// If a connection was accepted
/*			if (socket != null) {
				// Do work to manage the connection (in a separate thread)
 //			   manageConnectedSocket(socket);
				try {
					mmServerSocket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}*/
		
		} // pour sortir de cette boucle, il faut quitter le thread
		
		
		
	}
	
	/** Will cancel the listening socket, and cause the thread to finish */
	public void cancel() {
		try {
			mmServerSocket.close();
		} catch (IOException e) { }
	}
}