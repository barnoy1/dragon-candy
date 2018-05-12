package com.platform.DragonCandy.General;

import android.content.Intent;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.platform.DragonCandy.Interface.ScreenManagerInterface;
import com.platform.DragonCandy.Screens.MainActivity;
import com.platform.DragonCandy.Screens.ScreenGameplay;
import com.platform.DragonCandy.Screens.Splash;

public class PlatformGame extends Game implements ScreenManagerInterface{	
		
	public SpriteBatch gameBatch;
	
	public enum gameScreenLegend{
		gameplay,main;
	}
	
   @Override
    public void create() {
	   gameBatch = new SpriteBatch();
	   //ScreenManagerInterface ScreenListener = this;
	   //setScreen(new ScreenSplash(this,ScreenListener));
	   setScreen(new ScreenGameplay(this));
    }

	@Override
	public void OnScreenSelect(gameScreenLegend loadScreen) {
		// TODO Auto-generated method stub
		ScreenManagerInterface ScreenListener = this;
		switch (loadScreen){
		case gameplay:
			setScreen(new ScreenGameplay(this));
			break;	
		default:
			break;
		}
		
	}
}

