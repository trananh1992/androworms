package com.androworms;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.ProgressBar;

import com.androworms.R;

/** Minuteur servant à afficher la durée restante de visibilité du Bluetooth.
 *  Le minuteur est utilisé pour l'interface Bluetooth > Serveur
 */
public class TacheMinuteurVisibiliteBluetooth extends AsyncTask<Void, Integer, Boolean> {
	
	private static final String TAG = "Minuteur";
	private ActiviteCreationPartie activiteCreationPartie;
	
	private static final int CENT_POUR_CENT = 100;
	private static final int SEC_EN_MS = 1000;
	
	public TacheMinuteurVisibiliteBluetooth(ActiviteCreationPartieBluetooth activiteCreationPartieBluetooth) {
		this.activiteCreationPartie = activiteCreationPartieBluetooth.getActiviteCreationPartie();
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void executerTacheEnParralelle() {
		/** ATTENTION : Code spécial 
		 * Les tâches asynchrones sont des Thread qui ont la particularité de pouvoir interagir avec l'interface graphique.
		 * Seulement ces tâches asynchrones ont des inconvénients :
		 * - elles ne peuvent pas être stoppé proprement
		 * - elles ont été modifié entre Android 2.3.x et Android 3.0
		 * Les AsyncTask sont executé en parrallèle sur Android 2.3.x alors qu'elles sont éxecuté en série sur Android 3.0+
		 * Sur Android 3.0+, il est quand même possible d'executer les tâches en asynchrones grâce à la fonction "executeOnExecutor".
		 * Cette fonction n'est diponible que sur l'API 11+.
		 * C'est pour ça que l'on ne fait pas exactement la même chose dans tous les cas et qu'il y a un @TargetApi code.
		 * **/
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			// Sur les téléphones avec Android 3.0+
			Log.d(TAG, "executeOnExecutor()");
			executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		}
		else {
			// Sur les téléphones avec Android 2.x
			Log.d(TAG, "execute()");
			execute();
		}
	}
	
	@Override
	protected Boolean doInBackground(Void... params) {
		try {
			int i = CENT_POUR_CENT;
			while (i>0) {
				synchronized(this) {
					// La valeur ActiviteMultiJoueur.DUREE_VISIBILITE_BLUETOOTH est en seconde, alors que le wait() prend des milisecondes.
					// On attendre 1 centième de DUREE_VISIBILITE_BLUETOOTH pour avancer de 1%.
					wait(ActiviteCreationPartieBluetooth.DUREE_VISIBILITE_BLUETOOTH * SEC_EN_MS / CENT_POUR_CENT);
				}
				// On publie la progression du minuteur
				publishProgress(i);
				// On avance de 1%
				i--;
			}
		} catch (InterruptedException e) {
			Log.e(TAG,"Exception " + e.getMessage());
			return false;
		}
		return true;
	}
	
	@Override
	protected void onProgressUpdate(Integer... progress) {
		// Durant le minuteur, on actualise en fonction de la progression
		ProgressBar pbMinuteur = (ProgressBar)activiteCreationPartie.findViewById(R.id.pb_Minuteur);
		pbMinuteur.setProgress(progress[0]);
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		// A la fin du minuteur
		if (result) {
			ProgressBar pbMinuteur = (ProgressBar)activiteCreationPartie.findViewById(R.id.pb_Minuteur);
			pbMinuteur.setProgress(0);
		}
	}
}