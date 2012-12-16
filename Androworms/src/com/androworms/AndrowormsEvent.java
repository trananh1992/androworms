package com.androworms;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class AndrowormsEvent implements OnClickListener {
	
	private static final String TAG = "Androworms.MenuPrincipalActivityEvent";
	
	private AndrowormsActivity menuPrincipalActivity;
	
	public AndrowormsEvent(AndrowormsActivity menuPrincipalActivity) {
		this.menuPrincipalActivity = menuPrincipalActivity;
	}

	public void onClick(View arg0) {
		// TODO pour savoir sur quelle bouton on a cliqué
		
		Log.v(TAG,"Lancement du jeu");
		
		Intent intent = new Intent(this.menuPrincipalActivity, GameActivity.class);
		this.menuPrincipalActivity.startActivity(intent);
	}
}