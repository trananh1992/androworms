package com.androworms;

import java.util.List;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androworms.R;

public class ActiviteMultiJoueur extends Activity {
	
	private static final String TAG = "Androworms.ActiviteMultiJoueur";
	
	// Adaptateur du Bluetooth
	public static BluetoothAdapter mBluetoothAdapter;
	
	// Partie Bluetooth > Serveur : pour faire une animation du temps restant de la visibilité Bluetooth
	private MinuteurVisibiliteBluetooth ch;
	public static final int DUREE_VISIBILITE_BLUETOOTH = 120;
	
	// Listes des appareils Bluetooth jumélés et à proximité
	public List<BluetoothDevice> appareilJumele;
	public List<BluetoothDevice> appareilProximite;
	
	// Codes de demande de l'Intent sur l'activation/visibilité du Bluetooth
	public static final int DEMANDE_ACTIVATION_BLUETOOTH = 1;
	public static final int DEMANDE_VISIBILITE_BLUETOOTH = 2;
	
	// Mis à vrai si on est Bluetooth > Serveur et qu'on lance le serveur.
	// Dans le OnDetroy(), il faut pouvoir savoir si on a lancé le serveur ou pas pour l'arreter.
	public boolean estServeurLance = false;
	
	private ActiviteMultiJoueurFonctionsIHM fonctionsIHM;
	
	public boolean isServeur = false;
	
	// Thread pour la communication Bluetooth
	public ServeurConnexionBluetooth serveurConnexionBluetooth;
	public ClientConnexionBluetooth clientConnexionBluetooth;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Log.v(TAG, "Lancement de l'activité MultiJoueur");
		
		// Récupération de l'adaptateur Bluetooth
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		
		fonctionsIHM  = new ActiviteMultiJoueurFonctionsIHM(this);
		
		/* Choix entre Bluetooth et 2 joueurs */
		setContentView(R.layout.multi_joueur);
		
		ActiviteMultiJoueurEvent evenementMultiJoueur = new ActiviteMultiJoueurEvent(this);
		// Bouton partie Bluetooth
		ImageButton imgbtnBluetooth = (ImageButton)findViewById(R.id.imgbtn_bluetooth);
		imgbtnBluetooth.setOnClickListener(evenementMultiJoueur);
		// Bouton partie Multi-joueurs
		ImageButton imgbtnDeuxJoueurs = (ImageButton)findViewById(R.id.imgbtn_deux_joueurs);
		imgbtnDeuxJoueurs.setOnClickListener(evenementMultiJoueur);
	}
	
	/** Changement de vue entre le choix "Bluetooth/2-joueurs" vers "Bluetooth Client/Serveur" */
	public void changerVue(ActiviteMultiJoueurEvent evenementMultiJoueur) {
		setContentView(R.layout.multi_joueur_bluetooth);
		
		ImageButton imgbtnBluetoothServeur = (ImageButton)findViewById(R.id.imgbtn_bluetooth_serveur);
		imgbtnBluetoothServeur.setOnClickListener(evenementMultiJoueur);
		
		ImageButton imgbtnBluetoothClient = (ImageButton)findViewById(R.id.imgbtn_bluetooth_client);
		imgbtnBluetoothClient.setOnClickListener(evenementMultiJoueur);
	}
	
	/** Démarrage du serveur Bluetooth */
	public void demarrerServeurBluetooth() {
		Log.d(TAG, "DEMARAGE DU SERVEUR BLUETOOTH");
		serveurConnexionBluetooth = new ServeurConnexionBluetooth(this);
		serveurConnexionBluetooth.execute(this);
	}
	
	/** Démarrage du client Bluetooth */
	public void demarrerClientBluetooth(BluetoothDevice device) {
		Log.d(TAG, "DEMARAGE DU CLIENT BLUETOOTH");
		
		// Elements de l'interface
		ProgressBar pbBluetoothAnalyse = (ProgressBar)findViewById(R.id.pb_bluetooth_analyse);
		
		// On actualise l'interface graphique du client
		pbBluetoothAnalyse.setVisibility(View.VISIBLE);
		
		clientConnexionBluetooth = new ClientConnexionBluetooth(device);
		clientConnexionBluetooth.execute(this);
	}
	
	/** Gestion des demandes d'activation/visibilité du Bluetooth */
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case DEMANDE_ACTIVATION_BLUETOOTH:
			// On reçois une réponse de l'activation Bluetooth
			
			if (resultCode == Activity.RESULT_OK) {
				// L'utilisateur a accepté d'activé le Bluetooth
				if (isServeur) {
					// Si on est le serveur, on peut le démarrer
					demarrerServeurBluetooth();
				}
			} else {
				// L'utilisateur a refusé d'activer le Bluetooth (ou il s'agit d'une erreur)
				Log.v(TAG,"L'utilisateur a refusé d'activer le Bluetooth (ou il s'agit d'une erreur)");
			}
			
			// On actualise l'interface graphique dans les deux cas (sinon, le ToggleButton se met quand même à ON)
			if (isServeur) {
				fonctionsIHM.actualisationInterfaceBluetoothServeur();
			} else {
				fonctionsIHM.actualisationInterfaceBluetoothClient();
			}
			
			break;
		case DEMANDE_VISIBILITE_BLUETOOTH:
			// On reçois une réponse de l'activation de la visiblité du Bluetooth (uniquement pour le serveur Bluetooth)
			
			if (resultCode == DUREE_VISIBILITE_BLUETOOTH) {
				// L'utilisateur a accepté l'activation de la visiblité du Bluetooth
				// (C'est un petit bizarre que le resultCode soit égale à la DUREE_VISIBILITE_BLUETOOTH mais ça marche comme ça !)
				
				// On actualise l'interface graphique pour le minuteur
				actualisationMinuteur();
				
				// On start le minuteur
				Log.v(TAG, "Début du minuteur");
				ch = new MinuteurVisibiliteBluetooth();
				ch.execute(ActiviteMultiJoueur.this);
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
					// TODO : eventuellement mettre cette appreil en tête de liste.
					//        1) C'est comme ça que fait le système Android
					//        2) Statistiquement, un appareil jumélée et Bluetooth-proximité a de fortes chances d'être un joueur potentielle
					appareilProximite.add(device);
					Log.v(TAG,"...en plus je l'avais pas encore !");
				} else {
					Log.v(TAG,"...en fait, je l'avais déjà !");
				}
				
				fonctionsIHM.actualisationInterfaceBluetoothClient();
			} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
				// TODO : si aucun element : afficher "pas d'appareil à proximité"
				
				ProgressBar pbBluetoothAnalyse = (ProgressBar)findViewById(R.id.pb_bluetooth_analyse);
				pbBluetoothAnalyse.setVisibility(View.INVISIBLE);
				
				Button btnAnalyse = (Button)findViewById(R.id.btn_analyse);
				btnAnalyse.setEnabled(true);
			}
		}
	};
	
	/** Actualisation de l'affichage du minuteur */
	public void actualisationMinuteur() {
		TextView tvMaVisibilite = (TextView)findViewById(R.id.tv_maVisibilite);
		ProgressBar pbMinuteur = (ProgressBar)findViewById(R.id.pb_Minuteur);
		
		pbMinuteur.setVisibility(View.VISIBLE);
		tvMaVisibilite.setText("Ma visibilité : ");
	}
	
	@Override
	public void onStop() {
		super.onStop();
		Log.v(TAG,"onStop()");
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		Log.v(TAG,"onDestroy()");
		
		// On s'assure de désactiver l'analyse des périphériques Bluetooth
		if (mBluetoothAdapter != null) {
			mBluetoothAdapter.cancelDiscovery();
		}
		
		
		if (estServeurLance) {
			// On supprime le receiver qui permet de trouver les appareils Bluetooth à proximité
			this.unregisterReceiver(mReceiver);
			estServeurLance = false;
		}
		
		if (serveurConnexionBluetooth != null) {
			serveurConnexionBluetooth.cancel(true);
		}
		
		if (clientConnexionBluetooth != null) {
			clientConnexionBluetooth.cancel(true);
		}
		
		if (ch != null && ch.getStatus() == AsyncTask.Status.RUNNING) {
			ch.cancel(true);
		}
	}

	public ActiviteMultiJoueurFonctionsIHM getFonctionsIHM() {
		return fonctionsIHM;
	}
	
	/** Lancement du jeu en Bluetooth */
	public void lancerLeJeu() {
		Log.v(TAG, "=============================================");
		Log.v(TAG, "=====   Démarrage du jeu en Bluetooth   =====");
		Log.v(TAG, "=============================================");
		Intent intent = new Intent(this, ActiviteJeu.class);
		Bundle b = new Bundle();
		b.putBoolean("bluetooth", true);
		intent.putExtras(b);
		this.startActivity(intent);
	}
}