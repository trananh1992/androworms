package com.androworms;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RelativeLayout;

/** Ce composant graphique est un RelativeLayout personnalisé pour Androworms.
 * Il dessine tous les objets présents sur la carte
 * Il permet d'être zoomé et déplacer ce qui a pour effet de zoomer et déplacer tous les composants "ImageView" enfants de ce Layout.
 */
public class MoteurGraphique extends RelativeLayout {
	
	private static final String TAG = "Androworms.MoteurGraphique";
	
	/* Constantes de tailles des composants*/
	public static final int MAP_WIDTH = 2560;
	public static final int MAP_HEIGHT = 1440;
	
	private static final int TAILLE_MAX_TIR = 300;
	
	//Valeurs du zoom maximum et du zoom de lancement en fonction du zoom minimum
	public static final float ZOOM_MAX_MULT = 4;
	public static final float ZOOM_DEBUT_MULT = 2;
	
	//Constantes pour l'accélération de la translation en cas de mouvement rapide
	public static final int NB_PIXELS_ACCELERATION = 10;
	public static final int COEFF_ACCELERATION = 3;
	
	//Constantes qui servent pour dessiner un tir en cours
	private static final int EPAISSEUR_FLECHE_TIR = 30;
	private static final int EPAISSEUR_ONDE_TIR = 10;
	private static final float ANGLE_ONDE_TIR = 30f;
	private static final int ANGLE_DEMITOUR = 180;
	private static final int INCREMENT_ONDE_TIR = 20;
	private static final int COULEUR_MAXIMUM = 255;
	private static final float INCREMENT_COULEUR = 1/2;
	private static final int TAILLE_BOUT_FLECHE_TIR = 50;
	private static final int ANGLE_BOUT_FLECHE_TIR = 45;
	
	private static final int TAILLE_MATRIX = 9;
	
	private PointF positionFond;
	private PointF positionJoueur1;
	
	// Images pour le jeu
	private Bitmap bmFond;
	private Bitmap bmTerrain;
	private Bitmap bmQaudrillage;
	
	//Matrice qui gère le zoom et la translation d'une image
	private Matrix matrix;

	// TIR
	// position du début du tir
	private PointF pointTir;
	
	//position touchée en ce moment (ou dernière position touchée)
	private PointF positionTouche;
	
	private Monde monde;
	
	
	public MoteurGraphique(Context context) {
		super(context);
		constructeurPartage(context);
	}
	
	public MoteurGraphique(Context context, AttributeSet attrs) {
		super(context, attrs);
		constructeurPartage(context);
	}
	
	public MoteurGraphique(Context context, AttributeSet attrs, int defStyle) {
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
		
		GameActivity.setMode(GameActivity.RIEN);
		this.setWillNotDraw(false);
		this.setClickable(true);
		positionFond = new PointF(0, 0);
		positionJoueur1 = new PointF(80, 470);
		
		/* Bitmap */
		bmFond = prepareBitmap(getResources().getDrawable(R.drawable.image_fond_640x360), MAP_WIDTH, MAP_HEIGHT);
		bmTerrain = prepareBitmap(getResources().getDrawable(R.drawable.terrain_jeu_defaut_640x360), MAP_WIDTH, MAP_HEIGHT);
		bmQaudrillage = prepareBitmap(getResources().getDrawable(R.drawable.image_quadrillage_640x360), MAP_WIDTH, MAP_HEIGHT);
		
		//on crée une nouvelle matrice
		matrix = new Matrix();
		
		pointTir = new PointF(-1, -1);
		positionTouche = new PointF(-1, -1);
		
		/* évenements */
		setOnTouchListener(new EvenementJeu(context, this));
		
		float[] m = new float[TAILLE_MATRIX];
		matrix.getValues(m);
	}
	
	private static Bitmap prepareBitmap(Drawable drawable, int width, int height) {
		Bitmap bitmap = Bitmap.createBitmap(width, height,Bitmap.Config.ARGB_8888);
		drawable.setBounds(0, 0, width, height);
		Canvas canvas = new Canvas(bitmap);
		drawable.draw(canvas);
		return bitmap;
	}
	
	
	
	@Override
	protected void onDraw(Canvas canvas) {
		
	}
	
	
	@Override
	protected void dispatchDraw(Canvas canvas) {
		// Application de la matrice avec la translation et le zoom
		canvas.setMatrix(matrix);
		
		// Dessins des objets du jeu
		canvas.drawBitmap(bmFond, positionFond.x, positionFond.y, null);
		canvas.drawBitmap(bmTerrain, positionFond.x, positionFond.y, null);
		canvas.drawBitmap(bmQaudrillage, positionFond.x, positionFond.y, null);
		
		if (monde != null)
		{
			//TODO Stocker les bitmap pour la performance
			Bitmap bmPerso = prepareBitmap(getResources().getDrawable(Personnage.getIdImage()),
											Personnage.JOUEUR_WIDTH,
											Personnage.JOUEUR_WIDTH);
			Bitmap bmObj;
			
			for(Personnage p : monde.getListePersonnage())
			{
				canvas.drawBitmap(bmPerso, p.getPosition().x, p.getPosition().y, null);
			}
			
			for(ObjetSurCarte objSurCarte : monde.getListeObjetCarte())
			{
				Objet obj = objSurCarte.getObjet();
				Point taille = obj.getTailleImage();
				bmObj = prepareBitmap(getResources().getDrawable(obj.getIdImage()),
										taille.x,
										taille.y);
				canvas.drawBitmap(bmObj, objSurCarte.getPosition().x, objSurCarte.getPosition().y, null);
			}
			
		}
		
		// Dessins des objets pour le tir
		if (GameActivity.getMode() == GameActivity.TIR) {
			// Pour le tir, on a pas de translation ni de zoom
			Matrix m = new Matrix();
			canvas.setMatrix(m);
			
			PointF deplacement = new PointF(pointTir.x - positionTouche.x,
					 pointTir.y - positionTouche.y);
			float distance = deplacement.length();
					
			// Mise en place des outils de dessins
			Paint paint = new Paint();
			paint.setStrokeWidth(EPAISSEUR_ONDE_TIR);
			paint.setAntiAlias(true);
			paint.setStrokeCap(Paint.Cap.ROUND);
			paint.setStyle(Paint.Style.STROKE);
			
			float angleBase = ((float)(Math.atan2 (deplacement.y, deplacement.x)* ANGLE_DEMITOUR /Math.PI));
			// Ajustement de l'angle
			float angle = angleBase + ANGLE_DEMITOUR; //demi tour
			angle -= ANGLE_ONDE_TIR / 2; //pour centrer l'onde sur le doigt
			
			// Onde violette
			paint.setColor(Color.MAGENTA);
			RectF rect = new RectF();
			for (int i=0;(i<=distance && i < TAILLE_MAX_TIR);i+= INCREMENT_ONDE_TIR) {
				rect.set(pointTir.x - i, pointTir.y - i,
						pointTir.x + i, pointTir.y + i);
				canvas.drawArc(rect, angle, ANGLE_ONDE_TIR, false, paint);
			}
			
			PointF coinSuperieurGaucheJoueur = transpositionPointSurEcran(positionJoueur1);
			PointF coinSuperieurDroitJoueur = new PointF(coinSuperieurGaucheJoueur.x + Personnage.JOUEUR_WIDTH, coinSuperieurGaucheJoueur.y);
			dessinerFleches(coinSuperieurDroitJoueur, canvas, paint, angleBase, distance);
		}
	}
	
	/** Fonction qui dessine une flèche sur le bonhomme
	 * @param canvas
	 * @param paint
	 * @param distance
	 */
	public void dessinerFleches(PointF depart, Canvas canvas, Paint paint, float angleBase, float distance) {
		
		// flèche de tir sur le bonhomme
		if (distance >= COULEUR_MAXIMUM / INCREMENT_COULEUR ) {
			paint.setColor(Color.rgb(COULEUR_MAXIMUM,0,0));
		} else {
			paint.setColor(Color.rgb(COULEUR_MAXIMUM, COULEUR_MAXIMUM-(int)(distance * INCREMENT_COULEUR),0));
		}
		paint.setStrokeWidth(EPAISSEUR_FLECHE_TIR);
		
		//Extrémité de la flèche
		PointF finFleche = new PointF();
		finFleche.x = (float) (distance * Math.cos(Math.toRadians(angleBase))) + depart.x;
		finFleche.y = (float) (distance * Math.sin(Math.toRadians(angleBase))) + depart.y;
		// On dessine la grande ligne
		canvas.drawLine(depart.x, depart.y, finFleche.x, finFleche.y, paint);

		// Petits traits aux bouts de la flèche
		PointF ptFleche1  = new PointF(); 
		PointF ptFleche2  = new PointF();
		ptFleche1.x = (float) (finFleche.x - TAILLE_BOUT_FLECHE_TIR * Math.sin(Math.toRadians(angleBase + ANGLE_BOUT_FLECHE_TIR)));
		ptFleche1.y = (float) (finFleche.y + TAILLE_BOUT_FLECHE_TIR * Math.cos(Math.toRadians(angleBase + ANGLE_BOUT_FLECHE_TIR)));
		ptFleche2.x = (float) (finFleche.x - TAILLE_BOUT_FLECHE_TIR * Math.cos(Math.toRadians(angleBase + ANGLE_BOUT_FLECHE_TIR)));
		ptFleche2.y = (float) (finFleche.y - TAILLE_BOUT_FLECHE_TIR * Math.sin(Math.toRadians(angleBase + ANGLE_BOUT_FLECHE_TIR)));
		// On dessine les petits traits (si on veut fermer rajouter path.close()
		Path path = new Path();
		path.moveTo(finFleche.x, finFleche.y);
		path.lineTo(ptFleche1.x, ptFleche1.y);
		path.moveTo(finFleche.x, finFleche.y);
		path.lineTo(ptFleche2.x, ptFleche2.y);
        canvas.drawPath(path, paint);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	

	private PointF transpositionPointSurEcran(PointF pt)
	{
		float[] mm = new float[TAILLE_MATRIX];
		matrix.getValues(mm);
		float transX = mm[Matrix.MTRANS_X];
		float transY = mm[Matrix.MTRANS_Y];
		float scaleX = mm[Matrix.MSCALE_X];
		float scaleY = mm[Matrix.MSCALE_Y];
		
		PointF result = new PointF(pt.x  * scaleX + transX, pt.y  * scaleY + transY);
		
		return result;
	}

	public Matrix getMatrice() {
		return matrix;
	}

	public PointF getPointTir() {
		return pointTir;
	}
	
	public PointF getPositionTouche() {
		return this.positionTouche;
	}

	public void setPositionTouche(PointF ptTouche) {
		this.positionTouche = ptTouche;
	}
	
	public void setMonde(Monde monde) {
		this.monde = monde;
	}
}