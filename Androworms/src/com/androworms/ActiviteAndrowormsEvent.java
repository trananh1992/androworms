package com.androworms;

import android.app.AlertDialog;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.androworms.debug.ActiviteDebug;
import com.androworms.multijoueurs.ActiviteMultiJoueur;

public class ActiviteAndrowormsEvent implements OnClickListener {
	
	private static final String TAG = "Androworms.ActiviteAndrowormsEvent";
	
	private ActiviteAndroworms activiteAndroworms;
	
	public ActiviteAndrowormsEvent(ActiviteAndroworms activiteAndroworms) {
		this.activiteAndroworms = activiteAndroworms;
	}

	public void onClick(View arg0) {
		if (arg0 instanceof Button) {
			Button b = (Button)arg0;
			if (b.getId() == R.id.btn_menu_jouer) {
				// Bouton pour jouer
				Log.v(TAG, "Clic sur le bouton pour jouer");
				Intent intent = new Intent(this.activiteAndroworms, ActiviteCreationPartie.class);
				this.activiteAndroworms.startActivity(intent);
			}
			else if (b.getId() == R.id.btn_menu_editeur) {
				// Bouton de l'éditeur de carte
				Log.v(TAG, "Clic sur le bouton de l'éditeur de carte");
				Intent intent = new Intent(this.activiteAndroworms, ActiviteChoixOption.class);
				this.activiteAndroworms.startActivity(intent);
			}
			else if (b.getId() == R.id.btn_menu_score) {
				// Bouton de gestion des scores
				Log.v(TAG, "Clic sur le bouton de la gestion des scores");
				new AlertDialog.Builder(activiteAndroworms).setTitle("Androworms").setMessage("La gestion des scores n'est pas encore disponible dans cette version").setNeutralButton("Close", null).show();
			}
			else if (b.getId() == R.id.btn_menu_parametres) {
				// Bouton de paramètres
				Log.v(TAG, "Clic sur le bouton des paramètres");
				Intent intent = new Intent(this.activiteAndroworms, ActiviteParametres.class);
				this.activiteAndroworms.startActivity(intent);
			}
			else if (b.getId() == R.id.btn_DEBUG) {
				// Bouton de DEBUG
				Log.v(TAG, "Clic sur le bouton de DEBUG");
				Intent intent = new Intent(this.activiteAndroworms, ActiviteDebug.class);
				this.activiteAndroworms.startActivity(intent);
			}
			else if (b.getId() == R.id.btn_GYRO) {
				// Bouton de Gyro
				Log.v(TAG, "Clic sur le bouton du Gyroscope");
				Intent intent = new Intent(this.activiteAndroworms, ActiviteGyro.class);
				this.activiteAndroworms.startActivity(intent);
			}
			else if (b.getId() == R.id.btn_menu_multi) {
				// Bouton de l'éditeur de carte
				Log.v(TAG, "Clic sur le bouton du mode multi-joueurs");
				Intent intent = new Intent(this.activiteAndroworms, ActiviteMultiJoueur.class);
				this.activiteAndroworms.startActivity(intent);
			}
		}
	}
}