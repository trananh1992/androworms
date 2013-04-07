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
import android.graphics.Paint;
import android.graphics.Rect;
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

public class ActiviteEditeurCarte extends Activity implements OnClickListener, OnTouchListener {
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
	private Canvas drawCanvas = null;
	private Bitmap upCalc = null;
	static final String TAG = "ActiviteCreationCarte";
	static final int MAX_COLOR_VALUE = 255;
	static final int DEFINITION = 3;
	
	public ActiviteEditeurCarte() {
		super();
	}
	
	public ActiviteEditeurCarte(ActiviteAndroworms activiteMenuPrincipal) {
		this.activiteMenuPrincipal = activiteMenuPrincipal;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		/* Affiche la vue par défaut */
		setContentView(R.layout.activite_editeur_carte);
		ImageView surface = (ImageView) findViewById(R.id.CurrentMap);
		((ImageView) findViewById(R.id.background)).setBackgroundResource(R.color.ciel);
		
		surface.setOnTouchListener(this);
		
		/* Ajout du gestionnaire d'évenement pour le bouton de caméra */
		OnClickListener camCl = new ActiviteCamera(this);
		findViewById(R.id.TakePicture).setOnClickListener(camCl);
		
		/* Ajout des gestionnaires d'évênements pour les boutons de dessins */
		this.setAlphaListener();
		this.setDrawListener();
		
		findViewById(R.id.alpha_auto).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				autoAlpha();
			}
		});
	}
	
	/** Gestionnaire d'évênement permettant le lancement de cette activité */
	public void onClick(View arg0) {
		Intent intent = new Intent(this.activiteMenuPrincipal, ActiviteEditeurCarte.class);
		this.activiteMenuPrincipal.startActivity(intent);
		
	}
	
	private void fusionnerCalques(Bitmap result, Bitmap base) {
		for (int i = 0; i < base.getWidth(); i++) {
			for (int j = 0; j < base.getHeight(); j++) {
				int color = base.getPixel(i, j);
				if (color == getResources().getColor(R.color.ciel)) {
					result.setPixel(i, j, 0);
				} else if (Color.alpha(color) != 0) {
					result.setPixel(i, j, color);
				}
			}
		}
	}
	
	private void saveMap(String name) {
		final int compression = 100;
		File root = Environment.getExternalStorageDirectory();
		File fichierCarte = new File(root, ActiviteAndroworms.DOSSIER_CARTE);
		boolean status = true;
		if (!fichierCarte.exists()) {
			status = fichierCarte.mkdir();
		}
		
		/* échec lors de la création du dossier */
		if (!status) {
			Log.e(TAG, "Echec lors de la création du dossier Androworms.");
			return;
		}
		
		/* création du path complet vers la photo */
		File photoPath = new File(fichierCarte, name);
		/* sauvegarde de la photo */
		FileOutputStream filoutputStream;
		try {
			filoutputStream = new FileOutputStream(photoPath);
			ImageView background = (ImageView) findViewById(R.id.background);
			BitmapDrawable drawable = (BitmapDrawable) background.getDrawable();
			Bitmap backgroundBitmap = null;
			if (null != drawable) {
				backgroundBitmap = drawable.getBitmap();
			}
			ImageView surface = (ImageView) findViewById(R.id.CurrentMap);
			Bitmap b = ((BitmapDrawable) ((ImageView) surface).getDrawable()).getBitmap();
			Bitmap result = null;
			/* fusion des calques si il y en à deux */
			if (backgroundBitmap != null) {
				result = backgroundBitmap.copy(Bitmap.Config.ARGB_8888, true);
			} else {
				result = Bitmap.createBitmap(b.getWidth(), b.getHeight(), Bitmap.Config.ARGB_8888);
			}
			fusionnerCalques(result, b);
			
			result.compress(Bitmap.CompressFormat.PNG, compression, filoutputStream);
			filoutputStream.flush();
			filoutputStream.close();
		} catch (FileNotFoundException e) {
			Log.e(TAG, "file not found");
		} catch (IOException e) {
			Log.e(TAG, "IO Exception");
		}
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mustSave) {
				AlertDialog.Builder dlg = new AlertDialog.Builder(this);
				
				dlg.setTitle(R.string.sauvegarder_carte);
				dlg.setMessage(R.string.nom_carte);
				
				final EditText edit = new EditText(this);
				edit.setSingleLine();
				dlg.setView(edit);
				dlg.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						/* Création de l'intent pour passer à l'activité parent l'endroit où est la photo */
						Intent i = new Intent();
						i.putExtra("image", "");
						setResult(RESULT_OK, i);
						finish();
					}
				});
				
				dlg.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						String value = edit.getText().toString();
						/* on crée le dossier pour stocker la photo */
						if (value.length() > 0) {
							saveMap(value);
						}
						/* Création de l'intent pour passer à l'activité parent l'endroit où est la photo */
						Intent i = new Intent();
						i.putExtra("image", value);
						setResult(RESULT_OK, i);
						finish();
					}
				});
				dlg.show();
			}
			return super.onKeyDown(keyCode, event);
		}
		return false;
	}
	
	/** Ajout du gestionnaire d'évenement pour les boutons de taille de brosse Alpha */
	private void setAlphaBrushSizeListener() {
		OnClickListener brushSizeAlpha = new OnClickListener() {
			public void onClick(View v) {
				drawSolid = NO_BRUSH;
				drawAlpha = NO_BRUSH;
				if (v.equals(findViewById(R.id.alpha_small_brush))) {
					drawAlpha = SMALL_BRUSH;
				} else if (v.equals(findViewById(R.id.alpha_medium_brush))) {
					drawAlpha = MEDIUM_BRUSH;
				} else if (v.equals(findViewById(R.id.alpha_big_brush))) {
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
	
	/** Ajout du gestionnaire d'évenement pour le bouton de alpha */
	private void setAlphaListener() {
		findViewById(R.id.erase_button).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				findViewById(R.id.draw_small_brush).setVisibility(View.INVISIBLE);
				findViewById(R.id.draw_medium_brush).setVisibility(View.INVISIBLE);
				findViewById(R.id.draw_big_brush).setVisibility(View.INVISIBLE);
				if (findViewById(R.id.alpha_big_brush).getVisibility() == View.VISIBLE) {
					findViewById(R.id.alpha_big_brush).setVisibility(View.INVISIBLE);
					findViewById(R.id.alpha_medium_brush).setVisibility(View.INVISIBLE);
					findViewById(R.id.alpha_small_brush).setVisibility(View.INVISIBLE);
					drawAlpha = NO_BRUSH;
				} else {
					findViewById(R.id.alpha_big_brush).setVisibility(View.VISIBLE);
					findViewById(R.id.alpha_medium_brush).setVisibility(View.VISIBLE);
					findViewById(R.id.alpha_small_brush).setVisibility(View.VISIBLE);
				}
			}
		});
		this.setAlphaBrushSizeListener();
	}
	
	/** Ajout du gestionnaire d'évenement pour les boutons de taille de brosse Solid */
	private void setSolidBrushSizeListener() {
		OnClickListener brushSizeSolid = new OnClickListener() {
			public void onClick(View v) {
				drawSolid = NO_BRUSH;
				drawAlpha = NO_BRUSH;
				if (v.equals(findViewById(R.id.draw_small_brush))) {
					drawSolid = SMALL_BRUSH;
				} else if (v.equals(findViewById(R.id.draw_medium_brush))) {
					drawSolid = MEDIUM_BRUSH;
				} else if (v.equals(findViewById(R.id.draw_big_brush))) {
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
	
	/** Ajout du gestionnaire d'évenement pour le bouton de Solid */
	private void setDrawListener() {
		findViewById(R.id.draw).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				findViewById(R.id.alpha_small_brush).setVisibility(View.INVISIBLE);
				findViewById(R.id.alpha_medium_brush).setVisibility(View.INVISIBLE);
				findViewById(R.id.alpha_big_brush).setVisibility(View.INVISIBLE);
				if (findViewById(R.id.draw_big_brush).getVisibility() == View.VISIBLE) {
					findViewById(R.id.draw_big_brush).setVisibility(View.INVISIBLE);
					findViewById(R.id.draw_small_brush).setVisibility(View.INVISIBLE);
					findViewById(R.id.draw_medium_brush).setVisibility(View.INVISIBLE);
					drawSolid = NO_BRUSH;
				} else {
					findViewById(R.id.draw_big_brush).setVisibility(View.VISIBLE);
					findViewById(R.id.draw_small_brush).setVisibility(View.VISIBLE);
					findViewById(R.id.draw_medium_brush).setVisibility(View.VISIBLE);
				}
			}
		});
		this.setSolidBrushSizeListener();
	}
	
	/** Initialisation de la surface de dessin avec une image vide */
	private void initImageView() {
		ImageView surface = (ImageView) findViewById(R.id.CurrentMap);
		Bitmap b = Bitmap.createBitmap(surface.getWidth(), surface.getHeight(), Bitmap.Config.ARGB_8888);
		upCalc = Bitmap.createBitmap(b.getWidth(), b.getHeight(), b.getConfig());
		drawCanvas = new Canvas(upCalc);
		drawCanvas.drawBitmap(b, null, new Rect(0, 0, drawCanvas.getWidth(), drawCanvas.getHeight()), null);
		surface.setImageBitmap(upCalc);
	}
	
	private void separeAlpha(int width, int height, Bitmap base, int densityL, int densityD) {
		int i, j;
		final int nComponents = 3;
		
		// for(i=0;i<(width-DEFINITION+1);i+=DEFINITION)
		for (i = 0; i < width; i += DEFINITION) {
			// for(j=0;j<(height-DEFINITION+1);j+=DEFINITION)
			for (j = 0; j < height; j += DEFINITION) {
				int k, l;
				int density = 0;
				int sizeX = Math.min(DEFINITION, width - i);
				int sizeY = Math.min(DEFINITION, height - j);
				// for(k=0;k<DEFINITION;k++)
				for (k = 0; k < sizeX; k++) {
					// for(l=0;l<DEFINITION;l++)
					for (l = 0; l < sizeY; l++) {
						int color = base.getPixel(i + k, j + l);
						density += ((Color.red(color) + Color.green(color) + Color.blue(color)) / nComponents);
					}
				}
				density = density / (sizeX * sizeY);
				if (Math.abs(densityL - density) > Math.abs(densityD - density)) {
					Paint paint = new Paint();
					paint.setStrokeWidth(DEFINITION);
					paint.setColor(getResources().getColor(R.color.ciel));
					drawCanvas.drawPoint(i, j, paint);
					
				}
			}
		}
	}
	
	private void autoAlpha4() {
		ImageView surface = (ImageView) findViewById(R.id.CurrentMap);
		BitmapDrawable drawable = ((BitmapDrawable) ((ImageView) findViewById(R.id.background)).getDrawable());
		if (null == drawable) {
			return;
		}
		Bitmap b = drawable.getBitmap();
		if (null == b) {
			return;
		}
		int height = b.getHeight();
		int width = b.getWidth();
		int i, j;
		final int nComponents = 3;
		int origin = b.getPixel(0, 0);
		int densityD = (Color.red(origin) + Color.green(origin) + Color.blue(origin)) / nComponents;
		int densityL = (Color.red(origin) + Color.green(origin) + Color.blue(origin)) / nComponents;
		if (!initializedImageView) {
			initImageView();
			initializedImageView = true;
		}
		
		for (i = 0; i < (width - DEFINITION + 1); i += DEFINITION) {
			for (j = 0; j < (height - DEFINITION + 1); j += DEFINITION) {
				
				int color = b.getPixel(i, j);
				if (Color.alpha(color) != MAX_COLOR_VALUE) {
					continue;
				}
				int density = (Color.red(color) + Color.green(color) + Color.blue(color)) / nComponents;
				if (density < densityL) {
					densityL = density;
				} else if (density > densityD) {
					densityD = density;
				}
			}
		}
		separeAlpha(width, height, b, densityL, densityD);
		
		((ImageView) surface).draw(drawCanvas);
		((ImageView) surface).invalidate();
	}
	
	private void autoAlpha() {
		autoAlpha4();
		mustSave = true;
	}
	
	private boolean drawEvent(View surface, MotionEvent event) {
		final int factorSize = 20;
		final int echantillonage = 25;
		final float tailleHerbe = 10f;
		int size = 0;
		/* Sélection de la taille de la brosse de dessin */
		if (drawAlpha != NO_BRUSH) {
			size = factorSize * drawAlpha;
		} else {
			size = factorSize * drawSolid;
		}
		int x = 0;
		int y = 0;
		mustSave = true;
		/* récupération de l'image sur laquelle on dessine */
		Bitmap bitmap = ((BitmapDrawable) ((ImageView) surface).getDrawable()).getBitmap();
		int action = event.getAction();
		/* On ne dessine qu'en cas d'appuie ou de déplacement */
		switch (action) {
		case MotionEvent.ACTION_DOWN:
		case MotionEvent.ACTION_MOVE:
			break;
		default:
			return false;
		}
		x = (int) event.getX();
		x = Math.max(0, x);
		x = Math.min(bitmap.getWidth() - 1, x);
		y = (int) event.getY();
		y = Math.max(0, y);
		y = Math.min(bitmap.getHeight() - 1, y);
		if (drawAlpha != NO_BRUSH) {
			Paint paint = new Paint();
			paint.setStyle(Paint.Style.FILL);
			paint.setColor(getResources().getColor(R.color.ciel));
			drawCanvas.drawCircle(x, y, size, paint);
		} else {
			Paint paint = new Paint();
			paint.setStyle(Paint.Style.FILL);
			paint.setColor(getResources().getColor(R.color.terre));
			drawCanvas.drawCircle(x, y, size, paint);
			for (int i = 0; i < (2 * echantillonage * Math.PI); i++) {
				double radian = ((double) size) + (tailleHerbe / 2);
				double cos = Math.cos((double) i / echantillonage);
				double sin = Math.sin((double) i / echantillonage);
				int xG = (int) ((double) x + (double) (radian * cos));
				int yG = (int) ((double) y + (double) (radian * sin));
				if (yG >= drawCanvas.getHeight() || yG < 0 || xG >= drawCanvas.getWidth() || xG < 0) {
					continue;
				}
				Paint paint2 = new Paint();
				paint2.setColor(getResources().getColor(R.color.herbe));
				paint2.setStyle(Paint.Style.FILL_AND_STROKE);
				paint2.setStrokeWidth(tailleHerbe);
				int currentColor = upCalc.getPixel(xG, yG);
				if (currentColor != getResources().getColor(R.color.terre) && currentColor != getResources().getColor(R.color.herbe)) {
					drawCanvas.drawPoint(xG, yG, paint2);
				}
			}
		}
		((ImageView) surface).draw(drawCanvas);
		((ImageView) surface).invalidate();
		return true;
	}
	
	public boolean onTouch(View surface, MotionEvent event) {
		/* Si aucune brosse n'est sélectionnée, on a rien à dessiner */
		if (drawAlpha == NO_BRUSH && drawSolid == NO_BRUSH) {
			return false;
		}
		if (!initializedImageView) {
			initImageView();
			initializedImageView = true;
		}
		return drawEvent(surface, event);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent retour) {
		/* Retour de l'activité fille de prise de photo */
		if ((requestCode == TAKE_PICTURE) && (resultCode == RESULT_OK)) {
			/* récupération de l'emplacement de stockage de l'image */
			String photoPath = retour.getStringExtra("image");
			FileInputStream stream = null;
			ImageView surface = (ImageView) findViewById(R.id.CurrentMap);
			/* On ouvre l'image */
			try {
				stream = new FileInputStream(photoPath);
				byte data[] = new byte[stream.available()];
				stream.read(data);
				
				/* on libère le précédent bitmap */
				if (upCalc != null) {
					upCalc.recycle();
				}
				
				/* On met l'image en fond de l'activité */
				upCalc = BitmapFactory.decodeByteArray(data, 0, data.length);
				Bitmap overlay = Bitmap.createBitmap(surface.getWidth(), surface.getHeight(), upCalc.getConfig());
				
				drawCanvas = new Canvas(overlay);
				drawCanvas.drawBitmap(upCalc, new Rect(0, 0, upCalc.getWidth(), upCalc.getHeight()),
						new Rect(0, 0, surface.getWidth(), surface.getHeight()), null);
				((ImageView) findViewById(R.id.background)).setImageBitmap(overlay);
				stream.close();
				boolean success = new File(photoPath).delete();
				if (!success) {
					Log.e(TAG, "Unable to delete temporary file");
				}
			} catch (FileNotFoundException e) {
				finish();
			} catch (IOException e) {
				finish();
			} finally {
				try {
					if (stream != null) {
						stream.close();
					}
				} catch (Exception e) {
				}
			}
		}
	}
}