package com.androworms;

import android.app.AlertDialog;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

import com.androworms.debug.ActiviteDebug;

public class ActiviteAndrowormsEvent implements OnClickListener {
	
	private static final String TAG = "Androworms.ActiviteAndrowormsEvent";
	
	private ActiviteAndroworms activiteAndroworms;
	
	public ActiviteAndrowormsEvent(ActiviteAndroworms activiteAndroworms) {
		this.activiteAndroworms = activiteAndroworms;
	}
	
	public void onClick(View v) {
		if (v instanceof Button) {
			Button btn = (Button) v;
			if (btn.getId() == R.id.btn_menu_jouer) {
				// Bouton pour jouer
				Log.v(TAG, "Clic sur le bouton pour jouer");
				Intent intent = new Intent(this.activiteAndroworms, ActiviteCreationPartie.class);
				this.activiteAndroworms.startActivity(intent);
			} else if (btn.getId() == R.id.btn_menu_editeur) {
				// Bouton de l'éditeur de carte
				Log.v(TAG, "Clic sur le bouton de l'éditeur de carte");
				Intent intent = new Intent(this.activiteAndroworms, ActiviteChoixOption.class);
				this.activiteAndroworms.startActivity(intent);
			} else if (btn.getId() == R.id.btn_menu_score) {
				// Bouton de gestion des scores
				Log.v(TAG, "Clic sur le bouton de la gestion des scores");
				new AlertDialog.Builder(activiteAndroworms).setTitle(R.string.app_name).setMessage(R.string.gestion_score_non_disponible).setNeutralButton(R.string.fermer, null).show();
			} else if (btn.getId() == R.id.btn_menu_parametres) {
				// Bouton de paramètres
				Log.v(TAG, "Clic sur le bouton des paramètres");
				Intent intent = new Intent(this.activiteAndroworms, ActiviteParametres.class);
				this.activiteAndroworms.startActivity(intent);
			} else if (btn.getId() == R.id.btn_DEBUG) {
				// Bouton de DEBUG
				Log.v(TAG, "Clic sur le bouton de DEBUG");
				Intent intent = new Intent(this.activiteAndroworms, ActiviteDebug.class);
				this.activiteAndroworms.startActivity(intent);
			} else if (btn.getId() == R.id.btn_GYRO) {
				// Bouton de Gyro
				Log.v(TAG, "Clic sur le bouton du Gyroscope");
				Intent intent = new Intent(this.activiteAndroworms, ActiviteGyro.class);
				this.activiteAndroworms.startActivity(intent);
			} else if (btn.getId() == R.id.btn_test_jeu) {
				// Bouton de test rapide du jeu
				Intent intent = new Intent(this.activiteAndroworms, ActiviteJeu.class);
				ParametresPartie.getParametresPartie().setModeJeu(ParametresPartie.MODE_SOLO);
				ParametresPartie.getParametresPartie().setEstCartePerso(false);
				ParametresPartie.getParametresPartie().setNomCarte("terrain_jeu_defaut_1.png");
				this.activiteAndroworms.startActivity(intent);
			}
		} else if (v instanceof ImageButton) {
			ImageButton ib = (ImageButton) v;
			if (ib.getId() == R.id.ib_partager) {
				Intent intent = new Intent(this.activiteAndroworms, ActivitePartage.class);
				this.activiteAndroworms.startActivity(intent);
			}
		}
	}
}