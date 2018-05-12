package com.platform.DragonCandy.General;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class Conf {

	// screen
	public static final float WIDTH = Gdx.graphics.getWidth();
	public static final float HEIGHT = Gdx.graphics.getHeight();
	public  static final float END_SCREEN = -50f;
	
	public static final int SPACE_COUNTER_BLOCK_MAX = 3;
	public static final int SPACE_COUNTER_BLOCK_MIN = 1;
	
	
	// Background
	public static final float SCREEN_BACKGROUND_WIDTH = Gdx.graphics.getWidth()-1;
	public static final float SCREEN_BACKGROUND_GRASS_WIDTH = Gdx.graphics.getWidth() * 1.442f -1; // 2769 -1	
	public static final float SCREEN_BACKGROUND_GRASS_HEIGHT = Conf.HEIGHT/ 10.158f;	
	
	public static final float SCREEN_REGULAR_COLLISION_LAYER_SCROLL_SPEED = 12.5f;
	public static final float SCREEN_REGULAR_BACKGROUND_SCROLL_SPEED = 1.0f;
	public static final float SCREEN_DRIFT_FACTOR = 2;

	// player	
	public static final float PLAYER_WIDTH = Conf.WIDTH / 5.7330546f;
	public static final float PLAYER_HEIGHT = Conf.HEIGHT / 4.6371837f;	
	public static final float PLAYER_OFFSET_X = 9f;
	public static final float PLAYER_OFFSET_Y = 100f;
	public static final float GRAVITY = 6.8f;
	public static final float ACC_DECREASED_RATE = 2.2f; //0.8f;
	public static final float ACC_FORCE = 25.2f; //16f;
	public static final float LOWER_BOUND = Conf.HEIGHT / 54f; 
	public static final float UPPER_BOUND = Conf.HEIGHT - PLAYER_HEIGHT;
	public static final Vector2 IMPACT_VECTOR_INITAL_VALUES = new Vector2().add(9f, 10f);
	public static final Vector2 IMAPCT_DEGRAGATION = new Vector2().add(1.5f, 6.5f);
	public static final float IMAPCT_FLYING_UP_LIMIT = 5f;
	public static final float FALLING_DOWN = 40.5f;
	
	
	// coins	
	public static final float COIN_WIDTH = Conf.WIDTH / 17.85f;
	public static final float COIN_HIEGHT =  Conf.HEIGHT / 11.46f;
	public static final int[] COIN_SPACE_SMALL = {(int) (COIN_WIDTH * 1.4f),
		(int) (COIN_HIEGHT * 1.4f)};
	
	
	// blocks texture
	public static final float BLOCK_UPPER_BOUND = Conf.HEIGHT / 0.96f; //0.96f;
	public static final float EDGE_OF_SCREEN = Gdx.graphics.getWidth();
	public static final float BLOCK_BOX_SIZE = Gdx.graphics.getHeight() / 9f; //120; //
	
	// object thread data
	public static final int MAX_BLOCK_ON_SCREEN_INDEX = 8; // 0 .. 8
	public static final int TWO_PART_START_BLOCK_INDEX_MIN = 1;
	public static final int TWO_PART_START_BLOCK_INDEX_MAX = 4;
	public static final int TWO_PART_LENGTH_MIN = 3;
	public static final int TWO_PART_LENGTH_MAX = 5;
	
	public static final int ONE_PART_START_BLOCK_INDEX_MIN = 0;
	public static final int ONE_PART_START_BLOCK_INDEX_MAX = 3;
	public static final int ONE_PART_LENGTH_MIN = 2;
	public static final int ONE_PART_LENGTH_MAX = 4;
	
	// tool
	public static final int TOOL_TIME_USAGE_LIMIT = 8;
	public static final int TOOL_TIME_USAGE_MUSIC_LIMIT = 4;
	public static final float TOOL_WIDTH =  Conf.WIDTH / 17.85f;
	public static final float TOOL_HIEGHT = Conf.HEIGHT / 11.46f;
			
	// post sign
	public static final float POST_SIGN_WIDTH =  (float) Math.floor(Conf.WIDTH / 5.4857f);
	public static final float POST_SIGN_HIEGHT = (float) Math.floor(Conf.HEIGHT / 4.32f);
		
	// HUD
	public static final float BITMAP_SCALE_SMALL = 1.65f;
	public static final float BITMAP_SCALE_LARGE = 1.5f;
	public static final float BITMAP_TITLE_Y = Conf.HEIGHT/ 1.05f;
	public static final float SMALL_SPACE = Conf.WIDTH/10f;
	public static final float VERY_SMALL_SPACE = Conf.WIDTH/25f;
	
	// PAUSE window
	public static final float WINDOW_PAUSE_WIDTH = (float) (Gdx.graphics.getWidth() / 1.5);
	public static final float WINDOW_PAUSE_HEIGHT = Gdx.graphics.getHeight() / 2;
	public static final float WINDOW_GROW_WIDTH = 20f;
	public static final float WINDOW_GROW_HEIGHT = 20f;
	
	// effects
	public static long SYSTIME_MS = 500;	
	
	//pause Range
	public static final float PAUSE_RANGE_WIDTH = 100;
	public static final float PAUSE_RANGE_HEIGHT = 100; 
	
	public static final int EndRoundTimeOut = 10;
	// ===================Global Mathods======================= //	
	public static boolean IsRange(float r, float min, float max){			
		if ((max >= r) && (r >= min)) return true;
		else return false;
	}
	// ======================================================= //	 
	public static int randInt(int min, int max) {
    
    // nextInt is normally exclusive of the top value,
    // so add 1 to make it inclusive
	 Random rand = new Random();
    int randomNum = rand.nextInt((max - min) + 1) + min;
    
    return randomNum;
	}	
	// ======================================================= //	 
	public static boolean isBetween(int x, int lower, int upper) {
		  return lower <= x && x <= upper;
	}
}

