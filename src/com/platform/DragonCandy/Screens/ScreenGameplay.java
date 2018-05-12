package com.platform.DragonCandy.Screens;

import java.util.ArrayList;
import java.util.LinkedList;

import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.platform.DragonCandy.General.Conf;
import com.platform.DragonCandy.General.InputProcessor;
import com.platform.DragonCandy.General.Memory;
import com.platform.DragonCandy.General.PhoneDevice;
import com.platform.DragonCandy.General.PlatformGame;
import com.platform.DragonCandy.General.PlatformGame.gameScreenLegend;
import com.platform.DragonCandy.HUD.FontGame;
import com.platform.DragonCandy.HUD.FontGame.FontType;
import com.platform.DragonCandy.Interface.ScreenManagerInterface;
import com.platform.DragonCandy.gameObject.Background;
import com.platform.DragonCandy.gameObject.Block;
import com.platform.DragonCandy.gameObject.CoinPattern;
import com.platform.DragonCandy.gameObject.Effects;
import com.platform.DragonCandy.gameObject.Player;
import com.platform.DragonCandy.gameObject.PostSign;
import com.platform.DragonCandy.gameObject.TextureAtlas;
import com.platform.DragonCandy.gameObject.Tool;
import com.platform.DragonCandy.gameObject.Background.BackgroundType;
import com.platform.DragonCandy.gameObject.Block.Tile;
import com.platform.DragonCandy.gameObject.CoinPattern.CoinData;
import com.platform.DragonCandy.gameObject.Effects.enumEffects;
import com.platform.DragonCandy.gameObject.TextureAtlas.EndRoundDynamicMenuTexture;
import com.platform.DragonCandy.logic.LevelBuilder;
import com.platform.DragonCandy.logic.ObjectThread;
import com.platform.DragonCandy.logic.LevelBuilder.Color;

public class ScreenGameplay implements Screen  {

	//===========================Variables=====================//
		
	private LevelBuilder levelbuilder;
	private boolean DEBUG = false;
	private PlatformGame game;
	private ScreenManagerInterface ScreenListener;
	
	// screen 
	private SpriteBatch batch;	
	
	// HUD
	public static FontGame fntScore;
	private FontGame fntDistance = new FontGame(FontType.Distance);
	public static FontGame fntCheckPoint;
	public static FontGame fntPause;
	
	// player
	public Player playerUser; 
	public Vector2 playerUserPosition = new Vector2();
	long estimatedTimeEffect;
	
	// HUD
	float fntScoreTextWidth = 0;
	float fntDistanceTextWidth = 0;
	
	// Game state
	public enum GameState{
		Running,GameOver,EndRound;
	}
	public static GameState ThisGameRound;
	private boolean dispose_flag;
	public volatile boolean PauseGame = false;
		
	// buttons
	private ArrayList<Rectangle> Buttons = new ArrayList<Rectangle>();
	private InputProcessor inputProcessor;
	
	// Tools
	private long startTimeTool;
		
	// distance
	private static double startTimeDis;
	private static int DisCalcCounter=0;
	private double _LastDistance;

	// Textures
	public com.platform.DragonCandy.gameObject.TextureAtlas.CoinTexture CoinTexture;
	public com.platform.DragonCandy.gameObject.TextureAtlas.BrickTexture BrickTexture;
	public com.platform.DragonCandy.gameObject.TextureAtlas.ToolTexture ToolTexture;
	public com.platform.DragonCandy.gameObject.TextureAtlas.EndRoundDynamicMenuTexture DynamicEndRoundMenu;
	public com.platform.DragonCandy.gameObject.TextureAtlas.Pause PauseTexture;
	
	
	//background
	 LinkedList<Background> backGroundLayersBuffer;
	 LinkedList<Background> grassLayersBuffer;
	 
	 // end round timer
	 private long startTimeEndRound;
	 
	 private final Pool<Background> BackgroundPool = new Pool<Background>() {
		    @Override
		    protected Background newObject() {
		        return new Background(Conf.SCREEN_BACKGROUND_WIDTH,
						 Conf.SCREEN_BACKGROUND_WIDTH, BackgroundType.background);
		    }
	 };	 
	 private final Pool<Background> BackgroundGrassPool = new Pool<Background>() {
		    @Override
		    protected Background newObject() {
		        return new Background(Conf.SCREEN_BACKGROUND_GRASS_WIDTH,
						Conf.SCREEN_BACKGROUND_GRASS_WIDTH, BackgroundType.grass);
		    }
	 };
	 
	 UIThread UIthread;
	//===========================Variables=====================//
	
	
	public ScreenGameplay(PlatformGame game){
        this.game = game;  		
	}
	
	@Override
	public void show() {			
		INIT();		
	}
	
	@Override
	public void hide() {
		// TODO Auto-generated method stub
		PostSign.Destroy();
		levelbuilder = null;
	}

	@Override
	public void render(float arg0) {
		
		if (dispose_flag==false){
			
			// TODO Auto-generated method stub
			// red, green, blue, alpha
			Gdx.gl.glClearColor(0.2f, 0.3f, 0.65f, 0.5f);
		    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);	  	   	 	 
		    
		    batch.begin();
		    
		    // attach _Distance calc thread to main thread.
		    if (levelbuilder._Distance > _LastDistance) 
		    {
		    	_LastDistance = levelbuilder._Distance;
		    	levelbuilder.SyncGenerateCreator(PauseGame);
		    }
		    
		    // render background
		    Managment_Background();
		    
		    //  collision handler
		    Collision_Handler();
		    
		    // End Round Mangment
		    dispose_flag = End_Round_Managment();
		    
		    // user input
		    userInput();
		    
		    // pause control
		    Pause_Mangment();
		    
		    // player management
		    Management_Player();
		    
		    // render the HUD
		    UpdateHUD();
		    
		    batch.end();
		    
		    if (dispose_flag==true){
		    	MainActivity.androidFunctions.switchActivity(0);
		    	hide();
		    }
		    	
	    }
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub		
		batch.dispose();
	}

	@Override
	public void pause() {
		//stop music if running
	      PhoneDevice.Settings.stopMusic();
	}

	
	@Override
	public void resize(int arg0, int arg1) {
		// TODO Auto-generated method stub		
	}

	@Override
	public void resume() {
		//stop music if running
		PhoneDevice.Settings.playMusic(PhoneDevice.Settings.MusicEnum.psyche_up);
	    
	}

	//==============================METHODS==========================//
	private void INIT(){
		
		// game state
		batch = new SpriteBatch();	
		inputProcessor = new InputProcessor();
		ThisGameRound = GameState.Running;
		Background.BgBoundriesCounter=0;
		
		// create the player
		playerUserPosition.add(40f, 600f);
		playerUser = new Player(playerUserPosition);
		if (DEBUG==true) playerUser.color=Color.ALL;
		
		// draw background 
		 backGroundLayersBuffer = new LinkedList<Background>();
		 backGroundLayersBuffer.add(new Background(0,Conf.SCREEN_BACKGROUND_WIDTH, BackgroundType.background));
		 
		 grassLayersBuffer = new LinkedList<Background>();
		 grassLayersBuffer.add(new Background(0,Conf.SCREEN_BACKGROUND_GRASS_WIDTH, BackgroundType.grass));
		
	    PhoneDevice.Settings.playMusic(PhoneDevice.Settings.MusicEnum.psyche_up);
	    
	    // create a thread which will sync generate method according to player's progress
	    levelbuilder = new LevelBuilder();
	    startTimerThread();
	    
	    // font
	    fntCheckPoint = new FontGame(FontType.CheckPoint);
	    fntScore = new FontGame(FontType.Score);
	    fntPause = new FontGame(FontType.Pause);
	    
	    fntScore.setBitmapText("Score: 0");
	    fntDistance.setBitmapText("Distance: 0.0m");
	    fntCheckPoint.setBitmapText("CheckPoint: 5m");
	    
	    // Texture Object 
	    CoinTexture = new com.platform.DragonCandy.gameObject.TextureAtlas.CoinTexture();
	    BrickTexture =  new com.platform.DragonCandy.gameObject.TextureAtlas.BrickTexture();
	    ToolTexture = new com.platform.DragonCandy.gameObject.TextureAtlas.ToolTexture();
	    DynamicEndRoundMenu = new com.platform.DragonCandy.gameObject.TextureAtlas.EndRoundDynamicMenuTexture();
	    PauseTexture = new com.platform.DragonCandy.gameObject.TextureAtlas.Pause();

	    // input processor buttons array for ingame
	    inputProcessor.clearArea();
		Buttons.add(PauseTexture.Rect); //  must be first so the first check will be if pause was pressed.
		Buttons.add(new Rectangle().set(0,0,Conf.WIDTH, Conf.HEIGHT));
		inputProcessor.setListener(Buttons.get(0));
		inputProcessor.setListener(Buttons.get(1));
		
	}
	
	private void startTimerThread() {
		UIthread = new UIThread();
		UIthread.start();
	 }

	  private class UIThread extends Thread {
	    @Override
	    public void run() {	      
	        SystemClock.sleep(500);
	        while (ThisGameRound == GameState.Running) { 
	        	
	        	switch (DisCalcCounter){
				case 0:
					startTimeDis = System.currentTimeMillis();  
					DisCalcCounter++;
					break;
				case 1:
					if (PauseGame==false){
						double estimatedTime = (System.currentTimeMillis() - startTimeDis);
						if ((estimatedTime/1000)>=levelbuilder._GenerationObjectInterval){
							startTimeDis = estimatedTime;
							if (ScreenGameplay.ThisGameRound==GameState.Running){
								levelbuilder._Distance += 0.1;
								
								fntDistance.setBitmapText("Distance: " +  
										String.format("%.1f", levelbuilder._Distance) + "m");
								DisCalcCounter=0;		
							}
						}						
					}
					break;
				}				
	        	
	        }
	      }	    
	  }
		
	private boolean CollisionDetection(Rectangle a, Rectangle b){
		
		boolean Collision=false;
		
		float offset_x = Conf.PLAYER_OFFSET_X;		
		a.x += offset_x;
		
		
		float distance_x=Math.abs(a.x-b.x);
		float distance_y=Math.abs(a.y-b.y);
		
		if (distance_x< b.width && distance_x< a.width && distance_y<b.height && distance_y<a.height){
			Collision=true;							
		}else{
			Collision=false;	        
		}
		
		return Collision;
	}
	
	private void Collision_Handler(){
		
		int len =  levelbuilder.ObjectThreadList.size();
		if (len==0) return; // no object thread 
		
		for (int j=len-1;j>=0;j--){			
			ObjectThread objThrd = null;
			objThrd = levelbuilder.ObjectThreadList.get(j);	
			objThrd.UpdateObjectSpeed(PauseGame);
			if (objThrd.isAlive()==false) objThrd.start();
			
			switch (objThrd.getTypeObj())
			{
				case block: Management_Block(objThrd.get_threadBlock()); break;			
				case coin: Managment_Coins(objThrd.get_threadCoinPattern()); break;				
				case tool: Managment_Tool(objThrd.get_threadTool()); break;	
				case postsign: Managment_PostSign(objThrd.get_threadPostSign()); break;
			}
		}
	}
	
	private void UpdateHUD(){
		
		fntScore.getGamefont().draw(batch, fntScore.getBitmapText(),
				Conf.WIDTH/19, Conf.BITMAP_TITLE_Y);
		
		if (fntScoreTextWidth==0){
			fntScoreTextWidth = fntScore.getGamefont().
					getBounds(fntScore.getBitmapText()).width;
		}
		
		fntDistance.getGamefont().draw(batch, fntDistance.getBitmapText(), 
				Conf.SMALL_SPACE + Conf.VERY_SMALL_SPACE  + fntScoreTextWidth,
				Conf.BITMAP_TITLE_Y);
		
		if (fntDistanceTextWidth==0){
			fntDistanceTextWidth = fntDistance.getGamefont().
					getBounds(fntScore.getBitmapText()).width;
		}
		
		fntCheckPoint.getGamefont().draw(batch, fntCheckPoint.getBitmapText(), 
				fntScoreTextWidth + 
				fntDistanceTextWidth + Conf.VERY_SMALL_SPACE + Conf.SMALL_SPACE * 3,
				Conf.BITMAP_TITLE_Y);
		
	}
	//==============================METHODS==========================//
	
	
	//==============================Managment_Tool==========================//	
	private void Managment_Tool(Tool tool){
				
	boolean collision = CollisionDetection(playerUser.bounds,tool.RectTool);
			switch (tool.toolCycle)
	    	{
	    	case 0:
	    		
	    		// tool is not grabbed yet and no collision detection
	    		if (!collision){
	    			// show floating effect 
	    			 tool.pe.start();
		    		 // floating effect
		    		 tool.pe.setPosition(tool.RectTool.x + tool.RectTool.getWidth()/2,
		    				 tool.RectTool.y + tool.RectTool.getHeight()/2);	
		    		 
		    		 batch.draw(tool.getTextureTool(), tool.RectTool.x,tool.RectTool.y,tool.RectTool.width,
			    			 tool.RectTool.height);	
		    		 
		    		 tool.pe.update(Gdx.graphics.getDeltaTime());
					    tool.pe.draw(batch,Gdx.graphics.getDeltaTime()); 
		    		}
	    			
	    		else {
	    			
	    			if (PauseGame) 
						 return;
	    			
	    			tool.toolCycle++;
	    		}
	    		break;
	    		
	    	case 1: 
	    		
	    		// tool is grabbed - collision detection is asserted
	    		// set player effect
	    		tool.pe.reset();
	    		tool.pe.dispose();
	    		playerUser.ToolGrab(tool.color);	    		
	    		tool.toolCycle++;	  
	    		PhoneDevice.Settings.setMusicValue(false);
	    		break;
	    	case 2: 
	    		// tool cycle is done - do nothing	    		
	    		break;
	    	}						
		}

	
	//==============================Managment_Tool==========================//
	
	private void Managment_PostSign(PostSign postSign){
		
		 batch.draw(postSign.getSignPostTexture(), 
				 postSign.getSignPostRect().x,
				 postSign.getSignPostRect().y,
				 Conf.POST_SIGN_WIDTH, Conf.POST_SIGN_HIEGHT);	
		 
		
		// post sign initial position text
		 
		 float PostSignTitleTextWidth = PostSign.PostSignTitle.getGamefont().
					getBounds(PostSign.PostSignTitle.getBitmapText()).width;
			
		 PostSign.PostSignTitle.getGamefont().draw(batch, 
				 PostSign.PostSignTitle.getBitmapText(),
				 (postSign.getSignPostRect().x + postSign.getSignPostRect().width/2
						 - PostSignTitleTextWidth/2 - 30), 
				 postSign.getSignPostRect().y + postSign.getSignPostRect().height * (0.75f) + 20);
		
		// post sign initial position text
		 
		 PostSign.PostSignBestScore.getGamefont().draw(batch, 
				 PostSign.PostSignBestScore.getBitmapText(),  
				 (postSign.getSignPostRect().x + postSign.getSignPostRect().width/2
						 - PostSignTitleTextWidth/2 - 30), 
				 postSign.getSignPostRect().y + postSign.getSignPostRect().height * 0.55f + 20);
		
	}
	
	//====================Background Managment Methods========================//
	private void Managment_Background(){
	
		// if the game is not over : continue to scroll the background
		float localspeedBackground = 0;
		float localspeedGrass = 0 ;
		
		switch(ThisGameRound){
		case EndRound:
			// drift speed: normal speed devided by drift factor
			localspeedBackground = levelbuilder.BackGroundSpeed / Conf.SCREEN_DRIFT_FACTOR;
			localspeedGrass = levelbuilder._ObjectsSpeed / Conf.SCREEN_DRIFT_FACTOR;
			break;
		case GameOver:
			// freeze: speed is 0.
			localspeedBackground = localspeedGrass = 0;
			break;
		case Running:
			// speed is set according to player advance in the game
			if (PauseGame==true){
				localspeedBackground = localspeedGrass = 0;			
			}
			else if (PauseGame==false){
				localspeedBackground = levelbuilder.BackGroundSpeed;
				localspeedGrass = levelbuilder._ObjectsSpeed;				
			}
			break;
		default:
			break;
		
		}
		
		
		int len = backGroundLayersBuffer.size()-1;
		for (int j=len;j>=0;j--){
			Background itemBackground;
			itemBackground = backGroundLayersBuffer.get(j);
			
			batch.draw(itemBackground.getBgTypeTexure(), itemBackground.getBgRect().x,
					itemBackground.getBgRect().y, 
					itemBackground.getBgRect().width,
					itemBackground.getBgRect().height);						
			itemBackground.moveBgRect(itemBackground.getBgRect(), localspeedBackground);
			
			if (itemBackground.getBgRect().x==-1){			
				itemBackground = BackgroundPool.obtain();
				backGroundLayersBuffer.add(itemBackground);	
				Log.i("Background", "Added new Background");
			}			
			else if (itemBackground.getBgRect().x ==-itemBackground.getBgLimit()){				
				backGroundLayersBuffer.remove(j);	
				//itemBackground.Destroy();
				//BackgroundPool.free(itemBackground);				
				//itemBackground = null;						
				Log.i("Background", "Background Destroyed");
			}	
						
		}
		
		
		len = grassLayersBuffer.size()-1;
		for (int j=len;j>=0;j--){
			
			Background itemBackground;
			itemBackground = grassLayersBuffer.get(j);
						
			batch.draw(itemBackground.getBgTypeTexure(), itemBackground.getBgRect().x,
					itemBackground.getBgRect().y,
					itemBackground.getBgRect().width,
					itemBackground.getBgRect().height);			
			itemBackground.moveBgRect(itemBackground.getBgRect(), localspeedGrass);
					
			if (itemBackground.getBgRect().x <(-1*itemBackground.getBgLimit())){				
				grassLayersBuffer.remove(j);
				itemBackground.Destroy();
				//BackgroundPool.free(itemBackground);				
				//itemBackground = null;						
				Log.i("Background", "GrassLayer Destroyed");
			}	
			else if ((itemBackground.getBgRect().x==-1) || (grassLayersBuffer.size()<2)){		
				itemBackground = BackgroundGrassPool.obtain();
				grassLayersBuffer.add(itemBackground);	
				Log.i("Background", "Added new GrassLayer: " + String.valueOf((itemBackground.getBgRect().x)));
			}
				
		}
		
			
		Level_Boundries_Managment();
	}
	//====================Background Management Methods========================//
	
	
	//====================Block Management Methods========================//
	private void Management_Block(Block block){
		
		 switch(block.BlockState){
		 
		 case 0: // player is not lost game. continue with tile micro managment
			 
			 for (Tile tile : block.Wall){		
				 
				 Rectangle rectTile = new Rectangle();
				 rectTile = tile.getRectTile();
				 boolean CoinCollision = CollisionDetection(playerUser.bounds,rectTile);
				 
				 switch(tile.state)
				 {
				 
				 case 0: // no collision, show the block
					 
					 batch.draw(tile.getBlockImg(), rectTile.x,rectTile.y,rectTile.width,rectTile.height);	
					 
					 if (PauseGame) 
						 continue;
						 
						 if (CoinCollision==true){
							 tile.state++; 												 			 				
						 }						 
						 else if (ThisGameRound==GameState.Running){	 
							 if (block.Passed==false){
								 if (playerUser.position.x > block.Wall.get(0).rectTile.x){
									 levelbuilder.gamePoints = levelbuilder.gamePoints + 1;
									 fntScore.setBitmapText("Score: " + String.valueOf(levelbuilder.gamePoints));
									 block.Passed=true;
								 }
							 }
							 else if (block.Passed==true){
								 //do nothing.
							 }							 
						 }
						 
						 
					 break;
					 
					 
				 case 1: 
					 
					 batch.draw(tile.getBlockImg(),rectTile.x,rectTile.y,rectTile.width,rectTile.height);						 
					 // if there is a collotion, draw it one time only 			 				 
					 
						// collision, but player has key
						 PhoneDevice.Vibrate();	
						 if ((playerUser.color==block.color) || (playerUser.color==Color.ALL)){		
							 
								tile.setBlockImg(tile.BlockType);	
					    		tile.PlayBreakThoughSound();	
						 }
						 // collision, but player dont have a key: break from tile loop and 
					 	 // draw block crash effect
						 else if (DEBUG==false){
							 block.BlockState=1;
							 ThisGameRound = GameState.GameOver;	
							 break;
						 }			
						 
					 tile.pe = Effects.setNewEffect(enumEffects.TileDistruction, block.color);						 
					 tile.pe.setPosition(tile.getRectTile().x,
			    				tile.getRectTile().y+tile.Height/2);	
					 tile.pe.start();	
					 tile.state++;						 				
					 break;
					 
				 case 2:
					 // continue to draw the effect: no matter collision result
					 tile.pe.setPosition(tile.getRectTile().x,
			    				tile.getRectTile().y+tile.Height/2);	
					 
					 tile.pe.update(Gdx.graphics.getDeltaTime());
				     tile.pe.draw(batch,Gdx.graphics.getDeltaTime());				     					
					 break;
					 
				  }				 
			 }				
			 break;
		 case 1: // player hit the wall without key		
			 
			 for (Tile tile : block.Wall){		
				 
				 Rectangle rectTile = new Rectangle();
				 rectTile = tile.getRectTile();				
				 batch.draw(tile.getBlockImg(), rectTile.x,rectTile.y,rectTile.width,rectTile.height);												 					 
			 }
			 
			 block.PlayCrashSound();		
			 block.pe = Effects.setNewEffect(enumEffects.Crash, block.color);						 
			 block.pe.setPosition(playerUser.bounds.x + playerUser.bounds.width,
					 playerUser.bounds.y + playerUser.bounds.height/2);	
			 block.pe.start();
			 block.BlockState++;
			 break;
			 
		 case 2:
			 
			 for (Tile tile : block.Wall){		
				 
				 Rectangle rectTile = new Rectangle();
				 rectTile = tile.getRectTile();				
				 batch.draw(tile.getBlockImg(), rectTile.x,rectTile.y,rectTile.width,rectTile.height);												 					 
			 }
			 
			 block.pe.update(Gdx.graphics.getDeltaTime());
			 block.pe.draw(batch,Gdx.graphics.getDeltaTime());	
			 break;
		 }		
	 }	 	
	//====================Block Management Methods========================//
	
	
	//====================coin Management Methods========================//
	private void Managment_Coins(CoinPattern cp){
		 // coin managment: collection of coins and effects
	
		
		   	for (CoinData coin : cp.getCoins()){
	    	    	
	    	boolean CoinCollision = CollisionDetection(playerUser.bounds,coin.location);
	    	if (CoinCollision) {
	    		// if player collected the coin
	    		// change the this coin image to collected	    	
	    		
	    		if (PauseGame) 
					 continue;
	    		
	    		if (!coin.Collected){
	    			coin.Collected=true;
	    			levelbuilder.gamePoints = levelbuilder.gamePoints + coin.value;
	    			fntScore.setBitmapText("Score: " + String.valueOf(levelbuilder.gamePoints));
	    			coin.PlayCoinCollectedSound();	
	    			
	    			Vector2 peCoord = new Vector2().add(coin.location.x, 
	    					coin.location.y+ coin.coinTexture.Height/2);
	    					    				
	    			coin.pe.setPosition(coin.location.x,coin.location.y + 
	    					coin.coinTexture.Height/2);		    			
	    			coin.pe.start();			    			    			
	    		}	
	    		
	    		coin.pe.update(Gdx.graphics.getDeltaTime());
	    		coin.pe.draw(batch,Gdx.graphics.getDeltaTime());
	
	    	}
	    	else if (coin.Collected==false){
	    			    		
				batch.draw(coin.coinTexture.TextureCandy,
						coin.location.x,coin.location.y,coin.location.width,
						coin.location.height);	    		    	
				 
	    	}		    		
	    }		    					
	}
	//====================coin Managment Methods========================//
	
	//==============================Management_Player==========================//
	private void Management_Player(){
		
		// render the Player
		switch(ThisGameRound){
		case EndRound:
			return;			
		case GameOver:
			// do nothing
			break;
		case Running:
			if (PauseGame==false){
				playerUser.PlayerMovement();						
			}
			else if (PauseGame==true){
				//  do not update gravity
			}
			break;		
		}
	
		batch.draw(playerUser.getframe(PauseGame), playerUser.getPosition().x , playerUser.getPosition().y,
	    		playerUser.getBounds().width, playerUser.getBounds().height);
		
		
		
		switch  (playerUser.EquipedToolCycle)
		{	
		case 0:
			startTimeTool = System.currentTimeMillis();   
			break;
			
		case 1: 
			
			if (PauseGame==true){				
				return;
			}
			
			// tool is grabbed
			estimatedTimeEffect = System.currentTimeMillis() - startTimeTool;										
			
			playerUser.pe_grab.setPosition(playerUser.getPosition().x + playerUser.getBounds().width/2,
					playerUser.getPosition().y + playerUser.getBounds().height/2);	
    		
				playerUser.pe_grab.update(Gdx.graphics.getDeltaTime());
				playerUser.pe_grab.draw(batch,Gdx.graphics.getDeltaTime()); 							
			
			if (estimatedTimeEffect >= Conf.SYSTIME_MS){
				estimatedTimeEffect = 0;
				playerUser.ToolEquiped();
				startTimeTool = System.currentTimeMillis(); 
				playerUser.EquipedToolCycle++;
			}else{
				// do nothing
			}
			break;
					
		case 2:
			
			if (PauseGame==true){				
				return;
			}
			
			estimatedTimeEffect = (System.currentTimeMillis() - startTimeTool) / 1000;				
			playerUser.pe_equip.setPosition(playerUser.getPosition().x + playerUser.getBounds().width/2,
			playerUser.getPosition().y + playerUser.getBounds().height/2);	
			playerUser.pe_equip.update(Gdx.graphics.getDeltaTime());
			playerUser.pe_equip.draw(batch,Gdx.graphics.getDeltaTime()); 				
			if (estimatedTimeEffect>=Conf.TOOL_TIME_USAGE_LIMIT){
				playerUser.EquipedToolCycle=0;		
				playerUser.color = Color.NONE;				
			}
			
			if (estimatedTimeEffect>=Conf.TOOL_TIME_USAGE_MUSIC_LIMIT){
				PhoneDevice.Settings.setMusicValue(true);
			}
		}				
	}
	//==============================Management_Player==========================//
	
	
	//==============================LEVEL_BOUNDRIES_Managment=====================//
	private void Level_Boundries_Managment(){
		
		if (DEBUG==true) return;
				
		switch (Background.BgBoundriesCounter){
		case 0:
			if ( //(playerUser.bounds.y==Conf.LOWER_BOUND) || 
					(playerUser.position.y >= Conf.UPPER_BOUND + Conf.PLAYER_OFFSET_Y)){					
						ThisGameRound = GameState.GameOver;
						Background.BgBoundriesCounter++;
					}		
			break;
			
		case 1:
			// create the particle effect
			Background.PlayCrashSound();		
			Background.pe = Effects.setNewEffect(enumEffects.Crash, Color.NONE);						 
			Background.pe.setPosition(playerUser.bounds.x + playerUser.bounds.width,
					 playerUser.bounds.y + playerUser.bounds.height/2);	
			Background.pe.start();
			Background.BgBoundriesCounter++;
			break;
			
		case 2:
			 Background.pe.update(Gdx.graphics.getDeltaTime());
			 Background.pe.draw(batch,Gdx.graphics.getDeltaTime());	
			 if (Background.pe.isComplete()){
				 Background.BgBoundriesCounter=0;
			 }
			 break;	 			 
		}
	}				
	
	//==============================LEVEL_BOUNDRIES_Managment=====================//
	
	
	
	//==============================End_Round_Managment==========================//
	private boolean End_Round_Managment(){
		
		 boolean dispose_flag = false;
		 // Check lose condition
		switch (ThisGameRound){
		
		case EndRound:
			
			// draw buttons
			EndRoundDynamicMenuFunctions.CreateButtons(Buttons, DynamicEndRoundMenu);
			
			inputProcessor.setListener(Buttons.get(0));
			inputProcessor.setListener(Buttons.get(1));
			
			// draw button
			batch.draw(DynamicEndRoundMenu.TextureRetry, Buttons.get(0).x,Buttons.get(0).y,
					Buttons.get(0).getWidth(), Buttons.get(0).getHeight());
			
			batch.draw(DynamicEndRoundMenu.TextureExit, Buttons.get(1).x,Buttons.get(1).y,
					Buttons.get(1).getWidth(), Buttons.get(1).getHeight());
			
			
			estimatedTimeEffect = (System.currentTimeMillis() - startTimeEndRound) / 1000;								
			if (estimatedTimeEffect>=Conf.EndRoundTimeOut){
				dispose_flag = true;
			}
			
			break;
			
		case GameOver:
			// begin losing sequence
	    	 PhoneDevice.Settings.stopMusic();
	    	 UIthread = null;
		    	if (playerUser.Lost==false) playerUser.Lost = playerUser.PlayerLost();
		    	else if (playerUser.Lost){
		    		
		    		batch.draw(DynamicEndRoundMenu.TextureWindow, Gdx.graphics.getWidth()/2 - DynamicEndRoundMenu.RectWindow.getWidth()/2,
		    				Gdx.graphics.getHeight()/2 - DynamicEndRoundMenu.RectWindow.getHeight()/2,
		    				DynamicEndRoundMenu.Width,DynamicEndRoundMenu.Height);		
		    		
		    		
		    			//clear running game areas
		    			inputProcessor.clearArea();
		    			
		    			// EndRound is after game over when the losing game animation is complete and
		    			// the player sprite had disapeared.		    			
		    			ThisGameRound = GameState.EndRound;		  
		    			
		    			// save best score
		    			String sBestscore = Memory.LoadFromInternalMemory("Score");
		    			int iBestScore = Integer.parseInt(sBestscore);
		    			int iCurrentScore = levelbuilder.gamePoints;
		    			if (iCurrentScore > iBestScore){
		    				Memory.SaveToInternalMemory("Score",
		    						String.valueOf(iCurrentScore));
		    			}
		    			
		    			startTimeEndRound = System.currentTimeMillis();   
		    		    		
		    	}			    
			break;
		
		case Running:
			// do nothing: wait until player lost the round
			break;
			
		default:
			break;
		
		}	  
		
		return dispose_flag;
	}
	//==============================End_Round_Managment==========================//
	
	//==============================Pause_Managment==========================//	
	private void Pause_Mangment(){
						
		if (ThisGameRound == GameState.Running){
			
			// show next button state (compliment)
			if (PauseGame==false){
				batch.draw(PauseTexture.TextureStop, PauseTexture.Rect.x,
						PauseTexture.Rect.y, PauseTexture.Width,
						PauseTexture.Height);
			}
			else if (PauseGame==true){
				batch.draw(PauseTexture.TextureResume, PauseTexture.Rect.x,
						PauseTexture.Rect.y, PauseTexture.Width,
						PauseTexture.Height);
				
				fntPause.setBitmapText("P A U S E");
				
				float fntPauseWidth =  fntPause.getGamefont().
						getBounds(fntPause.getBitmapText()).width;
				
				fntPause.getGamefont().draw(batch, fntPause.getBitmapText(),  
						Conf.WIDTH/2 - fntPauseWidth/2,
						Conf.HEIGHT/2);
				
			}
			
		}		
	}
	//==============================Pause_Managment==========================//	
	
	//==============================userInput==========================//	
	private void userInput()
	{
		if (ThisGameRound == GameState.Running){
		
			switch(inputProcessor.IsButtonTouched()){
			case 0:
				PauseGame = !PauseGame;		
				PhoneDevice.Settings.setMusicValue(!PauseGame);
				break;
				
			case 1:
				if (PauseGame==false)
				{
					playerUser.Control();						
				}
				else if (PauseGame==true){
					PauseGame=false;
				}
				break;
			}
			
			
		} 
		else if (ThisGameRound == GameState.EndRound){
			
			switch(inputProcessor.IsButtonTouched()){
			case 0:
				// retry 
				game.OnScreenSelect(gameScreenLegend.gameplay);	
				break;
			case 1:	
				// abort to main menu
				dispose_flag = true;
				break;
				
			}

		}
	}
	//==============================userInput==========================//	
	
}
