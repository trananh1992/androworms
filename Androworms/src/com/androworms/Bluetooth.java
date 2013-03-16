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
 */
public class Bluetooth extends Contact {
	
	private static final String TAG = "Androworms.Bluetooth";
	
	// La taille du Buffer par défaut
	private static final int TAILLE_BUFFER = 1024;
	
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
			os.write(texte.getBytes(Charset.defaultCharset()));
			os.flush();
		} catch (IOException e) {
			Log.e(TAG, "Erreur dans l'envoi d'un message texte", e);
		}
	}

	/** Recevoir du texte d'une socket Bluetooth */
	public static String recevoirTexte(BluetoothSocket socket) {
		Log.v(TAG, "recevoirTexte()");
		
		byte[] bufferComplet = lectureByte(socket);
		String messageRecu = new String(bufferComplet, 0, bufferComplet.length, Charset.defaultCharset());
		
		return messageRecu;
	}
    
	/** Envoyer un objet à une socket Bluetooth */
	public static void envoyerObjet(BluetoothSocket socket, Object obj) {
		Log.v(TAG, "envoyerObjet()");
		/*
		 * Plus d'informations sur le passage d'objet sérialisable : //http://stackoverflow.com/questions/2836646/java-serializable-object-to-byte-array
		 */
		ObjectOutputStream out = null;
		try {
			OutputStream os = socket.getOutputStream();
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			out = new ObjectOutputStream(bos);
			out.writeObject(obj);
			byte[] yourBytes = bos.toByteArray();
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
		Log.v(TAG, "recevoirObjet()");
		Object obj = null;
			
		byte[] bufferComplet = lectureByte(socket);

		Log.v(TAG, "__next__");
		
		ByteArrayInputStream bis = new ByteArrayInputStream(bufferComplet);
		ObjectInput in = null;
		try {
			Log.v(TAG, "__object__(1)");
			in = new ObjectInputStream(bis);
			Log.v(TAG, "__object__(2)");
			obj = in.readObject();
			Log.v(TAG, "__object__(3)");
		} catch (IOException e) {
			Log.e(TAG, "Erreur dans la réception d'un objet (3)", e);
		} catch (ClassNotFoundException e) {
			Log.e(TAG, "Erreur dans la réception d'un objet (1)", e);
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
		
		Log.v(TAG, "__done__");
		
		return obj;
	}
	
	/** Envoyer un objet à une socket Bluetooth */
	public static void envoyerImage(BluetoothSocket socket, Bitmap bm) {
		try {
			OutputStream os = socket.getOutputStream();
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
			byte[] yourBytes = bos.toByteArray();
			os.write(yourBytes);
			bos.close();
			Log.v(TAG, "envoyerObjet -> taille = " + yourBytes.length);
		} catch (IOException e) {
			Log.e(TAG, "Erreur dans l'envoi d'un objet", e);
		}
	}

	/** Recevoir un objet d'une socket Bluetooth */
	public static Bitmap recevoirImage(BluetoothSocket socket) {
		byte[] bufferComplet = lectureByte(socket);
		Log.v(TAG, "_image_");
		Bitmap bm = BitmapFactory.decodeByteArray(bufferComplet, 0, bufferComplet.length);
		Log.v(TAG, "__done__");
		return bm;
	}
    
	/** Lecture des bytes */
	public static byte[] lectureByte(BluetoothSocket socket) {
		byte[] buffer = new byte[0];
		byte[] saveBuffer1 = new byte[0];
		byte[] saveBuffer2 = new byte[0];
		byte[] bufferComplet = null;
		int tailleLu = 0;
		int tailleTotal = 0;
		int nbBoucle = 0;
		boolean resteEncoreALire = true;
		
		try {
			InputStream is = socket.getInputStream();

			while (resteEncoreALire) {
				Log.v(TAG, "nbBoucle = " + nbBoucle);
				resteEncoreALire = false;

				if (tailleTotal > 0) {
					Log.v(TAG, "if [tailleTotal > 0] -> VRAI");
					if (nbBoucle % 2 == 1) {
						Log.v(TAG, "if [nbBoucle % 2 == 1] -> VRAI");
						Log.v(TAG, "On utilise le [saveBuffer1] qui est de taille [" + (TAILLE_BUFFER * nbBoucle) + "]");
						saveBuffer1 = new byte[TAILLE_BUFFER * nbBoucle];
						if (saveBuffer2.length > 0) {
							Log.v(TAG, "if [saveBuffer2.length > 0] -> VRAI");
							Log.v(TAG, "on copie [saveBuffer2] dans [saveBuffer1]");
							Log.v(TAG, "on copie [saveBuffer2] dans [saveBuffer1]    taille=(" + saveBuffer2.length + ";" + saveBuffer1.length + ")");
							Log.v(TAG, "on copie " + (TAILLE_BUFFER * (nbBoucle - 1)) + " caractres à partir de 0");
							System.arraycopy(saveBuffer2, 0, saveBuffer1, 0, TAILLE_BUFFER * (nbBoucle - 1));
						} else {
							Log.v(TAG, "if [saveBuffer2.length > 0] -> FAUX");
							Log.v(TAG, "saveBuffer2 n'est pas encore utilisé");
						}
						Log.v(TAG, "on copie [buffer] dans [saveBuffer1]    taille=(" + buffer.length + ";" + saveBuffer1.length + ")");
						Log.v(TAG, "on copie " + (TAILLE_BUFFER) + " caractres à partir de " + (TAILLE_BUFFER * (nbBoucle - 1)));
						System.arraycopy(buffer, 0, saveBuffer1, TAILLE_BUFFER * (nbBoucle - 1), TAILLE_BUFFER);
					} else {
						Log.v(TAG, "if [nbBoucle % 2 == 1] -> FAUX");
						Log.v(TAG, "On utilise le [saveBuffer2] qui est de taille [" + (TAILLE_BUFFER * nbBoucle) + "]");
						saveBuffer2 = new byte[TAILLE_BUFFER * nbBoucle];
						Log.v(TAG, "on copie [saveBuffer1] dans [saveBuffer2]    taille=(" + saveBuffer1.length + ";" + saveBuffer2.length + ")");
						Log.v(TAG, "on copie " + (TAILLE_BUFFER * (nbBoucle - 1)) + " caractres à partir de 0");
						System.arraycopy(saveBuffer1, 0, saveBuffer2, 0, TAILLE_BUFFER * (nbBoucle - 1));
						Log.v(TAG, "on copie [buffer] dans [saveBuffer2]    taille=(" + buffer.length + ";" + saveBuffer2.length + ")");
						Log.v(TAG, "on copie " + (TAILLE_BUFFER) + " caractres à partir de " + (TAILLE_BUFFER * (nbBoucle - 1)));
						System.arraycopy(buffer, 0, saveBuffer2, TAILLE_BUFFER * (nbBoucle - 1), TAILLE_BUFFER);
					}

				} else {
					Log.v(TAG, "if [tailleTotal > 0] -> FAUX");
				}

				buffer = new byte[TAILLE_BUFFER];
				tailleLu = is.read(buffer);
				Log.v(TAG, "tailleLu = " + tailleLu);
				if (tailleLu == -1) {
					Log.v(TAG, "if [tailleLu == -1] -> VRAI");
					Log.e(TAG, "ERREUR ?");
				}
				tailleTotal += tailleLu;
				Log.v(TAG, "tailleTotal = " + tailleTotal);
				Log.v(TAG, "is.available() = " + is.available());
				if (is.available() > 0 && tailleLu == TAILLE_BUFFER) {
					Log.v(TAG, "if [tailleLu == TAILLE_BUFFER] -> VRAI");
					Log.v(TAG, "on fera encore une itération de boucle");
					resteEncoreALire = true;
					nbBoucle++;
				} else {
					Log.v(TAG, "if [tailleLu == TAILLE_BUFFER] -> FAUX");
					Log.v(TAG, "on fera plus d'itération de boucle");
				}
			}

			Log.v(TAG, "*** FIN DE LA LECTURE ***");

			bufferComplet = new byte[tailleTotal];
			Log.v(TAG, "tailleTotal = " + tailleTotal);
			if (nbBoucle % 2 == 1) {
				Log.v(TAG, "if [nbBoucle % 2 == 1] -> VRAI");
				System.arraycopy(saveBuffer1, 0, bufferComplet, 0, TAILLE_BUFFER * nbBoucle);
				System.arraycopy(buffer, 0, bufferComplet, TAILLE_BUFFER * nbBoucle, tailleLu);
			} else {
				Log.v(TAG, "if [nbBoucle % 2 == 1] -> FAUX");
				System.arraycopy(saveBuffer2, 0, bufferComplet, 0, TAILLE_BUFFER * nbBoucle);
				System.arraycopy(buffer, 0, bufferComplet, TAILLE_BUFFER * nbBoucle, tailleLu);
			}

			Log.v(TAG, "*** FIN DU BUFFER-REEL ***");
			
		} catch (IOException e) {
			Log.e(TAG, "Erreur dans la réception d'un objet", e);
		}
		
		return bufferComplet;
	}
}