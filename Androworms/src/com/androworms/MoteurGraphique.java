package com.androworms;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Vibrator;
import android.support.v4.util.LruCache;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
	private static final int TEMPS_VIBRATION = 300;
	
	// Valeurs du zoom maximum et du zoom de lancement en fonction du zoom minimum
	public static final float ZOOM_MAX_MULT = 4;
	public static final float ZOOM_DEBUT_MULT = 2;
	
	// Constantes pour l'accélération de la translation en cas de mouvement rapide
	public static final int NB_PIXELS_ACCELERATION = 10;
	public static final int COEFF_ACCELERATION = 3;
	
	// Constantes qui servent pour dessiner un tir en cours
	private static final int EPAISSEUR_ONDE_TIR = 10;
	private static final float ANGLE_ONDE_TIR = 30f;
	public static final int ANGLE_DEMITOUR = 180;
	private static final int INCREMENT_ONDE_TIR = 20;
	private static final int COULEUR_MAXIMUM = 255;
	private static final float INCREMENT_COULEUR = 0.5f;
	private static final int TAILLE_BOUT_FLECHE_TIR = 50;
	private static final int ANGLE_BOUT_FLECHE_TIR = 45;
	
	private static final int DISTANCE_MAX = 250;
	
	private static final int TAILLE_MATRIX = 9;
	// Nombre maximum d'images stockees dans le cache
	private static final int NB_MAX_IMAGE_CACHE = 10;
	
	// Images pour le jeu
	private static LruCache<Integer, Bitmap> memoireCache;
	private Bitmap bmFond;
	private Bitmap bmTerrain;
	
	// Matrice qui gère le zoom et la translation d'une image
	private Matrix matrix;
	private float[] mm;

	// TIR
	// position du début du tir
	private PointF pointTir;
	
	// position touchée en ce moment (ou dernière position touchée)
	private PointF positionTouche;
	
	private Noyau noyau = null;
	private EvenementJeu evtJeu;
	
	private Context context;
	private List<ImageSurCarte> images;
	private ProgressBar pbTest;
	
	private AnimationDrawable animDroitePersoPrincipal;
	private AnimationDrawable animGauchePersoPrincipal;
	private AnimationDrawable animDroitePerso2;
	private AnimationDrawable animGauchePerso2;
	
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
	
	public Bitmap getImage(int reference) {
		//this.premierPlan = Bitmap.createBitmap(tag, 1280, 720, true);
		return BitmapFactory.decodeResource(context.getResources(), reference);
	}
	
	/**
	 * Cette fonction initialise le composants avec un constructeur partagé
	 * 
	 * @param context
	 */
	private void constructeurPartage(Context context) {
		Log.v(TAG, "constructeurPartage()");
		
		this.context = context;
		
		ActiviteJeu.setMode(ActiviteJeu.RIEN);
		this.setWillNotDraw(false);
		this.setClickable(true);
		
		/* Bitmap */
		// On crée un cache
		if (memoireCache == null) {
			memoireCache = new LruCache<Integer, Bitmap>(NB_MAX_IMAGE_CACHE);
		}
	    
		try {
			bmFond = getBitmap(context, R.drawable.image_fond, MAP_WIDTH, MAP_HEIGHT);
		} catch(OutOfMemoryError e) {
			Log.e(TAG, "Erreur de chargement les bitmaps sont trop lourds");
			//TODO terminer ou dire quelque chose...
		}
		
		// On crée une nouvelle matrice
		matrix = new Matrix();
		// On crée un tableau ici pour la performance
		this.mm = new float[TAILLE_MATRIX];
		
		pointTir = new PointF(-1, -1);
		positionTouche = new PointF(-1, -1);
		
		/* évenements */
		evtJeu = new EvenementJeu(context, this);
		setOnTouchListener(evtJeu);
		
		matrix.getValues(mm);
		images = new ArrayList<ImageSurCarte>();
		
		
		pbTest = new ProgressBar(context);
		pbTest.layout(0, 0, 20, 20);
		pbTest.setVisibility(View.INVISIBLE);
		this.addView(pbTest);
	}
	
	private static Bitmap prepareBitmap(Bitmap b, int width, int height) {
		return Bitmap.createScaledBitmap(b, width, height, true);
	}
	
	public void actualiserGraphisme() {
		// Permet d'actualiser les graphismes en appellant la fonction dispatchDraw()
		invalidate();
	}
	
	@Override
	protected void dispatchDraw(Canvas canvas) {
		// Application de la matrice avec la translation et le zoom à tout le canvas
		canvas.setMatrix(matrix);
		// Dessin des objets du jeu
		/*
		canvas.drawBitmap(bmFond, 0, 0, null);
		if (noyau.getMonde().getTerrain() != null) {
			canvas.drawBitmap(noyau.getMonde().getTerrain(), 0, 0, null);
		}
		*/
		
		canvas.drawBitmap(noyau.getMonde().getTerrain(), 0,0,null);
		for(ImageSurCarte v : this.images) {
			v.actualiser();
		}
		
		//canvas.drawBitmap(noyau.getMonde().getMondeView(), 0,0,null);
		
		//Quand on a modifié tous les imageView on peut dessiner
		super.dispatchDraw(canvas);
		
		
		// Apres le dessin des views, on rajoute le dessins des objets pour le tir
		if (ActiviteJeu.getMode() == ActiviteJeu.TIR_EN_COURS) {
			// Pour le tir, on a pas de translation ni de zoom
			Matrix m = new Matrix();
			canvas.setMatrix(m);
			
			PointF deplacement = new PointF(pointTir.x - positionTouche.x, pointTir.y - positionTouche.y);
			float distance = deplacement.length();
			if( distance > DISTANCE_MAX) {
				deplacement.x = ((DISTANCE_MAX/distance)* deplacement.x) ;
				deplacement.y = ((DISTANCE_MAX/distance)* deplacement.y);
				distance = deplacement.length();
			}
			
			
			float angleBase = ((float)(Math.atan2 (deplacement.y, deplacement.x)* ANGLE_DEMITOUR /Math.PI));
			
			
			// Mise en place des outils de dessins
			Paint paint = new Paint();
			paint.setAntiAlias(true);
			paint.setStrokeCap(Paint.Cap.ROUND);
			
			if (noyau.getParametresApplication().getBoolean(ActiviteParametres.PARAMETRE_AFFICHAGE_FORCE_CLE, ActiviteParametres.PARAMETRE_AFFICHAGE_FORCE_DEFAUT)) {
				dessinerOnde(canvas, paint, angleBase, distance);
			}
			dessinerFleches(canvas, paint, angleBase, distance);
		}
	}
	
	/** Fonction qui dessine une flèche sur le bonhomme
	 * @param canvas
	 * @param paint
	 * @param distance
	 */
	public void dessinerOnde(Canvas canvas, Paint paint, float angleBase, float distance) {
		// Mode de dessin
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
		// Mode de dessin
		paint.setStrokeWidth(EPAISSEUR_ONDE_TIR);
		paint.setStyle(Paint.Style.STROKE);
		
		PointF ptMilieuJoueur = new PointF();
		Personnage persoPrincipal = noyau.getMonde().getPersonnagePrincipal();
		ptMilieuJoueur.set(persoPrincipal.getPosition());
		
		//On place le point au milieu du joueur
		ptMilieuJoueur.offset((float)persoPrincipal.getWidthImageTerrain() / 2, (float)persoPrincipal.getHeightImageTerrain() / 2);
		
		PointF depart = transpositionPointSurEcran(ptMilieuJoueur);
		
		// flèche de tir sur le bonhomme
		if (distance >= COULEUR_MAXIMUM / INCREMENT_COULEUR) {
			paint.setColor(Color.rgb(COULEUR_MAXIMUM,0,0));
		} else {
			paint.setColor(Color.rgb(COULEUR_MAXIMUM, COULEUR_MAXIMUM - (int)(distance * INCREMENT_COULEUR),0));
		}
		
		paint.setStrokeWidth(this.getResources().getInteger(R.integer.epaisseurFlecheTir));
		
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
	
	/** Retourne les coordonnées d'un point avec la translation et le zoom associé.
	 * @param pt : Point de l'objet relative à la carte
	 * @return : Point de l'objet relative à l'écran
	 */
	public PointF transpositionPointSurEcran(PointF pt) {
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
	
	public static Bitmap getBitmap(Context ctx, int idImage, int width, int heigth) {
		Bitmap result = memoireCache.get(idImage);
		
		if (result == null) {
			//On charge l'image
			Bitmap b = ((BitmapDrawable)ctx.getResources().getDrawable(idImage)).getBitmap();
			result = prepareBitmap(b, width, heigth);
			//on la stocke dans la mémoire cache
			memoireCache.put(idImage, result);
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
		this.bmTerrain.recycle();
		//vide le cache
		memoireCache.evictAll(); 
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
	
	/* Méthodes à appeler depuis le noyau */
	public void setNoyau(Noyau noyau) {
		this.noyau = noyau;
		Monde monde = this.noyau.getMonde();
		this.evtJeu.setNoyau(noyau);
		
		for(ImageView v : this.images)
		{
			this.removeView(v);
		}
		
		
		if (monde != null) {
			bmTerrain = monde.getTerrain();
			
			for(Personnage p : monde.getListePersonnage()) {
				ImageSurCarte imgSurCarte = new ImageSurCarte(this.context, p, this);
				//On garde la référence pour le zoom et translation
				images.add(imgSurCarte);
				
				if (p.equals(monde.getPersonnagePrincipal())) {
					imgSurCarte.setBackgroundResource(R.drawable.animation_android_droite);
				} else {
					imgSurCarte.setBackgroundResource(R.drawable.android_face_desactive);
				}
			}
			
			for(ObjetSurCarte objSurCarte : monde.getListeObjetCarte()) {
				ImageSurCarte imgSurCarte = new ImageSurCarte(this.context, objSurCarte, this);
				//On garde la référence pour le zoom et translation
				images.add(imgSurCarte);
				ImageView objet = (ImageView) imgSurCarte;
				objet.setBackgroundResource(R.drawable.missile);
				Log.v(TAG,"Traitement d'un objet sur carte.");
			}
			
			this.actualiserGraphisme();
		}
	}
	
	public void ajouterArme(Personnage p, int image) {	
		for(ImageSurCarte img : images) {
			
			if(img.getElement().equals(p)) {
				ImageView perso = (ImageView) img;
				Drawable[] layers = new Drawable[2];
				if(perso.getBackground().getClass() == LayerDrawable.class)
				{
					LayerDrawable draw = (LayerDrawable) perso.getBackground();
					layers[0] = draw.getDrawable(0);
				}
				else
				{
					layers[0] = perso.getBackground();
				}
				layers[1] = context.getResources().getDrawable(image);
				LayerDrawable layerDrawable = new LayerDrawable(layers);
				perso.setBackgroundDrawable(layerDrawable);
			}
		}
	}
	
	public void animerAndroidDroite(Personnage p) {
		Personnage persoPrincipal = noyau.getMonde().getPersonnagePrincipal();
		
		for(ImageSurCarte perso : images) {
			
			if(perso.getElement().equals(p)) {
				perso.setBackgroundResource(R.drawable.animation_android_droite);
				
				if(persoPrincipal.equals(p)) {
					animDroitePersoPrincipal = (AnimationDrawable) perso.getBackground();
					animDroitePersoPrincipal.start();
				}
				else {
					animDroitePerso2 = (AnimationDrawable) perso.getBackground();
					animDroitePerso2.start();
				}
			}
		}
	}
	
	public void animerAndroidGauche(Personnage p) {
		Personnage persoPrincipal = noyau.getMonde().getPersonnagePrincipal();
		
		for(ImageSurCarte perso : images) {
			
			if(perso.getElement().equals(p)) {
				perso.setBackgroundResource(R.drawable.animation_android_gauche);
				
				if(persoPrincipal.equals(p)) {
					animGauchePersoPrincipal = (AnimationDrawable) perso.getBackground();
					animGauchePersoPrincipal.start();
				}
				else {
					animGauchePerso2 = (AnimationDrawable) perso.getBackground();
					animGauchePerso2.start();
				}
			}
		}
	}
	
	public void stopAnimationAndroid() {
		if (animDroitePersoPrincipal != null) {
			animDroitePersoPrincipal.stop();
		}
		if (animDroitePerso2 != null) {
			animDroitePerso2.stop();
		}
		if (animGauchePersoPrincipal != null) {
			animGauchePersoPrincipal.stop();
		}
		if (animGauchePerso2 != null) {
			animGauchePerso2.stop();
		}
	}
	
	public ImageSurCarte ajouterElementSurCarte(ElementSurCarte elt) {
		ImageSurCarte isc = new ImageSurCarte(this.context, elt, this);
		isc.setBackgroundResource(elt.getImageTerrain());
		//On garde la référence pour le zoom et translation
		images.add(isc);
		Log.v(TAG,"Traitement d'un objet sur carte.");
		return isc;
	}
	
	public boolean supprimerElementSurCarte(ElementSurCarte elt) {
		for(ImageSurCarte img : this.images) {
			if (img.getElement() == elt) {
				this.removeView(img);
			}
		}
		for(int i = 0; i < images.size(); i++) {
			if(images.get(i).getElement() == elt) {
				images.remove(i);
				return true;
			}
		}
		return false;
	}

	public void debutCalcul() {
		pbTest.setVisibility(View.VISIBLE);
	}
	public void finCalcul() {
		pbTest.setVisibility(View.INVISIBLE);
	}

	public void remetAplusTard(Runnable r, int tps) {
		postDelayed(r, tps);
	}

	public EvenementJeu getEvtJeu() {
		return evtJeu;
	}
	
	public void vibration() {
		// On fais une vibration à chaque explosion
		if (noyau.getParametresApplication().getBoolean(ActiviteParametres.PARAMETRE_VIBRATIONS_CLE, ActiviteParametres.PARAMETRE_VIBRATIONS_DEFAUT)) {
			// On récupère l'instance Vibrator depuis le context de l'activité
			Vibrator v = (Vibrator)this.context.getSystemService(Context.VIBRATOR_SERVICE);
			// Vibration durant 300 milliseconds
			v.vibrate(TEMPS_VIBRATION);
		}
	}
	
	public void desactiveJoueur() {
		Personnage p = this.noyau.getMonde().getPersonnagePrincipal();
		for(ImageSurCarte perso : this.images) {
			if (perso.getElement().equals(p)) {
				perso.setBackgroundResource(R.drawable.android_face_desactive);
			}
		}
	}
	
	public void activeJoueur() {
		Personnage p = this.noyau.getMonde().getPersonnagePrincipal();
		for(ImageSurCarte perso : this.images) {
			if (perso.getElement().equals(p)) {
				perso.setBackgroundResource(R.drawable.android_face);
			}
		}
	}
}