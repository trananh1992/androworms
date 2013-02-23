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


/*
 * Cette classe inclue toutes les informations pour contacter
 * un joueur en bluetooth.
 */
public class Bluetooth extends Contact {
	
	private static final String TAG = "Androworms.Bluetooth";
	
	public Bluetooth(String adresse) {
		super();
		BluetoothAdapter ba = BluetoothAdapter.getDefaultAdapter();
		init(ba, adresse);
	}
	
	public static void envoyerTexte(BluetoothSocket socket, String texte) {
		try {
			OutputStream os = socket.getOutputStream();
			os.write(texte.getBytes());
		} catch (IOException e) {
			Log.e(TAG, "Erreur dans l'envoi d'un message texte", e);
		}
	}
	
	public static String recevoirTexte(BluetoothSocket socket) {
		String messageRecu = null;
		try {
			InputStream is = socket.getInputStream();
			byte[] buffer = new byte[1024];
			is.read(buffer);
			messageRecu = new String(buffer);
		} catch (IOException e) {
			Log.e(TAG, "Erreur dans la réception du message texte", e);
		}
		return messageRecu;
	}
	
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
	
	public static Object recevoirObjet(BluetoothSocket socket) {
		Object obj = null;
		try {
			InputStream is = socket.getInputStream();

			byte[] buffer = new byte[1024];
			is.read(buffer);

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
		} catch (IOException e) {
			Log.e(TAG, "Erreur dans la réception d'un objet", e);
		}
		return obj;
	}
}