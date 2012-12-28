package com.androworms;

import android.util.Log;
import android.view.View;

public class IHMTestReseau implements android.view.View.OnClickListener {
	
	private Noyau noyau;
	
	public IHMTestReseau(Noyau n) {
		this.noyau = n;		
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		Log.v("IHMTestReseau", "Lancement du test réseau !");
		noyau.testReseau();
	}

}
