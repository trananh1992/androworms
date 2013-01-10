package com.androworms;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.ImageView;

public class ActiviteCreationCarte extends Activity implements OnClickListener,OnTouchListener {
	private ActiviteAndroworms activiteMenuPrincipal;
	static final int TAKE_PICTURE = 0;
	private static final int BIG_BRUSH = 3;
	private static final int MEDIUM_BRUSH = 2;
	private static final int SMALL_BRUSH = 1;
	private static final int NO_BRUSH = 0;
	private int drawAlpha = 0;
	private int drawSolid = 0;
	private boolean initializedImageView = false;
	private boolean mustSave = false;
	
	/* Gestionnaire d'évênement permettant le lancement de cette activité */
	public void onClick(View arg0) {
		Intent intent = new Intent(this.activiteMenuPrincipal, ActiviteCreationCarte.class);
		this.activiteMenuPrincipal.startActivity(intent);
		
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if(mustSave)
			{
				AlertDialog.Builder dlg = new AlertDialog.Builder(this);
				
				dlg.setTitle("Save current map ?");
				dlg.setMessage("Type map name:");
				
				final EditText edit = new EditText(this);
				dlg.setView(edit);
				
				dlg.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						String value = edit.getText().toString();
						Log.e("input dialog",value);
						/* on crée le dossier pour stocker la photo */
						File root = Environment.getExternalStorageDirectory();
						File androworms = new File(root,"Androworms");
						boolean status = true;
						if (!androworms.exists()) {
							status = androworms.mkdir();
						}
						
						/* échec lors de la création du dossier */
						if(!status) {
							Log.e("fichier","échec lors de la création du dossier Androworms.");
							return;
						}
						
						/* création du path complet vers la photo */
						File photoPath = new File(androworms,value);
						/* sauvegarde de la photo */
						FileOutputStream filoutputStream;
						try {
							filoutputStream = new FileOutputStream(photoPath);
							ImageView surface = (ImageView) findViewById(R.id.CurrentMap);
							Bitmap b = ((BitmapDrawable)((ImageView)surface).getDrawable()).getBitmap();
							b.compress(Bitmap.CompressFormat.PNG, 100, filoutputStream);
							filoutputStream.flush();
							filoutputStream.close();
						} catch (FileNotFoundException e) {
							Log.e("CreationCarte","file not found");
						} catch (IOException e) {
							Log.e("CreationCarte","IO Exception");
						}
						finish();
					}
				});
				dlg.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						finish();
					}
				});
				dlg.show();
			}
			return super.onKeyDown(keyCode, event);
		}
		return false;
	}
	
	public ActiviteCreationCarte(ActiviteAndroworms activiteMenuPrincipal) {
		this.activiteMenuPrincipal = activiteMenuPrincipal;
	}
	
	public ActiviteCreationCarte() {
		super();
	}
	
	/* Ajout du gestionnaire d'évenement pour les boutons de taille de brosse Alpha */
	private void setAlphaBrushSizeListener() {
		OnClickListener brushSizeAlpha = new OnClickListener() {
			public void onClick(View v) {
				drawSolid = NO_BRUSH;
				drawAlpha = NO_BRUSH;
				if( v.equals(findViewById(R.id.alpha_small_brush))) {
					drawAlpha = SMALL_BRUSH;
				}
				else if( v.equals(findViewById(R.id.alpha_medium_brush))) {
					drawAlpha = MEDIUM_BRUSH;
				}
				else if( v.equals(findViewById(R.id.alpha_big_brush))) {
					drawAlpha = BIG_BRUSH;
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
	
	/* Ajout du gestionnaire d'évenement pour le bouton de alpha */
	private void setAlphaListener() {
		findViewById(R.id.erase_button).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				findViewById(R.id.draw_small_brush).setVisibility(View.INVISIBLE);
				findViewById(R.id.draw_medium_brush).setVisibility(View.INVISIBLE);
				findViewById(R.id.draw_big_brush).setVisibility(View.INVISIBLE);
				if(findViewById(R.id.alpha_big_brush).getVisibility()==View.VISIBLE) {
					findViewById(R.id.alpha_big_brush).setVisibility(View.INVISIBLE);
					findViewById(R.id.alpha_medium_brush).setVisibility(View.INVISIBLE);
					findViewById(R.id.alpha_small_brush).setVisibility(View.INVISIBLE);
					drawAlpha = NO_BRUSH;
				}
				else {
					findViewById(R.id.alpha_big_brush).setVisibility(View.VISIBLE);
					findViewById(R.id.alpha_medium_brush).setVisibility(View.VISIBLE);
					findViewById(R.id.alpha_small_brush).setVisibility(View.VISIBLE);
				}
			}
		});
		this.setAlphaBrushSizeListener();
	}
	
	/* Ajout du gestionnaire d'évenement pour les boutons de taille de brosse Solid */
	private void setSolidBrushSizeListener() {
		OnClickListener brushSizeSolid = new OnClickListener() {
			public void onClick(View v) {
				drawSolid = NO_BRUSH;
				drawAlpha = NO_BRUSH;
				if( v.equals(findViewById(R.id.draw_small_brush))) {
					drawSolid = SMALL_BRUSH;
				}
				else if( v.equals(findViewById(R.id.draw_medium_brush))) {
					drawSolid = MEDIUM_BRUSH;
				}
				else if( v.equals(findViewById(R.id.draw_big_brush))) {
					drawSolid = BIG_BRUSH;
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
	
	/* Ajout du gestionnaire d'évenement pour le bouton de Solid */
	private void setDrawListener() {
		findViewById(R.id.draw).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				findViewById(R.id.alpha_small_brush).setVisibility(View.INVISIBLE);
				findViewById(R.id.alpha_medium_brush).setVisibility(View.INVISIBLE);
				findViewById(R.id.alpha_big_brush).setVisibility(View.INVISIBLE);
				if(findViewById(R.id.draw_big_brush).getVisibility()==View.VISIBLE) {
					findViewById(R.id.draw_big_brush).setVisibility(View.INVISIBLE);
					findViewById(R.id.draw_small_brush).setVisibility(View.INVISIBLE);
					findViewById(R.id.draw_medium_brush).setVisibility(View.INVISIBLE);
					drawSolid = NO_BRUSH;
				}
				else {
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
		
		/* Ajout des gestionnaires d'évênements pour les boutons de dessins */
		this.setAlphaListener();
		this.setDrawListener();
		
		findViewById(R.id.alpha_auto).setOnClickListener(new OnClickListener() {
			public void onClick(View v)
			{
				autoAlpha();
			}
		});
	}
	
	/* initialisation de la surface de dessin avec une image vide */
	private void initImageView() {
		ImageView surface = (ImageView) findViewById(R.id.CurrentMap);
		Bitmap b = Bitmap.createBitmap(surface.getWidth(), surface.getHeight(), Bitmap.Config.ARGB_8888);
		surface.setImageBitmap(b);
	}
	
	private void autoAlpha4()
	{
		ImageView surface = (ImageView) findViewById(R.id.CurrentMap);
		Bitmap b = ((BitmapDrawable)((ImageView)surface).getDrawable()).getBitmap();
		int height = b.getHeight();
		int width = b.getWidth();
		Bitmap transformed = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
		int i,j;
		final int nComponents = 3;
		final int maxComponentValue = 255;
		int dark = b.getPixel(0,0);
		int densityD = (Color.red(dark)+Color.green(dark)+Color.blue(dark))/nComponents;
		int light = b.getPixel(0,0);
		int densityL = (Color.red(light)+Color.green(light)+Color.blue(light))/nComponents;
		
		Log.v("autoAlpha", "begin computing alpha");
		for(i=0;i<width;i++)
		{
			for(j=0;j<height;j++)
			{
				int color = b.getPixel(i, j);
				if(Color.alpha(color)!=255)
				{
					continue;
				}
				int density = (Color.red(color)+Color.green(color)+Color.blue(color))/nComponents;
				if(density<densityL)
				{
					light = color;
					densityL = density;
				}
				else if(density > densityD)
				{
					dark = color;
					densityD = density;
				}
			}
		}
		for(i=0;i<width;i++)
		{
			for(j=0;j<height;j++)
			{
				int color = b.getPixel(i, j);
				if(Color.alpha(color)!=255)
				{
					continue;
				}
				int density = (Color.red(color)+Color.green(color)+Color.blue(color))/nComponents;
				if(Math.abs(densityL-density)<Math.abs(densityD-density))
				{
					transformed.setPixel(i, j, Color.argb(maxComponentValue, Color.red(color), Color.green(color), Color.blue(color)));
				}
				else
				{
					transformed.setPixel(i, j, Color.argb(0, Color.red(color), Color.green(color), Color.blue(color)));
				}
			}
		}
		Bitmap overlay = Bitmap.createBitmap(transformed.getWidth(), transformed.getHeight(), transformed.getConfig());
		Canvas c = new Canvas(overlay);
		c.drawBitmap(transformed, new Matrix(), null);
		((ImageView) surface).setImageBitmap(overlay);
	}
	
	private void autoAlpha()
	{
		autoAlpha4();
		mustSave = true;
	}
	
	private boolean drawEvent(View surface, MotionEvent event)
	{
		final int factorSize = 20;
		final int couleurTerre = 0xff9f551e;
		int size = 0;
		/* Sélection de la taille de la brosse de dessin */
		if(drawAlpha != NO_BRUSH) {
			size = factorSize*drawAlpha;
		}
		else {
			size = factorSize*drawSolid;
		}
		int x=0;
		int y=0;
		mustSave = true;
		/* récupération de l'image sur laquelle on dessine */
		Bitmap bitmap = ((BitmapDrawable)((ImageView)surface).getDrawable()).getBitmap();
		int action = event.getAction();
		int color;
		int red,green,blue;
		/* On ne dessine qu'en cas d'appuie ou de déplacement */
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
		/* on boucle sur le petit carré de coté "size" autour du doigt */
		for(int i=(x-size);i<(x+size);i++)
		{
			if(i>=0 && i<bitmap.getWidth())
			{
				for(int j=(y-size);j<(y+size);j++)
				{
					/* On dessine du alpha ou de la terre si on est dans l'image
					 * et pour sur les points qui forment un cercle de rayon size
					 * autour du doigt */
					if(j>=0 && j<bitmap.getHeight() && (Math.sqrt(Math.pow(x-i, 2)+Math.pow(y-j, 2))<size))
					{
						if(drawAlpha != NO_BRUSH)
						{
							color = bitmap.getPixel(i, j);
							red = Color.red(color);
							green = Color.green(color);
							blue = Color.blue(color);
							bitmap.setPixel(i, j, Color.argb(0,red,green,blue));
						}
						else
						{
							bitmap.setPixel(i, j, couleurTerre);
						}
					}
				}
			}
		}
		/* On actualise l'image */
		Bitmap overlay = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
		Canvas c = new Canvas(overlay);
		c.drawBitmap(bitmap, new Matrix(), null);
		((ImageView) surface).setImageBitmap(overlay);
		return true;
	}
	
	
	public boolean onTouch(View surface, MotionEvent event) {
		/* Si aucune brosse n'est sélectionnée, on a rien à dessiner */
		if(drawAlpha == NO_BRUSH && drawSolid == NO_BRUSH) {
			return false;
		}
		if(!initializedImageView) {
			initImageView();
			initializedImageView = true;
		}
		return drawEvent(surface,event);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent retour) {
		/* Retour de l'activité fille de prise de photo */
		if ((requestCode == TAKE_PICTURE) && (resultCode == RESULT_OK)) {
			/* récupération de l'emplacement de stockage de l'image */
			String photoPath = retour.getStringExtra("image");
			FileInputStream stream;
			/* On ouvre l'image */
			try {
				stream = new FileInputStream(photoPath);
				byte data[] = new byte[stream.available()];
				stream.read(data);
				
				/* On met l'image en fond de l'activité */
				ImageView surface = (ImageView) findViewById(R.id.CurrentMap);
				Bitmap b = BitmapFactory.decodeByteArray(data, 0, data.length).copy(Bitmap.Config.ARGB_8888, true);
				Bitmap overlay = Bitmap.createBitmap(surface.getWidth(), surface.getHeight(), b.getConfig());
				Canvas c = new Canvas(overlay);
				c.drawBitmap(b, Math.max(0, (surface.getHeight()-b.getHeight())/2),Math.max(0,(surface.getWidth()-b.getWidth())/2), null);
				((ImageView) surface).setImageBitmap(overlay);
				
				initializedImageView = true;
				stream.close();
				new File(photoPath).delete();
			} catch (FileNotFoundException e) {
				finish();
			} catch (IOException e) {
				finish();
			}
		}
	}
}