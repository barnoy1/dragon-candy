package com.platform.DragonCandy.gameObject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.platform.DragonCandy.General.Conf;
import com.platform.DragonCandy.gameObject.Effects.enumEffects;
import com.platform.DragonCandy.gameObject.TextureAtlas.ToolTexture;
import com.platform.DragonCandy.logic.LevelBuilder;
import com.platform.DragonCandy.logic.LevelBuilder.Color;
import com.platform.DragonCandy.logic.LevelBuilder.Move;

public class Tool {

	public Rectangle RectTool;	
	
	private Texture TextureTool;
	
	public ParticleEffect pe;
	public ParticleEffect pe_grab;
	public int toolCycle=0;
	private boolean effectStart;	
	public boolean Grab;
	
	public Color color;
	private float[] acc = new float[1];
	private Move Dir=null;
	private int[] MovingBounds = new int[1];
	
	public Tool(Vector2 location, Color color, float[] acc, int[] movingBounds, Move Dir){
		
		location.y = PosToCord(location.y);
		
		RectTool = new Rectangle().set(location.x, location.y, Conf.TOOL_WIDTH, Conf.TOOL_HIEGHT);
		pe =  Effects.setNewEffect(enumEffects.ToolFloat, color);	
		toolCycle =0;
					
		this.color = color;
		
		SetMovementSettings(acc, movingBounds, Dir);
		TextureTool = ToolTexture.getTool(color);
		pe.start();			 
	}
	
	private void SetMovementSettings(float[] acc, int[] movingBounds,
			Move dir) {
		// TODO Auto-generated method stub
		this.acc = acc;		
		this.MovingBounds = movingBounds;
		this.Dir = dir;
		
	}

	public Texture getTextureTool() {
		return TextureTool;
	}
	
	private float PosToCord(float y){		
		return y *  (Conf.SCREEN_BACKGROUND_GRASS_HEIGHT +
				Conf.TOOL_HIEGHT/4);
	}
	public void Update(float overrideBlockDriftSpeed){
		
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
			
			RectTool.set(RectTool.x - acc[0], RectTool.y,
					RectTool.getWidth(), RectTool.getHeight());	
		}
		
		// Moving block : Change in Y axis position
		else{
			
			// Y variables
			float End_Ypos =  Conf.LOWER_BOUND + MovingBounds[1] * RectTool.height;
			float Start_Ypos =  Conf.LOWER_BOUND + MovingBounds[0] * RectTool.height;		
			
			if (this.Dir==Move.UP){
				if (End_Ypos>=RectTool.y){
					RectTool.set(RectTool.x - acc[0], RectTool.y + acc[1],
							RectTool.width, RectTool.height);					
				}
				else{
					this.Dir=Move.DOWN;		
					return;
				}
			}
			
			if (this.Dir==Move.DOWN){
				if (RectTool.y>Start_Ypos){
					
					RectTool.set(RectTool.x - acc[0], RectTool.y - acc[1],
							RectTool.width, RectTool.height);							
				}
				else{
					this.Dir=Move.UP;
					return;
				}
			}
			
		}			
	}
	
	public static class Hammer{
		
		private static Texture TextureHammer_RED = new Texture(Gdx.files.internal("data/object/Tool/tool-hammer-red.png"));
		private static Texture TextureHammer_BLUE = new Texture(Gdx.files.internal("data/object/Tool/tool-hammer-blue.png"));
		private static Texture TextureHammer_GREEN = new Texture(Gdx.files.internal("data/object/Tool/tool-hammer-green.png"));

	}
		
}
