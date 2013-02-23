package com.androworms;

import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androworms.ui.BluetoothCustomAdapter;

public class ActiviteCreationPartieBluetooth {
	
	private static final String TAG = "Androworms.ActiviteCreationPartie.Bluetooth";
	
	// Codes de demande de l'Intent sur l'activation/visibilité du Bluetooth
	public static final int DEMANDE_ACTIVATION_BLUETOOTH_SERVEUR = 1;
	public static final int DEMANDE_ACTIVATION_BLUETOOTH_CLIENT = 2;
	public static final int DEMANDE_VISIBILITE_BLUETOOTH_SERVEUR = 3;
	
	// Activité de création de la partie
	private ActiviteCreationPartie activiteCreationPartie;
	private ActiviteCreationPartieBluetoothServeur activiteCreationPartieBluetoothServeur;
	private ActiviteCreationPartieBluetoothClient activiteCreationPartieBluetoothClient;
	
	// Adaptateur du Bluetooth
	public static BluetoothAdapter mBluetoothAdapter;
	
	// Partie Bluetooth > Serveur : pour faire une animation du temps restant de la visibilité Bluetooth
	private TacheMinuteurVisibiliteBluetooth ch;
	public static final int DUREE_VISIBILITE_BLUETOOTH = 120;
	
	// Listes des appareils Bluetooth jumélés et à proximité
	public List<BluetoothDevice> appareilJumele;
	public List<BluetoothDevice> appareilProximite;
	
	// Mis à vrai si on est Bluetooth > Serveur et qu'on lance le serveur.
	// Dans le OnDetroy(), il faut pouvoir savoir si on a lancé le serveur ou pas pour l'arreter.
	private boolean serveurLance = false;
	
	
	// Thread pour la communication Bluetooth
	public TacheServeurConnexionBluetooth serveurConnexionBluetooth;
	public TacheClientConnexionBluetooth clientConnexionBluetooth;
	
	
	/** Constructeur de ActiviteCreationPartieBluetooth */
	public ActiviteCreationPartieBluetooth(ActiviteCreationPartie activiteCreationPartie) {
		this.activiteCreationPartie = activiteCreationPartie;
		this.activiteCreationPartieBluetoothServeur = new ActiviteCreationPartieBluetoothServeur(this);
		this.activiteCreationPartieBluetoothClient = new ActiviteCreationPartieBluetoothClient(this);
		
		// Récupération de l'adaptateur Bluetooth
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	}
	
	public ActiviteCreationPartie getActiviteCreationPartie() {
		return activiteCreationPartie;
	}
	
	
	
	
	/** Afficher l'interface du serveur */
	public void chargementInterfaceBluetoothServeur() {
		activiteCreationPartieBluetoothServeur.chargementInterfaceBluetoothServeur();
	}
	
	/** Afficher l'interface du client */
	public void chargementInterfaceBluetoothClient() {
		activiteCreationPartieBluetoothClient.chargementInterfaceBluetoothClient();
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
		tvMessage.setText("Tentative de connexion en cours...");
		
		clientConnexionBluetooth = new TacheClientConnexionBluetooth(this, device);
		clientConnexionBluetooth.execute();
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
			activiteCreationPartieBluetoothServeur.actualisationInterfaceBluetoothServeur();
			
			break;
		case DEMANDE_ACTIVATION_BLUETOOTH_CLIENT:
			// On reçois une réponse de l'activation Bluetooth
			if (resultCode == Activity.RESULT_OK) {
				// L'utilisateur a accepté d'activé le Bluetooth
				
				listerAppareilsJumeles();
			
			} else {
				// L'utilisateur a refusé d'activer le Bluetooth (ou il s'agit d'une erreur)
				Log.v(TAG,"L'utilisateur a refusé d'activer le Bluetooth (ou il s'agit d'une erreur)");
			}
			
			// On actualise l'interface graphique dans les deux cas (sinon, le ToggleButton se met quand même à ON)
			activiteCreationPartieBluetoothClient.actualisationInterfaceBluetoothClient();
			
			break;
		case DEMANDE_VISIBILITE_BLUETOOTH_SERVEUR:
			// On reçois une réponse de l'activation de la visiblité du Bluetooth (uniquement pour le serveur Bluetooth)
			
			if (resultCode == DUREE_VISIBILITE_BLUETOOTH) {
				// L'utilisateur a accepté l'activation de la visiblité du Bluetooth
				// (C'est un petit bizarre que le resultCode soit égale à la DUREE_VISIBILITE_BLUETOOTH mais ça marche comme ça !)
				
				// On actualise l'interface graphique pour le minuteur
				actualisationMinuteur();
				
				// On start le minuteur
				Log.v(TAG, "Début du minuteur");
				ch = new TacheMinuteurVisibiliteBluetooth(this);
				ch.executerTacheEnParralelle();
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
	public final BroadcastReceiver mReceiver = new BroadcastReceiver() {
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
					appareilProximite.add(device);
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
	
	
	/** Actualisation de l'affichage du minuteur */
	public void actualisationMinuteur() {
		TextView tvMaVisibilite = (TextView)activiteCreationPartie.findViewById(R.id.tv_maVisibilite);
		ProgressBar pbMinuteur = (ProgressBar)activiteCreationPartie.findViewById(R.id.pb_Minuteur);
		
		pbMinuteur.setVisibility(View.VISIBLE);
		tvMaVisibilite.setText("Ma visibilité : ");
	}
	
	/** Liste les appareils bluetooth jumélés **/
	public void listerAppareilsJumeles() {
		Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
		if (pairedDevices.size() > 0) {
			appareilJumele.clear();
			for (BluetoothDevice device : pairedDevices) {
				// J'ajoute les appareils jumelés à une liste
				appareilJumele.add(device);
			}
		}
	}
	
	/** Actualise la liste des appreils Bluetooth **/
	public void rafraichirListeAppareils() {
		final ListView lv = (ListView)activiteCreationPartie.findViewById(R.id.liste_appareils_bluetooth);
		
		// On vide la liste pour éventullement la remplir après
		lv.setAdapter(null);
		ArrayAdapter<BluetoothDevice> adapterA,adapterB;
		BluetoothDevice[] valuesA, valuesB;
		
		
		valuesA = new BluetoothDevice[appareilJumele.size()];
		for (int j=0;j<appareilJumele.size();j++) {
			BluetoothDevice device = appareilJumele.get(j);
			valuesA[j] = device;
		}
		valuesB = new BluetoothDevice[appareilProximite.size()];
		for (int j=0;j<appareilProximite.size();j++) {
			BluetoothDevice device = appareilProximite.get(j);
			valuesB[j] = device;
		}
		
		// FIXME : j'ai pas compris les paramètres 2 et 3 du ArrayAdapter
		// android.R.layout.simple_list_item_checked       simple_list_item_activated_1
		adapterA = new ArrayAdapter<BluetoothDevice>(activiteCreationPartie, android.R.layout.simple_list_item_checked, valuesA);
		adapterB = new ArrayAdapter<BluetoothDevice>(activiteCreationPartie, android.R.layout.simple_list_item_checked, valuesB);
		
		BluetoothCustomAdapter adapter = new BluetoothCustomAdapter(activiteCreationPartie);
		
		adapter.ajouterSections("Appareils appairés (jumelés ?)", adapterA);
		adapter.ajouterSections("Appareils à proximité", adapterB);
		
		lv.setAdapter(adapter);
	}
	
	public boolean isServeurLance() {
		return serveurLance;
	}

	public void setServeurLance(boolean serveurLance) {
		this.serveurLance = serveurLance;
	}
	
	protected void onDestroy() {
		// On s'assure de désactiver l'analyse des périphériques Bluetooth
		if (mBluetoothAdapter != null) {
			mBluetoothAdapter.cancelDiscovery();
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
		
		if (ch != null && ch.getStatus() == AsyncTask.Status.RUNNING) {
			ch.cancel(true);
		}
	}
}