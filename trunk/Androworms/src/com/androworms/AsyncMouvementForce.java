package com.androworms;

import java.util.List;

import android.os.AsyncTask;
import android.util.Log;

public class AsyncMouvementForce extends AsyncTask<Object, Integer, Boolean> {

	public MoteurGraphique mg;
	public List<Personnage> lp;
	private static final String TAG = "Androworms.MoteurGraphique";

	
	public AsyncMouvementForce(MoteurGraphique mg, List<Personnage> lp) {
		this.mg = mg;
		this.lp = lp;
	}
	
	@Override
	protected Boolean doInBackground(Object... arg0) {
		// TODO Auto-generated method stub
		boolean loop = true;
		while(loop) {
			loop = false;	
			// *
			for(int i = 0; i < lp.size(); i++) {
				if(lp.get(i).getMouvementForces().size() != 0) {
					loop =  true;
					lp.get(i).setPosition(lp.get(i).getMouvementForces().get(0));
					lp.get(i).getMouvementForces().remove(0);	
					Log.v(TAG, "mouvement" + lp.get(i).getPosition().x 
							+ "," + lp.get(i).getPosition().y);
				}
			}
			try {
				Thread.sleep(1000);
			} catch (Exception e) {
				Log.e(TAG, "fin de affecterMouvementForces", e);
			}
			// */
			publishProgress(0);
		}
		Log.v(TAG, "fin de affecterMouvementForces");
		return null;
	}
	
	@Override
	protected void onProgressUpdate(Integer...integers) {
		mg.invalidate();
	}

}
