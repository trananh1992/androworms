package com.androworms;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivityEvent implements OnClickListener {
	MainActivity mainActivity;
	public MainActivityEvent(MainActivity mainActivity) {
		this.mainActivity = mainActivity;
	}

	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		//mainActivity.setContentView(R.layout.menu_principal);
		Intent intent = new Intent(this.mainActivity, ActiviteMenuPrincipal.class);
		this.mainActivity.startActivity(intent);
	}

}
