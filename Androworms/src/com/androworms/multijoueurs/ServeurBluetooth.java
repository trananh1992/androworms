package com.androworms.multijoueurs;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.androworms.ImageInformation;
import com.androworms.Personnage;

public class ServeurBluetooth extends Thread {
	
	//TODO : valider la visibilité de ces paramètres et leur utilité
	private static final String TAG = "ServeurBluetooth";
	
	private final BluetoothServerSocket mmServerSocket;
	
	public BluetoothSocket socket = null;
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
				activiteMultiJoueur.getFonctionsIHM().addNewPlayer(socket.getRemoteDevice());
				
			} catch (IOException e) {
				break;
			}
			// If a connection was accepted
			if (socket != null) {
				// Do work to manage the connection (in a separate thread)
				manageConnectedSocket(socket);
				try {
					mmServerSocket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}
		
		} // pour sortir de cette boucle, il faut quitter le thread
		
	}
	
	private void manageConnectedSocket(BluetoothSocket mmSocket) {
		receiveMessage(mmSocket);
		sendMessage(mmSocket);
	}
	
	private void sendMessage(BluetoothSocket mmSocket) {
		try {
			OutputStream os = mmSocket.getOutputStream();
			
			Log.v("a","Je suis le SERVEUR et je vais envoyer un objet !");
			
			Personnage p = new Personnage("devine qui c'est ? n'import quoi", new ImageInformation());
			
			//http://stackoverflow.com/questions/2836646/java-serializable-object-to-byte-array
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(bos);  
			out.writeObject(p);
			byte[] yourBytes = bos.toByteArray();
			os.write(yourBytes);
			out.close();
			bos.close();
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void receiveMessage(BluetoothSocket mmSocket) {
		try {
			InputStream is = mmSocket.getInputStream();
			
			
			byte[] buffer = new byte[1024];
			is.read(buffer);
			
			Log.d("MESSAGE RECU",new String(buffer));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/** Will cancel the listening socket, and cause the thread to finish */
	public void cancel() {
		try {
			mmServerSocket.close();
		} catch (IOException e) { }
	}
}