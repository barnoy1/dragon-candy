package com.platform.DragonCandy.gameObject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.platform.DragonCandy.General.Conf;
import com.platform.DragonCandy.General.InputProcessor;
import com.platform.DragonCandy.General.PhoneDevice;
import com.platform.DragonCandy.General.PhoneDevice.Settings;
import com.platform.DragonCandy.General.PhoneDevice.Settings.SoundsEnum;
import com.platform.DragonCandy.gameObject.Effects.enumEffects;
import com.platform.DragonCandy.logic.LevelBuilder;
import com.platform.DragonCandy.logic.LevelBuilder.Color;

public class Player {
	
	private static final int  FRAME_COLS = 8;
	private static final int  FRAME_ROWS = 1;
	private static float ACC_Y=0f;
	public  Color color;
	public static 
	
	enum State{
		 Walking, Lose, flying;
	}

	public Vector2 position = new Vector2();
	private static float WIDTH;
	public static State state = State.Walking;
	private static float HEIGHT;
	
	private static Texture playerTexture;
	private static Animation Lose;
	private static Animation Walk;
	private static Animation fly;
	@SuppressWarnings("unused")
	private static int frameWidth;
	@SuppressWarnings("unused")
	private static int frameHeight;
	
	public Rectangle bounds = new Rectangle();
	private float stateTime;
	private TextureRegion[] walkFrames;
	private TextureRegion[] looseFrames;
	private TextureRegion[] flyFrames;
	private TextureRegion currentFrame;
	
	private Sound wavGrabTool = Gdx.audio.newSound(Gdx.files.internal("data/object/Tool/effects/grab-tool-sound-effect.mp3"));	
	public Vector2 impact = new Vector2().add(Conf.IMPACT_VECTOR_INITAL_VALUES);
	private float initialLosePosY=-1;
	private boolean bounceUpDone = false;
	public boolean Lost = false;
	public int EquipedToolCycle = 0;
	public ParticleEffect pe_grab;
	public ParticleEffect pe_equip;
	
	public Player(Vector2 position){
	/*
	 * Connstructor
	 */
		
		color = Color.NONE; 
		
		//playerTexture = new Texture("data/player/koalio.png");
		playerTexture = new Texture("data/player/dragon.png");
		frameWidth = playerTexture.getWidth()/FRAME_COLS;
		frameHeight = playerTexture.getHeight()/FRAME_ROWS;
        //TextureRegion[][] tmp = TextureRegion.split(playerTexture, 18, 26);
        TextureRegion[][] tmp = TextureRegion.split(playerTexture, frameWidth, frameHeight);
        //TextureRegion[][] tmp = TextureRegion.split(playerTexture, 190, 137);
        walkFrames = new TextureRegion[1 * 4];
        flyFrames = new TextureRegion[1 * 4];
        looseFrames = new TextureRegion[1];
        int index = 0;
        EquipedToolCycle = 0;
        
        // attach walkFrames according to TextureRegion
        for (int i = 0; i < FRAME_ROWS; i++) {        	        	
            for (int j = 0; j < 4; j++) {
            	walkFrames[index++] = tmp[i][j];                		
            }                            
        }
        
     // attach walkFrames according to TextureRegion
        index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {        	        	
            for (int j = 4; j < FRAME_COLS; j++) {
            	flyFrames[index++] = tmp[i][j];                		
            }                            
        }
        
        // attach looseFrames according to TextureRegion
        looseFrames[0]=tmp[0][0];
        
        /*
		Jump = new Animation(0, regions[3]);
		Walk = new Animation(0.15f, regions[2],regions[3],regions[4]);
		Walk.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
		*/
        Walk = new Animation(0.1f, walkFrames);
        fly = new Animation(0.1f, flyFrames);
        Lose = new Animation(0.1f, looseFrames);
        
        Player.WIDTH = Conf.PLAYER_WIDTH;
		Player.HEIGHT = Conf.PLAYER_HEIGHT;
		
		this.position=position;
		bounds.set(position.x, position.y, Player.WIDTH, Player.HEIGHT);
		stateTime=0f;
		state = State.Walking;
		
	}
	
	public void PlayGrabSound(){
		//wavGrabTool.play(0.15f);
		PhoneDevice.Settings.playSound(PhoneDevice.Settings.SoundsEnum.GrabTool);
	}
	
	public TextureRegion getframe(boolean pause){
	
		if (pause==true){
			// dont update animation
		}
		else if (pause==false){
			
			currentFrame = null;	

			stateTime += Gdx.graphics.getDeltaTime();  
			switch (state)
			{
				case Walking:
					currentFrame = Walk.getKeyFrame(stateTime, true);
					break;
				case flying:
					currentFrame = fly.getKeyFrame(stateTime, true);
					break;
				case Lose:
					currentFrame = Lose.getKeyFrame(stateTime, true);
					break;
			}
		
			bounds.set(position.x, position.y, Player.WIDTH, Player.HEIGHT);
		}
		
		return currentFrame;
	}
	
	public void ToolGrab(Color color){	
		
		this.color = color;
		EquipedToolCycle=1; // player tool cycle is set to grabbed 
		pe_grab  = Effects.setNewEffect(enumEffects.GrabTool, this.color);		
		pe_grab.start();		
		PlayGrabSound();
		PhoneDevice.Settings.playSound(PhoneDevice.Settings.SoundsEnum.GrabTool);
	}
	
	public void ToolEquiped(){
		
		pe_grab.reset();
		pe_equip  = Effects.setNewEffect(enumEffects.PlayerEquiped, this.color);		
		pe_equip.start();		
	}

	public Vector2 getPosition() {
		return position;
	}

	public Rectangle getBounds() {
		return bounds;
	}

	public boolean PlayerLost(){
		
		// change player animation to lose
		state = State.Lose;
		
		// bounce back		
		this.position.x = this.position.x - impact.x;				
		if (impact.x>0) impact.x=impact.x - Conf.IMAPCT_DEGRAGATION.x;
		else impact.x=0;
		
		// bounce up in y
		if (initialLosePosY==-1) initialLosePosY=this.position.y;
		else{				
			if (this.position.y<(initialLosePosY + Conf.IMAPCT_FLYING_UP_LIMIT)){
				this.position.y = this.position.y + Conf.IMAPCT_DEGRAGATION.y;
			}				
			else bounceUpDone=true;					
		}
		
		// fall down
		if (bounceUpDone){			
			if (this.position.y>-(40 + this.HEIGHT)){
				this.position.y = this.position.y - Conf.FALLING_DOWN;				
			}				
			else return true;
		}
		
		return false;		
	}
	
	public void Control() {
		// check if the screen was pressed: user wants the player to jump
		ACC_Y= Conf.ACC_FORCE;	
	}	
	
	public void  PlayerMovement()
	{
		this.position.y= this.position.y + (ACC_Y-Conf.GRAVITY);
		
		if (ACC_Y>0) ACC_Y = ACC_Y - Conf.ACC_DECREASED_RATE; // decrease speed by decrease acc in y
		else ACC_Y=0;
		
		// set Upper and Lower bounds: player cannot exceed those limits
		if (this.position.y<Conf.LOWER_BOUND) this.position.y=Conf.LOWER_BOUND;
		else if (this.position.y > Conf.UPPER_BOUND + Conf.PLAYER_OFFSET_Y) 
			this.position.y=Conf.UPPER_BOUND + Conf.PLAYER_OFFSET_Y;
		
		if (state != State.Lose){
			if (this.position.y <= Conf.LOWER_BOUND){
				state = State.Walking;
			}
			else{
				state = State.flying;
			}
		}
	}
}
