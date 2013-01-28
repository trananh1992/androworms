package com.androworms.multijoueurs;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.OutputStream;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.androworms.Contact;
import com.androworms.Personnage;

/** Lors de la création d'une partie en Multi-joueur en Bluetooth,
 *  ce Thread est crée pour crée l'échange de socket entre le client et le serveur.
 */
public class ClientConnexionBluetooth extends Thread {
	
	private static final String TAG_CLIENT = "ClientBluetooth";
	private BluetoothSocket socketServeur;
	
	public ClientConnexionBluetooth(BluetoothDevice device) {
		try {
			socketServeur = device.createRfcommSocketToServiceRecord(Contact.ANDROWORMS_UUID);
		} catch (IOException e) {
			
		}
	}
	
	public void run() {
		// On annule la recherche d'appareil à proximité si elle était lancé (elle sert plus à rien)
		ActiviteMultiJoueur.mBluetoothAdapter.cancelDiscovery();
		
		try {
			socketServeur.connect();
			Log.d(TAG_CLIENT, "Le client à accepté la connexion !");
			
		} catch (IOException connectException) {
			// Impossible de se connecter, donc on clos la connexion
			try {
				socketServeur.close();
			} catch (IOException closeException) {
				
			}
			return;
		}
		
		// Do work to manage the connection (in a separate thread)
		//manageConnectedSocket(socketServeur);
	}
	
	private void manageConnectedSocket(BluetoothSocket mmSocket) {
		sendMessage(mmSocket);
		receiveMessage(mmSocket);
	}
	
	private void sendMessage(BluetoothSocket mmSocket) {
		Log.v("a","Je suis le client et j'envoie un MESSAGE STRING !");
		try {
			OutputStream os =mmSocket.getOutputStream();
			
			String s = "Hello, je suis le client et j'envoie un message String !";
			os.write(s.getBytes());
			
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
			/*Personnage p = buffer;
			Log.d("MESSAGE RECU",buffer.toString());*/
			
			ByteArrayInputStream bis = new ByteArrayInputStream(buffer);
			ObjectInput in = null;
			try {
				in = new ObjectInputStream(bis);
				Object o = in.readObject(); 
				Personnage p = (Personnage)o;
				
				Log.d("MESSAGE RECU : ",p.getNom());
				
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				bis.close();
				in.close();
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}