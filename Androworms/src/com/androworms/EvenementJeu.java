package com.androworms;

import com.androworms.utile.Informations;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.View.OnTouchListener;

public class EvenementJeu extends ScaleGestureDetector.SimpleOnScaleGestureListener implements OnTouchListener  {
	
	private static final String TAG = "Androworms.EvenementJeu";
	
	private static final int TAILLE_MATRIX = 9;
	
	private MoteurGraphique moteurGraph;
	private Noyau noyau;
	
	// Traquer le mouvement
	private PointF positionAncienneTouche;
	private PointF positionNouvelleTouche;
	
	// Gestion du zoom
	private ScaleGestureDetector mScaleDetector;
	private float scaleCourant;
	
	//zoom qui permet de ne pas afficher de bordures
	private float zoomMin;
	//zoom qui dépend de zoomMin
	private float zoomMax; 
		
	public EvenementJeu(Context ctx, MoteurGraphique mg) {
		this.moteurGraph = mg;
		
		positionNouvelleTouche = new PointF(-1, -1);
		positionAncienneTouche = new PointF(-1, -1);
		
		mScaleDetector = new ScaleGestureDetector(ctx, this);

		/* Gestion des limites pour le zoom */
		zoomMin = Math.max((float)Informations.getWidthPixels() / MoteurGraphique.MAP_WIDTH,
				(float)Informations.getHeightPixels() /MoteurGraphique.MAP_HEIGHT);
		zoomMax = zoomMin * MoteurGraphique.ZOOM_MAX_MULT;
		
		scaleCourant = zoomMin * MoteurGraphique.ZOOM_DEBUT_MULT;
		
		//on attribue le zoom min à la matrice
		//TODO changer 0 0 par les coordonées qu'on souhaite afficher
		mg.getMatrice().postScale(scaleCourant, scaleCourant, 0, 0);
	}
	
	public void setNoyau(Noyau noyau) {
		this.noyau = noyau;
	}

	public boolean onTouch(View v, MotionEvent event) {
		
		positionAncienneTouche.set(positionNouvelleTouche);
		//On n'a ici que la position du doigt principal
		positionNouvelleTouche = new PointF(event.getX(), event.getY());
		//on renseigne la touche dans le moteur graphique pour le dessin du tir
		this.moteurGraph.setPositionTouche(positionNouvelleTouche);
		
		//On lance la détection du zoom
		mScaleDetector.onTouchEvent(event);
		
		/*       Gestion des doigts
		 * Chaque fois qu'un doigt se rajoute sur l'écran, un id lui est attribué. (un id est un nombre qui part de 0 jusqu'à environ 10)
		 * Quand un doigt se retire de l'écran, l'ID est libéré.
		 * Lorsque l'on appuie sur l'écran avec un doigt, il reçois l'ID le plus petit disponible.
		 * Le doigt principale est toujours le doigt d'ID 0. Il est donc possible que à un moment il n'y ai pas de doigt principal sur l'écran.
		 * (Exemple : doigt A posé sur l'écran (doigt principal) -> doigt B posé sur l'écran -> doigt A levé)
		 */
		switch (event.getActionMasked()) {
		case MotionEvent.ACTION_DOWN:
			actionAppuiPremierDoigt();
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
			actionAppuiDoigtSupplementaire();
			break;
		case MotionEvent.ACTION_MOVE:
			actionDeplacementDoigt();
			break;
		case MotionEvent.ACTION_UP:
			actionLeveeDoigtUnique();
			break;
		case MotionEvent.ACTION_POINTER_UP:
			actionLeveeDoigt();
			break;
		default:
			break;
		}
		
		this.moteurGraph.actualiserGraphisme();
		return true;
	}
	
	private void actionAppuiPremierDoigt() {
		// Action : Appui sur l'écran lorsqu'il n'y a aucun doigt. Ce doigt est donc le doigt principal
		if (ActiviteJeu.getMode() == ActiviteJeu.RIEN) {
			//on passe en mode translation
			ActiviteJeu.setMode(ActiviteJeu.DEPLACEMENT);
		} else if (ActiviteJeu.getMode() == ActiviteJeu.TIR 
				||  ActiviteJeu.getMode() == ActiviteJeu.TIR_EN_COURS) {
			//on commence à tirer ou on continue le tir
			ActiviteJeu.setMode(ActiviteJeu.TIR_EN_COURS);
			this.moteurGraph.getPointTir().set(positionNouvelleTouche);
		}
		positionAncienneTouche = new PointF(-1, -1);
	}
	
	private void actionAppuiDoigtSupplementaire() {
		// Action : Lorsque l'on a un ou plusieurs doigt sur l'écran
		// et qu'on appuie avec un doigt supplémentaire
		// Géré par le zoom
	}
	
	private void actionDeplacementDoigt() {
		// Action : Un doigt sur l'écran qui bouge
		if (ActiviteJeu.getMode() == ActiviteJeu.DEPLACEMENT) {
			// En mode déplacement, position_ancienne_touche n'est jamais égale à -1 sinon erreur
			
			float tempX, tempY;
			
			tempX = positionNouvelleTouche.x - positionAncienneTouche.x;
			tempY = positionNouvelleTouche.y - positionAncienneTouche.y;
			
			// Accelérer le déplacement quand on fait de grands mouvement ! (à paramètrer plus finement !)
			if (Math.abs(tempX) > MoteurGraphique.NB_PIXELS_ACCELERATION) {
				tempX *= MoteurGraphique.COEFF_ACCELERATION;
			}
			if (Math.abs(tempY) > MoteurGraphique.NB_PIXELS_ACCELERATION) {
				tempY *= MoteurGraphique.COEFF_ACCELERATION;
			}
			
			this.moteurGraph.getMatrice().postTranslate(tempX, tempY);
		
			fixTrans();
		}
	}
	
	private void actionLeveeDoigtUnique() {
		// Action : Levée du dernier doigt sur l'écran.
		if (ActiviteJeu.getMode() == ActiviteJeu.DEPLACEMENT) {
			//on finit la translation
			ActiviteJeu.setMode(ActiviteJeu.RIEN);
		}
		if (ActiviteJeu.getMode() == ActiviteJeu.TIR_EN_COURS) {
			//on finit le tir
			ActiviteJeu.setMode(ActiviteJeu.TIR);
			PointF deplacement = new PointF(this.moteurGraph.getPointTir().x - positionNouvelleTouche.x,
					this.moteurGraph.getPointTir().y - positionNouvelleTouche.y);
			float distance = deplacement.length();
			float angle = ((float)(Math.atan2 (deplacement.y, deplacement.x)* MoteurGraphique.ANGLE_DEMITOUR /Math.PI));
		
			noyau.effectuerTir(distance, angle);
		}
		positionAncienneTouche = new PointF(-1, -1);
	}
	
	private void actionLeveeDoigt() {
		// Action : lorsque que l'on a plusieurs doigts sur l'écran et que l'on lève un doigt
		// Géré par le zoom
	}
	
	
	// Début d'une session de zoom
	@Override
	public boolean onScaleBegin(ScaleGestureDetector detector) {
		if (ActiviteJeu.getMode() != ActiviteJeu.TIR
				&& ActiviteJeu.getMode() != ActiviteJeu.TIR_EN_COURS) {
			ActiviteJeu.setMode(ActiviteJeu.ZOOM);
			return true;
		} else {
			return false;
		}
	}
	
	// Changement de zoom
	@Override
	public boolean onScale(ScaleGestureDetector detector) {
		if (ActiviteJeu.getMode() == ActiviteJeu.ZOOM) {
			// Scale sur cette évenement
			float mScaleFactor = detector.getScaleFactor();
			
			// Scale globale sur l'image d'origine
			float temp = scaleCourant * mScaleFactor;
			// Gestion du Zoom max et zoom min
			if (zoomMin > temp) { 
				mScaleFactor = zoomMin / scaleCourant;
				scaleCourant = zoomMin;
			} else if (temp > zoomMax) { 
				mScaleFactor = zoomMax / scaleCourant;
				scaleCourant = zoomMax;
			} else {
				scaleCourant = temp;
			}
			
			// Application du Zoom
			this.moteurGraph.getMatrice().postScale(mScaleFactor, mScaleFactor, detector.getFocusX(), detector.getFocusY());
			fixTrans();
			return true;
		} else {
			return false;
		}
		
	}
	
	// A la fin d'une session de zoom
	@Override
	public void onScaleEnd(ScaleGestureDetector detector) {
		ActiviteJeu.setMode(ActiviteJeu.RIEN);
	}
	
	/** Fonction qui corrige la translation si elle dépasse
	 */
	private void fixTrans() {
		// On cherches les translations en x et y voulus par le reste du programme
		float[] m = new float[TAILLE_MATRIX];
		this.moteurGraph.getMatrice().getValues(m);
		float transX = m[Matrix.MTRANS_X];
		float transY = m[Matrix.MTRANS_Y];
		
		// Valeurs de zoom minimum ateignable dans les 2 directions pour voir toute l'image
		float zoomMinX = (float)Informations.getWidthPixels() / MoteurGraphique.MAP_WIDTH;
		float zoomMinY = (float)Informations.getHeightPixels() / MoteurGraphique.MAP_HEIGHT;
		// Valeurs de translation maximum (formule trouvée en testant...)
		 //scaleCourant - zoomMin est positif
		float maxTransX = MoteurGraphique.MAP_WIDTH * (scaleCourant - zoomMinX);
		float maxTransY = MoteurGraphique.MAP_HEIGHT * (scaleCourant - zoomMinY);
		//Valeur de translation à rajouter pour ne pas que ça dépasse
		float fixTransX = getFixTrans(transX, maxTransX);
		float fixTransY = getFixTrans(transY, maxTransY);
		
		if (fixTransX != 0 || fixTransY != 0) {
			// Il faut corriger la matrice sinon ça dépasse
			this.moteurGraph.getMatrice().postTranslate(fixTransX, fixTransY);
		}
	}
	
	//Retourne la valeur à ajouter à la translation pour qu'elle se trouve bien
	// entre -transMax et 0
	private float getFixTrans(float trans, float transMax) {
		//Attention trans est forcement négatif donc il faut qu'il soit entre -transMax et 0
		if (trans > 0) {
			//Ca dépasse par le côté gauche ou le haut
			return -trans;
		}
		if (trans < - transMax) {
			//Ca dépasse par le côté droit ou le bas
			return -trans - transMax;
		}
		//Ca dépasse pas
		return 0;
	}
}