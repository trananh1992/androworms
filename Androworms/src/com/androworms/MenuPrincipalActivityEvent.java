package com.androworms;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class MenuPrincipalActivityEvent implements OnClickListener {
	
	private static final String TAG = "Androworms.MenuPrincipalActivityEvent";
	
	private MenuPrincipalActivity menuPrincipalActivity;
	
	public MenuPrincipalActivityEvent(MenuPrincipalActivity menuPrincipalActivity) {
		this.menuPrincipalActivity = menuPrincipalActivity;
	}

	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
		Log.v(TAG,"btn_Solo");
		Intent intent = new Intent(this.menuPrincipalActivity, GameActivity.class);
		this.menuPrincipalActivity.startActivity(intent);
	}

}
