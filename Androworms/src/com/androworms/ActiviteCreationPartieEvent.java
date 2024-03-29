package com.androworms;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class ActiviteCreationPartieEvent implements OnClickListener {
	
	private ActiviteCreationPartie activiteCreationPartie;
	private static final String TAG = "Androworms.ActiviteCreationPartieEvent";
	
	/** Intialisation des évenements sur l'ActiviteCreationPartie */
	public ActiviteCreationPartieEvent(ActiviteCreationPartie activiteCreationPartie) {
		this.activiteCreationPartie = activiteCreationPartie;
	}
	
	/** Gestion des actions sur les composants */
	public void onClick(View v) {
		if (v instanceof Button) {
			Button btn = (Button)v;
			
			switch (btn.getId()) {
			// Boutons communs à plusieurs pages : "Précedent" et "Suivant"
			case R.id.btn_precedent:
				activiteCreationPartie.etapePrecedente(false);
				break;
			case R.id.btn_suivant:
				activiteCreationPartie.etapeSuivante();
				break;
			// Boutons de la première page : Choix du mode de jeu
			case R.id.btn_partie_solo:
				ParametresPartie.getParametresPartie().setModeJeu(ParametresPartie.MODE_SOLO);
				activiteCreationPartie.etapeSuivante();
				break;
			case R.id.btn_multi_joueur:
				ParametresPartie.getParametresPartie().setModeJeu(ParametresPartie.MODE_2JOUEURS);
				activiteCreationPartie.etapeSuivante();
				break;
			case R.id.btn_bluetooth_creer:
				ParametresPartie.getParametresPartie().setModeJeu(ParametresPartie.MODE_BLUETOOTH_SERVEUR);
				activiteCreationPartie.etapeSuivante();
				break;
			case R.id.btn_bluetooth_rejoindre:
				ParametresPartie.getParametresPartie().setModeJeu(ParametresPartie.MODE_BLUETOOTH_CLIENT);
				activiteCreationPartie.etapeSuivante();
				break;
			case R.id.btn_wifi_creer:
				ParametresPartie.getParametresPartie().setModeJeu(ParametresPartie.MODE_WIFI_SERVEUR);
				activiteCreationPartie.etapeSuivante();
				break;
			case R.id.btn_wifi_rejoindre:
				ParametresPartie.getParametresPartie().setModeJeu(ParametresPartie.MODE_WIFI_CLIENT);
				activiteCreationPartie.etapeSuivante();
				break;
			// Etape 4 : Choix des équipes
			case R.id.btn_demarrer_la_partie:
				activiteCreationPartie.etapeSuivante();
				break;
			default:
				break;
			}
			
		}
		else if (v instanceof ImageView) {
			ImageView imgv = (ImageView)v;
			
			switch (v.getId()) {
			default :
				Log.v(TAG, "Affichage d'une popup d'aide");
				
				AlertDialog.Builder builder = new AlertDialog.Builder(activiteCreationPartie);
				
				switch (imgv.getId()) {
				// Boutons d'aide de la première page : Choix du mode de jeu
				case R.id.iv_aide_1_telephone:
					builder.setMessage(R.string.aide_partie_un_joueur);
					break;
				case R.id.iv_aide_2_telephones:
					builder.setMessage(R.string.aide_partie_n_joueurs);
					break;
				default:
					break;
				}
				
				builder.setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.dismiss();
					}
				});
				builder.setCancelable(false);
				AlertDialog alert = builder.create();
				alert.show();
			}
		}
	}
}