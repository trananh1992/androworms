package com.androworms;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;

/** Activité de gestion de l'appareil photo pour la création de cartes.
 */
public class ActiviteCamera extends Activity implements SurfaceHolder.Callback, OnClickListener, OnTouchListener {
	
	private static final String TAG = "Androworms.ActiviteCamera.Event";
	
	private Camera camera;
	private boolean isPreviewRunning = false;
	private Handler mAutoFocusHandler;
	private int mAutoFocusMessage;
	private File photoPath;
	private ActiviteEditeur activiteCreationCarte;
	static final int TAKE_PICTURE = 0;
	
	public ActiviteCamera() {
		super();
	}
	
	public ActiviteCamera(ActiviteEditeur activiteCreationCarte) {
		this.activiteCreationCarte = activiteCreationCarte;
	}
	
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		
		/* On attache la vue de fond avec la caméra */
		SurfaceView surfaceView;
		SurfaceHolder surfaceHolder;
		getWindow().setFormat(PixelFormat.TRANSLUCENT);
		setContentView(R.layout.activite_camera);
		surfaceView = (SurfaceView) findViewById(R.id.surface);
		surfaceHolder = surfaceView.getHolder();
		surfaceHolder.addCallback(this);
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		
		/* Ajout de la gestion de l'évênement sur le bouton */
		ImageButton close = (ImageButton) findViewById(R.id.takepicture);
		close.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				takeThePicture();
			}
		});
		Log.v(TAG, "Androworms : ending on create");
	}
	
	/** Début de section des callbacks appelés automatiquement lors de la prise de photo */
	private Camera.ShutterCallback mShutterCallback = new Camera.ShutterCallback() {
		public void onShutter() {
		}
	};
	
	/** Callback principal, lorsque l'image au format jpeg est disponible (on l'enregistre) */
	private Camera.PictureCallback mPictureCallbackJpeg = new Camera.PictureCallback() {
		public void onPictureTaken(byte[] data, Camera c) {
			try {
				/* sauvegarde de la photo */
				FileOutputStream filoutputStream = new FileOutputStream(photoPath);
				filoutputStream.write(data);
				filoutputStream.flush();
				filoutputStream.close();
				
				/* Création de l'intent pour passer à l'activité parent l'endroit où est la photo */
				Intent i = new Intent();
				i.putExtra("image", photoPath.getAbsolutePath().toString());
				setResult(RESULT_OK, i);
				
				/* on force la fin de l'activité */
				finish();
			} catch (IOException e) {
			}
		}
	};
	
	/** Fonction appelée en cas d'appuie sur le bouton de photo */
	private void takeThePicture() {
		try {
			/* on crée le dossier pour stocker la photo */
			File root = Environment.getExternalStorageDirectory();
			File fichierPhoto = new File(root, ActiviteAndroworms.DOSSIER_CARTE);
			boolean status = true;
			if (!fichierPhoto.exists()) {
				status = fichierPhoto.mkdir();
			}
			
			/* échec lors de la création du dossier */
			if (!status) {
				Log.e(TAG, "échec lors de la création du dossier Androworms.");
				return;
			}
			
			/* création du path complet vers la photo */
			photoPath = new File(fichierPhoto, "maPhoto.jpg");
			
			/* lancement de la capture de la photo */
			camera.takePicture(mShutterCallback, null, mPictureCallbackJpeg);
		} catch (Exception ex) {
			Log.e(getClass().getSimpleName(), ex.getMessage(), ex);
		}
	}
	
	/** L'appuie sur la touche DPAD CENTER prend aussi une photo (non testé) */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
			takeThePicture();
			return true;
		}
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return super.onKeyDown(keyCode, event);
		}
		return false;
	}
	
	public void requestAutoFocus(Handler handler, int message) {
		if (camera != null) {
			mAutoFocusHandler = handler;
			mAutoFocusMessage = message;
			camera.autoFocus(autoFocusCallback);
		}
	}
	
	public void surfaceCreated(SurfaceHolder holder) {
		/*
		 * la surface de visualisation à été créer, on affiche alors les données de la caméra
		 */
		camera = Camera.open();
		if (camera == null) {
			/*
			 * Si l'appareil android ne dispose pas de caméra arrière, on ouvre alors la caméra de face
			 */
			camera = Camera.open(0);
			
			/*
			 * L'appareil ne dispose probablement pas de caméra, ou alors elle est déjà utilisée
			 */
			if (null == camera) {
				finish();
			}
		}
	}
	
	/** La surface à changée (lorsqu'on applique la caméra dessus par exemple) */
	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		if (isPreviewRunning) {
			camera.stopPreview();
		}
		try {
			/* On a stoppé la caméra le temps de changer la dimension de l'affichage des données */
			camera.setPreviewDisplay(holder);
			Parameters param = camera.getParameters();
			List<Size> sizes = param.getSupportedPreviewSizes();
			for (int i = sizes.size() - 1; i >= 0; i--) {
				if (sizes.get(i).width <= w && sizes.get(i).height <= h) {
					param.setPreviewSize(sizes.get(i).width, sizes.get(i).height);
					camera.setParameters(param);
					break;
				}
			}
		} catch (IOException e) {
		}
		camera.startPreview();
		isPreviewRunning = true;
	}
	
	public void surfaceDestroyed(SurfaceHolder holder) {
		camera.stopPreview();
		isPreviewRunning = false;
		camera.release();
	}
	
	private final Camera.AutoFocusCallback autoFocusCallback = new Camera.AutoFocusCallback() {
		public void onAutoFocus(boolean success, Camera camera) {
			final int delai = 1500;
			if (mAutoFocusHandler != null) {
				Message message = mAutoFocusHandler.obtainMessage(mAutoFocusMessage, success);
				mAutoFocusHandler.sendMessageDelayed(message, delai);
				mAutoFocusHandler = null;
			}
		}
	};
	
	public boolean onTouch(View v, MotionEvent event) {
		return false;
	}
	
	/** Gestionnaire d'évênement lançant cette activité */
	public void onClick(View v) {
		Intent intent = new Intent(this.activiteCreationCarte, ActiviteCamera.class);
		this.activiteCreationCarte.startActivityForResult(intent, TAKE_PICTURE);
		
	}
}