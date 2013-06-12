package com.androworms;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

/** Création d'une partie Bluetooth (liée à l'activité de création de partie)
 */
public class ActiviteCreationPartieBluetooth {
	
	private static final String TAG = "Androworms.ActiviteCreationPartie.Bluetooth";
	
	// Codes de demande de l'Intent sur l'activation/visibilité du Bluetooth
	public static final int DEMANDE_ACTIVATION_BLUETOOTH_SERVEUR = 1;
	public static final int DEMANDE_DESACTIVATION_BLUETOOTH_SERVEUR = 2;
	public static final int DEMANDE_ACTIVATION_BLUETOOTH_CLIENT = 3;
	public static final int DEMANDE_DESACTIVATION_BLUETOOTH_CLIENT = 4;
	public static final int DEMANDE_VISIBILITE_BLUETOOTH_SERVEUR = 5;
	
	// Activité de création de la partie
	private ActiviteCreationPartie activiteCreationPartie;
	private ActiviteCreationPartieBluetoothServeur activiteCreationPartieBluetoothServeur;
	private ActiviteCreationPartieBluetoothClient activiteCreationPartieBluetoothClient;
	
	// Partie Bluetooth > Serveur : pour faire une animation du temps restant de la visibilité Bluetooth
	private TacheMinuteurVisibiliteBluetooth tacheMinuteurVisiviliteBlueooth;
	public static final int DUREE_VISIBILITE_BLUETOOTH = 120;
	
	
	// Mis à vrai si on est Bluetooth > Serveur et qu'on lance le serveur.
	// Dans le OnDestroy(), il faut pouvoir savoir si on a lancé le serveur ou pas pour l'arreter.
	private boolean serveurLance = false;
	
	
	// Thread pour la communication Bluetooth
	private TacheServeurConnexionBluetooth serveurConnexionBluetooth;
	private TacheClientConnexionBluetooth clientConnexionBluetooth;
	
	
	/** Constructeur de ActiviteCreationPartieBluetooth */
	public ActiviteCreationPartieBluetooth(ActiviteCreationPartie activiteCreationPartie) {
		this.activiteCreationPartie = activiteCreationPartie;
		this.activiteCreationPartieBluetoothServeur = new ActiviteCreationPartieBluetoothServeur(this);
		this.activiteCreationPartieBluetoothClient = new ActiviteCreationPartieBluetoothClient(this);
	}
	
	/** Obtenir l'activite de création de partie */
	public ActiviteCreationPartie getActiviteCreationPartie() {
		return activiteCreationPartie;
	}
	
	/** Obtenir le fichier d'interface du Serveur Bluetooth */
	public ActiviteCreationPartieBluetoothServeur getServeur() {
		return activiteCreationPartieBluetoothServeur;
	}
	
	/** Obtenir le fichier d'interface du Client Bluetooth */
	public ActiviteCreationPartieBluetoothClient getClient() {
		return activiteCreationPartieBluetoothClient;
	}
	
	public TacheServeurConnexionBluetooth getServeurConnexionBluetooth() {
		return serveurConnexionBluetooth;
	}
	
	
	/** Gestion des demandes d'activation/visibilité du Bluetooth */
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case DEMANDE_ACTIVATION_BLUETOOTH_SERVEUR:
			// On reçois une réponse de l'activation Bluetooth
			if (resultCode == Activity.RESULT_OK) {
				// L'utilisateur a accepté d'activé le Bluetooth
				
				// Si on est le serveur, on peut le démarrer
				demarrerServeurBluetooth();
			} else {
				// L'utilisateur a refusé d'activer le Bluetooth (ou il s'agit d'une erreur)
				Log.v(TAG,"L'utilisateur a refusé d'activer le Bluetooth (ou il s'agit d'une erreur)");
			}
			
			// On actualise l'interface graphique dans les deux cas (sinon, le ToggleButton se met quand même à ON)
			getServeur().actualisationInterfaceBluetoothServeur();
			
			break;
		case DEMANDE_DESACTIVATION_BLUETOOTH_SERVEUR:
			getServeur().actualisationInterfaceBluetoothServeur();
			break;
		case DEMANDE_ACTIVATION_BLUETOOTH_CLIENT:
			// On reçois une réponse de l'activation Bluetooth
			if (resultCode == Activity.RESULT_OK) {
				// L'utilisateur a accepté d'activé le Bluetooth
				
				getClient().listerAppareilsJumeles();
			
			} else {
				// L'utilisateur a refusé d'activer le Bluetooth (ou il s'agit d'une erreur)
				Log.v(TAG,"L'utilisateur a refusé d'activer le Bluetooth (ou il s'agit d'une erreur)");
			}
			
			// On actualise l'interface graphique dans les deux cas (sinon, le ToggleButton se met quand même à ON)
			getClient().actualisationInterfaceBluetoothClient();
			
			break;
		case DEMANDE_DESACTIVATION_BLUETOOTH_CLIENT:
			getClient().actualisationInterfaceBluetoothClient();
			break;
		case DEMANDE_VISIBILITE_BLUETOOTH_SERVEUR:
			// On reçois une réponse de l'activation de la visiblité du Bluetooth (uniquement pour le serveur Bluetooth)
			
			if (resultCode == DUREE_VISIBILITE_BLUETOOTH) {
				// L'utilisateur a accepté l'activation de la visiblité du Bluetooth
				// (C'est un petit bizarre que le resultCode soit égale à la DUREE_VISIBILITE_BLUETOOTH mais ça marche comme ça !)
				
				// On actualise l'interface graphique pour le minuteur
				getServeur().actualisationMinuteur();
				
				// On start le minuteur
				Log.v(TAG, "Début du minuteur");
				tacheMinuteurVisiviliteBlueooth = new TacheMinuteurVisibiliteBluetooth(this);
				tacheMinuteurVisiviliteBlueooth.executerTacheEnParralelle();
			} else {
				// L'utilisateur a refusé l'activation de la visiblité du Bluetooth (ou il s'agit d'une erreur)
				Log.v(TAG,"L'utilisateur a refusé l'activation de la visiblité du Bluetooth");
			}
			
			break;
		default:
			break;
		}
	}
	
	/** Pour chercher les appareils Bluetooth à proximité */
	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			
			// Quand l'analyse permet de découvrir un appareil
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				// On récupère l'objet BluetoothDevice depuis l'intent
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				Log.v(TAG,"j'ai trouvé un appareil du nom de "+device.getName()+" ("+device.getAddress()+")");
				
				// On regarde si on est jumélé avec cette appareil
				if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
					// TODO : éventuellement mettre cette appreil en tête de liste.
					//        1) C'est comme ça que fait le système Android
					//        2) Statistiquement, un appareil jumélée et Bluetooth-proximité a de fortes chances d'être un joueur potentielle
					getClient().addAppareilProximite(device);
					Log.v(TAG,"...en plus je l'avais pas encore !");
				} else {
					Log.v(TAG,"...en fait, je l'avais déjà !");
				}
				
				activiteCreationPartieBluetoothClient.actualisationInterfaceBluetoothClient();
			} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
				// TODO : si aucun element : afficher "pas d'appareil à proximité"
				
				ProgressBar pbBluetoothAnalyse = (ProgressBar)activiteCreationPartie.findViewById(R.id.pb_bluetooth_analyse);
				pbBluetoothAnalyse.setVisibility(View.INVISIBLE);
				
				Button btnAnalyse = (Button)activiteCreationPartie.findViewById(R.id.btn_analyse);
				btnAnalyse.setEnabled(true);
				
				TextView tvMessage = (TextView)activiteCreationPartie.findViewById(R.id.tv_message);
				tvMessage.setText(R.string.selectionner_appareil_Bluetooth);
			}
		}
	};
	
	/** Pour chercher les appareils Bluetooth à proximité */
	public BroadcastReceiver getmReceiver() {
		return mReceiver;
	}
	
	/** Démarrage du serveur Bluetooth */
	public void demarrerServeurBluetooth() {
		Log.d(TAG, "DEMARAGE DU SERVEUR BLUETOOTH");
		serveurConnexionBluetooth = new TacheServeurConnexionBluetooth(this);
		serveurConnexionBluetooth.execute();
	}
	
	/** Démarrage du client Bluetooth */
	public void demarrerClientBluetooth(BluetoothDevice device) {
		Log.d(TAG, "DEMARAGE DU CLIENT BLUETOOTH");
		
		// Elements de l'interface
		ProgressBar pbBluetoothAnalyse = (ProgressBar)activiteCreationPartie.findViewById(R.id.pb_bluetooth_analyse);
		TextView tvMessage = (TextView)activiteCreationPartie.findViewById(R.id.tv_message);
		
		// On actualise l'interface graphique du client
		pbBluetoothAnalyse.setVisibility(View.VISIBLE);
		tvMessage.setText(R.string.tentative_connexion_en_cours);
		
		clientConnexionBluetooth = new TacheClientConnexionBluetooth(this, device);
		clientConnexionBluetooth.execute();
	}
	
	/** Savoir si le serveur Bluetooth est lancé */
	public boolean isServeurLance() {
		return serveurLance;
	}
	
	/** Définir si le serveur Bluetooth est lancé */
	public void setServeurLance(boolean serveurLance) {
		this.serveurLance = serveurLance;
	}
	
	/** Lors de l'arrêt de l'activité, il faut arrêter proprement toutes les opérations en cours */
	protected void onDestroy() {
		// On s'assure de désactiver l'analyse des périphériques Bluetooth
		if (Bluetooth.getBluetoothAdapter() != null) {
			Bluetooth.getBluetoothAdapter().cancelDiscovery();
		}
		
		if (isServeurLance()) {
			// On supprime le receiver qui permet de trouver les appareils Bluetooth à proximité
			activiteCreationPartie.unregisterReceiver(mReceiver);
			setServeurLance(false);
		}
		
		if (serveurConnexionBluetooth != null) {
			Log.v("Androworms.TacheServeurConnexionBluetooth", "onDestroy() -> fermetureConnexionsForce()");
			serveurConnexionBluetooth.fermetureConnexionsForce();
		}
		
		if (clientConnexionBluetooth != null) {
			clientConnexionBluetooth.cancel(true);
		}
		
		if (tacheMinuteurVisiviliteBlueooth != null && tacheMinuteurVisiviliteBlueooth.getStatus() == AsyncTask.Status.RUNNING) {
			tacheMinuteurVisiviliteBlueooth.cancel(true);
		}
	}
}