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
import android.widget.ImageButton;
import android.widget.ImageView;

public class ActiviteCreationCarte extends Activity implements OnClickListener,OnTouchListener {
	private ActiviteAndroworms activiteMenuPrincipal;
	static final int TAKE_PICTURE = 0;
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
				ImageButton big = (ImageButton) findViewById(R.id.alpha_big_brush);
				ImageButton medium = (ImageButton) findViewById(R.id.alpha_medium_brush);
				ImageButton small = (ImageButton) findViewById(R.id.alpha_small_brush);
				if( v == small)
				{
					drawAlpha = 1;
				}
				else if( v == medium)
				{
					drawAlpha = 2;
				}
				else if( v == big)
				{
					drawAlpha = 3;
				}
				else
				{
					drawAlpha = 0;
				}
				drawSolid = 0;
				big.setVisibility(View.INVISIBLE);
				small.setVisibility(View.INVISIBLE);
				medium.setVisibility(View.INVISIBLE);
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
				ImageButton big = (ImageButton) findViewById(R.id.alpha_big_brush);
				ImageButton medium = (ImageButton) findViewById(R.id.alpha_medium_brush);
				ImageButton small = (ImageButton) findViewById(R.id.alpha_small_brush);
				findViewById(R.id.draw_small_brush).setVisibility(View.INVISIBLE);
				findViewById(R.id.draw_medium_brush).setVisibility(View.INVISIBLE);
				findViewById(R.id.draw_big_brush).setVisibility(View.INVISIBLE);
				if(big.getVisibility()==View.VISIBLE)
				{
					big.setVisibility(View.INVISIBLE);
					small.setVisibility(View.INVISIBLE);
					medium.setVisibility(View.INVISIBLE);
					drawAlpha = 0;
				}
				else
				{
					big.setVisibility(View.VISIBLE);
					small.setVisibility(View.VISIBLE);
					medium.setVisibility(View.VISIBLE);
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
				ImageButton big = (ImageButton) findViewById(R.id.draw_big_brush);
				ImageButton medium = (ImageButton) findViewById(R.id.draw_medium_brush);
				ImageButton small = (ImageButton) findViewById(R.id.draw_small_brush);
				if( v == small)
				{
					drawSolid = 1;
				}
				else if( v == medium)
				{
					drawSolid = 2;
				}
				else if( v == big)
				{
					drawSolid = 3;
				}
				else
				{
					drawSolid = 0;
				}
				drawAlpha = 0;
				big.setVisibility(View.INVISIBLE);
				small.setVisibility(View.INVISIBLE);
				medium.setVisibility(View.INVISIBLE);
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
				ImageButton big = (ImageButton) findViewById(R.id.draw_big_brush);
				ImageButton medium = (ImageButton) findViewById(R.id.draw_medium_brush);
				ImageButton small = (ImageButton) findViewById(R.id.draw_small_brush);
				findViewById(R.id.alpha_small_brush).setVisibility(View.INVISIBLE);
				findViewById(R.id.alpha_medium_brush).setVisibility(View.INVISIBLE);
				findViewById(R.id.alpha_big_brush).setVisibility(View.INVISIBLE);
				if(big.getVisibility()==View.VISIBLE)
				{
					big.setVisibility(View.INVISIBLE);
					small.setVisibility(View.INVISIBLE);
					medium.setVisibility(View.INVISIBLE);
					drawSolid = 0;
				}
				else
				{
					big.setVisibility(View.VISIBLE);
					small.setVisibility(View.VISIBLE);
					medium.setVisibility(View.VISIBLE);
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
		int size = 20*drawAlpha;
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
		int size = 20*drawSolid;
		int x=0;
		int y=0;
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
						bitmap.setPixel(i, j, Color.argb(0xff,0x9F,0x55,0x1E));
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
		if(drawAlpha == 0 && drawSolid == 0)
		{
			return false;
		}
		if(!initializedImageView)
		{
			initImageView();
			initializedImageView = true;
		}
		if(drawAlpha >0)
		{
			return onTouchAlpha(surface, event);
		}
		else if(drawSolid>0)
		{
			return onTouchSolid(surface, event);
		}
		/* Impossible d'arriver ici */
		return false;
	}
	
	protected void onActivityResult(int requestCode, int resultCode,
            Intent retour) {
        if ((requestCode == TAKE_PICTURE) && (resultCode == RESULT_OK)) {
            //byte data[] = retour.getByteArrayExtra("image");
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
			} catch (FileNotFoundException e) {
				finish();
			} catch (IOException e) {
				finish();
			}
        }
    }

}
