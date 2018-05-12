package com.platform.DragonCandy.Screens;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.platform.DragonCandy.Interface.IActivity;
import com.platform.DragonCandy.R;

public class Splash extends Activity {
	
	private int currentApiVersion = android.os.Build.VERSION.SDK_INT;
	private ProgressBar ProgressBar;
	private TextView textViewPersent;
	private int persent = 0;
	
	private InterstitialAd mInterstitialAd;
	private static final String TAG = "AdFullScreen";
    private static final String AD_UNIT_ID =  "xxx";
    public static IActivity androidFunctions;
    
		protected void onCreate(Bundle savedInstanceState) {
		    super.onCreate(savedInstanceState);        
		    setContentView(R.layout.splash);
		    
		    
	        // SCREEN_ORIENTATION_LANDSCAPE
	        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	        // FLAG_KEEP_SCREEN_ON
	        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	        // HIDE NAVIGATION BAR
	        HideNavigationBar();
	        
	        ProgressBar = (ProgressBar)findViewById(R.id.progressBar);
	      
	        textViewPersent = (TextView)findViewById(R.id.textViewPersent);
	        textViewPersent.setTextColor(Color.BLACK);
	        textViewPersent.setText("0 %");
	        
	        
	        mInterstitialAd = new InterstitialAd(this);
			mInterstitialAd.setAdUnitId(AD_UNIT_ID);
	 
			mInterstitialAd.setAdListener(new AdListener() {
				@Override
				public void onAdLoaded() {
					Log.d("ad", "onAdLoaded");
					//Toast.makeText(Splash.this, "Ad loaded successfully", Toast.LENGTH_SHORT).show();
					//showInterstitial();
				}
	 
				@Override
				public void onAdFailedToLoad(int errorCode) {
					String errorMessage = String.format("Failed to load add : "+ getErrorReason(errorCode));
					Log.d("ad", errorMessage);
					//Toast.makeText(Splash.this, errorMessage, Toast.LENGTH_SHORT).show();
				}
			});
			
			loadInterstitial();
			
	        setLoadingSequence(new Handler());
	        
	        androidFunctions = new IActivity((Splash)this);
		}
	
		
		private void setLoadingSequence(final Handler handler){
		   	 new Thread() {
		   			@Override
		   			public void run()
		   			{
		   				_isLoading.run();
		   				handler .post(new Runnable() {
		   					public void run()
		   					{
		   						try
		   						{
		   							Intent i = new Intent(Splash.this, MainActivity.class);
		   							startActivity(i);
		   							
		   							/*
		   							 * overridePendingTransition(R.animator.activity_open_translate,
		   							 * R.animator.activity_close_scale);
		   							 */
		   								
		   									   					
		   						}
		   						catch (Exception e)
		   						{
		   							; // nop.
		   						}
		   					}
		   				});
		   			};
		   		}.start();    		
		}
	   
	    private Runnable _isLoading = new Runnable() {
			public void run() 
			{
				for(persent =0;persent<=100;persent++){
					
					runOnUiThread(new Runnable() {
					    public void run() {
					        // Update UI elements
					    	textViewPersent.setText(String.valueOf(persent) + " %");
					    }
					});
					ProgressBar.setProgress(persent);
					
					try {
						Thread.sleep(40);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}	
				
				// runnable was complete
			}
	    };
	
		private void HideNavigationBar(){
				
				final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
				        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
				        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
				        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
				        | View.SYSTEM_UI_FLAG_FULLSCREEN
				        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
		
				    // This work only for android 4.4+
				    if(currentApiVersion >= Build.VERSION_CODES.KITKAT)
				    {
		
				        getWindow().getDecorView().setSystemUiVisibility(flags);
		
				        // Code below is to handle presses of Volume up or Volume down.
				        // Without this, after pressing volume buttons, the navigation bar will
				        // show up and won't hide
				        final View decorView = getWindow().getDecorView();
				        decorView
				            .setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener()
				            {
		
				                @Override
				                public void onSystemUiVisibilityChange(int visibility)
				                {
				                    if((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0)
				                    {
				                        decorView.setSystemUiVisibility(flags);
				                    }
				                }
				            });
				    }
			}
			
			@SuppressLint("NewApi")
			@Override
			public void onWindowFocusChanged(boolean hasFocus)
			{
			    super.onWindowFocusChanged(hasFocus);
			    if(currentApiVersion >= Build.VERSION_CODES.KITKAT && hasFocus)
			    {
			        getWindow().getDecorView().setSystemUiVisibility(
			            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
			                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
			                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
			                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
			                | View.SYSTEM_UI_FLAG_FULLSCREEN
			                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
			    }
			}
			
			public void loadInterstitial() {
				AdRequest adRequest = new AdRequest.Builder()
				//.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
				//.addTestDevice("You can add you device id here, run code once and get id from logs")
				.build();
				
				mInterstitialAd.loadAd(adRequest);
			}
			
			
			public void showInterstitial() {
				if (mInterstitialAd.isLoaded()) {					
					mInterstitialAd.show();					
				} else {
					//Log.d("ad", "Interstitial ad is not loaded yet");
				}
				
			}
			
			/** 
			 * Gets a string error reason from an error code
			 * 
			 * @param errorCode
			 * @return
			 */
			private String getErrorReason(int errorCode) {
				
				String errorReason = "unknown error";
				
				switch(errorCode) {
					case AdRequest.ERROR_CODE_INTERNAL_ERROR:
						errorReason = "internal error";
						break;
					case AdRequest.ERROR_CODE_INVALID_REQUEST:
						errorReason = "invalid request";
						break;
					case AdRequest.ERROR_CODE_NETWORK_ERROR:
						errorReason = "network Error";
						break;
					case AdRequest.ERROR_CODE_NO_FILL:
						errorReason = "no fill";
						break;
				}
				return errorReason;
			}
			
			@Override
			protected void onDestroy() {
				showInterstitial();
				super.onDestroy();
			}
			
			@Override
			public void onResume() {
			    super.onResume();
			    overridePendingTransition(R.animator.activity_open_translate,
							R.animator.activity_close_scale);
			}
			
}

