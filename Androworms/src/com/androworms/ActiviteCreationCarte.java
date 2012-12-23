package com.androworms;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
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
	private boolean drawAlpha = false;
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
		final int blueSkyColor = 0xff77B5FE;
		
		/* Affiche la vue par défaut */
		setContentView(R.layout.edition_carte);
		ImageView surface = (ImageView) findViewById(R.id.CurrentMap);
		((ImageView)findViewById(R.id.background)).setBackgroundColor(blueSkyColor);
		
		surface.setOnTouchListener(this);
		OnClickListener camCl = new ActiviteCamera(this);
		findViewById(R.id.TakePicture).setOnClickListener(camCl);
		findViewById(R.id.erase_button).setOnClickListener(new OnClickListener()
		{
			public void onClick(View v) {
				if(have_picture)
				{
					drawAlpha = !drawAlpha;
				}
		}
		});
	}

	public boolean onTouch(View surface, MotionEvent event) {
		if(!drawAlpha)
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
		Bitmap overlay = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
		Canvas c = new Canvas(overlay);
		c.drawBitmap(bitmap, new Matrix(), null);
		((ImageView) surface).setImageBitmap(overlay);
		return true;
	}
	
	protected void onActivityResult(int requestCode, int resultCode,
            Intent retour) {
        if ((requestCode == TAKE_PICTURE) && (resultCode == RESULT_OK)) {
            byte data[] = retour.getByteArrayExtra("image");
            ImageView surface = (ImageView) findViewById(R.id.CurrentMap);
            Bitmap b = BitmapFactory.decodeByteArray(data, 0, data.length).copy(Bitmap.Config.ARGB_8888, true);
            surface.setImageBitmap(Bitmap.createScaledBitmap(b,surface.getWidth(),surface.getHeight(),false));
            have_picture = true;
        }
    }

}
