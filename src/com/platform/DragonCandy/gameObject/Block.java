package com.platform.DragonCandy.gameObject;

import java.util.ArrayList;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.Rectangle;
import com.platform.DragonCandy.General.Conf;
import com.platform.DragonCandy.General.PhoneDevice;
import com.platform.DragonCandy.General.PhoneDevice.Settings;
import com.platform.DragonCandy.General.PhoneDevice.Settings.SoundsEnum;
import com.platform.DragonCandy.gameObject.Effects.enumEffects;
import com.platform.DragonCandy.logic.LevelBuilder;
import com.platform.DragonCandy.logic.LevelBuilder.Color;
import com.platform.DragonCandy.logic.LevelBuilder.Move;
import com.platform.DragonCandy.logic.LevelBuilder.enumBlockType;

 		

//==============================BLOCK=========================//
public class Block {

	public enum tilePos{
		bottom,middle,top
	};
	
	public ArrayList<Tile> Wall = new ArrayList<Tile>();	
	
	private float[] acc = new float[1];
	private Move Dir=null;	
	public Color color=null;	
	private int[] MovingBounds = new int[1];
	public boolean Block_GameOverCondition = false;
	public int BlockState = 0;
	public ParticleEffect pe;
	public boolean Passed = false;
	
	public Block(float PosX, int[] blockVector, enumBlockType BlockType, Color blockcode,
			float[] acc, int[] MovingBounds, Move Dir){
		
		setColor(blockcode);
		
		for (int i=0;i<=blockVector.length-1;i++){
			if (blockVector[i]!=0){	
				
				Tile thisTile = null;
				
				if (blockVector[i]==1){
					 thisTile = new Tile(BlockType, tilePos.bottom);
				}
				if (blockVector[i]==2){
					 thisTile = new Tile(BlockType, tilePos.middle);
				}
				if (blockVector[i]==3){
					 thisTile = new Tile(BlockType, tilePos.top);
				}				
				
				float PosY = Conf.LOWER_BOUND + i * thisTile.Height;
				thisTile.setRectTile(PosX, PosY);
				Wall.add(thisTile);
			}
		}
		
		// set the 'movement' settings		
		SetMovementSettings(acc, MovingBounds, Dir);
		
	}
	
	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public void PlayCrashSound(){
		PhoneDevice.Settings.playSound(PhoneDevice.Settings.SoundsEnum.Crash);
	}
	public void SetMovementSettings(float[] acc, int[] MovingBounds,Move Dir){
		this.acc = acc;
		this.Dir=Dir;	
		this.MovingBounds = MovingBounds;
	}
	
	public void UpdateBlock(float overrideBlockDriftSpeed){		

			/* overrideBlockDriftSpeed:
			 * -----------------------
			 * -1 : act as normal: do not pay attention to overrideBlockDriftSpeed
			 * 0 : freeze : return.
			 * else: set speed to override speed
			 */
		
			
			if (overrideBlockDriftSpeed==0){ 
				return;
			}			
			else if (overrideBlockDriftSpeed==-1){
				// change nothing: act as level builder speed
			}
			else{
				// override speed
				if (acc[1]<0){
					// Change only in x (no acc in Y dir)
					acc[1] = overrideBlockDriftSpeed;
				}
				else{
					// change in both X and Y axis
					acc[0] = acc[1] = overrideBlockDriftSpeed;
				}
			}
				
			// Fixed block : No change in Y axis position
			if ((MovingBounds[0]<0) ||  (MovingBounds[1]<0) || (acc[1]<0) ){
				
				for (int i=0;i<=Wall.size()-1;i++){
							
					Rectangle thisTileRect = Wall.get(i).getRectTile();					
					Wall.get(i).setRectTile(thisTileRect.x - acc[0], thisTileRect.y);					
				}
				
			}
			// Moving block : Change in Y axis position
			else{
				
				// Y variables
				float Height = Wall.get(0).getRectTile().height;
				float End_Ypos =  Conf.LOWER_BOUND + MovingBounds[1] * Height;
				float Start_Ypos =  Conf.LOWER_BOUND + MovingBounds[0] * Height;		
				
				if (this.Dir==Move.UP){
					if (End_Ypos>=Wall.get(Wall.size()-1).getRectTile().y){
						
						for (int i=0;i<=Wall.size()-1;i++){							

							Rectangle thisTileRect = Wall.get(i).getRectTile();					
							Wall.get(i).setRectTile(thisTileRect.x - acc[0], 
									thisTileRect.y+ acc[1]);								
						}
											
					}
					else{
						this.Dir=Move.DOWN;		
						return;
					}
				}
				
				if (this.Dir==Move.DOWN){
					if (Wall.get(0).getRectTile().y>Start_Ypos){
						
						for (int i=0;i<=Wall.size()-1;i++){							
							Rectangle thisTileRect = Wall.get(i).getRectTile();					
							Wall.get(i).setRectTile(thisTileRect.x - acc[0], 
									thisTileRect.y - acc[1]);	
						}							
					}
					else{
						this.Dir=Move.UP;
						return;
					}
				}
				
			}
			
			
	}
	
	public void Destroy(){		
		
		pe = null;
		acc = null;
		color=null;
		MovingBounds = null;
		
		int len = Wall.size()-1;
		for (int j=len;j>=0;j--){
			Tile tile = Wall.get(j);
			Wall.remove(j);			
			tile.Destroy();
			tile = null;
		}		
	}
	
//==============================BLOCK=========================//

	
	
	
//==============================TILE=========================//
	public class Tile{
	
		public ParticleEffect pe;
		public Rectangle rectTile = new Rectangle();
		public int state;
		private Texture BlockImg;
		private tilePos thisTilePos;
		public enumBlockType BlockType;
		public float Width;
		public float Height;		
		public Sound CrashSound;
		public boolean crashed = false;
		
		public Tile(enumBlockType BlockType, tilePos tilepos){
			
			this.BlockType = BlockType;
			thisTilePos = tilepos;
			state = 0;
			SetBlock(BlockType);
			
		}
				
		public void ReleaseTileEffect(){
			this.pe.reset();
			this.pe.dispose();	
		}
		
		public Rectangle getRectTile() {
			return rectTile;
		}


		public void setRectTile(float PosX, float PosY) {
			rectTile.set(PosX,PosY,Width,Height);			
		}


		public void SetBlock(enumBlockType BlockType){
			
			setBlockImg(BlockType);
			
			switch (BlockType){
			case brick:			
				Width = TextureAtlas.BrickTexture.WIDTH;
				Height = TextureAtlas.BrickTexture.HEIGHT;
				break;
				
			default:
				break;			
			}
		}
		
		public void PlayBreakThoughSound(){
			PhoneDevice.Settings.playSound(PhoneDevice.Settings.SoundsEnum.BrickBreakThough);							
		}
		
		public Texture getBlockImg() {
			return BlockImg;
		}
		
		public void setBlockImg(enumBlockType BlockType) {
			
			Color c = getColor();
			
			
			//Upon creation of tile
			switch (BlockType){
			case brick:
				BlockImg = TextureAtlas.BrickTexture.getBrick(c, thisTilePos);				
				break;
				
			default:
				break;			
			}
			
		}
		
		public void setBlockImg(boolean Crashed) {
			
			//Upon destruction of tile
			setBlockImg(this.BlockType);
			
		}
		
		public void Destroy(){	
			pe = null;
			rectTile = null;
			BlockImg.dispose();
			BlockType = null;
			CrashSound = null;			
		}

	}
	
	//==============================TILE=========================//
	
}

