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
 * Cette classe inclue toutes les informations pour contacter un joueur en bluetooth.
 * 
 * 
 * Protocole de communication Bluetooth : (avec ENTETE_TAILLE_MESSAGE en constante)
 * Sections : [       EN-TÊTE         ][                MESSAGE                  ]
 * Taille   : [ ENTETE_TAILLE_MESSAGE ][   variable (présent dans l'en-tête)     ]
 * Contenu  : [  {taille du message}  ][               {message}                 ]
 * Plus d'informations sur : https://code.google.com/p/androworms/issues/detail?id=30
 * 
 * 
 * Plus d'informations sur le passage d'objet sérialisable : //http://stackoverflow.com/questions/2836646/java-serializable-object-to-byte-array
 */
public class Bluetooth extends Contact {
	
	private static final String TAG = "Androworms.Bluetooth";
	private static final int BLUETOOTH_QUALITE_TRANSMISSION_IMAGE = 100;
	
	// Taille de l'en-tête
	public static final int ENTETE_TAILLE_MESSAGE = 5;
	
	// Stockage de l'adaptateur Bluetooth de l'appareil
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
			os.write(creationEntete(texte.length()));
			os.write(texte.getBytes(Charset.defaultCharset()));
			os.flush();
		} catch (IOException e) {
			Log.e(TAG, "Erreur dans l'envoi d'un message texte", e);
		}
	}
	
	/** Recevoir du texte d'une socket Bluetooth */
	public static String recevoirTexte(BluetoothSocket socket) {
		int tailleMessage = lectureEnteteTailleMessage(socket);
		byte[] bufferComplet = lectureByte(socket, tailleMessage);
		return new String(bufferComplet, 0, bufferComplet.length, Charset.defaultCharset());
	}
	
	/** Envoyer un objet à une socket Bluetooth.
		L'objet doit être séréalisé. Plus d'informations sur le passage d'objet sérialisable : //http://stackoverflow.com/questions/2836646/java-serializable-object-to-byte-array */
	public static void envoyerObjet(BluetoothSocket socket, Object obj) {
		ByteArrayOutputStream bos = null;
		ObjectOutputStream out = null;
		try {
			OutputStream os = socket.getOutputStream();
			bos = new ByteArrayOutputStream();
			out = new ObjectOutputStream(bos);
			out.writeObject(obj);
			byte[] yourBytes = bos.toByteArray();
			os.write(creationEntete(yourBytes.length));
			os.write(yourBytes);
			os.flush();
		} catch (IOException e) {
			Log.e(TAG, "Erreur dans l'envoi d'un objet", e);
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (bos != null) {
					bos.close();
				}
			} catch (IOException e) {
				Log.e(TAG, "Erreur dans la fermeture des flux pour l'envoi d'un objet", e);
			}
		}
	}
	
	/** Recevoir un objet d'une socket Bluetooth */
	public static Object recevoirObjet(BluetoothSocket socket) {
		Object obj = null;
		
		int tailleMessage = lectureEnteteTailleMessage(socket);
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
			try {
				if (bis != null) {
					bis.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				Log.e(TAG, "Erreur dans la fermeture des flux pour la réception d'un objet", e);
			}
		}
		return obj;
	}
	
	/** Envoyer un objet à une socket Bluetooth */
	public static void envoyerImage(BluetoothSocket socket, Bitmap bm) {
		ByteArrayOutputStream bos = null;
		try {
			OutputStream os = socket.getOutputStream();
			bos = new ByteArrayOutputStream();
			bm.compress(Bitmap.CompressFormat.JPEG, BLUETOOTH_QUALITE_TRANSMISSION_IMAGE, bos);
			byte[] yourBytes = bos.toByteArray();
			os.write(creationEntete(yourBytes.length));
			os.write(yourBytes);
			os.flush();
		} catch (IOException e) {
			Log.e(TAG, "Erreur dans l'envoi d'un objet", e);
		} finally {
			try {
				if (bos != null) {
					bos.close();
				}
			} catch (IOException e) {
				Log.e(TAG, "Erreur dans la fermeture des flux pour l'envoi d'une image", e);
			}
		}
	}
	
	/** Recevoir un objet d'une socket Bluetooth */
	public static Bitmap recevoirImage(BluetoothSocket socket) {
		int tailleMessage = lectureEnteteTailleMessage(socket);
		byte[] bufferComplet = lectureByte(socket, tailleMessage);
		return BitmapFactory.decodeByteArray(bufferComplet, 0, bufferComplet.length);
	}
	
	/** Création de l'en-tête du message */
	public static byte[] creationEntete(int tailleMessage) {
		// L'en-tête est composé de la taille du message et transmis sous forme de string sur ENTETE_TAILLE_MESSAGE caractères
		String tailleMessageString = String.valueOf(tailleMessage);
		if (tailleMessageString.length() > ENTETE_TAILLE_MESSAGE) {
			Log.e(TAG, "ERREUR !! La constante ENTETE_TAILLE_MESSAGE est trop petite !!!");
			Log.e(TAG, "ERREUR !! Actuellement elle est de "+ENTETE_TAILLE_MESSAGE+" alors qu'elle devrait être à au moins "+tailleMessageString.length());
		}
		StringBuffer res = new StringBuffer();
		while ((res.length()+tailleMessageString.length()) < ENTETE_TAILLE_MESSAGE) {
			res.append("0");
		}
		res.append(tailleMessageString);
		return res.toString().getBytes(Charset.defaultCharset());
	}
	
	/** Lecture du prefixe du message qui contient la taille du message */
	public static int lectureEnteteTailleMessage(BluetoothSocket socket) {
		byte[] buffer = lectureByte(socket, ENTETE_TAILLE_MESSAGE);
		
		String messageRecu = new String(buffer, 0, buffer.length, Charset.defaultCharset());
		return Integer.parseInt(messageRecu);
	}
	
	/** Lecture des bytes émis sur la socket et qui sont de taille tailleMessage */
	public static byte[] lectureByte(BluetoothSocket socket, int tailleMessage) {
		// Création du buffer de destination des bytes
		byte[] buffer = new byte[tailleMessage];
		// Taille qui va être lu à chaque itération
		int tailleLu = 0;
		// Taille total qui ont été lu
		int tailleTotalLu = 0;
		
		try {
			InputStream is = socket.getInputStream();
			
			// Si le flux de données est grand, le message ne pourra être lu d'un coup et il sera segmenté par Android.
			// On connait avec certitude la taille du message, donc il suffit d'itérer pour obtenir toutes les parties
			while (tailleTotalLu < tailleMessage) {
				tailleLu = is.read(buffer, tailleTotalLu, tailleMessage-tailleTotalLu);
				if (tailleLu == -1) {
					Log.e(TAG, "ERREUR ?");
				}
				if (tailleLu < tailleMessage) {
					Log.v(TAG, "Segmentation du message => (tailleLu/tailleTotalLu/TailleMessage)");
					Log.v(TAG, "Segmentation du message -> ("+tailleLu+"/"+tailleTotalLu+"/"+tailleMessage+")");
				}
				tailleTotalLu += tailleLu;
			}
		} catch (IOException e) {
			Log.e(TAG, "Erreur dans la réception d'un objet", e);
		}
		
		return buffer;
	}
}