package com.platform.DragonCandy.gameObject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.platform.DragonCandy.logic.LevelBuilder;
import com.platform.DragonCandy.logic.LevelBuilder.Color;

public class Effects {

	private static ParticleEffect pe;
	// effects
	public enum enumEffects{
		CoinCollection,TileDistruction, GrabTool, ToolFloat, PlayerEquiped, Crash;
	}	
		
	public static ParticleEffect setNewEffect(enumEffects effectType, Color color){

		switch(effectType){
		case CoinCollection: // coin collection effect
			pe = new ParticleEffect();
			pe.load(Gdx.files.internal("data/object/Coin/coin_collected.p"),Gdx.files.internal("data/effects"));
			break;
		
		case Crash:
			pe = new ParticleEffect();
			pe.load(Gdx.files.internal("data/object/block/effects/tile-crash.p"),Gdx.files.internal("data/effects"));
			break;
			
		case TileDistruction: // Distruction of tile
			
			pe = new ParticleEffect();
			switch(color)
			{			
			case BLUE:
				pe.load(Gdx.files.internal("data/object/block/effects/tile-destruction-blue.p"),Gdx.files.internal("data/effects"));				
				break;			
			case GREEN:
				pe.load(Gdx.files.internal("data/object/block/effects/tile-destruction-green.p"),Gdx.files.internal("data/effects"));				
				break;			
			case RED:
				pe.load(Gdx.files.internal("data/object/block/effects/tile-destruction-red.p"),Gdx.files.internal("data/effects"));				
				break;		
								
			default:
				break;			
			}			
			break;
			
		case GrabTool: // Equipment of tool
			
			pe = new ParticleEffect();
			switch(color)
			{
			
			case BLUE:
				pe.load(Gdx.files.internal("data/object/Tool/effects/grap-tool-blue.p"),Gdx.files.internal("data/effects"));				
				break;			
			case GREEN:
				pe.load(Gdx.files.internal("data/object/Tool/effects/grap-tool-green.p"),Gdx.files.internal("data/effects"));				
				break;			
			case RED:
				pe.load(Gdx.files.internal("data/object/Tool/effects/grap-tool-red.p"),Gdx.files.internal("data/effects"));				
				break;							
			default:
				break;
			
			}
			break;
			
		case ToolFloat: // floating tool
			pe = new ParticleEffect();			
			switch(color)
			{
			case BLUE:
				pe.load(Gdx.files.internal("data/object/Tool/effects/float-tool-blue.p"),Gdx.files.internal("data/effects"));				
				break;			
			case GREEN:
				pe.load(Gdx.files.internal("data/object/Tool/effects/float-tool-green.p"),Gdx.files.internal("data/effects"));				
				break;			
			case RED:
				pe.load(Gdx.files.internal("data/object/Tool/effects/float-tool-red.p"),Gdx.files.internal("data/effects"));				
				break;							
			default:
				break;			
			}
			break;
		case PlayerEquiped:
			
			pe = new ParticleEffect();
			switch(color)
			{
			
			case BLUE:
				pe.load(Gdx.files.internal("data/player/effects/equip-tool-blue.p"),Gdx.files.internal("data/effects"));				
				break;			
			case GREEN:
				pe.load(Gdx.files.internal("data/player/effects/equip-tool-green.p"),Gdx.files.internal("data/effects"));				
				break;			
			case RED:
				pe.load(Gdx.files.internal("data/player/effects/equip-tool-red.p"),Gdx.files.internal("data/effects"));				
				break;							
			default:
				break;			
			}
			break;
		}
		
		return pe;
	}
	
	public static ParticleEffect setNewEffect(enumEffects effectType){
		return  setNewEffect(effectType, Color.NONE);
	}
	
}
