package com.androworms;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.plus.PlusClient;

/** Activité de gestion du partage de l'application via les réseaux sociaux
 */
public class ActivitePartage extends Activity implements ConnectionCallbacks, OnConnectionFailedListener {
	
	private static final String TAG = "Partage";
	
	// Google+
	private static final int REQUEST_CODE_RESOLVE_ERR = 9000;
	private ProgressDialog mConnectionProgressDialog;
	private PlusClient mPlusClient;
	private ConnectionResult mConnectionResult;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activite_partage);
		
		
		// Bouton "partager"
		ImageButton ibPartagerGeneral = (ImageButton)findViewById(R.id.ib_partager_general);
		ibPartagerGeneral.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// création de l'Intent pour envoyer
				Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
				
				// on définit le type
				shareIntent.setType("text/plain");
				
				// on ajoute un sujet
				shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Viens jouer à Androworms !");
				
				// On ajoute le message
				shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, R.string.message_partage);
				
				// On démarre le sélécteur d'applications pour le partage
				startActivity(Intent.createChooser(shareIntent, "Partager l'application"));
			}
		});
		
		// Bouton "Evaluer sur le Play Store"
		Button btnEvaluerPlayStore = (Button)findViewById(R.id.btn_play_store);
		btnEvaluerPlayStore.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String url = "market://details?id=com.androworms";
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(url));
				startActivity(i);
			}
		});
		
		
		
		// Bouton "Facebook-Login"
		com.facebook.widget.LoginButton btnLoginFacebook = (com.facebook.widget.LoginButton)findViewById(R.id.btn_login_facebook);
		btnLoginFacebook.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				loginFacebook();
			}
		});
		
		
		// Bouton "Google+"
		com.google.android.gms.common.SignInButton btnLoginGoogle = (com.google.android.gms.common.SignInButton)findViewById(R.id.btn_login_google);
		btnLoginGoogle.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				mPlusClient.connect();
			}
		});
		
		mPlusClient = new PlusClient.Builder(this, this, this)
			.setVisibleActivities("http://schemas.google.com/AddActivity", "http://schemas.google.com/BuyActivity")
			.build();
		// Barre de progression à afficher si l'échec de connexion n'est pas résolu.
		mConnectionProgressDialog = new ProgressDialog(this);
		mConnectionProgressDialog.setMessage("Connexion...");
		
		
		// Bouton "Twitter"
		Button btnPartageTwitter = (Button)findViewById(R.id.btn_partager_twitter);
		btnPartageTwitter.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String tweetUrl = "https://twitter.com/intent/tweet?text=" +
						v.getContext().getString(R.string.message_partage) +
						"&url=" + v.getContext().getString(R.string.url_google_code);
				
				Log.v(TAG, "Twitter-URL:" + tweetUrl);
				findViewById(R.id.inc_pageweb).setVisibility(View.VISIBLE);
				WebView pageWeb = (WebView)findViewById(R.id.wv_pageweb);
				pageWeb.loadUrl(tweetUrl);
			}
		});
	}
	
	
	public void loginFacebook() {
		Session.openActiveSession(this, true, new Session.StatusCallback() {
			
			// callback when session changes state
			@Override
			public void call(Session session, SessionState state, Exception exception) {
				Log.v(TAG, "call()");
				if (session.isOpened()) {
					Log.v(TAG, "session.isOpened()");
					
					// make request to the /me API
					Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {
						
						// callback after Graph API response with user object
						@Override
						public void onCompleted(GraphUser user, Response response) {
							Log.v(TAG, "onCompleted()");
							if (user != null) {
								Log.v(TAG, "user != null");
								//TextView welcome = (TextView) findViewById(R.id.welcome);
								//welcome.setText("Hello " + user.getName() + "!");
								Button ibPartagerFacebook = (Button) findViewById(R.id.btn_partager_facebook);
								ibPartagerFacebook.setText("Hello " + user.getName() + "!");
							}
						}
					});
				}
			}
		});
	}
	
	
	@Override
	protected void onStart() {
		super.onStart();
		
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		mPlusClient.disconnect();
	}
	
	@Override
	public void onConnectionFailed(ConnectionResult result) {
		if (result.hasResolution()) {
			try {
				result.startResolutionForResult(this, REQUEST_CODE_RESOLVE_ERR);
			} catch (SendIntentException e) {
				mPlusClient.connect();
			}
		}
		// Enregistrer le résultat et résoudre l'échec de connexion lorsque l'utilisateur clique.
		mConnectionResult = result;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
		super.onActivityResult(requestCode, responseCode, intent);
		if (requestCode == REQUEST_CODE_RESOLVE_ERR && responseCode == RESULT_OK) {
			mConnectionResult = null;
			mPlusClient.connect();
		}
		else {
			Session.getActiveSession().onActivityResult(this, requestCode, responseCode, intent);
		}
	}
	
	@Override
	public void onConnected(Bundle connectionHint) {
		String accountName = mPlusClient.getAccountName();
		Toast.makeText(this, accountName + " est connecté.", Toast.LENGTH_LONG).show();
	}
	
	@Override
	public void onDisconnected() {
		Log.d(TAG, "disconnected");
	}
	
}