package com.androworms;

import java.io.IOException;
import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Bundle;
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

public class CamHandler extends Activity implements SurfaceHolder.Callback, OnClickListener, OnTouchListener {
	private static final String TAG = "Androworms.CamHandler.Event";
	private Camera camera;
	private boolean isPreviewRunning = false;
	
	private SurfaceView surfaceView;
	private SurfaceHolder surfaceHolder;
	private Handler mAutoFocusHandler;
	private int mAutoFocusMessage;
	
	
	ActiviteMenuPrincipal activiteMenuPrincipal;
	
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		Log.v(TAG,"Androworms : begining on create");
		getWindow().setFormat(PixelFormat.TRANSLUCENT);
		setContentView(R.layout.camera);
		surfaceView = (SurfaceView)findViewById(R.id.surface);
		surfaceHolder = surfaceView.getHolder();
		surfaceHolder.addCallback(this);
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		
		/*ImageButton close = (ImageButton) findViewById(R.id.takepicture);
		close.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				takeThePicture();// TODO : a créer
			}
		});*/
		Log.v(TAG,"Androworms : ending on create");
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
	}
	
	Camera.ShutterCallback mShutterCallback = new Camera.ShutterCallback() {
		public void onShutter() {
			Log.e(getClass().getSimpleName(), "SHUTTER CALLBACK");
		}
	};
	
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
			//takeThePicture();
			return true;
		}
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return super.onKeyDown(keyCode, event);
		}
		return false;
	}
	
	protected void onResume() {
		Log.e(getClass().getSimpleName(), "onResume");
		super.onResume();
	}
	
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
	
	protected void onStop() {
		Log.e(getClass().getSimpleName(), "onStop");
		super.onStop();
	}
	
	public void requestAutoFocus(Handler handler, int message) {
		if (camera != null)
		{
			mAutoFocusHandler = handler;
			mAutoFocusMessage = message;
			camera.autoFocus(autoFocusCallback);
		}
	}
	
	public void surfaceCreated(SurfaceHolder holder) {
		Log.e(getClass().getSimpleName(), "surfaceCreated");
		camera = Camera.open();
	}
	
	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		Log.e(getClass().getSimpleName(), "surfaceChanged");
		if (isPreviewRunning) {
			camera.stopPreview();
		}
		Camera.Parameters p = camera.getParameters();
		p.setPreviewSize(w, h);
		camera.setParameters(p);
		try {
			camera.setPreviewDisplay(holder);
		} catch (IOException e) {
		}
		camera.startPreview();
		isPreviewRunning = true;
	}
	
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.e(getClass().getSimpleName(), "surfaceDestroyed");
		camera.stopPreview();
		isPreviewRunning = false;
		camera.release();
	}
	
	private final Camera.AutoFocusCallback autoFocusCallback = new Camera.AutoFocusCallback() {
		public void onAutoFocus(boolean success, Camera camera) {
			if (mAutoFocusHandler != null) {
				Message message = mAutoFocusHandler.obtainMessage(mAutoFocusMessage, success);
				mAutoFocusHandler.sendMessageDelayed(message, 1500);
				mAutoFocusHandler = null;
			}
		}
	};
	
	public boolean onTouch(View v, MotionEvent event) {
		return false;
	}
	
	public void onClick(View v) {
		Log.v(TAG,"Androworms : Vous avez cliqué sur cam!");
		Intent intent = new Intent(this.activiteMenuPrincipal, CamHandler.class);
		Log.v(TAG,"Androworms : created intent!");
		this.activiteMenuPrincipal.startActivity(intent);
		Log.v(TAG,"Androworms : started activity!");
		
	}
	
	public CamHandler(ActiviteMenuPrincipal activiteMenuPrincipal) {
		this.activiteMenuPrincipal = activiteMenuPrincipal;
	}
	
	public CamHandler() {
		super();
	}
}
