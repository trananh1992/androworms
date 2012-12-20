package com.androworms;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;

public class ActiviteCreationCarte extends Activity implements OnClickListener,OnTouchListener {
	private static final String TAG = "Androworms.ActiviteCreationCarte.Event";
	private ActiviteAndroworms activiteAndroworms;
	static final int TAKE_PICTURE = 0;
	
	public void onClick(View arg0) {
		Log.v(TAG,"On a cliqué sur l'activité de creation de carte");
		Intent intent = new Intent(this.activiteAndroworms, ActiviteCreationCarte.class);
		Log.v(TAG,"Androworms : created intent!");
		this.activiteAndroworms.startActivityForResult(intent,TAKE_PICTURE);
		Log.v(TAG,"Androworms : started activity!");
		
	}
	
	public ActiviteCreationCarte(ActiviteAndroworms activiteMenuPrincipal) {
		this.activiteAndroworms = activiteMenuPrincipal;
	}
	
	public ActiviteCreationCarte() {
		super();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/* Affiche la vue par défaut */
		setContentView(R.layout.edition_carte);
		
		OnClickListener camCl = new ActiviteCamera(this);
		//	OnTouchListener camtl = new ActiviteCreationCarte(this);
		findViewById(R.id.TakePicture).setOnClickListener(camCl);
	}

	public boolean onTouch(View arg0, MotionEvent arg1) {
		// TODO Auto-generated method stub
		Log.v(TAG,"touch me");
		return false;
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TAKE_PICTURE) {
            if (resultCode == RESULT_OK) {
                
            }
        }
    }

}