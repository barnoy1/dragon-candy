package com.platform.DragonCandy.Interface;

import android.os.Handler;

import com.platform.DragonCandy.Screens.MainActivity;
import com.platform.DragonCandy.Screens.Splash;

public class IActivity {
	
    private MainActivity _mainActivity;
    private Splash _spashActivity;
    
    // constructors //
    public IActivity (MainActivity activity){
    		_mainActivity = activity;
    }
    
    public IActivity (Splash activity){
    	_spashActivity = activity;
    }
    
    public void showInterstitialAd(){
    	_spashActivity.showInterstitial();
    }
    
    public void switchActivity(int activityID) {
        _mainActivity.startMyActivity(activityID);
    }
    
    public void setAdDelaySequence(){
    	
    	final Handler handler = new Handler();
    	
	   	 new Thread() {
	   			@Override
	   			public void run()
	   			{
	   				_delay.run();
	   				handler .post(new Runnable() {
	   					
						public void run()
	   					{
							if (MainActivity.RaiseAd==true){
								MainActivity.RaiseAd=false; 
								
								//if ((MainActivity.AdCounter % MainActivity.MainScreenAdLimit)==0){
									Splash.androidFunctions
									.showInterstitialAd();
								//}									
								MainActivity.AdCounter++;		
							}							
	   					}
	   				});
	   			};
	   		}.start();    		
	}
	
	private Runnable _delay = new Runnable() {
		public void run() 
		{
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};
}