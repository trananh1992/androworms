package com.androworms.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.RelativeLayout;

import com.androworms.GameActivity;
import com.androworms.R;

/** Ce composant graphique est un RelativeLayout personnalisé pour Androworms.
 * Il permet d'être zoomé et déplacer ce qui a pour effet de zoomer et déplacer tous les composants "ImageView" enfants de ce Layout.
 */
public class TouchRelativeLayout extends RelativeLayout {
	
	private static final String TAG = "TESTAndroworms.TouchRelativeLayout";
	
	/* Constantes de tailles des composants*/
	private static final int MAP_WIDTH = 1280;
	private static final int MAP_HEIGHT = 720;
	private static final int JOUEUR_WIDTH = 180;
	private static final int JOUEUR_HEIGHT = 173;
	
	private static final int TAILLE_MAX_TIR = 300;
	
	// Traquer le mouvement
	private PointF positionAncienneTouche;
	private PointF positionNouvelleTouche;
	
	private PointF positionFond;
	private PointF positionJoueur1;
	private PointF positionJoueur2;
	
	// Images pour le jeu
	private Bitmap bmFond;
	private Bitmap bmTerrain;
	private Bitmap bmJoueur1;
	private Bitmap bmJoueur2;
	
	// Gestion du zoom
	private ScaleGestureDetector mScaleDetector;
	private float scaleCourant;
	private Matrix matrix;
	private static final float ZOOM_MIN = 1f; // pour rajouter une bordure sur le c$oté, mettre 0.8f ici
	private static final float ZOOM_MAX = 4.0f;
	
	// TIR
	PointF pointTir;
	
	public TouchRelativeLayout(Context context) {
		super(context);
		constructeurPartage(context);
	}
	
	public TouchRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		constructeurPartage(context);
	}
	
	public TouchRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		constructeurPartage(context);
	}
	
	/**
	 * Cette fonction initialise le composants avec un constructeur partagé
	 * 
	 * @param context
	 */
	private void constructeurPartage(Context context) {
		Log.v(TAG, "constructeurPartage()");
		
		
		GameActivity.mode = GameActivity.RIEN;
		this.setWillNotDraw(false);
		this.setClickable(true);
		pointTir = new PointF(-1, -1);
		positionNouvelleTouche = new PointF(-1, -1);
		positionAncienneTouche = new PointF(-1, -1);
		positionFond = new PointF(0, 0);
		positionJoueur1 = new PointF(80, 470);
		positionJoueur2 = new PointF(1000, 490);
		
		/* Bitmap */
		bmFond = prepareBitmap(getResources().getDrawable(R.drawable.image_fond_640x360),MAP_WIDTH, MAP_HEIGHT);
		bmTerrain = prepareBitmap(getResources().getDrawable(R.drawable.terrain_jeu_defaut_640x360), MAP_WIDTH, MAP_HEIGHT);
		bmJoueur1 = prepareBitmap(getResources().getDrawable(R.drawable.logo_android_robot), JOUEUR_WIDTH, JOUEUR_HEIGHT);
		bmJoueur2 = prepareBitmap(getResources().getDrawable(R.drawable.logo_android_robot), JOUEUR_WIDTH, JOUEUR_HEIGHT);
		
		/* scale detector */
		mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
		scaleCourant = 1;
		
		
		matrix = new Matrix();
	}
	
	private static Bitmap prepareBitmap(Drawable drawable, int width, int height) {
		Bitmap bitmap = Bitmap.createBitmap(width, height,Bitmap.Config.ARGB_8888);
		drawable.setBounds(0, 0, width, height);
		Canvas canvas = new Canvas(bitmap);
		drawable.draw(canvas);
		return bitmap;
	}
	
	/**
	 * Initialisation du TouchRelativeLayout Cette fonction doit être appelé après avoir ajouter les composants au RelativeLayout.
	 * Elle paramètre la liste des ImageView de ce layout pour pouvoir les manipuler.
	 * 
	 * @param context
	 */
	public void init(Context context) {
		Log.v(TAG, "init()");
		matrix = new Matrix();
		
		setOnTouchListener(new OnTouchListener() {
		
			public boolean onTouch(View v, MotionEvent event) {
				
				positionAncienneTouche.set(positionNouvelleTouche);
				positionNouvelleTouche = new PointF(event.getX(), event.getY());
				
				mScaleDetector.onTouchEvent(event);
				
				/*       Gestion des doigts
				 * Chaque fois qu'un doigt se rajoute sur l'écran, un id lui est attribué. (un id est un nombre qui part de 0 jusqu'à environ 10)
				 * Quand un doigt se retire de l'écran, l'ID est libéré.
				 * Lorsque l'on appuie sur l'écran avec un doigt, il reçois l'ID le plus petit disponible.
				 * Le doigt principale est toujours le doigt d'ID 0. Il est donc possible que à un moment il n'y ai pas de doigt principal sur l'écran.
				 * (Exemple : doigt A posé sur l'écran (doigt principal) -> doigt B posé sur l'écran -> doigt A levé)
				 */
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					// Action : Appui sur l'écran lorsqu'il n'y a aucun doigt. Ce doigt est donc le doigt principal
					if (GameActivity.mode != GameActivity.TIR) {
						GameActivity.mode = GameActivity.DEPLACEMENT;
					} else {
						pointTir.set(positionNouvelleTouche);
					}
					positionAncienneTouche = new PointF(-1, -1);
					break;
				
				case MotionEvent.ACTION_POINTER_DOWN:
					// Action : Lorsque l'on a un ou plusieurs doigt sur l'écran (et pas le doigt principal) et qu'on appuie avec un doigt
					if (GameActivity.mode != GameActivity.TIR) {
						GameActivity.mode = GameActivity.DEPLACEMENT;
					} else {
						pointTir.set(positionNouvelleTouche);
					}
					positionAncienneTouche = new PointF(-1, -1);
					break;
				case MotionEvent.ACTION_MOVE:
					// Action : Un doigt sur l'écran qui bouge
					if (GameActivity.mode == GameActivity.DEPLACEMENT) {
						// En mode déplacement, position_ancienne_touche n'est jamais égale à -1 sinon erreur
						
						float tempX, tempY;
						
						tempX = positionNouvelleTouche.x - positionAncienneTouche.x;
						tempY = positionNouvelleTouche.y - positionAncienneTouche.y;
						
						// Accelérer le déplacement quand on fait de grands mouvement ! (à paramètrer plus finement !)
						if (Math.abs(tempX) > 10) {
							tempX *= 3;
						}
						if (Math.abs(tempY) > 10) {
							tempY *= 3;
						}
						matrix.postTranslate(tempX, tempY);
					
						fixTrans();
					} else if (GameActivity.mode == GameActivity.TIR) {
						Log.v(TAG,"TIR (ACTION_MOVE)");
					}
					break;
				case MotionEvent.ACTION_UP:
					// Action : Lever du seul doigt sur l'écran. Ce doigt était donc le doigt principal
					if (GameActivity.mode != GameActivity.TIR) {
						GameActivity.mode = GameActivity.RIEN;
					}
					positionAncienneTouche = new PointF(-1, -1);
					break;
				
				case MotionEvent.ACTION_POINTER_UP:
					// Action : lorsque que l'on a plusieurs doigts sur l'écran et que l'on lève le doigt principal
					if (GameActivity.mode != GameActivity.TIR) {
						GameActivity.mode = GameActivity.RIEN;
					}
					positionAncienneTouche = new PointF(-1, -1);
					break;
				default:
					break;
				}
				
				invalidate();
				return true;
			}
			
		});
	}
	
	/**
	 * Fonction qui corrige la translation si elle dépasse
	 */
	private void fixTrans() {
		float[] m = new float[9];
		matrix.getValues(m);
		float transX = m[Matrix.MTRANS_X];
		float transY = m[Matrix.MTRANS_Y];
		
		float fixTransX = getFixTrans(transX, bmFond.getWidth(), bmFond.getWidth() * scaleCourant);
		float fixTransY = getFixTrans(transY, bmFond.getHeight(), bmFond.getHeight() * scaleCourant);
		
		if (fixTransX != 0 || fixTransY != 0) {
			matrix.postTranslate(fixTransX, fixTransY);
		}
	}
	
	private float getFixTrans(float trans, float viewSize, float contentSize) {
		float minTrans, maxTrans;
        
        if (contentSize <= viewSize) {
        	minTrans = 0;
            maxTrans = viewSize - contentSize;
        } else {
        	minTrans = viewSize - contentSize;
            maxTrans = 0;
        }
        
        if (trans < minTrans) {
        	return -trans + minTrans;
        }
        if (trans > maxTrans) {
        	return -trans + maxTrans;
        }
        return 0;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		
	}
	
	@Override
	protected void dispatchDraw(Canvas canvas) {
		// Application de la matric avec la translation et le zoom
		canvas.setMatrix(matrix);
		// Dessins des objets du jeu
		canvas.drawBitmap(bmFond, positionFond.x, positionFond.y, null);
		canvas.drawBitmap(bmTerrain, positionFond.x, positionFond.y, null);
		canvas.drawBitmap(bmJoueur1, positionJoueur1.x, positionJoueur1.y, null);
		canvas.drawBitmap(bmJoueur2, positionJoueur2.x, positionJoueur2.y, null);
		
		// Dessins des objets pour le tir
		if (GameActivity.mode == GameActivity.TIR) {
			// Pour le tir, on a pas de translation ni de zoom
			Matrix m = new Matrix();
			canvas.setMatrix(m);
			
			float deplacementX = pointTir.x - positionNouvelleTouche.x;
			float deplacementY = pointTir.y - positionNouvelleTouche.y;
			float distance = Math.round(Math.sqrt(Math.pow(positionNouvelleTouche.x - pointTir.x, 2) + Math.pow(positionNouvelleTouche.y - pointTir.y, 2)));
			
			// Mise en place des outils de dessins
			Paint paint = new Paint();
			paint.setStrokeWidth(10);
			paint.setAntiAlias(true);
			paint.setStrokeCap(Paint.Cap.ROUND);
			paint.setStyle(Paint.Style.STROKE);
			
			
			float angle = ((float)(Math.atan2 (deplacementY, deplacementX)*180.0d/Math.PI))+90.0f;
			// Ajustement de l'angle
			angle += 90;
			
			// Onde viollette
			paint.setColor(Color.MAGENTA);
			RectF rect = new RectF();
			for (int i=0;(i<=distance && i < TAILLE_MAX_TIR);i+=20) {
				rect.set(pointTir.x - i, pointTir.y - i,
						pointTir.x + i, pointTir.y + i);
				canvas.drawArc(rect, angle, 30, false, paint);
			}
						
			// flèche verte
			paint.setColor(Color.GREEN);
			paint.setStrokeWidth(30);
			canvas.drawLine(pointTir.x, pointTir.y,
					pointTir.x + distance, pointTir.y - distance, paint);
			
			// bout de la flèche. (si trop moche utiliser un Path et canvas.drawPath()
			canvas.drawLine(pointTir.x + distance, pointTir.y - distance,
					pointTir.x + distance - 50, pointTir.y - distance, paint);
			canvas.drawLine(pointTir.x + distance, pointTir.y - distance,
					pointTir.x + distance, pointTir.y - distance +50, paint);
			canvas.drawLine(pointTir.x + distance - 50, pointTir.y - distance,
					pointTir.x + distance, pointTir.y - distance +50, paint);
		}
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
		@Override
		public boolean onScaleBegin(ScaleGestureDetector detector) {
			if (GameActivity.mode != GameActivity.TIR) {
				GameActivity.mode = GameActivity.ZOOM;
				return true;
			} else {
				return false;
			}
		}
		
		@Override
		public boolean onScale(ScaleGestureDetector detector) {
			if (GameActivity.mode == GameActivity.ZOOM) {
				// Scale sur cette évenement
				float mScaleFactor = detector.getScaleFactor();
				
				// Scale globale sur l'image d'origine
				float temp = scaleCourant * mScaleFactor;
				// Gestion du Zoom max et zomm min
				if (ZOOM_MIN > temp) { 
					mScaleFactor = ZOOM_MIN / scaleCourant;
					scaleCourant = ZOOM_MIN;
				} else if (temp > ZOOM_MAX) { 
					mScaleFactor = ZOOM_MAX / scaleCourant;
					scaleCourant = ZOOM_MAX;
				} else {
					scaleCourant = temp;
				}
				// Application du Zoom
				matrix.postScale(mScaleFactor, mScaleFactor, detector.getFocusX(), detector.getFocusY());
				fixTrans();
				return true;
			} else {
				return false;
			}
			
		}
	}
}