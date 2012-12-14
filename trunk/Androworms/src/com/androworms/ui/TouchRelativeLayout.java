package com.androworms.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.RelativeLayout;

import com.androworms.R;

/** Ce composant graphique est un RelativeLayout personnalisé pour Androworms.
 * Il permet d'être zoomé et déplacer ce qui a pour effet de zoomer et déplacer tous les composants "ImageView" enfants de ce Layout.
 */
public class TouchRelativeLayout extends RelativeLayout {
	
	// Variables globales
	private static final String TAG = "TESTAndroworms.TouchRelativeLayout3";
	
	// 3 états
	private int mode;
	static final int RIEN = 0;
	static final int DEPLACEMENT = 1;
	static final int ZOOM = 2;
	
	// Traquer le mouvement
	private int idPointeurCourant;
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
	
	private Bitmap bmFondOriginal;
	private Bitmap bmTerrainOriginal;
	private Bitmap bmJoueur1Original;
	private Bitmap bmJoueur2Original;
	
	
	// Gestion du zoom
	private ScaleGestureDetector mScaleDetector;
	private float scaleCourant;
	private Matrix matrix;
	static final float ZOOM_MIN = 0.5f;
	static final float ZOOM_MAX = 4.0f;
	
	
	// Constructeurs
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
	
	// Fonctions
	
	/**
	 * Cette fonction initialise le composants avec un constructeur partagé
	 * 
	 * @param context
	 */
	private void constructeurPartage(Context context) {
		Log.v(TAG, "constructeurPartage()");
		
		mode = RIEN;
		this.setWillNotDraw(false);
		this.setClickable(true);
		positionNouvelleTouche = new PointF(-1, -1);
		positionAncienneTouche = new PointF(-1, -1);
		positionFond = new PointF(0, 0);
		positionJoueur1 = new PointF(80, 470);
		positionJoueur2 = new PointF(1000, 490);
		
		/* Bitmap */
		bmFondOriginal = prepareBitmap(getResources().getDrawable(R.drawable.image_fond_640x360),1280, 720);
		bmTerrainOriginal = prepareBitmap(getResources().getDrawable(R.drawable.terrain_jeu_defaut_640x360), 1280, 720);
		bmJoueur1Original = prepareBitmap(getResources().getDrawable(R.drawable.logo_android_robot), 180, 173);
		bmJoueur2Original = prepareBitmap(getResources().getDrawable(R.drawable.logo_android_robot), 180, 173);
		
		bmFond = bmFondOriginal;
		bmTerrain = bmTerrainOriginal;
		bmJoueur1 = bmJoueur1Original;
		bmJoueur2 = bmJoueur2Original;
		
		/* scale detector */
		mScaleDetector = new ScaleGestureDetector(context, new ScaleListener2());
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
		
		idPointeurCourant = 0;
		setOnTouchListener(new OnTouchListener() {
		
			public boolean onTouch(View v, MotionEvent event) {
				
				positionAncienneTouche.set(positionNouvelleTouche);
				positionNouvelleTouche = new PointF(event.getX(), event.getY());
				
				// Log.v(TAG, "onTouch() (" + event.getX() + ";" + event.getY() + ")   --> NbPointeur="+event.getPointerCount());
				
				// scale
				mScaleDetector.onTouchEvent(event);
				
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN: // Appuie sur l'écran avec un doigt
					// Log.v(TAG, "ACTION_DOWN");
					mode = DEPLACEMENT;
					positionAncienneTouche = new PointF(-1, -1);
					break;
				
				case MotionEvent.ACTION_POINTER_DOWN: // En théorie 2 doigts quiappuie en même temps (non reproductible)
					// Log.v(TAG, "ACTION_POINTER_DOWN");
					mode = DEPLACEMENT;
					positionAncienneTouche = new PointF(-1, -1);
					break;
				
				case MotionEvent.ACTION_MOVE: // Un doigt qui bouge sur l'écran
				
					if (mode == DEPLACEMENT) {
						// Log.v(TAG, "ACTION_MOVE -- DEPLACEMENT");
						// En mode déplacement, position_ancienne_touche n'est jamais égale à -1 sinon erreur
						
						float tempX, tempY;
						
						// position_fond.x += position_nouvelle_touche.x -
						// position_ancienne_touche.x;
						// position_fond.y += position_nouvelle_touche.y -
						// position_ancienne_touche.y;
						
						// position_joueur_1.x += position_nouvelle_touche.x -
						// position_ancienne_touche.x;
						// position_joueur_1.y += position_nouvelle_touche.y -
						// position_ancienne_touche.y;
						
						
						
						
						// Terrain
						tempX = positionFond.x + positionNouvelleTouche.x - positionAncienneTouche.x;
						tempY = positionFond.y + positionNouvelleTouche.y - positionAncienneTouche.y;
						//if (tempX > 0 && (tempX + bm_fond.getWidth()) < 1280) {
							positionFond.x = tempX;
							positionJoueur1.x += positionNouvelleTouche.x - positionAncienneTouche.x;
							positionJoueur2.x += positionNouvelleTouche.x - positionAncienneTouche.x;
						//} else {
						//	Log.d(TAG, "REFUS (deplacement-x) :: tempX="+tempX+";  width="+bm_fond.getWidth());
						//}
						//if (tempY > 0 && (tempY + bm_fond.getHeight()) < 720) {
							positionFond.y = tempY;
							positionJoueur1.y += positionNouvelleTouche.y - positionAncienneTouche.y;
							positionJoueur2.y += positionNouvelleTouche.y - positionAncienneTouche.y;
						//}
						
						
						// Position bonhomme
						 /*tempX = position_joueur_1.x + position_nouvelle_touche.x - position_ancienne_touche.x;
						 tempY = position_joueur_1.y + position_nouvelle_touche.y - position_ancienne_touche.y;
						 if (tempX > 0 && (tempX + bm_joueur_1.getWidth()) < 1280) {
							 position_joueur_1.x = tempX;
						 }
						 if (tempY > 0 && (tempY + bm_joueur_1.getHeight()) < 720) {
							 position_joueur_1.y = tempY;
						 }*/
						 
					
					}/* else {
						// Log.v(TAG, "ACTION_MOVE -- other ("+mode+")");
					}*/
					
					break;
				
				case MotionEvent.ACTION_UP: // Un doigt qu'on leve (to check)
					// Log.v(TAG, "ACTION_UP");
					mode = RIEN;
					positionAncienneTouche = new PointF(-1, -1);
					break;
				
				case MotionEvent.ACTION_POINTER_UP: // Lorsque l'on leve 2 doigt (to ceck, parce que ça peut aussi être le second doigt qui se leve)
					// Log.v(TAG, "ACTION_POINTER_UP");
					mode = RIEN;
					positionAncienneTouche = new PointF(-1, -1);
					break;
				default:
					break;
				}
				
				invalidate();
				return true;// true ou false ?? -->
							// http://developer.android.com/reference/android/view/View.OnTouchListener.html
			}
		});
	
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		//Log.v(TAG, "onDraw()");
	}
	
	@Override
	protected void dispatchDraw(Canvas canvas) {
		//Log.v(TAG, "dispatchDraw()");
		
		// les 2 solutions marchent : on laisse les 2 pour le moment
		canvas.setMatrix(matrix);
		//canvas.scale(scale_courant, scale_courant);
		
		canvas.drawBitmap(bmFond, positionFond.x, positionFond.y, null);
		canvas.drawBitmap(bmTerrain, positionFond.x, positionFond.y, null);
		canvas.drawBitmap(bmJoueur1, positionJoueur1.x, positionJoueur1.y, null);
		canvas.drawBitmap(bmJoueur2, positionJoueur2.x, positionJoueur2.y, null);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		Log.v(TAG, "onMeasure()");
	
	}
	
	private class ScaleListener2 extends
			ScaleGestureDetector.SimpleOnScaleGestureListener {
		@Override
		public boolean onScaleBegin(ScaleGestureDetector detector) {
			mode = ZOOM;
			return true;
		}
		
		@Override
		public boolean onScale(ScaleGestureDetector detector) {
			
			float mScaleFactor = detector.getScaleFactor();
			
			Log.v(TAG, "onScale() = " + scaleCourant);
			
			float temp = scaleCourant * mScaleFactor;
			if (ZOOM_MIN < temp && temp < ZOOM_MAX) {
				scaleCourant = temp;
				
				
				matrix = new Matrix();
				
				matrix.postScale(scaleCourant, scaleCourant);
				
				
				/*original_width = bm_fond_original.getWidth();
				original_height = bm_fond_original.getHeight();
				bm_fond = Bitmap.createBitmap(bm_fond_original, 0, 0, Math.min(original_width, 1280), Math.min(original_height,720), matrix, false);
				
				original_width = bm_terrain_original.getWidth();
				original_height = bm_terrain_original.getHeight();
				bm_terrain = Bitmap.createBitmap(bm_terrain_original, 0, 0, Math.min(original_width, 1280), Math.min(original_height,720), matrix, false);
				*/
				
				
				//lst_imageview.get(2).setImageMatrix(matrix);
				
				/*
				original_width = bm_joueur_1_original.getWidth();
				original_height = bm_joueur_1_original.getHeight();
				bm_joueur_1 = Bitmap.createBitmap(bm_joueur_1_original, 0, 0, original_width, original_height, matrix, false);
				
				bm_joueur_1.recycle();*/
				
				
				/*lst_imageview.get(0).setImageBitmap(bm_fond);
				lst_imageview.get(1).setImageBitmap(bm_terrain);
				lst_imageview.get(2).setImageBitmap(bm_joueur_1);*/
				
				return true;
			}
			else {
				return false;
			}
			
		}
	}
}
