package com.androworms;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class AndrowormsActivityEvent implements OnClickListener {
	
	private static final String TAG = "Androworms.MenuPrincipal.Event";
	private AndrowormsActivity androwormsActivity;
	
	public AndrowormsActivityEvent(AndrowormsActivity androwormsActivity) {
		this.androwormsActivity = androwormsActivity;
	}

	public void onClick(View arg0) {
		Log.v(TAG,"Androworms : Vous avez cliqué !");
		Intent intent = new Intent(this.androwormsActivity, MenuPrincipalActivity.class);
		this.androwormsActivity.startActivity(intent);
	}
}
