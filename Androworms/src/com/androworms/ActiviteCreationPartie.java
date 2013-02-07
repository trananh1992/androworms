package com.androworms;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ActiviteCreationPartie extends Activity {
	
	private static final String TAG = "Androworms.ActiviteCreationPartie";
	
	private static final int MODE_SOLO = 1;
	private static final int MODE_2JOUEURS = 2;
	private static final int MODE_BLUETOOTH_SERVEUR = 3;
	private static final int MODE_BLUETOOTH_CLIENT = 4;
	private static final int MODE_WIFI_SERVEUR = 5;
	private static final int MODE_WIFI_CLIENT = 6;
	
	private ActiviteCreationPartieBluetooth activiteCreationPartieBluetooth;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v(TAG, "Début de la création de la partie");
		etape1();
	}
	
	private void etape1() {
		setContentView(R.layout.activite_creation_partie_1_mode);
		
		Button btnPartieSolo = (Button)findViewById(R.id.btn_partie_solo);
		btnPartieSolo.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				etape2ModeSolo();
			}
		});
		Button btnMultiJoueur = (Button)findViewById(R.id.btn_multi_joueur);
		btnMultiJoueur.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				etape2ModeDeuxjoueurs();
			}
		});
		Button btnBluetoothCreer = (Button)findViewById(R.id.btn_bluetooth_creer);
		btnBluetoothCreer.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				etape2ModeBluetoothServeur();
			}
		});
		Button btnBluetoothRejoindre = (Button)findViewById(R.id.btn_bluetooth_rejoindre);
		btnBluetoothRejoindre.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				etape2ModeBluetoothClient();
			}
		});
		Button btnWifiCreer = (Button)findViewById(R.id.btn_wifi_creer);
		btnWifiCreer.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				etape2ModeWifiServeur();
			}
		});
		Button btnWifiRejoindre = (Button)findViewById(R.id.btn_wifi_rejoindre);
		btnWifiRejoindre.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				etape2ModeWifiClient();
			}
		});
		
		Button btnSuivant = (Button)findViewById(R.id.btn_suivant);
		btnSuivant.setEnabled(false);
	}
	
	private void etape2ModeSolo() {
		/* Affichage de la vue */
		setContentView(R.layout.activite_creation_partie_2_solo);
		
		Button btnPrecedent = (Button)findViewById(R.id.btn_precedent);
		btnPrecedent.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				etape1();
			}
		});
		
		Button btnSuivant = (Button)findViewById(R.id.btn_suivant);
		btnSuivant.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				etape3(ActiviteCreationPartie.MODE_SOLO);
			}
		});
	}
	
	private void etape2ModeDeuxjoueurs() {
		// Non disponible pour le moment
		new AlertDialog.Builder(this).setTitle("Androworms").setMessage("Les parties 2 joueurs ne sont pas encore dispo !").setNeutralButton("Close", null).show();
	}
	
	private void etape2ModeBluetoothServeur() {
		activiteCreationPartieBluetooth = new ActiviteCreationPartieBluetooth(this);
		/* Affichage de la vue */
		setContentView(R.layout.multi_joueur_bluetooth_serveur);
		/* Chargement des composants */
		activiteCreationPartieBluetooth.chargementInterfaceBluetoothServeur();
	}
	
	private void etape2ModeBluetoothClient() {
		activiteCreationPartieBluetooth = new ActiviteCreationPartieBluetooth(this);
		/* Affichage de la vue */
		setContentView(R.layout.multi_joueur_bluetooth_client);
		/* Chargement des composants */
		activiteCreationPartieBluetooth.chargementInterfaceBluetoothClient();
	}
	
	private void etape2ModeWifiServeur() {
		// Non disponible pour le moment
		new AlertDialog.Builder(this).setTitle("Androworms").setMessage("Les parties en Wifi-Direct ne sont pas encore dispo !").setNeutralButton("Close", null).show();
	}
	
	private void etape2ModeWifiClient() {
		// Non disponible pour le moment
		new AlertDialog.Builder(this).setTitle("Androworms").setMessage("Les parties en Wifi-Direct ne sont pas encore dispo !").setNeutralButton("Close", null).show();
	}
	
	private void etape3(final int mode) {
		/* Affichage de la vue */
		setContentView(R.layout.activite_creation_partie_3);
		
		Button btnPrecedent = (Button)findViewById(R.id.btn_precedent);
		btnPrecedent.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				switch (mode) {
				case ActiviteCreationPartie.MODE_SOLO:
					etape2ModeSolo();
					break;
				case ActiviteCreationPartie.MODE_2JOUEURS:
					etape2ModeDeuxjoueurs();
					break;
				case ActiviteCreationPartie.MODE_BLUETOOTH_SERVEUR:
					etape2ModeBluetoothServeur();
					break;
				case ActiviteCreationPartie.MODE_BLUETOOTH_CLIENT:
					etape2ModeBluetoothClient();
					break;
				case ActiviteCreationPartie.MODE_WIFI_SERVEUR:
					etape2ModeWifiClient();
					break;
				case ActiviteCreationPartie.MODE_WIFI_CLIENT:
					etape2ModeWifiClient();
					break;
				default :
					etape1();
				}
			}
		});
		
		Button btnSuivant = (Button)findViewById(R.id.btn_suivant);
		btnSuivant.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				etape4(mode);
			}
		});
	}
	
	private void etape4(final int mode) {
		setContentView(R.layout.activite_creation_partie_4);
		
		Button btnPrecedent = (Button)findViewById(R.id.btn_precedent);
		btnPrecedent.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				etape3(mode);
			}
		});
		
		Button btnDemarrerPartie = (Button)findViewById(R.id.btn_demarrer_la_partie);
		btnDemarrerPartie.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(ActiviteCreationPartie.this, ActiviteJeu.class);
				startActivity(intent);
				/* On arrête l'application comme ça quand on sera sur la partie de jeu et qu'on fait la flèche de "retour à l'activité précédente",
				   on arrivera sur le menu principal */
				finish();
			}
		});
	}
	
	@Override
	public void onStop() {
		super.onStop();
		Log.v(TAG,"onStop()");
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		// Destroy des éléments du Bluetooth
		activiteCreationPartieBluetooth.onDestroy();
	}
	
	/** DEBUT DE SECTION : Fonctions pour le Bluetooth */
	
	/** Gestion des demandes d'activation/visibilité du Bluetooth */
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		activiteCreationPartieBluetooth.onActivityResult(requestCode, resultCode, data);
	}
	
	public void passerEtape3DepuisServeurBluetooth() {
		etape3(ActiviteCreationPartie.MODE_BLUETOOTH_SERVEUR);
	}
	
	public void passerEtape3DepuisClientBluetooth() {
		etape3(ActiviteCreationPartie.MODE_BLUETOOTH_CLIENT);
	}
	
	/** FIN DE SECTION : Fonctions pour le Bluetooth */
}