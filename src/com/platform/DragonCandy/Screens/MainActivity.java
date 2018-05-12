package com.platform.DragonCandy.Screens;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Handler;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.platform.DragonCandy.General.PhoneDevice;
import com.platform.DragonCandy.General.PlatformGame;
import com.platform.DragonCandy.Interface.IActivity;
import com.platform.DragonCandy.R;



public class MainActivity extends AndroidApplication {

	public static IActivity androidFunctions;
    public static int AdCounter = 0;
    public static boolean RaiseAd = true;
	///public static int MainScreenAdLimit = 3;
	
	public static Vibrator vibrator;
	private int currentApiVersion = android.os.Build.VERSION.SDK_INT;
	private ImageView imgPlay,imgSound,imgMusic,imgViberate;
	
	// get settings
	boolean sound = PhoneDevice.Settings.isSound();
	boolean music = PhoneDevice.Settings.isMusic();
	boolean vibration = PhoneDevice.Settings.isVibration();

	public static Context mContext;
	
	@Override
	 public void onCreate (android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      
        // remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        
		// SCREEN_ORIENTATION_LANDSCAPE
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        // FLAG_KEEP_SCREEN_ON
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        // CREATE VIBRATOR
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);  
        
    	HideNavigationBar();

        initializeForView(new PlatformGame());   
        
        imgPlay =(ImageView)findViewById(R.id.imgPlay);
        imgPlay.setOnClickListener(OnClickButton);
        
        imgSound =(ImageView)findViewById(R.id.ImgSound);
        imgSound.setOnClickListener(OnClickButton);
                
        imgMusic =(ImageView)findViewById(R.id.imgMusic);
        imgMusic.setOnClickListener(OnClickButton);
        
        imgViberate =(ImageView)findViewById(R.id.ImgViberate);
        imgViberate.setOnClickListener(OnClickButton);
        
        
		// load buttons settings GUI
		LoadButtonGUI();
		
		mContext = getApplicationContext();
		
		androidFunctions = new IActivity((MainActivity)this);
		
		androidFunctions.setAdDelaySequence();
		
	}
	public static Context getmContext() {
		return mContext;
	}

		private OnClickListener OnClickButton = new OnClickListener() {
	        public void onClick(View v){
	            
	        	switch (v.getId()){
	   		 		   	
	        		case R.id.imgPlay: 	   		
	        				RaiseAd=true; // ready to set ad for next time
		   			 		initialize(new PlatformGame());   			   			 		
	        			break;
	        			
	        		case R.id.imgMusic: 
	        			music = !music;
	        			PhoneDevice.Settings.setMusic(music);
	        			PhoneDevice.Settings.playSound(PhoneDevice.Settings.SoundsEnum.ButtonClick);
	        			break;
					
	        		case R.id.ImgSound:
	        			sound = !sound;
	        			PhoneDevice.Settings.setSound(sound);
	        			PhoneDevice.Settings.playSound(PhoneDevice.Settings.SoundsEnum.ButtonClick);
	        			break;
	        			
	        		case R.id.ImgViberate:
	        			vibration = !vibration;
	        			PhoneDevice.Settings.setVibration(vibration);
	        			PhoneDevice.Vibrate();
	        			PhoneDevice.Settings.playSound(PhoneDevice.Settings.SoundsEnum.ButtonClick);
	        			break;
	        	}
	        	
	        	LoadButtonGUI();
	        }
		};
		
		public static Vibrator getVibrator() {
			return vibrator;
		}
		
		private void LoadButtonGUI(){
			
			if (sound){
				imgSound.setImageDrawable(getResources().
						getDrawable(R.drawable.sound_on));    	
			}
			else{
				imgSound.setImageDrawable(getResources().
						getDrawable(R.drawable.sound_off));
			}
			
			if (music){
				imgMusic.setImageDrawable(getResources().
						getDrawable(R.drawable.music_on));    		 
			}
			else{
				imgMusic.setImageDrawable(getResources().
						getDrawable(R.drawable.music_off));    	
			}
			
			if (vibration){
				imgViberate.setImageDrawable(getResources().
						getDrawable(R.drawable.vibrate_on));    	
			}
			else{
				imgViberate.setImageDrawable(getResources().
						getDrawable(R.drawable.vibate_off));    	
			}
		}
		
		
		
		public  void HideNavigationBar(){
			
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

		public void startMyActivity(int activityID) {
	        switch(activityID){
	            case 0:
	                Intent i = new Intent(getApplicationContext(), Splash.class);
	                startActivity(i);
	                break;
	        }
	    }

		public IActivity getAndroidFunctions() {
			return androidFunctions;
		}
}
	

