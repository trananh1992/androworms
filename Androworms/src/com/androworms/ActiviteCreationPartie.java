package com.androworms;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;

public class ActiviteCreationPartie extends Activity {
	
	private static final String TAG = "Androworms.ActiviteCreationPartie";
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
				ParametresPartie.getParametresPartie().setModeJeu(ParametresPartie.MODE_SOLO);
				etape2();
			}
		});
		Button btnMultiJoueur = (Button)findViewById(R.id.btn_multi_joueur);
		btnMultiJoueur.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ParametresPartie.getParametresPartie().setModeJeu(ParametresPartie.MODE_2JOUEURS);
				etape2();
			}
		});
		Button btnBluetoothCreer = (Button)findViewById(R.id.btn_bluetooth_creer);
		btnBluetoothCreer.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ParametresPartie.getParametresPartie().setModeJeu(ParametresPartie.MODE_BLUETOOTH_SERVEUR);
				etape2();
			}
		});
		Button btnBluetoothRejoindre = (Button)findViewById(R.id.btn_bluetooth_rejoindre);
		btnBluetoothRejoindre.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ParametresPartie.getParametresPartie().setModeJeu(ParametresPartie.MODE_BLUETOOTH_CLIENT);
				etape2();
			}
		});
		Button btnWifiCreer = (Button)findViewById(R.id.btn_wifi_creer);
		btnWifiCreer.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ParametresPartie.getParametresPartie().setModeJeu(ParametresPartie.MODE_WIFI_SERVEUR);
				etape2();
			}
		});
		Button btnWifiRejoindre = (Button)findViewById(R.id.btn_wifi_rejoindre);
		btnWifiRejoindre.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ParametresPartie.getParametresPartie().setModeJeu(ParametresPartie.MODE_WIFI_CLIENT);
				etape2();
			}
		});
	}
	
	private void etape2() {
		switch (ParametresPartie.getParametresPartie().getModeJeu()) {
		case ParametresPartie.MODE_SOLO:
			etape2ModeSolo();
			break;
		case ParametresPartie.MODE_2JOUEURS:
			etape2ModeDeuxjoueurs();
			break;
		case ParametresPartie.MODE_BLUETOOTH_SERVEUR:
			etape2ModeBluetoothServeur();
			break;
		case ParametresPartie.MODE_BLUETOOTH_CLIENT:
			etape2ModeBluetoothClient();
			break;
		case ParametresPartie.MODE_WIFI_SERVEUR:
			etape2ModeWifiServeur();
			break;
		case ParametresPartie.MODE_WIFI_CLIENT:
			etape2ModeWifiClient();
			break;
		default :
			etape1();
		}
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
				SeekBar sbDifficulteIA = (SeekBar)findViewById(R.id.sb_difficulte_IA);
				ParametresPartie.getParametresPartie().setDifficuluteIA(sbDifficulteIA.getProgress());
				etape3();
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
		setContentView(R.layout.activite_creation_partie_2_bluetooth_serveur);
		/* Chargement des composants */
		activiteCreationPartieBluetooth.chargementInterfaceBluetoothServeur();
	}
	
	private void etape2ModeBluetoothClient() {
		activiteCreationPartieBluetooth = new ActiviteCreationPartieBluetooth(this);
		/* Affichage de la vue */
		setContentView(R.layout.activite_creation_partie_2_bluetooth_client);
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
	
	private void etape3() {
		/* Affichage de la vue */
		setContentView(R.layout.activite_creation_partie_3);
		
		ParametresPartie.getParametresPartie().setEstCartePerso(false);
		ParametresPartie.getParametresPartie().setNomCarte("terrain_jeu_defaut_1.png");
		
		Button btnPrecedent = (Button)findViewById(R.id.btn_precedent);
		btnPrecedent.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				etape2();
			}
		});
		
		Button btnSuivant = (Button)findViewById(R.id.btn_suivant);
		btnSuivant.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				etape4();
			}
		});
	}
	
	private void etape4() {
		setContentView(R.layout.activite_creation_partie_4);
		
		Button btnPrecedent = (Button)findViewById(R.id.btn_precedent);
		btnPrecedent.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				etape3();
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
		if (ParametresPartie.getParametresPartie().getModeJeu() == ParametresPartie.MODE_BLUETOOTH_SERVEUR
				|| ParametresPartie.getParametresPartie().getModeJeu() == ParametresPartie.MODE_BLUETOOTH_CLIENT) {
			// Destroy des éléments du Bluetooth
			activiteCreationPartieBluetooth.onDestroy();
		}
	}
	
	/** DEBUT DE SECTION : Fonctions pour le Bluetooth */
	
	/** Gestion des demandes d'activation/visibilité du Bluetooth */
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		activiteCreationPartieBluetooth.onActivityResult(requestCode, resultCode, data);
	}
	
	public void passerEtape3DepuisServeurBluetooth() {
		etape3();
	}
	
	public void passerEtape3DepuisClientBluetooth() {
		etape3();
	}
	
	/** FIN DE SECTION : Fonctions pour le Bluetooth */
}