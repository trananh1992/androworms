package com.androworms.multijoueurs;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;

import com.androworms.R;

/** Minuteur servant à afficher la durée restante de visibilité du Bluetooth.
 *  Le minuteur est utilisé pour l'interface Bluetooth > Serveur
 */
public class MinuteurVisibiliteBluetooth extends AsyncTask<ActiviteMultiJoueur, Integer, Boolean> {
	
	private static final String TAG = "Minuteur";
	private ActiviteMultiJoueur activiteMultiJoueur;
	
	private static final int CENT_POUR_CENT = 100;
	private static final int SEC_EN_MS = 1000;
	
	@Override
	protected Boolean doInBackground(ActiviteMultiJoueur... params) {
		this.activiteMultiJoueur = params[0];
		try {
			int i = CENT_POUR_CENT;
			while (i>0) {
				synchronized(this) {
					// La valeur ActiviteMultiJoueur.DUREE_VISIBILITE_BLUETOOTH est en seconde, alors que le wait() prend des milisecondes.
					// On attendre 1 centième de DUREE_VISIBILITE_BLUETOOTH pour avancer de 1%.
					wait(ActiviteMultiJoueur.DUREE_VISIBILITE_BLUETOOTH * SEC_EN_MS / CENT_POUR_CENT);
				}
				// On publie la progression du minuteur
				publishProgress(i);
				// On avance de 1%
				i--;
			}
		} catch (InterruptedException e) {
			Log.e(TAG,"Exception " + e.getMessage());
		}
		
		return true;
	}
	
	@Override
	protected void onProgressUpdate(Integer... progress) {
		// Durant le minuteur, on actualise en fonction de la progression
		ProgressBar pbMinuteur = (ProgressBar)activiteMultiJoueur.findViewById(R.id.pb_Minuteur);
		pbMinuteur.setProgress(progress[0]);
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		// A la fin du minuteur
		ProgressBar pbMinuteur = (ProgressBar)activiteMultiJoueur.findViewById(R.id.pb_Minuteur);
		pbMinuteur.setProgress(0);
	}
}