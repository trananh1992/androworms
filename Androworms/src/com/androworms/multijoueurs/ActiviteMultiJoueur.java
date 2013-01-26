package com.androworms.multijoueurs;

import java.util.ArrayList;
import java.util.UUID;

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
	
	// Identifiant (généré aléatoirement) pour Androworms.
	// Il sert à établir la connexion Bluetooth entre 2 appareils
	public static UUID ANDROWORMS_UUID = UUID.fromString("c2d080da-7610-4dde-96c1-01406eb4b24b");
	
	// Adaptateur du Bluetooth
	public static BluetoothAdapter mBluetoothAdapter;
	
	// Partie Bluetooth > Serveur : pour faire une animation du temps restant de la visibilité Bluetooth
	private Minuteur ch;
	public static final int DUREE_VISIBILITE_BLUETOOTH = 40;
	
	// Listes des appareils Bluetooth jumélés et à proximité
	public static ArrayList<BluetoothDevice> appareilJumele;
	public static ArrayList<BluetoothDevice> appareilProximite;
	
	// Codes de demande de l'Intent sur l'activation/visibilité du Bluetooth
	public static final int DEMANDE_ACTIVATION_BLUETOOTH = 1;
	public static final int DEMANDE_VISIBILITE_BLUETOOTH = 2;
	
	
	public static boolean estRegister = false;
	
	private FonctionsIHM fonctionsIHM;
	
	public boolean isServeur = false;
	
	// Thread pour la communication Bluetooth
	public ServeurBluetooth sb;
	public ClientBluetooth cb;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Log.v(TAG, "Lancement de l'activité MultiJoueur");
		
		// Récupération de l'adaptateur Bluetooth
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		
		fonctionsIHM  = new FonctionsIHM(this);
		
		/* Choix entre Bluetooth et 2 joueurs */
		setContentView(R.layout.multi_joueur);
		
		// Bouton partie Bluetooth
		ImageButton imgbtnBluetooth = (ImageButton)findViewById(R.id.imgbtn_bluetooth);
		imgbtnBluetooth.setOnClickListener(new EvenementMultiJoueur(this));
		// Bouton partie Multi-joueurs
		ImageButton imgbtnDeuxJoueurs = (ImageButton)findViewById(R.id.imgbtn_deux_joueurs);
		imgbtnDeuxJoueurs.setOnClickListener(new EvenementMultiJoueur(this));
	}
	
	/** Changement de vue entre le choix "Bluetooth/2-joueurs" vers "Bluetooth Client/Serveur" **/
	public void changerVue() {
		setContentView(R.layout.multi_joueur_bluetooth);
		
		ImageButton imgbtn_bluetooth_serveur = (ImageButton)findViewById(R.id.imgbtn_bluetooth_serveur);
		imgbtn_bluetooth_serveur.setOnClickListener(new EvenementMultiJoueur(this));
		
		ImageButton imgbtn_bluetooth_client = (ImageButton)findViewById(R.id.imgbtn_bluetooth_client);
		imgbtn_bluetooth_client.setOnClickListener(new EvenementMultiJoueur(this));
	}
	
	/** Démarrage du serveur Bluetooth **/
	public void demarrerServeurBluetooth() {
		Log.d(TAG, "DEMARAGE DU SERVEUR BLUETOOTH");
		sb = new ServeurBluetooth(this);
		sb.start();
	}
	
	/** Démarrage du client Bluetooth **/
	public void demarrerClientBluetooth(BluetoothDevice device) {
		Log.d(TAG, "DEMARAGE DU CLIENT BLUETOOTH");
		cb = new ClientBluetooth(device);
		cb.start();
	}
	
	/** Gestion des demandes d'activation/visibilité du Bluetooth **/
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
			
			if (resultCode == DUREE_VISIBILITE_BLUETOOTH) { // Houlà c'est bizarre, mais j'ai l'impression que ça marche comme ça !!
				// L'utilisateur a accepté l'activation de la visiblité du Bluetooth
				
				// On actualise l'interface graphique pour le minuteur
				refresh_UI_minuteur();
				
				// On start le minuteur
				Log.v(TAG, "Début du minuteur");
				ch = new Minuteur();
				ch.execute(ActiviteMultiJoueur.this);
			} else {
				// L'utilisateur a refusé l'activation de la visiblité du Bluetooth (ou il s'agit d'une erreur)
				Log.v(TAG,"L'utilisateur a refusé l'activation de la visiblité du Bluetooth");
			}
			
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
					ActiviteMultiJoueur.appareilProximite.add(device);
					Log.v(TAG,"...en plus je l'avais pas encore !");
				} else {
					Log.v(TAG,"...en fait, je l'avais déjà !");
				}
				
				fonctionsIHM.actualisationInterfaceBluetoothClient();
			} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
				// TODO : si aucun element : afficher "pas d'appareil à proximité"
				
				ProgressBar pb = (ProgressBar)findViewById(R.id.pb_bluetooth_analyse);
				pb.setVisibility(View.INVISIBLE);
				
				Button btn_analyse = (Button)findViewById(R.id.btn_analyse);
				btn_analyse.setEnabled(true);
			}
		}
	};
	
	public void refresh_UI_minuteur() {
		TextView tv_maVisibilite = (TextView)findViewById(R.id.tv_maVisibilite);
		ProgressBar pb_minuteur = (ProgressBar)findViewById(R.id.pb_Minuteur);
		
		pb_minuteur.setVisibility(View.VISIBLE);
		tv_maVisibilite.setText("Ma visibilité : ");
	}
	
	@Override
	public void onStop() {
		super.onStop();
		
		Log.v(TAG,"onStop()");
		
		if (ch != null && ch.getStatus() == AsyncTask.Status.RUNNING) {
			ch.cancel(true);
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		Log.v(TAG,"onDestroy()");
		
		// On s'assure de désactiver l'analyse des périphériques Bluetooth
		if (mBluetoothAdapter != null) {
			mBluetoothAdapter.cancelDiscovery();
		}
		
		
		if (estRegister) {
			// On supprime le receiver qui permet de trouver les appareils Bluetooth à proximité
			this.unregisterReceiver(mReceiver);
			estRegister = false;
		}
	}

	public FonctionsIHM getFonctionsIHM() {
		return fonctionsIHM;
	}
}