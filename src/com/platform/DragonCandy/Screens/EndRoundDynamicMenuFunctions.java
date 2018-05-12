package com.platform.DragonCandy.Screens;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.platform.DragonCandy.General.Conf;
import com.platform.DragonCandy.gameObject.TextureAtlas;

public class EndRoundDynamicMenuFunctions {
			
	
	
	public static void CreateButtons(ArrayList<Rectangle> Buttons, 
			TextureAtlas.EndRoundDynamicMenuTexture pause){
		
		Buttons.clear();
		
		Buttons.add(new Rectangle().set(Conf.WIDTH/2 - Conf.WINDOW_PAUSE_WIDTH / 2,
		Conf.HEIGHT/2 - Conf.WINDOW_PAUSE_HEIGHT / 4, 
		pause.buttonWidth, pause.buttonHeight));	

		Buttons.add(new Rectangle().set(Conf.WIDTH/2 + Conf.WINDOW_PAUSE_WIDTH / 2 - pause.buttonWidth,
		Conf.HEIGHT/2 - Conf.WINDOW_PAUSE_HEIGHT / 4, 
		pause.buttonWidth, pause.buttonHeight));	

	}
	
}
	

