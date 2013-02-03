package com.androworms.multijoueurs;

import android.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.androworms.R;
import com.androworms.utile.Informations;

public class ActiviteMultiJoueurEvent implements OnClickListener {
	
	private static final String TAG = "Androworms.EvenementMultiJoueur";
	private ActiviteMultiJoueur activiteMultiJoueur;
	
	public ActiviteMultiJoueurEvent(ActiviteMultiJoueur activiteMultiJoueur) {
		this.activiteMultiJoueur = activiteMultiJoueur;
	}

	public void onClick(View v) {
		if (v instanceof ImageButton) {
			ImageButton imgbtn = (ImageButton)v;
			
			if (imgbtn == activiteMultiJoueur.findViewById(R.id.imgbtn_bluetooth)) {
				/* Choix du Bluetooth */
				if (!Informations.isCompatibleBluetooth()) {
					// Le téléphone n'est pas compatible Blueooth
					new AlertDialog.Builder(activiteMultiJoueur).setTitle("Androworms").setMessage("Votre téléphone n'est pas compatible Bluetooth !").setNeutralButton("Close", null).show();
				} else {
					// Le téléphone est compatible Bluetooth
					
					/* Changement de vue > Choix entre serveur et client */
					activiteMultiJoueur.changerVue(this);
				}
			}
			else if (imgbtn == activiteMultiJoueur.findViewById(R.id.imgbtn_deux_joueurs)) {
				/* Choix du mode 2 joueurs */
				new AlertDialog.Builder(activiteMultiJoueur).setTitle("Androworms").setMessage("Les parties multi-joueurs ne sont pas encore dispo !").setNeutralButton("Close", null).show();
			}
			else if (imgbtn == activiteMultiJoueur.findViewById(R.id.imgbtn_bluetooth_serveur)) {
				/* Choix du Bluetooth > Serveur */
				activiteMultiJoueur.isServeur = true;
				
				/* Bluetooth > Serveur > Attente de joueur */
				activiteMultiJoueur.setContentView(R.layout.multi_joueur_bluetooth_serveur);
				
				/* Chargement des composants */
				activiteMultiJoueur.getFonctionsIHM().chargementInterfaceBluetoothServeur();
				/* Actualisation des elements graphiques *///DELETE ?
				//activiteMultiJoueur.getFonctions_IHM().refresh_UI_multijoueur_bluetooth_serveur();
			}
			else if (imgbtn == activiteMultiJoueur.findViewById(R.id.imgbtn_bluetooth_client)) {
				/* Choix du Bluetooth > Client */
				Log.v(TAG, "Choix du mode Bluetooth > Client");
				activiteMultiJoueur.isServeur = false;
				
				/* Bluetooth > Serveur > Connexion au serveur */
				activiteMultiJoueur.setContentView(R.layout.multi_joueur_bluetooth_client);
				
				/* Chargement des composants */
				activiteMultiJoueur.getFonctionsIHM().chargementInterfaceBluetoothClient();
				/* Actualisation des elements graphiques *///DELETE ?
				//activiteMultiJoueur.getFonctions_IHM().refresh_UI_multijoueur_bluetooth_client();
			}
		}
	}
}