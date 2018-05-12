package com.platform.DragonCandy.logic;

import java.util.Random;

import android.os.SystemClock;
import android.util.Log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.platform.DragonCandy.General.Conf;
import com.platform.DragonCandy.Interface.ScreenManagerInterface;
import com.platform.DragonCandy.Interface.ThreadInterface;
import com.platform.DragonCandy.Screens.ScreenGameplay;
import com.platform.DragonCandy.gameObject.Block;
import com.platform.DragonCandy.gameObject.CoinPattern;
import com.platform.DragonCandy.gameObject.PostSign;
import com.platform.DragonCandy.gameObject.Tool;
import com.platform.DragonCandy.gameObject.CoinPattern.CoinData;
import com.platform.DragonCandy.logic.LevelBuilder.Color;
import com.platform.DragonCandy.logic.LevelBuilder.Move;
import com.platform.DragonCandy.logic.LevelBuilder.enumBlockType;
import com.platform.DragonCandy.logic.LevelBuilder.enumCoinPattern;
import com.platform.DragonCandy.logic.LevelBuilder.object;



public class ObjectThread extends Thread {
	
	// objectThread variables	
	public volatile Block _threadBlock;
	public volatile CoinPattern _threadCoinPattern;	
	public volatile Tool _threadTool;
	public volatile PostSign _threadPostSign;
	
	private float _objSpeed;
	private object obj;
	private  ThreadInterface threadListener;
	private LevelBuilder levelbuilder;
	
	// object Random Variables 
	private static Random rand = new Random();
	
	// blocks
	private volatile Color Block_Color;
	private volatile float Block_accY;
	private volatile Move Block_MoveDir;
	private volatile int Block_MovingBounds_Low;
	private volatile int Block_MovingBounds_High;
	private volatile String block_partA;	
	private volatile String block_partB;
	
	// coins : public variables due to avoid collision with
	// the next generated item. Need to be read by levelbuilder.
	private volatile int Coin_startPosY;	
	public volatile int Coin_Num;
	public volatile enumCoinPattern coinPatternType;
	
	// tool
	private volatile Color Tool_Color;
	private volatile float Tool_accY;
	private volatile Move Tool_MoveDir;
	private volatile int Tool_MovingBounds_Low;
	private volatile int Tool_MovingBounds_High;
	private volatile float Tool_startPosY;	
	
	
	public ObjectThread(float objSpeed, object obj, ThreadInterface ThreadListener, LevelBuilder levelbuilder) {
		// TODO Auto-generated constructor stub	
		threadListener = ThreadListener;
		_objSpeed = objSpeed;
		this.levelbuilder  = levelbuilder;
		Initialize();
		Generate(obj);			
		//this.start();
	}
	
	
	@Override
    public void run() {		
		// run the thread object while it is still "on screen"
		do{			
			SystemClock.sleep(20);
			//UpdateObjectSpeed();
		}while (!(IsPassedScreen(getObjRect())));
		
		// NOTIFY LVL builder thread was done. so it can be removed
		levelbuilder.OnThreadDone();
		Log.i("ObjectThread", "objThrd is done");		
	}
	

	// ======================================================================================//			
	public Block get_threadBlock() {
		return _threadBlock;
	}

	public CoinPattern get_threadCoinPattern() {
		return _threadCoinPattern;
	}
 
	public Tool get_threadTool() {
		return _threadTool;
	}
    
	public PostSign get_threadPostSign(){
		return _threadPostSign;
	}
	
	public object getTypeObj() {
		return obj;
	}
	// ======================================================================================//		
	public void Generate(object objectType){		
		
		obj = objectType;
		generateVariables(obj);
		
		switch (obj){
		
		case postsign:
			_threadPostSign = new PostSign(new Vector2().add(Conf.EDGE_OF_SCREEN - 100,
					Conf.LOWER_BOUND),
					new float[] {Conf.SCREEN_REGULAR_COLLISION_LAYER_SCROLL_SPEED,-1});
			break;
			
		case block:
				
			// (Block_MovingBounds_High -Block_MovingBounds_Low) must be > block_parts (block_partA + block_partB)
			Log.i("blockGenerate", "Color: " + String.valueOf(Block_Color) +
					" ,accY: " + String.valueOf(Block_accY) +
					" ,Dir: " + String.valueOf(Block_MoveDir) +
					" ,block_partA: " + String.valueOf(block_partA) +
					" ,block_partB: " + String.valueOf(block_partB) +
					" ,Block_MovingBounds_Low: " + String.valueOf(Block_MovingBounds_Low) +
					" ,Block_MovingBounds_High: " + String.valueOf(Block_MovingBounds_High));
			
				
				_threadBlock = new Block(Conf.EDGE_OF_SCREEN + 25, SetBlockVector(block_partA,block_partB),
				enumBlockType.brick, Block_Color, 
				new float[] {_objSpeed, Block_accY},
				new int[] {Block_MovingBounds_Low,Block_MovingBounds_High},
				Block_MoveDir);			
				
			break;
			
		case coin:
			Log.i("coinGenerate", "starting Pos: " + String.valueOf(Coin_startPosY) +
				  " ,coin number: " + String.valueOf(Coin_Num));
				
				_threadCoinPattern = new CoinPattern(Coin_Num, coinPatternType, 
						(int) Conf.EDGE_OF_SCREEN+25,Coin_startPosY); 				
			break;
			
		case tool:	
			Log.i("toolGenerate", "Color: " + String.valueOf(Tool_Color) +
					" ,accY: " + String.valueOf(Tool_accY) +
					" ,Dir: " + String.valueOf(Tool_MoveDir) +
					" ,Tool_startPosY:"  + String.valueOf(Tool_startPosY) + 
					" ,Tool_MovingBounds_Low: " + String.valueOf(Tool_MovingBounds_Low) +
					" ,Tool_MovingBounds_High: " + String.valueOf(Tool_MovingBounds_High));
			
			//  generate Tools
			_threadTool = new Tool(new Vector2().add(Conf.EDGE_OF_SCREEN+25,Tool_startPosY),
		    		Tool_Color, 
		    		new float[] {_objSpeed ,Tool_accY},
					new int[] {Tool_MovingBounds_Low,Tool_MovingBounds_High}, Tool_MoveDir);		    
			
			break;
			
		default:
			break;
		
		}
		
	}
	// ======================================================================================//		
	public void Initialize(){
		_threadBlock = null;
		_threadCoinPattern = null;
		_threadTool = null;
		_threadPostSign = null;
	}
	
	// ======================================================================================//	
	private int[] SetBlockVector(String lowWall, String highWall){
		
		// block boundries : 0 - 12
		
		int startIndex_low = 0;
		int endIndex_low = 0;
		int startIndex_high = 0;
		int endIndex_high = 0;
		
		String lowBoundArray[] = lowWall.split("-");
	    startIndex_low = Integer.parseInt(lowBoundArray[0]);
	    endIndex_low = Integer.parseInt(lowBoundArray[1]);
	    
	    int[] tmpBlockVector = new int[13];
	    resetBlockVector(tmpBlockVector);
	    
	    if (highWall.trim().equals("n/a")){
	    	for (int i=0;i<=tmpBlockVector.length-1;i++){	    	
	    		 if (i>= startIndex_low && i<=endIndex_low){
	    			 tmpBlockVector[i] = getBlockVectorVal(startIndex_low,endIndex_low, i);	 
	    		 }	    		    		 
	    	}
	    }
	    
	    else if (!(highWall.trim().equals("n/a"))){
	    	// if there is a highBoundArray
	    	String highBoundArray[] = highWall.split("-");
	    	startIndex_high = Integer.parseInt(highBoundArray[0]);
		    endIndex_high = Integer.parseInt(highBoundArray[1]);
		    
		    for (int i=0;i<=tmpBlockVector.length-1;i++){
		    	 
	    		 if (i>= startIndex_low && i<=endIndex_low){
	    			 tmpBlockVector[i] = getBlockVectorVal(startIndex_low,endIndex_low, i);
	    		 }
	    		 else if (i>= startIndex_high && i<=endIndex_high){
	    			 tmpBlockVector[i] = getBlockVectorVal(startIndex_high,endIndex_high, i);	
	    		}
	    	}	    	
	    }	    
	    return tmpBlockVector;
	    
	}
	private int getBlockVectorVal(int low, int high, int i) {

		int blockVectorVal = 0;
		
		if (i == low)
			blockVectorVal = 1; 
		 
		else if (i> low && i< high)
			 blockVectorVal = 2;
		 		 
		else if (i == high)
			 blockVectorVal = 3;
		
		return blockVectorVal;
		 
	}


	//======================================================================================//			
	private void resetBlockVector(int[] tmpBlockVector){		
		for (int i=0;i<=tmpBlockVector.length-1;i++){
			tmpBlockVector[i]=0;
		}		
	}
	//======================================================================================//
	public void UpdateObjectSpeed(boolean pause){
	// called constantly from reneder
	
		float OverrideObjectsSpeed=0;
		switch (ScreenGameplay.ThisGameRound){
		case EndRound:
			// override the speed which was set by the level builder to drift speed.
			OverrideObjectsSpeed = _objSpeed / Conf.SCREEN_DRIFT_FACTOR;
			break;
		case GameOver:
			// freeze: dont call this method at all
			OverrideObjectsSpeed = 0;
			break;
		case Running:
			// dont set override speed if not pause: let the update methods use 
			// the speed level builder set to it.
			if (pause==false){
				OverrideObjectsSpeed = -1;	
			}		
			else if (pause==true){
				OverrideObjectsSpeed = 0;				
			}
			break;
		default:
			break;
		 
		}
		
		
		
		switch (obj){
		case block:
			_threadBlock.UpdateBlock(OverrideObjectsSpeed);	
			break;
			
		case coin:
			_threadCoinPattern.setCoins(_objSpeed, OverrideObjectsSpeed);
			break;
			
		case tool:
			_threadTool.Update(OverrideObjectsSpeed);
			break;
		
		case postsign:
			_threadPostSign.Update(OverrideObjectsSpeed);
		default:
			break;				
		}			
	}	 
	
	//======================================================================================//			 
	public Rectangle getObjRect(){
		
		if (obj==object.coin){
			int LastIndexCoin = _threadCoinPattern.getCoins().size()-1;
    		CoinData LastCoinOfPattern = _threadCoinPattern
    				.getCoins().get(LastIndexCoin);		    		    		
    		return LastCoinOfPattern.location;
		
		}
		else if (obj==object.block){
			return _threadBlock.Wall.get(_threadBlock.Wall.size()-1)
					.getRectTile();			
		}
		else if (obj==object.tool){
			return _threadTool.RectTool;		
		}
		else if (obj==object.postsign){
			return _threadPostSign.SignPostRect;	
		}
		// space
		else {
			return null;
		}
		
	}
	
	//======================================================================================//
	private boolean IsPassedScreen(Rectangle rectObj){		
		if (rectObj.x + rectObj.width < Conf.END_SCREEN) return true;
		else return false;		
	}	
	//******************************* generateVariables ************************************//
	
	private void generateVariables(object obj){
		
		switch (obj){
		case block:
			blockRandParam();	
			 break;
			 
		case coin:
			coinRandParam();
			break;
		case space:
			break;
		case tool:
			toolRandParam();
			break;
		default:
			break;
			
		}
	}
	//======================================================================================//		
	private void toolRandParam(){
		
		// set  block color
		toolRndColor(); 

		// starting pos
		toolStartingPos();
		
		// set block boundries
		toolRndBoundries(); 
		
		// set tool direction
		toolRandMoveDir();
		
		// set Tool accY
		toolRandAccY();
		
	}
	//======================================================================================//		
	private void toolStartingPos()
	{		
		 Tool_startPosY = (float) Conf.randInt(1, 5);
	}
	//======================================================================================//	
	private void toolRndBoundries(){
		
		int length = 0;
		float r = rand.nextFloat();				
		boolean bCoverLittleGround = (Conf.IsRange(r,0.90f,1f));
		 if (bCoverLittleGround==true){
			 length = Conf.randInt(0, 1);				
		 }
		 else if  (bCoverLittleGround==false){
			 length = Conf.randInt(2, 3);
		 }
		 		
		Tool_MovingBounds_Low= (int)Tool_startPosY;
		Tool_MovingBounds_High= Tool_MovingBounds_Low + length;				
	}
	//======================================================================================//
	private void toolRndColor(){
		 // set block color 
		 float r = rand.nextFloat();			 
		 if (Conf.IsRange(r,0.f,0.75f)){
			 Tool_Color = Color.RED;
		 }
		 else if (Conf.IsRange(r,0.75f,0.875f)){
			 Tool_Color = Color.BLUE;
		 }
		 else if (Conf.IsRange(r,0.875f,1.0f)){
			 Tool_Color = Color.GREEN;
		 }
	}
	//======================================================================================//		
	private void toolRandMoveDir(){
		 // set move block in y dir (up,down,none)		
		 
		 int upOrDownFlag = Conf.randInt(0,1);
		 if ((upOrDownFlag % 2)==0){
			 Tool_MoveDir = Move.UP;
		 }
		 else{
			 Tool_MoveDir = Move.DOWN;
		 }		 		 
	}	 	
	//======================================================================================//
	private void toolRandAccY(){		
				
		// set block accY 
		float r = rand.nextFloat();		
		boolean IsFixedFlag = Conf.IsRange(r,0.0f,0.55f);		
		 if (IsFixedFlag){
			 Tool_accY=0f;			 
			 return;
		 }
		
		 // if there is a long ground to cover: move slowly
	
		 int length =Tool_MovingBounds_High - Tool_MovingBounds_Low;		 
		 if (length==0){
			 Tool_accY=0f;			 
			 return;
		 }
		 else {
			 Tool_accY = Conf.SCREEN_REGULAR_COLLISION_LAYER_SCROLL_SPEED
					 * r;
		 }
		 
	}
	//======================================================================================//	
	private void coinRandParam(){
		// last possible starting position is 6
		Coin_startPosY = Conf.randInt(1, 6);
		
		float r = rand.nextFloat();
		
		boolean CoinPattern_IsColumn = Conf.IsRange(r, 0.0f, 0.55f);
		if (CoinPattern_IsColumn==true){
			coinPatternType = enumCoinPattern.Column;
			Coin_Num = Conf.randInt(2, 8 - Coin_startPosY);
		}
		else if (CoinPattern_IsColumn==false){
			coinPatternType = enumCoinPattern.Line;
			Coin_Num = Conf.randInt(2, 4);
		}
	}
	//======================================================================================//	
	private void blockRandParam(){
		
		
		boolean createBlockWithTwoParts;
		
		// if this is a warm up: only 1 part blocks
		if (levelbuilder.warmUp==true){
			createBlockWithTwoParts = true;
		}
		else{
			
			float r = rand.nextFloat();		
			createBlockWithTwoParts = 
					Conf.IsRange(r,0.0f,0.55f) ? true : false;					
		}
		
		// set  block color
		blockRndColor(); 
		
		// set block boundries
		blockRndBoundries(createBlockWithTwoParts); 
		
		// set block direction
		blockRandMoveDir(createBlockWithTwoParts);
		
		if (createBlockWithTwoParts==false){
			
			// one part		
			
			int startFromBlockIndex;
			
			int lengthBlock=Conf.randInt(Conf.ONE_PART_LENGTH_MIN + levelbuilder._LengthOffsetOneParts,
					Conf.ONE_PART_LENGTH_MAX);
			
			if (lengthBlock==Conf.ONE_PART_LENGTH_MAX){
				startFromBlockIndex = Conf.ONE_PART_START_BLOCK_INDEX_MIN;						
			}
			else{
				startFromBlockIndex = Conf.randInt(Conf.ONE_PART_START_BLOCK_INDEX_MIN,
						Conf.ONE_PART_START_BLOCK_INDEX_MAX);
			}
			
			block_partA = String.valueOf(startFromBlockIndex) + "-" 
					+ String.valueOf(startFromBlockIndex+lengthBlock - 1);					
			block_partB="n/a";
			
		}
		else if (createBlockWithTwoParts==true){
			
			
			// two parts		
			int lengthSpace = Conf.randInt(Conf.TWO_PART_LENGTH_MIN,
					Conf.TWO_PART_LENGTH_MAX - levelbuilder._LengthOffsetTwoParts);
			
			int startSpace;
			
			if (lengthSpace==Conf.TWO_PART_LENGTH_MAX){
				startSpace = Conf.TWO_PART_START_BLOCK_INDEX_MIN;
			}			
			else{
				startSpace = Conf.randInt(Conf.TWO_PART_START_BLOCK_INDEX_MIN,
						Conf.TWO_PART_START_BLOCK_INDEX_MAX);
			}
			
			int spaceCounter = lengthSpace;
			boolean trig = false;
			String sStopPartA = String.valueOf(Conf.TWO_PART_START_BLOCK_INDEX_MIN);
			String sStartPartB = String.valueOf(Conf.MAX_BLOCK_ON_SCREEN_INDEX);		
			
			for (int i=0;i<=Conf.MAX_BLOCK_ON_SCREEN_INDEX;i++){
				
				if (!trig){
					if (i==startSpace){
						sStopPartA = String.valueOf(i);		
						trig = true;
						continue;
					}
				}
				
				if (trig){
					if (spaceCounter==0){
						sStartPartB = String.valueOf(i);	
					}
					
					spaceCounter--;
										
				}			
			}

			block_partA = String.valueOf(0) + "-" 
					+ sStopPartA;					
			
			block_partB = sStartPartB + "-" 
					+   String.valueOf(8);							
						
		}
		
		// set block AccY
		blockRandAccY(createBlockWithTwoParts);
		
	}
	//======================================================================================//	
	private void blockRndBoundries(boolean createBlockWithTwoParts){

		// 10 blocks in screen in total
		
		if (createBlockWithTwoParts==false){
			Block_MovingBounds_Low=0;
			Block_MovingBounds_High=Conf.MAX_BLOCK_ON_SCREEN_INDEX;	
		}
		else if (createBlockWithTwoParts==true){
			Block_MovingBounds_Low = -1;
			Block_MovingBounds_High = -1;					
		}		
	}
	//======================================================================================//
	private void blockRndColor(){
		 // set block color 
		 float r = rand.nextFloat();			 
		 if (Conf.IsRange(r,0.f,0.33f)){
			 Block_Color = Color.RED;
		 }
		 else if (Conf.IsRange(r,0.33f,0.66f)){
			 Block_Color = Color.GREEN;
		 }
		 else if (Conf.IsRange(r,0.66f,1.0f)){
			 Block_Color = Color.BLUE;
		 }
	}
	//======================================================================================//
	private void blockRandAccY(boolean createBlockWithTwoParts){		
				
		// set block accY 
		float r = rand.nextFloat();		
		boolean IsFixedFlag = Conf.IsRange(r,0.0f,0.1f);		
		 if (IsFixedFlag){
			 Block_accY=0f;			 
			 return;
		 }
		 if (createBlockWithTwoParts==true){
			 Block_accY=-1f;
			 return;
		 }
		 
		 // if length block is big : move slower
		 String[] splitPart = block_partA.split("-");
		 int length =Integer.parseInt(splitPart[1]) - Integer.parseInt(splitPart[0]);
		 if (length>=Conf.TWO_PART_LENGTH_MAX - 2){
			 float slower =0f;
			 Block_accY = Conf.SCREEN_REGULAR_COLLISION_LAYER_SCROLL_SPEED
					 * (slower + 0.1f);		
		 }
		 else{
			//boolean slowerFaster = Conf.IsRange(r,0.0f,0.65f);
			//if (slowerFaster==true) relativeSpeed = 0.1f;
			//else if (slowerFaster==false) relativeSpeed = 0.3f;
			float relativeSpeed=0.1f * Conf.randInt(0, 5);			
			Block_accY = Conf.SCREEN_REGULAR_COLLISION_LAYER_SCROLL_SPEED
					* (relativeSpeed);		
		 }
		 		 
	}
	//======================================================================================//
	private void blockRandMoveDir(boolean createBlockWithTwoParts){
		 // set move block in y dir (up,down,none)		
		 if (createBlockWithTwoParts==true){
			 Block_MoveDir = Move.NONE;
			 return;
		 }
		 else if (Block_accY==-1f){
			 Block_MoveDir = Move.NONE;
			 return;
		 }
		 
		 float r = rand.nextFloat();	
		 boolean IsDown = Conf.IsRange(r,0.0f,0.5f);		
		 if (IsDown){
			 Block_MoveDir = Move.DOWN;
		 }
		 else{
			 Block_MoveDir = Move.UP;
		 }		 
		 
	}	 	
	//******************************* generateVariables ************************************//

}
