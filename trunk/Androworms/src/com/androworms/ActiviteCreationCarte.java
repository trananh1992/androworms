package com.androworms;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

public class ActiviteCreationCarte extends Activity implements OnClickListener,OnTouchListener {
	private static final String TAG = "Androworms.ActiviteCreationCarte.Event";
	private ActiviteAndroworms activiteMenuPrincipal;
	static final int TAKE_PICTURE = 0;
	
	public void onClick(View arg0) {
		Log.v(TAG,"On a cliqué sur l'activité de creation de carte");
		Intent intent = new Intent(this.activiteMenuPrincipal, ActiviteCreationCarte.class);
		Log.v(TAG,"Androworms : created intent!");
		this.activiteMenuPrincipal.startActivity(intent);
		Log.v(TAG,"Androworms : started activity!");
		
	}
	
	public ActiviteCreationCarte(ActiviteAndroworms activiteMenuPrincipal) {
		this.activiteMenuPrincipal = activiteMenuPrincipal;
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

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		Log.v(TAG,"touch me");
		return false;
	}
	
	protected void onActivityResult(int requestCode, int resultCode,
            Intent retour) {
		Log.e(TAG,"called");
		Log.e(TAG,"called");
		Log.e(TAG,"called");
		Log.e(TAG,"called");
		Log.e(TAG,"called");
		Log.e(TAG,"called");
		Log.e(TAG,"called " + requestCode  + " " + resultCode);
        if (requestCode == TAKE_PICTURE) {
            if (resultCode == RESULT_OK) {
                Log.e(TAG,"got new message!");
                byte data[] = retour.getByteArrayExtra("image");
                Log.e(TAG,"got "+ data);
                ImageView surface = (ImageView) findViewById(R.id.CurrentMap);
                Log.e(TAG,"got surface "+surface);
                //Canvas c = surface.getHolder().lockCanvas();
                Bitmap b = BitmapFactory.decodeByteArray(data, 0, data.length).copy(Bitmap.Config.ARGB_8888, true);
                //Bitmap mutableBitmap = bmp.copy(Bitmap.Config.ARGB_8888, true);
                Log.e(TAG,"builded bitmap "+b);
                surface.setImageBitmap(b);
                /*if( null == c)
                {
                	c = new Canvas(b);
                	Log.e(TAG,"created new canvas "+c);
                	surface.draw(c);
                }
                else
                {
                	c.setBitmap(b);
                	surface.getHolder().unlockCanvasAndPost(c);
                }*/
                
                
                
            }
        }
    }

}
