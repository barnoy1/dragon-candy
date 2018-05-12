package com.platform.DragonCandy.gameObject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.platform.DragonCandy.General.Conf;
import com.platform.DragonCandy.gameObject.Background.BackgroundType;
import com.platform.DragonCandy.gameObject.Block.tilePos;
import com.platform.DragonCandy.logic.LevelBuilder.Color;
public class TextureAtlas {

	//==============================BRICK=========================//
	public static class BrickTexture{			
		
		public static Texture brickImg_BLUE_Bottom;
		public static Texture brickImg_BLUE_Middle;
		public static Texture brickImg_BLUE_TOP;
		
		public static Texture brickImg_GREEN_Bottom;
		public static Texture brickImg_GREEN_Middle;
		public static Texture brickImg_GREEN_TOP;
		
		public static Texture brickImg_RED_Bottom;
		public static Texture brickImg_RED_Middle;
		public static Texture brickImg_RED_TOP;
		
		
		public static float WIDTH =  Conf.BLOCK_BOX_SIZE;
		public static float HEIGHT = Conf.BLOCK_BOX_SIZE;
		
		public BrickTexture(){
			
			//SetBoxSize();
			
			brickImg_RED_Bottom = new Texture("data/object/block/brick-red-bottom.png");
			brickImg_RED_Middle = new Texture("data/object/block/brick-red-middle.png");
			brickImg_RED_TOP = new Texture("data/object/block/brick-red-top.png");
				
			brickImg_BLUE_Bottom = new Texture("data/object/block/brick-blue-bottom.png");
			brickImg_BLUE_Middle = new Texture("data/object/block/brick-blue-middle.png");
			brickImg_BLUE_TOP = new Texture("data/object/block/brick-blue-top.png");
			
			brickImg_GREEN_Bottom = new Texture("data/object/block/brick-green-bottom.png");
			brickImg_GREEN_Middle = new Texture("data/object/block/brick-green-middle.png");
			brickImg_GREEN_TOP = new Texture("data/object/block/brick-green-top.png");
		
		}
		
		public static Texture getBrick(Color c, tilePos tilepos){
			switch(c){
			
			case BLUE: 
				
				if (tilepos == tilePos.bottom) return brickImg_BLUE_Bottom;				
				if (tilepos == tilePos.middle) return brickImg_BLUE_Middle;				
				if (tilepos == tilePos.top) return brickImg_BLUE_TOP;
				
				break;
				
			case GREEN: 
				
				if (tilepos == tilePos.bottom) return brickImg_GREEN_Bottom;				
				if (tilepos == tilePos.middle) return brickImg_GREEN_Middle;				
				if (tilepos == tilePos.top) return brickImg_GREEN_TOP;
				
				break;
				
			case RED: 
				
				if (tilepos == tilePos.bottom) return brickImg_RED_Bottom;				
				if (tilepos == tilePos.middle) return brickImg_RED_Middle;				
				if (tilepos == tilePos.top) return brickImg_RED_TOP;
				
			default:
				break;
			
			}
			
			return null;
			
		}
		
	}	
	//==============================BRICK=========================//
	
	
	//==============================COIN=========================//
	public static class CoinTexture{			

		public float Width;
		public float Height;
		public Texture TextureCandy;
		
		private static int lastVal = -1;
		
		public CoinTexture(){
			
			
			int TextureValue = Conf.randInt(1, 9);
			
			if (lastVal == TextureValue){
				TextureValue = Conf.randInt(1, 9);
			}
			lastVal = TextureValue;
			
			switch (TextureValue){
			case 1:
				TextureCandy = new Texture("data/object/Coin/candy_1.png");
				break;
			case 2:
				TextureCandy = new Texture("data/object/Coin/candy_2.png");
				break;
				
			case 3:
				TextureCandy = new Texture("data/object/Coin/candy_3.png");
				break;
				
			case 4:
				TextureCandy = new Texture("data/object/Coin/candy_4.png");
				break;		
				
			case 5:
				TextureCandy = new Texture("data/object/Coin/candy_5.png");
				break;
				
			case 6:
				TextureCandy = new Texture("data/object/Coin/candy_6.png");
				break;
				
			case 7:
				TextureCandy = new Texture("data/object/Coin/candy_7.png");
				break;
				
			case 8:
				TextureCandy = new Texture("data/object/Coin/candy_8.png");
				break;
				
			case 9:
				TextureCandy = new Texture("data/object/Coin/candy_9.png");
				break;
			}
			
			Width = TextureCandy.getWidth();
			Height = TextureCandy.getHeight();
		}
				
	}	
	//==============================COIN=========================//
		
	//==============================TOOL=========================//
		public static class ToolTexture{			
			public static Texture TextureHammer_RED; 
			public static Texture TextureHammer_BLUE; 
			public static Texture TextureHammer_GREEN;	
			
			public ToolTexture(){
				
				TextureHammer_RED = new Texture(Gdx.files
						.internal("data/object/Tool/tool-hammer-red.png"));
				TextureHammer_BLUE = new Texture(Gdx.files
						.internal("data/object/Tool/tool-hammer-blue.png"));
				TextureHammer_GREEN = new Texture(Gdx.files
						.internal("data/object/Tool/tool-hammer-green.png"));
			}
			
			public static Texture getTool(Color c){
				switch(c){
				case BLUE: return TextureHammer_BLUE;
				case GREEN: return TextureHammer_GREEN;
				case RED: return TextureHammer_RED;
				default:
					break;
				
				}
				return null;	
			}
		}	
	//==============================TOOL=========================//
	
		
		
	//==============================BackGroundTexture=========================//
	public static class BackGroundTexture{	
		
		public Texture backgroundtexture;
		
		public BackGroundTexture(BackgroundType bgType, int index){
			switch(bgType){
			
			case background:
				
				switch(index){
				case 0:
					backgroundtexture = new Texture(Gdx.
							files.internal("data/Background/background_0.png"));			
					break;
					
				case 1:
					backgroundtexture = new Texture(Gdx.
							files.internal("data/Background/background_1.png"));			
					break;
				
				case 2:
					backgroundtexture = new Texture(Gdx.
							files.internal("data/Background/background_2.png"));			
					break;
				
				case 3:
					backgroundtexture = new Texture(Gdx.
							files.internal("data/Background/background_3.png"));			
					break;					
				}
				
				break;
				
			case grass:
				backgroundtexture = new Texture(Gdx.
						files.internal("data/Background/ground.png"));
				break;
				
			default:
				break;
			
			}
		}
	}
		
	//==============================BackGroundTexture=========================//
		
		
		
		
	//==============================EndRoundDynamicMenuTexture=========================//
		
	public static class EndRoundDynamicMenuTexture {
	
	public Rectangle RectWindow;
	public Texture TextureWindow;
	public Texture TextureRetry;
	public Texture TextureExit;
	public float Width = 4.5f;
	public float Height = 4.5f;
	
	public float buttonWidth = (float) (Conf.WINDOW_PAUSE_WIDTH / 2.5);
	public float buttonHeight = Conf.WINDOW_PAUSE_HEIGHT / 2;
	
	
		public EndRoundDynamicMenuTexture(){
			 RectWindow = new Rectangle();
			 TextureWindow = new Texture(Gdx.files.internal("data/EndRoundDynamicMenu/loadingPauseWindow.png"));
			 TextureRetry = new Texture(Gdx.files.internal("data/EndRoundDynamicMenu/Retry.png"));
			 TextureExit = new Texture(Gdx.files.internal("data/EndRoundDynamicMenu/Exit.png"));
			 		
			 RectWindow.set(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2,
					 Width,Height);
		}
		
	}
	//==============================EndRoundDynamicMenuTexture=========================//
	
	
	//==============================EndRoundDynamicMenuTexture=========================//
	
		public static class Pause {
		
		public Rectangle Rect;
		public Texture TextureResume;
		public Texture TextureStop;
		public float Width = 100f;
		public float Height = 100f;
		public boolean run = false;
		
			public Pause(){
				 Rect = new Rectangle();
				 TextureResume = new Texture(Gdx.files.internal("data/pause/resume.png"));
				 TextureStop = new Texture(Gdx.files.internal("data/pause/stop.png"));
				 
				 Rect.set(Gdx.graphics.getWidth() - TextureStop.getWidth(),
						 Gdx.graphics.getHeight() - TextureStop.getHeight(),
						 TextureStop.getWidth(),TextureStop.getHeight());
			}
			
		}
		//==============================EndRoundDynamicMenuTexture=========================//

}
