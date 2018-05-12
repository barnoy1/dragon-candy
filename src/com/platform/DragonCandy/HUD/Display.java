package  com.platform.DragonCandy.HUD;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.platform.DragonCandy.General.Conf;

public class Display {

	public static class HUD
	{	
		
		//private static SpriteBatch batch;
		private static BitmapFont font;
		
		
	    public static String ConvertGameTime(int gameTime){
			
			int gameMin = gameTime / 60;
			int gameSec = gameTime - gameMin*60;
			
			String sSec = "" ;
			String sMin = "" ;
			
			String stopWatch = null;
			
			if (gameMin<10){
				sMin = "0" + String.valueOf(gameMin);
			}
			else if (gameMin>=10){
				sMin =  String.valueOf(gameMin);
			}
			
			if (gameSec<10){
				sSec = "0" + String.valueOf(gameSec);
			}
			else if (gameSec>=10){
				sSec =  String.valueOf(gameSec);
			}
			
			stopWatch = sMin + ":" + sSec;
			
			return stopWatch;
			
		}


		public static BitmapFont Create(){
		    //batch = new SpriteBatch();
			font = new BitmapFont(Gdx.files.internal("data/fonts/gameFont.fnt"),
			         Gdx.files.internal("data/fonts/gameFont.png"), false);		
			font.setScale(Conf.BITMAP_SCALE_SMALL, Conf.BITMAP_SCALE_SMALL);
			return font;
		}
		
		
	}
	
}
