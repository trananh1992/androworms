package com.androworms;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.androworms.utile.Informations;

public class ActiviteCreationPartieBluetoothServeur {
	
	private static final String TAG = "Androworms.ActiviteCreationPartie";
	private ActiviteCreationPartie activiteCreationPartie;
	private ActiviteCreationPartieBluetooth activiteCreationPartieBluetooth;
	
	public ActiviteCreationPartieBluetoothServeur(ActiviteCreationPartieBluetooth activiteCreationPartieBluetooth) {
		this.activiteCreationPartieBluetooth = activiteCreationPartieBluetooth;
		this.activiteCreationPartie = activiteCreationPartieBluetooth.getActiviteCreationPartie();
	}
	
	/** Chargement de l'interface Bluetooth > Serveur **/
	public void chargementInterfaceBluetoothServeur() {
		
		Log.v(TAG, "Chargement de l'interface : Bluetooth > Serveur");
		
		// Définition des composants
		ToggleButton tgEtatBluetooth = (ToggleButton)activiteCreationPartie.findViewById(R.id.tg_EtatBluetoothS);
		Button btnDemarrerPartie = (Button)activiteCreationPartie.findViewById(R.id.btn_demarrerPartie);
		Button btnMontrerBluetooth = (Button)activiteCreationPartie.findViewById(R.id.btn_MontrerBluetooth);
		
		// Actualisation des éléments de l'interface graphique du Serveur
		actualisationInterfaceBluetoothServeur();
		
		/** Actions sur les composants **/
		tgEtatBluetooth.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			// FIXME : ce bouton peut ne pas indiquer l'état réel du Bluetooth
			// Exemple : on désactive le Bluetooth en Alt/Tab
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					// Activation du Bluetooth (2 possibilités)
					// La première est de faire une Intent pour demander à l'utilisateur s'il est d'accord.
					Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
					activiteCreationPartie.startActivityForResult(enableBtIntent, ActiviteCreationPartieBluetooth.DEMANDE_ACTIVATION_BLUETOOTH_SERVEUR);
					// La seconde consiste à l'activer l'activer directement sans accord de l'utilisateur : mBluetoothAdapter.enable();
					// La documentation est formel sur le sujet : IL EST INTERDIT DE FAIRE LA METHODE 2 !
					// cf http://developer.android.com/reference/android/bluetooth/BluetoothAdapter.html#enable()
					
					// On refresh les infos
					actualisationInterfaceBluetoothServeur();
				} else {
					// Désactivation du Bluetooth
					BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
					mBluetoothAdapter.disable();
					// On refresh les infos
					actualisationInterfaceBluetoothServeur();
					// TODO : NE MARCHE PAS COMME IL DEVRAIT
					// ce refresh devrait vider la liste. mais je pense que la désactivation du Bletooth prend quelques secondes
					// et que du coup, la liste ne se vide pas.
				}
			}
		});
		
		/* Montrer Bluetooth */
		btnMontrerBluetooth.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Log.v(TAG, "On rendre le Bluetooth visible pour x secondes");
				Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
				discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, ActiviteCreationPartieBluetooth.DUREE_VISIBILITE_BLUETOOTH);
				//startActivity(discoverableIntent);
				activiteCreationPartie.startActivityForResult(discoverableIntent, ActiviteCreationPartieBluetooth.DEMANDE_VISIBILITE_BLUETOOTH_SERVEUR);
				
				// On refresh les infos
				actualisationInterfaceBluetoothServeur();
			}
		});
		
		/* On démarrer la partie */
		btnDemarrerPartie.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Log.v(TAG,"On démarre la partie ?");
				if (activiteCreationPartieBluetooth.serveurConnexionBluetooth == null) {
					Log.v(TAG,"Vous n'avez jamais lancer le serveur");
				} else {
					Log.v(TAG,"Arret du Thread");
					
					// On arrête le serveur de connexion Bluetooth
					activiteCreationPartieBluetooth.serveurConnexionBluetooth.cancel(true);
					
					Log.v("TAG","Je suis le SERVEUR et je clos le serveur ! Terminé les inscriptions ! ! on va joueur...");
					
					// TODO le serveur doit dire aux clients qu'on commence à jouer !
					
					activiteCreationPartie.passerEtape3DepuisServeurBluetooth();
				}
			}
		});
		
		/** Démarrage du serveur Bluetooth **/
		if (Informations.isBluetoothOn()) {
			activiteCreationPartieBluetooth.demarrerServeurBluetooth();
		}
	}
	
	/** Actualisation de l'interface Bluetooth > Serveur **/
	public void actualisationInterfaceBluetoothServeur() {
		Log.v(TAG, "Actualisation de l'interface : Bluetooth > Serveur");
		
		/** Définition des composants **/
		ToggleButton tgEtatBluetooth = (ToggleButton)activiteCreationPartie.findViewById(R.id.tg_EtatBluetoothS);
		TextView tvMonNomBluetooth = (TextView)activiteCreationPartie.findViewById(R.id.tv_monNomBluetooth);
		TextView tvMaVisibilite = (TextView)activiteCreationPartie.findViewById(R.id.tv_maVisibilite);
		TextView tvInformation = (TextView)activiteCreationPartie.findViewById(R.id.tv_information);
		Button btnMontrerBluetooth = (Button)activiteCreationPartie.findViewById(R.id.btn_MontrerBluetooth);
		ProgressBar pbMinuteur = (ProgressBar)activiteCreationPartie.findViewById(R.id.pb_Minuteur);
		ProgressBar pbAttenteConnexion = (ProgressBar)activiteCreationPartie.findViewById(R.id.pb_AttenteConnexion);
		TextView tvAttenteConnexion = (TextView)activiteCreationPartie.findViewById(R.id.tv_AttenteConnexion);
		
		/** Configuration des composants **/
		// Désactivé si l'appareil est pas compatible Bluetooth :: TODO encore utile ??
		tgEtatBluetooth.setEnabled(Informations.isCompatibleBluetooth());
		// Coché si le Bluetooth est déjà activé
		tgEtatBluetooth.setChecked(Informations.isBluetoothOn());
		
		//btn_demarrerPartie.setEnabled(false);
				
		if (!Informations.isBluetoothOn()) {
			// Le Bluetooth n'est pas actif
			
			// Elements visible + configuration
			tvInformation.setVisibility(View.VISIBLE);
			tvInformation.setText("Veuillez activer le Bluetooth pour pouvoir jouer !");
			
			// Elements masqués
			btnMontrerBluetooth.setVisibility(View.INVISIBLE);
			tvMonNomBluetooth.setVisibility(View.INVISIBLE);
			tvMaVisibilite.setVisibility(View.INVISIBLE);
			pbMinuteur.setVisibility(View.INVISIBLE);
			pbAttenteConnexion.setVisibility(View.INVISIBLE);
			tvAttenteConnexion.setVisibility(View.INVISIBLE);
			
		} else {
			// Le Bluetooth est actif
			
			
			// Elements visible + configuration
			btnMontrerBluetooth.setVisibility(View.VISIBLE);
			
			tvMonNomBluetooth.setVisibility(View.VISIBLE);
			tvMonNomBluetooth.setText("Mon nom : "+ActiviteCreationPartieBluetooth.mBluetoothAdapter.getName());
			
			tvMaVisibilite.setVisibility(View.VISIBLE);
			String tt = "Ma visibilité : ";
			if (ActiviteCreationPartieBluetooth.mBluetoothAdapter.getScanMode() == BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
				tt += "visible";
			} else {
				tt += "invisible";
			}
			tvMaVisibilite.setText(tt);
			
			pbMinuteur.setVisibility(View.INVISIBLE);
			
			pbAttenteConnexion.setVisibility(View.VISIBLE);
			tvAttenteConnexion.setVisibility(View.VISIBLE);
			
			
			// Elements masqués
			tvInformation.setVisibility(View.INVISIBLE);
		}
	}
}