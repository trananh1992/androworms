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
	
	private static final String TAG = "TESTAndroworms.TouchRelativeLayout";
	
	// 3 états
	private int mode;
	private static final int RIEN = 0;
	private static final int DEPLACEMENT = 1;
	private static final int ZOOM = 2;
	
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
	
	
	// Gestion du zoom
	private ScaleGestureDetector mScaleDetector;
	private float scaleCourant;
	private Matrix matrix;
	private static final float ZOOM_MIN = 1f;
	private static final float ZOOM_MAX = 4.0f;
	
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
		
		mode = RIEN;
		this.setWillNotDraw(false);
		this.setClickable(true);
		positionNouvelleTouche = new PointF(-1, -1);
		positionAncienneTouche = new PointF(-1, -1);
		positionFond = new PointF(0, 0);
		positionJoueur1 = new PointF(80, 470);
		positionJoueur2 = new PointF(1000, 490);
		
		/* Bitmap */
		bmFond = prepareBitmap(getResources().getDrawable(R.drawable.image_fond_640x360),1280, 720);
		bmTerrain = prepareBitmap(getResources().getDrawable(R.drawable.terrain_jeu_defaut_640x360), 1280, 720);
		bmJoueur1 = prepareBitmap(getResources().getDrawable(R.drawable.logo_android_robot), 180, 173);
		bmJoueur2 = prepareBitmap(getResources().getDrawable(R.drawable.logo_android_robot), 180, 173);

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
		
		idPointeurCourant = 0;
		
		setOnTouchListener(new OnTouchListener() {
		
			public boolean onTouch(View v, MotionEvent event) {
				
				positionAncienneTouche.set(positionNouvelleTouche);
				positionNouvelleTouche = new PointF(event.getX(), event.getY());
				
				mScaleDetector.onTouchEvent(event);
				
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN: // Appui sur l'écran avec un doigt
					mode = DEPLACEMENT;
					positionAncienneTouche = new PointF(-1, -1);
					break;
				
				case MotionEvent.ACTION_POINTER_DOWN: // En théorie 2 doigts quiappuie en même temps (non reproductible)
					mode = DEPLACEMENT;
					positionAncienneTouche = new PointF(-1, -1);
					break;
				
				case MotionEvent.ACTION_MOVE: // Un doigt qui bouge sur l'écran
				
					if (mode == DEPLACEMENT) {
						// En mode déplacement, position_ancienne_touche n'est jamais égale à -1 sinon erreur
						
						float tempX, tempY;
						
						tempX = positionNouvelleTouche.x - positionAncienneTouche.x;
						tempY = positionNouvelleTouche.y - positionAncienneTouche.y;
						matrix.postTranslate(tempX, tempY);
					
						fixTrans();
					}
					
					break;
				
				case MotionEvent.ACTION_UP: // Un doigt qu'on leve (to check)
					mode = RIEN;
					positionAncienneTouche = new PointF(-1, -1);
					break;
				
				case MotionEvent.ACTION_POINTER_UP: // Lorsque l'on leve 2 doigt (to ceck, parce que ça peut aussi être le second doigt qui se leve)
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

        if (fixTransX != 0 || fixTransY != 0)
        {
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

        if (trans < minTrans)
        {
            return -trans + minTrans;
        }
        if (trans > maxTrans)
        {
            return -trans + maxTrans;
        }
        return 0;
    }
	
	@Override
	protected void onDraw(Canvas canvas) {
		
	}
	
	@Override
	protected void dispatchDraw(Canvas canvas) {
		canvas.setMatrix(matrix);
		canvas.drawBitmap(bmFond, positionFond.x, positionFond.y, null);
		canvas.drawBitmap(bmTerrain, positionFond.x, positionFond.y, null);
		canvas.drawBitmap(bmJoueur1, positionJoueur1.x, positionJoueur1.y, null);
		canvas.drawBitmap(bmJoueur2, positionJoueur2.x, positionJoueur2.y, null);

	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
		@Override
		public boolean onScaleBegin(ScaleGestureDetector detector) {
			mode = ZOOM;
			return true;
		}
		
		@Override
		public boolean onScale(ScaleGestureDetector detector) {
			
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
		}
	}
}