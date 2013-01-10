package com.androworms;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.v4.util.LruCache;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

/** Ce composant graphique est un RelativeLayout personnalisé pour Androworms.
 * Il dessine tous les objets présents sur la carte
 * Il permet d'être zoomé et déplacé ce qui a pour effet de zoomer et déplacer tous les composants "ImageView" enfants de ce Layout.
 */
public class MoteurGraphique extends RelativeLayout {
	
	private static final String TAG = "Androworms.MoteurGraphique";
	
	/* Constantes de tailles des composants */
	public static final int MAP_WIDTH = 1280;
	public static final int MAP_HEIGHT = 720;
	
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
	public static final int ANGLE_DEMITOUR = 180;
	private static final int INCREMENT_ONDE_TIR = 20;
	private static final int COULEUR_MAXIMUM = 255;
	private static final float INCREMENT_COULEUR = 0.5f;
	private static final int TAILLE_BOUT_FLECHE_TIR = 50;
	private static final int ANGLE_BOUT_FLECHE_TIR = 45;
	
	private static final int TAILLE_MATRIX = 9;
	//Nombre maximum d'images stockees dans le cache
	private static final int NB_MAX_IMAGE_CACHE = 10;
	
	// Images pour le jeu
	private LruCache<Integer, Bitmap> memoireCache;
	private Bitmap bmFond;
	private Bitmap bmTerrain;
	
	//Matrice qui gère le zoom et la translation d'une image
	private Matrix matrix;
	private float[] mm;

	// TIR
	// position du début du tir
	private PointF pointTir;
	
	//position touchée en ce moment (ou dernière position touchée)
	private PointF positionTouche;
	
	private Noyau noyau;
	private EvenementJeu evtJeu;
	
	private Context context;
	private ImageView fond;
	private ImageView terrain;
	private List<ImageView> images;
	
	//DEBUG
	private static final boolean DEBUG_QUADRILLAGE = false;
	private Bitmap bmQuadrillage;
	private ImageView quadrillage;
	
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
		
		this.context = context;
		
		ActivityJeu.setMode(ActivityJeu.RIEN);
		this.setWillNotDraw(false);
		this.setClickable(true);
		
		/* Bitmap */
		// On crée un cache
		this.memoireCache = new LruCache<Integer, Bitmap>(NB_MAX_IMAGE_CACHE);
	    
		try {
			bmFond = prepareBitmap(getResources().getDrawable(R.drawable.image_fond_640x360), MAP_WIDTH, MAP_HEIGHT);
			bmTerrain = prepareBitmap(getResources().getDrawable(R.drawable.terrain_jeu_defaut_640x360), MAP_WIDTH, MAP_HEIGHT);
			bmQuadrillage = prepareBitmap(getResources().getDrawable(R.drawable.image_quadrillage_640x360), MAP_WIDTH, MAP_HEIGHT);
		} catch(OutOfMemoryError e) {
			Log.e(TAG, "Erreur de chargement les bitmaps sont trop lourds");
			//TODO terminer ou dire quelque chose...
		}
		
		fond = new ImageView(context);
		fond.setImageBitmap(bmFond);
		terrain = new ImageView(context);
		terrain.setImageBitmap(bmTerrain);
		quadrillage = new ImageView(context);
		quadrillage.setImageBitmap(bmQuadrillage);
		
		
		//on crée une nouvelle matrice
		matrix = new Matrix();
		//On crée un tableau ici pour la performance
		this.mm = new float[TAILLE_MATRIX];
		
		pointTir = new PointF(-1, -1);
		positionTouche = new PointF(-1, -1);
		
		/* évenements */
		evtJeu = new EvenementJeu(context, this);
		setOnTouchListener(evtJeu);
		
		matrix.getValues(mm);
		
		fond.setScaleType(ScaleType.MATRIX);
		terrain.setScaleType(ScaleType.MATRIX);
		quadrillage.setScaleType(ScaleType.MATRIX);
		fond.setImageMatrix(matrix);
		terrain.setImageMatrix(matrix);
		quadrillage.setImageMatrix(matrix);
		this.addView(fond);
		this.addView(terrain);
		if (DEBUG_QUADRILLAGE) {
			this.addView(quadrillage);
		}
		
		images = new ArrayList<ImageView>();
		
	}
	
	private static Bitmap prepareBitmap(Drawable drawable, int width, int height) {
		Bitmap bitmap = Bitmap.createBitmap(width, height,Bitmap.Config.ARGB_8888);
		drawable.setBounds(0, 0, width, height);
		Canvas canvas = new Canvas(bitmap);
		drawable.draw(canvas);
		return bitmap;
	}
	
	public void actualiserGraphisme() {
		invalidate();
	}
		
	@Override
	protected void dispatchDraw(Canvas canvas) {
		
		Log.v(TAG, "dispatchDraw");
		
		// on applique une matrice au fond (trans + scale)
		fond.setImageMatrix(matrix);
		terrain.setImageMatrix(matrix);
		
		if (DEBUG_QUADRILLAGE) {
			quadrillage.setImageMatrix(matrix);
		}
		
		matrix.getValues(mm);
		float scaleX = mm[Matrix.MSCALE_X];
		float scaleY = mm[Matrix.MSCALE_Y];
		
		// on copie la matrice appliquée au fond pour faire une custom sur les imageview
		// cette matrice n'a pas de trans mais juste un scale
		//le trans est fait sur le composant
		Matrix matrixZoom = new Matrix();
		matrixZoom.postScale(scaleX, scaleY);
		
		int i = 0;
		List<Personnage> persos = noyau.getMonde().getListePersonnage();
		List<ObjetSurCarte> objs = noyau.getMonde().getListeObjetCarte();
		boolean objEnCours = false;
		ObjetSurCarte obj;
		PointF pp;
		PointF taille;
		
		for(ImageView v : this.images) {

			if (i == persos.size()){
				i = 0;
				objEnCours = true;
			}
			
			if (objEnCours) {
				obj = objs.get(i);
				pp = transpositionPointSurEcran(obj.getPosition());
				taille = obj.getObjet().getTailleImageTerrain();
				//Calcul du carré où afficher l'image
				v.layout((int) pp.x,
						(int) pp.y, 
						(int)( pp.x + taille.x * scaleX),
						(int)( pp.y + taille.y * scaleY));
			} else {
				pp = transpositionPointSurEcran(persos.get(i).getPosition());
				//Calcul du carré où afficher l'image
				v.layout((int) pp.x,
						(int) pp.y, 
						(int)( pp.x + persos.get(i).getWidthImageTerrain() * scaleX),
						(int)( pp.y + persos.get(i).getHeightImageTerrain() * scaleY));
			}
			
			v.setImageMatrix(matrixZoom);
			i++;
		}
		
		//Quand on a modifié tous les imageView on peut dessiner
		super.dispatchDraw(canvas);
		
		
		// Apres le dessin des views, on rajoute le dessins des objets pour le tir
		if (ActivityJeu.getMode() == ActivityJeu.TIR_EN_COURS) {
			// Pour le tir, on a pas de translation ni de zoom
			Log.v(TAG, "On dessine les trucs");
			Matrix m = new Matrix();
			canvas.setMatrix(m);
			
			PointF deplacement = new PointF(pointTir.x - positionTouche.x,
					 pointTir.y - positionTouche.y);
			float distance = deplacement.length();
			
			
			float angleBase = ((float)(Math.atan2 (deplacement.y, deplacement.x)* ANGLE_DEMITOUR /Math.PI));
			
			
			// Mise en place des outils de dessins
			Paint paint = new Paint();
			paint.setAntiAlias(true);
			paint.setStrokeCap(Paint.Cap.ROUND);
			
			dessinerOnde(canvas, paint, angleBase, distance);
			dessinerFleches(canvas, paint, angleBase, distance);
		}
	}
	
	/** Fonction qui dessine une flèche sur le bonhomme
	 * @param canvas
	 * @param paint
	 * @param distance
	 */
	public void dessinerOnde(Canvas canvas, Paint paint, float angleBase, float distance) {
		paint.setStrokeWidth(EPAISSEUR_ONDE_TIR);
		paint.setStyle(Paint.Style.STROKE);
		
		// Ajustement de l'angle
		// On fait un demi-tour pour orienter l'onde dans l'autre sens
		float angle = angleBase + ANGLE_DEMITOUR; 
		// on centre l'onde en la décalant de la moitié de l'angle affiché
		angle -= ANGLE_ONDE_TIR / 2; 
		
		paint.setColor(Color.MAGENTA);
		RectF rect = new RectF();
		for (int i1=0;(i1<=distance && i1 < TAILLE_MAX_TIR);i1+= INCREMENT_ONDE_TIR) {
			rect.set(pointTir.x - i1, pointTir.y - i1,
					pointTir.x + i1, pointTir.y + i1);
			canvas.drawArc(rect, angle, ANGLE_ONDE_TIR, false, paint);
		}
	}
	
	/** Fonction qui dessine une flèche sur le bonhomme
	 * @param canvas
	 * @param paint
	 * @param distance
	 */
	public void dessinerFleches( Canvas canvas, Paint paint, float angleBase, float distance) {
		PointF ptMilieuJoueur = new PointF();
		Personnage persoPrincipal = noyau.getMonde().getPersonnagePrincipal();
		ptMilieuJoueur.set(persoPrincipal.getPosition());
		
		//On place le point au milieu du joueur
		ptMilieuJoueur.offset((float)persoPrincipal.getWidthImageTerrain() / 2, (float)persoPrincipal.getHeightImageTerrain() / 2);
		
		PointF depart = transpositionPointSurEcran(ptMilieuJoueur);
		
		// flèche de tir sur le bonhomme
		if (distance >= COULEUR_MAXIMUM / INCREMENT_COULEUR) {
			paint.setColor(Color.rgb(COULEUR_MAXIMUM,0,0));
			Log.v(TAG,"[A] Couleur : " + Integer.toHexString(paint.getColor())+"    ; distance = "+distance);
		} else {
			paint.setColor(Color.rgb(COULEUR_MAXIMUM, COULEUR_MAXIMUM - (int)(distance * INCREMENT_COULEUR),0));
			Log.v(TAG,"[B] Couleur : " + Integer.toHexString(paint.getColor())+"    ; distance = "+distance);
			Log.v(TAG,"=="+(distance * INCREMENT_COULEUR));
		}
		
		paint.setStrokeWidth(EPAISSEUR_FLECHE_TIR);
		
		//La fleche debute en dehors du joueur
		PointF debutFleche = new PointF();
		//On cherche à ne pas mettre la flèche sur le joueur
		float tailleJoueur = zoomPointSurEcran(new PointF((float)persoPrincipal.getWidthImageTerrain() / 2, (float)persoPrincipal.getHeightImageTerrain() / 2)).length(); 
		debutFleche.x = (float) (tailleJoueur * Math.cos(Math.toRadians(angleBase))) + depart.x;
		debutFleche.y = (float) (tailleJoueur * Math.sin(Math.toRadians(angleBase))) + depart.y;
		//Extrémité de la flèche
		PointF finFleche = new PointF();
		finFleche.x = (float) ((distance + tailleJoueur) * Math.cos(Math.toRadians(angleBase))) + depart.x;
		finFleche.y = (float) ((distance + tailleJoueur) * Math.sin(Math.toRadians(angleBase))) + depart.y;
		// On dessine la grande ligne
		canvas.drawLine(debutFleche.x, debutFleche.y, finFleche.x, finFleche.y, paint);

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
	
	/** Retourne les coordonnées d'un point avec la translation et le zoom associé.
	 * @param pt : Point de l'objet relative à la carte
	 * @return : Point de l'objet relative à l'écran
	 */
	private PointF transpositionPointSurEcran(PointF pt) {
		matrix.getValues(this.mm);
		float transX = mm[Matrix.MTRANS_X];
		float transY = mm[Matrix.MTRANS_Y];
		float scaleX = mm[Matrix.MSCALE_X];
		float scaleY = mm[Matrix.MSCALE_Y];
		
		PointF result = new PointF(pt.x  * scaleX + transX, pt.y  * scaleY + transY);
		
		return result;
	}
	
	/** Retourne les coordonnées d'un point avec juste le zoom associé.
	 * @param pt : Point de l'objet relative à la carte
	 * @return : Point de l'objet relative à l'écran
	 */
	private PointF zoomPointSurEcran(PointF pt) {
		matrix.getValues(this.mm);
		float scaleX = mm[Matrix.MSCALE_X];
		float scaleY = mm[Matrix.MSCALE_Y];
		
		PointF result = new PointF(pt.x  * scaleX, pt.y  * scaleY);
		
		return result;
	}
	
	private Bitmap getBitmap(int idImage, int width, int heigth) {
		Bitmap result = this.memoireCache.get(idImage);
		
		if (result == null) {
			//On charge l'image
			result = prepareBitmap(getResources().getDrawable(idImage), width, heigth);
			//on la stocke dans la mémoire cache
			this.memoireCache.put(idImage, result);
		}
		
		if (result == null) {
			Log.v(TAG, "la resssource n'a pas bien été chargée");
		}
		
		return result;
	}
	
	/**
	 * Libère les bitmap pour libérer de la mémoire
	 */
	public void nettoyer() {
		this.bmFond.recycle();
		this.bmQuadrillage.recycle();
		this.bmTerrain.recycle();
		//vide le cache
		this.memoireCache.evictAll(); 
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
	
	public void setNoyau(Noyau noyau) {
		this.noyau = noyau;
		Monde monde = this.noyau.getMonde();
		this.evtJeu.setNoyau(noyau);
		
		for(ImageView v : this.images)
		{
			this.removeView(v);
		}
		
		ImageView imgV;
		Bitmap bmObj;
		
		if (monde != null)
		{
			Personnage persoPrincipal = noyau.getMonde().getPersonnagePrincipal();
			Bitmap bmPerso = getBitmap(Personnage.getIdImage(), persoPrincipal.getWidthImageTerrain(), persoPrincipal.getHeightImageTerrain());

			int id = 1000;
			for(Personnage p : monde.getListePersonnage()) {
				//On ne fait que charger l'image, le positionnement sera fait dans dispatchDraw
				imgV = new ImageView(this.context);
				//On dessine le personnage
				imgV.setImageBitmap(bmPerso);
				//Pour pouvoir ensuite appliquer une matrice
				imgV.setScaleType(ScaleType.MATRIX);
				//On rajoute dans le layout
				this.addView(imgV);
				//On garde la référence pour le zoom et translation
				images.add(imgV);
			    
			    
			    // TODO : c'est pas bien, il faut changer ça avec une meilleur gestion des ID
				imgV.setId(id);
				p.setId(id);
				id++;
			}

			id = 2000;
			for(ObjetSurCarte objSurCarte : monde.getListeObjetCarte()) {
				Objet obj = objSurCarte.getObjet();
				PointF taille = obj.getTailleImageTerrain();
				bmObj = getBitmap(obj.getImageTerrain(), (int)taille.x, (int)taille.y);
				imgV = new ImageView(this.context);
				imgV.setImageBitmap(bmObj);
				imgV.setScaleType(ScaleType.MATRIX);
				this.addView(imgV);
				images.add(imgV);
			    
			    // TODO : c'est pas bien, il faut changer ça avec une meilleur gestion des ID
				imgV.setId(id);
				objSurCarte.setId(id);

				id++;
			}
			
			this.invalidate();
		}
	}
}