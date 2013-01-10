package com.androworms;

import com.androworms.ui.ClavierDirectionnel;

import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class EvenementClavier implements OnTouchListener {
	/* Gestion du déplacement du joueur avec le clavier */
	private static final int TEMPS_APPUIE = 60;
	private static final String TAG = "EvenementClavier";
	
	private Handler mHandler;
	private int idBtnCourrant;
	private Noyau noyau;

	/*
	 * Thread pour gérer toutes les x secondes le déplacement du joueur
	 * lorsque l'on reste appuyé
	 */
	private Runnable mAction;
	

	public EvenementClavier(Noyau n) {
		this.noyau = n;
		mAction = new ThreadTraitementAction();
	}
	
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
			/* Début d'appuie d'un boutton du clavier */
			case MotionEvent.ACTION_DOWN:
	
				idBtnCourrant = v.getId();
				/*
				 * Je crée un Handler qui va bouger le joueur tous les x
				 * secondes s'il reste appuyer dessus
				 */
				if (mHandler != null) {
					return true;
				}
				mHandler = new Handler();
				// TODO : le lancement du Thread n'est pas rentable pour
				// faire juste 1 déplacement.
				// il faudrait mettre le code du déplacement ici, et la
				// création du Thread avec postDelayed sur TEMPS_APPUIE
				// mais ce code est assez gros, et donc il faudrait faire
				// une fonction qui est appellé ici et depuis le Runnable.
				// (et mettre un commentaire !)
				mHandler.post(mAction);
				break;
			/* Fin d'appuie d'un boutton du clavier */
			case MotionEvent.ACTION_UP:
	
				idBtnCourrant = -1;
				/* Je supprime le Handler */
				if (mHandler == null) {
					return true;
				}
				mHandler.removeCallbacks(mAction);
				mHandler = null;
				break;
			default:
				break;
		}
		return true;
	}
	
	private class ThreadTraitementAction implements Runnable {
		public void run() {
			switch (idBtnCourrant) {
				// Déplacement vers la droite
				case ClavierDirectionnel.BOUTON_DROITE:
					Log.v(TAG, "Déplacement vers la droite");
					noyau.deplacementJoueurDroiteFromIHM();
					break;
				// Déplacement vers le haut
				case ClavierDirectionnel.BOUTON_HAUT:
					Log.v(TAG, "Déplacement vers la haut");
					noyau.sautJoueurDroiteFromIHM();
					break;
				// Déplacement vers la gauche
				case ClavierDirectionnel.BOUTON_GAUCHE:
					Log.v(TAG, "Déplacement vers la gauche");
					noyau.deplacementJoueurGaucheFromIHM();
					break;
				default:
					Log.v(TAG, "Déplacement default");
					break;
			}
			mHandler.postDelayed(this, TEMPS_APPUIE);
		}
	}

}
