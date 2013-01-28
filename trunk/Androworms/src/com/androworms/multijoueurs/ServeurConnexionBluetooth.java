package com.androworms.multijoueurs;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.androworms.Contact;
import com.androworms.ImageInformation;
import com.androworms.Personnage;

/** Lors de la création d'une partie en Multi-joueur en Bluetooth,
 *  ce Thread est crée pour crée pour tourner et attendre les connexions clientes.
 */
public class ServeurConnexionBluetooth extends Thread {
	
	private static final String TAG_SERVEUR = "Androworms.ServeurConnexionBluetooth";
	
	// On crée une socket Bluetooth pour le serveur lors de l'initalisation du jeu pour attendre les connexions clientes.
	// Une fois que le jeu est démarré, on a plus besoin de cette socket (le serveur de connexion est fermé).
	private BluetoothServerSocket socketServeur;
	
	// Liste des sockets des clients du jeu.
	public List<BluetoothSocket> socketClient;
	
	ActiviteMultiJoueur activiteMultiJoueur;
	
	public ServeurConnexionBluetooth(ActiviteMultiJoueur activiteMultiJoueur) {
		
		Log.v(TAG_SERVEUR, "Création de la socket publique");
		
		this.activiteMultiJoueur = activiteMultiJoueur;
		
		// Intialisation de la liste des sockets clients
		socketClient = new ArrayList<BluetoothSocket>();
		
		try {
			// Création de la socket avec le UUID (définit aléatoiremenet pour l'application)
			socketServeur = BluetoothAdapter.getDefaultAdapter().listenUsingRfcommWithServiceRecord("Androworms", Contact.ANDROWORMS_UUID);
		} catch (Exception e) {
			Log.e(TAG_SERVEUR, "Erreur sur la creation de la socket publique ");
			Log.e(TAG_SERVEUR, "\t"+e.getMessage());
		}
	}
	
	public void run() {
		BluetoothSocket socket = null;
		
		while (true) {
			try {
				socket = socketServeur.accept();
				
				if (socket.getRemoteDevice() != null) {
					Log.v(TAG_SERVEUR, "Le serveur a accepté une connexion ! (device=" + socket.getRemoteDevice().getName() + ")");
				}
				else {
					Log.e(TAG_SERVEUR,"Le serveur a accepté une connexion ! Mais ERREUR le device est est null");
				}
				
				// On ajoute le client dans la liste sur l'interface du client
				// FIXME : ne marche pas encore !
				activiteMultiJoueur.getFonctionsIHM().addNewPlayer(socket.getRemoteDevice());
				
				// On ajoute la socket à la liste des sockets clients du serveur
				socketClient.add(socket);
				
				manageConnectedSocket(socket);
				
			} catch (Exception e) {
				Log.v(TAG_SERVEUR, "Erreur dans la reception d'une connexion cliente sur le ServeurConnexionBluetooth");
				break;
			}
		}
	}
	
	private void manageConnectedSocket(BluetoothSocket mmSocket) {
		receiveMessage(mmSocket);
		sendMessage(mmSocket);
	}
	
	private void sendMessage(BluetoothSocket mmSocket) {
		/* Plus d'informations sur le passage d'objet sérialisable : //http://stackoverflow.com/questions/2836646/java-serializable-object-to-byte-array */
		try {
			Log.v(TAG_SERVEUR,"Je suis le SERVEUR et je vais envoyer un objet 'Personnage' !");
			
			OutputStream os = mmSocket.getOutputStream();
			
			Personnage p = new Personnage("Chuck Norris", new ImageInformation());
			
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(bos);  
			out.writeObject(p);
			byte[] yourBytes = bos.toByteArray();
			os.write(yourBytes);
			out.close();
			bos.close();
		} catch (IOException e) {
			Log.e(TAG_SERVEUR, e.getMessage());
		}
	}
	
	private void receiveMessage(BluetoothSocket mmSocket) {
		try {
			InputStream is = mmSocket.getInputStream();
			
			byte[] buffer = new byte[1024];
			is.read(buffer);
			
			String message_recu = new String(buffer);
			Log.d(TAG_SERVEUR, "MESSAGE RECU : " + message_recu);
			
		} catch (IOException e) {
			Log.e(TAG_SERVEUR, e.getMessage());
		}
	}
	
	/** Lorsque l'on arrête le serveur de connexion Bluetooth, il faut fermer la socket */
	public void arret() {
		try {
			socketServeur.close();
		} catch (IOException e) {
			
		}
	}
}