package com.androworms;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ActiviteAndrowormsEvent implements OnClickListener {
	
	private static final String TAG = "Androworms.MenuPrincipalActivityEvent";
	
	private ActiviteAndroworms activiteAndroworms;
	
	public ActiviteAndrowormsEvent(ActiviteAndroworms activiteAndroworms) {
		this.activiteAndroworms = activiteAndroworms;
	}

	public void onClick(View arg0) {
		// TODO pour savoir sur quelle bouton on a cliqué
		if (arg0 instanceof Button) {
			Button b = (Button)arg0;
			
			if (b.getId() == R.id.btn_solo) {
				Log.v(TAG,"Lancement du jeu");
				
				Intent intent = new Intent(this.activiteAndroworms, GameActivity.class);
				this.activiteAndroworms.startActivity(intent);
			}
		}
		
	}
}