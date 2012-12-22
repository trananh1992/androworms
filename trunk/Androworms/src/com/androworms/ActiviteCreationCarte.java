package com.androworms;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
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
	private boolean del_mode = false;
	private boolean have_picture = false;
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
		ImageView surface = (ImageView) findViewById(R.id.CurrentMap);
		//surface.setBackgroundColor(0xff77B5FE);
		surface.setBackgroundColor(0x00000000);
		((ImageView)findViewById(R.id.background)).setBackgroundColor(0xff77B5FE);
		
		surface.setOnTouchListener(this);
		OnClickListener camCl = new ActiviteCamera(this);
		findViewById(R.id.TakePicture).setOnClickListener(camCl);
		findViewById(R.id.erase_button).setOnClickListener(new OnClickListener()
		{
			public void onClick(View v) {
				if(have_picture)
				{
					del_mode = !del_mode;
				}
		}
		});
	}

	public boolean onTouch(View surface, MotionEvent event) {
		if(!del_mode)
		{
			return false;
		}
		final int size = 10;
		int x=0;
		int y=0;
		Bitmap bitmap = ((BitmapDrawable)((ImageView)surface).getDrawable()).getBitmap();
		int action = event.getAction();
		int color;
		int red,green,blue;
		switch (action)
		{
		case MotionEvent.ACTION_DOWN:
		case MotionEvent.ACTION_MOVE:
			break;
		default:
			return false;
		}
		x = (int) event.getX();
		x = Math.max(0, x);
		x = Math.min(bitmap.getWidth()-1, x);
		y = (int) event.getY();
		y = Math.max(0, y);
		y = Math.min(bitmap.getHeight()-1, y);
		for(int i=(x-size);i<(x+size);i++)
		{
			if(i>=0 && i<bitmap.getWidth())
			{
				for(int j=(y-size);j<(y+size);j++)
				{
					if(j>=0 && j<bitmap.getHeight())
					{
						color = bitmap.getPixel(i, j);
						red = Color.red(color);
						green = Color.green(color);
						blue = Color.blue(color);
						bitmap.setPixel(i, j, Color.argb(0,red,green,blue));
					}
				}
			}
		}
		((ImageView) surface).setImageBitmap(bitmap);
		return true;
	}
	
	protected void onActivityResult(int requestCode, int resultCode,
            Intent retour) {
		Log.e(TAG,"called " + requestCode  + " " + resultCode);
        if ((requestCode == TAKE_PICTURE) && (resultCode == RESULT_OK)) {
            Log.e(TAG,"got new message!");
            byte data[] = retour.getByteArrayExtra("image");
            Log.e(TAG,"got data");
            ImageView surface = (ImageView) findViewById(R.id.CurrentMap);
            Log.e(TAG,"got surface "+surface);
            Bitmap b = BitmapFactory.decodeByteArray(data, 0, data.length).copy(Bitmap.Config.ARGB_8888, true);
            Log.e(TAG,"builded bitmap "+b);
            surface.setImageBitmap(Bitmap.createScaledBitmap(b,surface.getWidth(),surface.getHeight(),false));
            have_picture = true;
        }
    }

}
