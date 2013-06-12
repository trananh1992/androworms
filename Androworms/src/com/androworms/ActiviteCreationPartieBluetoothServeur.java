package com.androworms;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

/** Activité de gestion de la création d'une partie Blueooth.
 * Cette activité est l'activité exécuté sur l'appareil qui fais serveur Bluetoth
 */
public class ActiviteCreationPartieBluetoothServeur {
	
	private static final String TAG = "Androworms.ActiviteCreationPartie.Bluetooth.Serveur";
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
		Button btnMontrerBluetooth = (Button)activiteCreationPartie.findViewById(R.id.btn_MontrerBluetooth);
		Button btnSuivant = (Button)activiteCreationPartie.findViewById(R.id.btn_suivant);
		
		// Actualisation des éléments de l'interface graphique du Serveur
		actualisationInterfaceBluetoothServeur();
		
		/** Actions sur les composants **/
		tgEtatBluetooth.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					// Activation du Bluetooth (2 possibilités)
					// La première est de faire une Intent pour demander à l'utilisateur s'il est d'accord.
					Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
					activiteCreationPartie.startActivityForResult(enableBtIntent, ActiviteCreationPartieBluetooth.DEMANDE_ACTIVATION_BLUETOOTH_SERVEUR);
					// La seconde consiste à l'activer l'activer directement sans accord de l'utilisateur : mBluetoothAdapter.enable();
					// La documentation est formel sur le sujet : IL EST INTERDIT DE FAIRE LA METHODE 2 !
					// cf http://developer.android.com/reference/android/bluetooth/BluetoothAdapter.html#enable()
				} else {
					// Désactivation du Bluetooth
					Bluetooth.getBluetoothAdapter().disable();
					
					// On lance une surveillance du changement d'état du Bluetooth car c'est une opération asynchrone.
					Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_STATE_CHANGED);
					activiteCreationPartie.startActivityForResult(enableBtIntent, ActiviteCreationPartieBluetooth.DEMANDE_DESACTIVATION_BLUETOOTH_SERVEUR);
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
				
				// On refresh les informations
				actualisationInterfaceBluetoothServeur();
			}
		});
		
		/* on passe à l'étape suivante */
		btnSuivant.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (activiteCreationPartieBluetooth.getServeurConnexionBluetooth() == null) {
					Log.v(TAG,"Vous n'avez jamais lancer le serveur");
				} else {
					
					AlertDialog.Builder builder = new AlertDialog.Builder(activiteCreationPartie);
					builder.setMessage(R.string.message_bluetooth_serveur_confirmation_continuer);
					builder.setCancelable(false);
					builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							// On arrête le serveur de connexion Bluetooth
							Log.v(TAG,"Arret du serveur de connexion Bluetooth");
							activiteCreationPartieBluetooth.getServeurConnexionBluetooth().fermetureConnexionsForce();
						}
					});
					builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
						}
					});
					AlertDialog alert = builder.create();
					alert.show();
				}
			}
		});
		
		/** Démarrage du serveur Bluetooth **/
		if (Bluetooth.getBluetoothAdapter().isEnabled()) {
			activiteCreationPartieBluetooth.demarrerServeurBluetooth();
		}
	}
	
	/** Actualisation de l'interface Bluetooth > Serveur **/
	public void actualisationInterfaceBluetoothServeur() {
		Log.v(TAG, "Actualisation de l'interface : Bluetooth > Serveur");
		
		/** Définition des composants **/
		ToggleButton tgEtatBluetooth = (ToggleButton)activiteCreationPartie.findViewById(R.id.tg_EtatBluetoothS);
		TextView tvMonNomBluetooth = (TextView)activiteCreationPartie.findViewById(R.id.tv_mon_nom_bluetooth);
		TextView tvMaVisibilite = (TextView)activiteCreationPartie.findViewById(R.id.tv_maVisibilite);
		TextView tvInformation = (TextView)activiteCreationPartie.findViewById(R.id.tv_message);
		Button btnMontrerBluetooth = (Button)activiteCreationPartie.findViewById(R.id.btn_MontrerBluetooth);
		ProgressBar pbMinuteur = (ProgressBar)activiteCreationPartie.findViewById(R.id.pb_Minuteur);
		ProgressBar pbAttenteConnexion = (ProgressBar)activiteCreationPartie.findViewById(R.id.pb_AttenteConnexion);
		
		/** Configuration des composants **/
		// Cocher si le Bluetooth est déjà activé
		tgEtatBluetooth.setChecked(Bluetooth.getBluetoothAdapter().isEnabled());
		
		
		
		if (!Bluetooth.getBluetoothAdapter().isEnabled()) {
			// Le Bluetooth n'est pas actif
			
			// Elements visible + configuration
			tvInformation.setVisibility(View.VISIBLE);
			tvInformation.setText(R.string.message_bluetooth_activer_bluetooth);
			
			// Elements masqués
			btnMontrerBluetooth.setVisibility(View.INVISIBLE);
			tvMonNomBluetooth.setVisibility(View.INVISIBLE);
			tvMaVisibilite.setVisibility(View.INVISIBLE);
			pbMinuteur.setVisibility(View.INVISIBLE);
			pbAttenteConnexion.setVisibility(View.INVISIBLE);
			
		} else {
			// Le Bluetooth est actif
			
			
			// Elements visible + configuration
			btnMontrerBluetooth.setVisibility(View.VISIBLE);
			
			tvMonNomBluetooth.setVisibility(View.VISIBLE);
			tvMonNomBluetooth.setText(R.string.mon_nom + Bluetooth.getBluetoothAdapter().getName());
			
			tvMaVisibilite.setVisibility(View.VISIBLE);
			String maVisibilite = tvMaVisibilite.getContext().getString(R.string.ma_visibilite);
			if (Bluetooth.getBluetoothAdapter().getScanMode() == BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
				maVisibilite += R.string.visibilite_visible;
			} else {
				maVisibilite += R.string.visibilite_invisible;
			}
			tvMaVisibilite.setText(maVisibilite);
			
			pbMinuteur.setVisibility(View.INVISIBLE);
			
			pbAttenteConnexion.setVisibility(View.VISIBLE);
			
			tvInformation.setVisibility(View.VISIBLE);
			tvInformation.setText(R.string.message_bluetooth_serveur_attente_connexion);
		}
	}
	
	/** Actualisation de l'affichage du minuteur */
	public void actualisationMinuteur() {
		TextView tvMaVisibilite = (TextView)activiteCreationPartie.findViewById(R.id.tv_maVisibilite);
		ProgressBar pbMinuteur = (ProgressBar)activiteCreationPartie.findViewById(R.id.pb_Minuteur);
		
		pbMinuteur.setVisibility(View.VISIBLE);
		tvMaVisibilite.setText(R.string.ma_visibilite);
	}
	
	/** L'utilisateur demande à revenir au statut précédent.
	 *  Dans certains cas, on veux afficher une popup pour lui demander s'il veux vraiment quitter */
	public void faireActionPrecedent() {
		ListView lv = (ListView)activiteCreationPartie.findViewById(R.id.lv_appareils_bluetoothS);
		
		if (lv.getCount() > 0) {
			// Un utilisateur à rejoint la partie. On demande confirmation à l'utilisateur pour revenir au statut précédent.
			AlertDialog.Builder builder = new AlertDialog.Builder(activiteCreationPartie);
			builder.setMessage(R.string.message_bluetooth_serveur_confirmation_quitter);
			builder.setCancelable(false);
			builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					activiteCreationPartie.etapePrecedente(true);
				}
			});
			builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();
				}
			});
			AlertDialog alert = builder.create();
			alert.show();
		}
		else {
			// Aucune véritable action de l'utilisateur, donc il peut retourner au statut précédent
			activiteCreationPartie.etapePrecedente(true);
		}
	}
}