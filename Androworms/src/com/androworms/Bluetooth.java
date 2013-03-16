package com.androworms;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

/**
 * Cette classe inclue toutes les informations pour contacter
 * un joueur en bluetooth.
 * 
 * Message Bluetooth :
 * [taille du message][contenu du message] ou message peut-être un string, un objet ou une image
 * La première section est de taille fix et définie. */
public class Bluetooth extends Contact {
	
	private static final String TAG = "Androworms.Bluetooth";
	
	// La taille de la section 1 contenant la taille du message (section 2).
	public static final int NOMBRE_BYTE_SECTION_TAILLE = 5;
	
	// Stockage ici de l'adaptateur Bluetooth de l'appareil
	private static BluetoothAdapter bluetoothAdapter = null;
	
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
			os.write(generationTailleMessage(texte.length()));
			os.write(texte.getBytes(Charset.defaultCharset()));
			os.flush();
		} catch (IOException e) {
			Log.e(TAG, "Erreur dans l'envoi d'un message texte", e);
		}
	}
	
	/** Recevoir du texte d'une socket Bluetooth */
	public static String recevoirTexte(BluetoothSocket socket) {
		int tailleMessage = lireTailleMessage(socket);
		byte[] bufferComplet = lectureByte(socket, tailleMessage);
		String messageRecu = new String(bufferComplet, 0, bufferComplet.length, Charset.defaultCharset());
		
		return messageRecu;
	}
	
	/** Envoyer un objet à une socket Bluetooth.
		L'objet doit être séréalisé. Plus d'informations sur le passage d'objet sérialisable : //http://stackoverflow.com/questions/2836646/java-serializable-object-to-byte-array */
	public static void envoyerObjet(BluetoothSocket socket, Object obj) {
		ObjectOutputStream out = null;
		try {
			OutputStream os = socket.getOutputStream();
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			out = new ObjectOutputStream(bos);
			out.writeObject(obj);
			byte[] yourBytes = bos.toByteArray();
			os.write(generationTailleMessage(bos.size()));
			os.write(yourBytes);
			os.flush();
			out.close();
			bos.close();
			
			Log.v(TAG, "envoyerObjet -> taille = " + yourBytes.length);
			
		} catch (IOException e) {
			Log.e(TAG, "Erreur dans l'envoi d'un objet", e);
		}
	}
	
	/** Recevoir un objet d'une socket Bluetooth */
	public static Object recevoirObjet(BluetoothSocket socket) {
		Object obj = null;
		
		int tailleMessage = lireTailleMessage(socket);
		byte[] bufferComplet = lectureByte(socket, tailleMessage);
		
		ByteArrayInputStream bis = new ByteArrayInputStream(bufferComplet);
		ObjectInput in = null;
		try {
			in = new ObjectInputStream(bis);
			obj = in.readObject();
		} catch (IOException e) {
			Log.e(TAG, "Erreur dans la réception d'un objet", e);
		} catch (ClassNotFoundException e) {
			Log.e(TAG, "Erreur dans la réception d'un objet", e);
		} finally {
			Log.v(TAG, "finally");
			try {
				if (bis != null) {
					bis.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return obj;
	}
	
	/** Envoyer un objet à une socket Bluetooth */
	public static void envoyerImage(BluetoothSocket socket, Bitmap bm) {
		try {
			OutputStream os = socket.getOutputStream();
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
			byte[] yourBytes = bos.toByteArray();
			os.write(generationTailleMessage(bos.size()));
			os.write(yourBytes);
			bos.close();
			Log.v(TAG, "envoyerObjet -> taille = " + yourBytes.length);
		} catch (IOException e) {
			Log.e(TAG, "Erreur dans l'envoi d'un objet", e);
		}
	}
	
	/** Recevoir un objet d'une socket Bluetooth */
	public static Bitmap recevoirImage(BluetoothSocket socket) {
		int tailleMessage = lireTailleMessage(socket);
		byte[] bufferComplet = lectureByte(socket, tailleMessage);
		Bitmap bm = BitmapFactory.decodeByteArray(bufferComplet, 0, bufferComplet.length);
		return bm;
	}
    
	/** ----------------------------- */
	public static byte[] generationTailleMessage(int tailleMessage) {
		String taille = String.valueOf(tailleMessage);
		String res = "0";
		while ((res.length()+taille.length()) < NOMBRE_BYTE_SECTION_TAILLE) {
			res += "0";
		}
		res += taille;
		return res.getBytes(Charset.defaultCharset());
	}
	
	/** ----------------------------- */
	public static int lireTailleMessage(BluetoothSocket socket) {
		int taille = -1;
		try {
			InputStream is = socket.getInputStream();
			
			byte[] buffer = new byte[NOMBRE_BYTE_SECTION_TAILLE];
			int nbCarLu = is.read(buffer);
			if (nbCarLu == -1) {
				Log.e(TAG, "ERREUR ?");
			}
			String messageRecu = new String(buffer, 0, buffer.length, Charset.defaultCharset());
			taille = Integer.parseInt(messageRecu);
			
		} catch (IOException e) {
			Log.e(TAG, "Erreur dans la réception d'un objet", e);
		}
		return taille;
	}
	
	/** Lecture des bytes */
	public static byte[] lectureByte(BluetoothSocket socket, int tailleMessage) {
		byte[] buffer = new byte[tailleMessage];
		int tailleLu = 0;
		
		try {
			InputStream is = socket.getInputStream();
			
			buffer = new byte[tailleMessage];
			tailleLu = is.read(buffer);
			if (tailleLu == -1) {
				Log.e(TAG, "ERREUR ?");
			}
			
		} catch (IOException e) {
			Log.e(TAG, "Erreur dans la réception d'un objet", e);
		}
		
		return buffer;
	}
}