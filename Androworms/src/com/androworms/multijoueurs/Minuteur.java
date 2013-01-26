package com.androworms.multijoueurs;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;

import com.androworms.R;

/** Minuteur servant à afficher la durée restante de visibilité du Bluetooth.
 *  Le minuteur est utilisé pour l'interface Bluetooth > Serveur
 */
public class Minuteur extends AsyncTask<ActiviteMultiJoueur, Integer, Boolean> {
	
	private static final String TAG = "Minuteur";
	private ActiviteMultiJoueur activiteMultiJoueur;
	
	@Override
	protected Boolean doInBackground(ActiviteMultiJoueur... params) {
		this.activiteMultiJoueur = params[0];
		Log.v(TAG, "doInBackground()");
		
		try {
			int i = 100;
			while (i>0) {
				synchronized(this) {
					wait(ActiviteMultiJoueur.DUREE_VISIBILITE_BLUETOOTH * 10);
				}
				publishProgress(i);
				i--;
			}
		} catch (InterruptedException e) {
			Log.e(TAG,"Exception " + e.getMessage());
		}
		
		return true;
	}
	
	@Override
	protected void onProgressUpdate(Integer... progress) {
		// Pour afficher la progression
		Log.v(TAG,"onProgressUpdate() --> "+progress[0]); 
		
		ProgressBar pbMinuteur = (ProgressBar)activiteMultiJoueur.findViewById(R.id.pb_Minuteur);
		pbMinuteur.setProgress(progress[0]);
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		// A la fin de l'opération
		Log.v(TAG,"onPostExecute()");
		
		ProgressBar pbMinuteur = (ProgressBar)activiteMultiJoueur.findViewById(R.id.pb_Minuteur);
		pbMinuteur.setProgress(0);
	}
}