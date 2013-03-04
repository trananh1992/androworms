package com.androworms;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

/**
 * Cette classe inclue toutes les informations pour contacter
 * un joueur en bluetooth.
 */
public class Bluetooth extends Contact {
	
	private static final String TAG = "Androworms.Bluetooth";
	
	// La javadoc indique que l'UTF-8 est disponible sur toutes les implémentations de Java
	// http://developer.android.com/reference/java/nio/charset/Charset.html
	private static final String CHARSET_NAME = "UTF-8";
	
	// La taille du Buffer par défaut
	private static final int TAILLE_BUFFER = 1024;
	
	// Stockage ici de l'adaptateur Bluetooth de l'appareil
	private static BluetoothAdapter bluetoothAdapter = null;
	
	/** Message de ACK pour singifie que le destinataire à bien reçu le message */
	public static final String MESSAGE_ACK = "ACK";
	
	public Bluetooth(String adresse) {
		super();
		init(bluetoothAdapter, adresse);
	}
	
	/** Intialiation de l'adaptateur Bluetooth */
	public static void setBluetoothAdapter(BluetoothAdapter bluetoothAdapter) {
		Bluetooth.bluetoothAdapter = bluetoothAdapter;
	}
	
	/** Récupération de l'adaptateur Bluetooth */
	public static BluetoothAdapter getBluetoothAdapter() {
		return bluetoothAdapter;
	}
	
	/** Envoyer du texte à une socket Bluetooth */
	public static void envoyerTexte(BluetoothSocket socket, String texte) {
		try {
			OutputStream os = socket.getOutputStream();
			os.write(texte.getBytes(CHARSET_NAME));
		} catch (IOException e) {
			Log.e(TAG, "Erreur dans l'envoi d'un message texte", e);
		}
	}
	
	/** Recevoir du texte d'une socket Bluetooth */
	public static String recevoirTexte(BluetoothSocket socket) {
		String messageRecu = "";
		try {
			// Reception de TAILLE_BUFFER byte.
			InputStream is = socket.getInputStream();
			byte[] buffer = new byte[TAILLE_BUFFER];
			int res = is.read(buffer);
			if (res != -1) {
				messageRecu = new String(buffer, CHARSET_NAME);
			}
			// Si on dépasse TAILLE_BUFFER, alors on récupère la suite
			while (res == TAILLE_BUFFER) {
				buffer = new byte[TAILLE_BUFFER];
				res = is.read(buffer);
				if (res != -1) {
					String tmp = new String(buffer, CHARSET_NAME);
					messageRecu = messageRecu.concat(tmp);
				}
			}
		} catch (IOException e) {
			Log.e(TAG, "Erreur dans la réception du message texte", e);
		}
		return messageRecu;
	}
	
	/** Envoyer un objet à une socket Bluetooth */
	public static void envoyerObjet(BluetoothSocket socket, Object obj) {
		/* Plus d'informations sur le passage d'objet sérialisable : //http://stackoverflow.com/questions/2836646/java-serializable-object-to-byte-array */
		try {
			OutputStream os = socket.getOutputStream();
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(bos);  
			out.writeObject(obj);
			byte[] yourBytes = bos.toByteArray();
			os.write(yourBytes);
			out.close();
			bos.close();
		} catch (IOException e) {
			Log.e(TAG, "Erreur dans l'envoi d'un objet", e);
		}
	}
	
	/** Recevoir un objet d'une socket Bluetooth */
	public static Object recevoirObjet(BluetoothSocket socket) {
		// FIXME : traiter au cas où le buffer dépasse 1024
		Object obj = null;
		try {
			InputStream is = socket.getInputStream();
			
			byte[] buffer = new byte[TAILLE_BUFFER];
			int res = is.read(buffer);
			if (res != -1) {
				ByteArrayInputStream bis = new ByteArrayInputStream(buffer);
				ObjectInput in = null;
				try {
					in = new ObjectInputStream(bis);
					obj = in.readObject(); 
				} catch (ClassNotFoundException e) {
					Log.e(TAG, "Erreur dans la réception d'un objet", e);
				} finally {
					bis.close();
					in.close();
				}
			}
		} catch (IOException e) {
			Log.e(TAG, "Erreur dans la réception d'un objet", e);
		}
		return obj;
	}
}