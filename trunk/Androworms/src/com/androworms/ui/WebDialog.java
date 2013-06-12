package com.androworms.ui;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class WebDialog extends Dialog {
	
	public static final int DEFAULT_THEME = android.R.style.Theme_Translucent_NoTitleBar;
	
	private String url;
	private WebView webView;
	private ProgressDialog spinner;
	private ImageView crossImageView;
	private FrameLayout contentFrameLayout;
	

	
	/**
	 * Constructor which can be used to display a dialog with an already-constructed URL.
	 *
	 * @param context the context to use to display the dialog
	 * @param url     the URL of the Web Dialog to display; no validation is done on this URL, but it should
	 *                be a valid URL pointing to a Facebook Web Dialog
	 */
	public WebDialog(Context context, String url) {
		this(context, url, DEFAULT_THEME);
	}
	
	/**
	 * Constructor which can be used to display a dialog with an already-constructed URL and a custom theme.
	 *
	 * @param context the context to use to display the dialog
	 * @param url     the URL of the Web Dialog to display; no validation is done on this URL, but it should
	 *                be a valid URL pointing to a Facebook Web Dialog
	 * @param theme   identifier of a theme to pass to the Dialog class
	 */
	public WebDialog(Context context, String url, int theme) {
		super(context, theme);
		this.url = url;
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialogInterface) {
				//sendCancelToListener();
			}
		});
		
		spinner = new ProgressDialog(getContext());
		spinner.requestWindowFeature(Window.FEATURE_NO_TITLE);
		spinner.setMessage("Chargement...");
		spinner.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialogInterface) {
				//sendCancelToListener();
				WebDialog.this.dismiss();
			}
		});
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		contentFrameLayout = new FrameLayout(getContext());
		
		/* Create the 'x' image, but don't add to the contentFrameLayout layout yet
		 * at this point, we only need to know its drawable width and height
		 * to place the webview
		 */
		 //createCrossImage();
		
		/* Now we know 'x' drawable width and height,
		 * layout the webivew and add it the contentFrameLayout layout
		 */
		 int crossWidth = crossImageView.getDrawable().getIntrinsicWidth();
		 //setUpWebView(crossWidth / 2);
		 
		 /* Finally add the 'x' image to the contentFrameLayout layout and
		  * add contentFrameLayout to the Dialog view
		  */
		 contentFrameLayout.addView(crossImageView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		 addContentView(contentFrameLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
	}
}