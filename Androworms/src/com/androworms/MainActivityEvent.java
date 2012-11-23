package com.androworms;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivityEvent implements OnClickListener {
	
	private static final String TAG = "Androworms.MenuPrincipal.Event";
	private MainActivity mainActivity;
	
	public MainActivityEvent(MainActivity mainActivity) {
		this.mainActivity = mainActivity;
	}

	public void onClick(View arg0) {
		Log.v(TAG,"Androworms : Vous avez cliqué !");
		Intent intent = new Intent(this.mainActivity, ActiviteMenuPrincipal.class);
		this.mainActivity.startActivity(intent);
	}

}
