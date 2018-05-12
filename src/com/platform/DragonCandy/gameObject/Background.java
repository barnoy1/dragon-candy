package com.platform.DragonCandy.gameObject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.Rectangle;
import com.platform.DragonCandy.General.Conf;
import com.platform.DragonCandy.General.PhoneDevice;
import com.platform.DragonCandy.General.PhoneDevice.Settings;
import com.platform.DragonCandy.General.PhoneDevice.Settings.SoundsEnum;

public class Background {

	public enum BackgroundType{
		background,
		grass;
	}
	public BackgroundType bgType;
	
	private Rectangle bgRect;
	private float bgLimit;
	public Texture BgTypeTexure;
		
	private static int BGcounter = 0;

	// losing conditions when player make contact with level boundries
	public static ParticleEffect pe;
	public static int BgBoundriesCounter;
	
	public Background(float startposX, float bglimit, BackgroundType bgType){
		
		// background		
		bgRect= new Rectangle();
		bgLimit = bglimit;
		
	
		switch(bgType){
		
			case background:				
				bgRect.set(startposX, 0f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());									
				int index = BGcounter % 4;				
				BgTypeTexure =  new TextureAtlas.BackGroundTexture
						(bgType, index).backgroundtexture;
				
				BGcounter++;
				break;
				
			case grass:	
				BgTypeTexure =  new TextureAtlas.BackGroundTexture
				(bgType, -1).backgroundtexture;
				bgRect.set(startposX, 0f, BgTypeTexure.getWidth(), Conf.SCREEN_BACKGROUND_GRASS_HEIGHT);								
				break;		
		}
		
		
	} 
	
	public float getBgLimit() {
		return bgLimit;
	}

	public Texture getBgTypeTexure() {
		return BgTypeTexure;
	}

	public Rectangle getBgRect() {
		return bgRect;
	}
	
	public void moveBgRect(Rectangle bgRect, float bgspeed) {
		this.bgRect.x = bgRect.x - bgspeed;
	}
	
	public void setBgRect(Rectangle bgRect, float startposX) {
		this.bgRect.x = startposX;
	}
	
	public static void PlayCrashSound(){
		PhoneDevice.Settings.playSound(PhoneDevice.Settings.SoundsEnum.Crash);
	}

	public void Destroy(){		
		BgTypeTexure.dispose();
		pe = null;
		bgRect = null;	
	}
	
	
}
