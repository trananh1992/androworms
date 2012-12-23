package com.androworms;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

public class ActiviteCreationCarte extends Activity implements OnClickListener,OnTouchListener {
	private ActiviteAndroworms activiteMenuPrincipal;
	static final int TAKE_PICTURE = 0;
	private static final int bigBrush = 3;
	private static final int mediumBrush = 2;
	private static final int smallBrush = 1;
	private static final int noBrush = 0;
	private int drawAlpha = 0;
	private int drawSolid = 0;
	private boolean initializedImageView = false;
	public void onClick(View arg0) {
		Intent intent = new Intent(this.activiteMenuPrincipal, ActiviteCreationCarte.class);
		this.activiteMenuPrincipal.startActivity(intent);
		
	}
	
	public ActiviteCreationCarte(ActiviteAndroworms activiteMenuPrincipal) {
		this.activiteMenuPrincipal = activiteMenuPrincipal;
	}
	
	public ActiviteCreationCarte() {
		super();
	}
	
	private void setAlphaBrushSizeListener()
	{
		/* Ajout du gestionnaire d'évenement pour les boutons de taille de brosse Alpha */
		OnClickListener brushSizeAlpha = new OnClickListener()
		{
			public void onClick(View v) {
				drawAlpha = drawSolid = noBrush;
				if( v.equals(findViewById(R.id.alpha_small_brush)))
				{
					drawAlpha = smallBrush;
				}
				else if( v.equals(findViewById(R.id.alpha_medium_brush)))
				{
					drawAlpha = mediumBrush;
				}
				else if( v.equals(findViewById(R.id.alpha_big_brush)))
				{
					drawAlpha = bigBrush;
				}
				findViewById(R.id.alpha_small_brush).setVisibility(View.INVISIBLE);
				findViewById(R.id.alpha_medium_brush).setVisibility(View.INVISIBLE);
				findViewById(R.id.alpha_big_brush).setVisibility(View.INVISIBLE);
			}
		};
		findViewById(R.id.alpha_big_brush).setOnClickListener(brushSizeAlpha);
		findViewById(R.id.alpha_medium_brush).setOnClickListener(brushSizeAlpha);
		findViewById(R.id.alpha_small_brush).setOnClickListener(brushSizeAlpha);
	}
	
	private void setAlphaListener()
	{
		/* Ajout du gestionnaire d'évenement pour le bouton de alpha */
		findViewById(R.id.erase_button).setOnClickListener(new OnClickListener()
		{
			public void onClick(View v) {
				findViewById(R.id.draw_small_brush).setVisibility(View.INVISIBLE);
				findViewById(R.id.draw_medium_brush).setVisibility(View.INVISIBLE);
				findViewById(R.id.draw_big_brush).setVisibility(View.INVISIBLE);
				if(findViewById(R.id.alpha_big_brush).getVisibility()==View.VISIBLE)
				{
					findViewById(R.id.alpha_big_brush).setVisibility(View.INVISIBLE);
					findViewById(R.id.alpha_medium_brush).setVisibility(View.INVISIBLE);
					findViewById(R.id.alpha_small_brush).setVisibility(View.INVISIBLE);
					drawAlpha = noBrush;
				}
				else
				{
					findViewById(R.id.alpha_big_brush).setVisibility(View.VISIBLE);
					findViewById(R.id.alpha_medium_brush).setVisibility(View.VISIBLE);
					findViewById(R.id.alpha_small_brush).setVisibility(View.VISIBLE);
				}
		}
		});
		this.setAlphaBrushSizeListener();
		
	}
	
	private void setSolidBrushSizeListener()
	{
		/* Ajout du gestionnaire d'évenement pour les boutons de taille de brosse Solid */
		OnClickListener brushSizeSolid = new OnClickListener()
		{
			public void onClick(View v) {
				drawSolid = drawAlpha = noBrush;
				if( v.equals(findViewById(R.id.draw_small_brush)))
				{
					drawSolid = smallBrush;
				}
				else if( v.equals(findViewById(R.id.draw_medium_brush)))
				{
					drawSolid = mediumBrush;
				}
				else if( v.equals(findViewById(R.id.draw_big_brush)))
				{
					drawSolid = bigBrush;
				}
				findViewById(R.id.draw_big_brush).setVisibility(View.INVISIBLE);
				findViewById(R.id.draw_small_brush).setVisibility(View.INVISIBLE);
				findViewById(R.id.draw_medium_brush).setVisibility(View.INVISIBLE);
			}
		};
		findViewById(R.id.draw_big_brush).setOnClickListener(brushSizeSolid);
		findViewById(R.id.draw_medium_brush).setOnClickListener(brushSizeSolid);
		findViewById(R.id.draw_small_brush).setOnClickListener(brushSizeSolid);
	}
	
	private void setDrawListener()
	{
		/* Ajout du gestionnaire d'évenement pour le bouton de Solid */
		findViewById(R.id.draw).setOnClickListener(new OnClickListener()
		{
			public void onClick(View v) {
				findViewById(R.id.alpha_small_brush).setVisibility(View.INVISIBLE);
				findViewById(R.id.alpha_medium_brush).setVisibility(View.INVISIBLE);
				findViewById(R.id.alpha_big_brush).setVisibility(View.INVISIBLE);
				if(findViewById(R.id.draw_big_brush).getVisibility()==View.VISIBLE)
				{
					findViewById(R.id.draw_big_brush).setVisibility(View.INVISIBLE);
					findViewById(R.id.draw_small_brush).setVisibility(View.INVISIBLE);
					findViewById(R.id.draw_medium_brush).setVisibility(View.INVISIBLE);
					drawSolid = noBrush;
				}
				else
				{
					findViewById(R.id.draw_big_brush).setVisibility(View.VISIBLE);
					findViewById(R.id.draw_small_brush).setVisibility(View.VISIBLE);
					findViewById(R.id.draw_medium_brush).setVisibility(View.VISIBLE);
				}
		}
		});
		this.setSolidBrushSizeListener();
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
		
		/* Ajout du gestionnaire d'évenement pour le bouton de caméra */
		OnClickListener camCl = new ActiviteCamera(this);
		findViewById(R.id.TakePicture).setOnClickListener(camCl);
		
		this.setAlphaListener();
		this.setDrawListener();
	}
	
	private void initImageView() {
		ImageView surface = (ImageView) findViewById(R.id.CurrentMap);
		Bitmap b = Bitmap.createBitmap(surface.getWidth(), surface.getHeight(), Bitmap.Config.ARGB_8888);
		surface.setImageBitmap(b);
	}
	
	private boolean onTouchAlpha(View surface, MotionEvent event)
	{
		final int factorSize = 20;
		int size = factorSize*drawAlpha;
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
					if(j>=0 && j<bitmap.getHeight() && (Math.sqrt(Math.pow(x-i, 2)+Math.pow(y-j, 2))<size))
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
	
	private boolean onTouchSolid(View surface, MotionEvent event)
	{
		final int sizeFactor = 20;
		int size = sizeFactor*drawSolid;
		int x=0;
		int y=0;
		final int couleurTerre = 0xff9f551e;
		Bitmap bitmap = ((BitmapDrawable)((ImageView)surface).getDrawable()).getBitmap();
		int action = event.getAction();
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
					if(j>=0 && j<bitmap.getHeight() && (Math.sqrt(Math.pow(x-i, 2)+Math.pow(y-j, 2))<size))
					{
						bitmap.setPixel(i, j, couleurTerre);
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
	
	public boolean onTouch(View surface, MotionEvent event) {
		if(drawAlpha == noBrush && drawSolid == noBrush)
		{
			return false;
		}
		if(!initializedImageView)
		{
			initImageView();
			initializedImageView = true;
		}
		if(drawAlpha != noBrush)
		{
			return onTouchAlpha(surface, event);
		}
		else if(drawSolid != noBrush)
		{
			return onTouchSolid(surface, event);
		}
		/* Impossible d'arriver ici */
		return false;
	}
	
	protected void onActivityResult(int requestCode, int resultCode,
            Intent retour) {
        if ((requestCode == TAKE_PICTURE) && (resultCode == RESULT_OK)) {
            String photoPath = retour.getStringExtra("image");
            FileInputStream stream;
			try {
				stream = new FileInputStream(photoPath);
				byte data[] = new byte[stream.available()];
				stream.read(data);
				ImageView surface = (ImageView) findViewById(R.id.CurrentMap);
	            Bitmap b = BitmapFactory.decodeByteArray(data, 0, data.length).copy(Bitmap.Config.ARGB_8888, true);
	            surface.setImageBitmap(Bitmap.createScaledBitmap(b,surface.getWidth(),surface.getHeight(),false));
	            initializedImageView = true;
	            stream.close();
			} catch (FileNotFoundException e) {
				finish();
			} catch (IOException e) {
				finish();
			}
        }
    }

}
