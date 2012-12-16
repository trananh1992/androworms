package com.androworms;

import java.util.Date;

import android.os.AsyncTask;
import android.util.Log;

public class Chargement extends AsyncTask<AndrowormsActivity, Integer, Boolean> {
	public static String TAG = "Chargement";
	public static int TEMPS_PAUSE = 5000;
	
	private AndrowormsActivity activiteSplash;
	private boolean chargementFini;
	private boolean touchDone;
	
	@Override
	protected Boolean doInBackground(AndrowormsActivity... params) {
		Log.v(TAG,"doInBackground()");
		chargementFini = false;
		Date dateDebutChargement = new Date();
		
		this.activiteSplash = params[0];
		
		/** DEBUT DE ZONE SPECIAL POUR LES OPERATIONS DE CHARGEMENT DE DONNEES POUR ANDROWORMS **/
		
		/* Chargement des informations du téléphone */
		Informations.init(activiteSplash.getWindowManager().getDefaultDisplay());
		
		
		/** FIN DE ZONE SPECIAL POUR LES OPERATIONS DE CHARGEMENT DE DONNEES POUR ANDROWORMS **/
		
		chargementFini = true;

		if (!touchDone) {
			try {
				synchronized(this){
					Log.v(TAG,"Lancement timer");
					wait(TEMPS_PAUSE - (new Date().getTime() - dateDebutChargement.getTime()));
				}
			} catch (InterruptedException e) {
				Log.e(TAG,"Exception " + e.getMessage());
			}
		}
		
		Log.v(TAG,"Fin timer");
		return true;
	}
	
	@Override
	protected void onProgressUpdate(Integer... progress) {
		// Pour afficher la progression
		Log.v(TAG,"onProgressUpdate()");
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		// A la fin de l'opération
		Log.v(TAG,"onPostExecute()");
		//o.getThread().start();
		activiteSplash.chargerMenuPrincipal();
	}
	
	public void finish() {
		Log.v(TAG,"Toucher !");
		if (this.chargementFini) {
			synchronized(this) {
				this.notify();
			}
		}
		else {
			this.touchDone = true;
		}
	}
}