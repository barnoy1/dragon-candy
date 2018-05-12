package com.platform.DragonCandy.logic;

import java.util.LinkedList;
import java.util.Random;

import android.util.Log;

import com.platform.DragonCandy.General.Conf;
import com.platform.DragonCandy.Interface.ThreadInterface;
import com.platform.DragonCandy.Screens.ScreenGameplay;

public class LevelBuilder implements ThreadInterface {
	
	// ================================ LEVEL BUILDER VARIABLE =========================//
	
	public volatile float _ObjectsSpeed;	
	public float BackGroundSpeed;	
	public volatile double _Distance = 0.1;
	public volatile double _GenerationObjectInterval=0.3f; //5
	public volatile int _LengthOffsetOneParts = 0;
	public volatile int _LengthOffsetTwoParts = 0;
	
	private static Random rand = new Random();
	
	// difficulty
	private boolean setNewSpeed = false;
	private boolean setNewTarget = false;
	public volatile long _CheckPointDistanceInterval = 5;
	
	private float blockGenerateRange;
	private float cointGenerateRange;
	private float toolGenerateRange;
	private int spaceMinGenerateBlock;
	private int spaceMaxGenerateBlock;
	private int spaceCounter = 0;
	
	// limit
	private boolean speedLimit = false;
	public boolean warmUp = true;
	private int stageDiff = 0; //easy
	
	// score
	public int gamePoints;	
	
	// post sign
	private static boolean newRound = true;
	
	
	// ================================ LEVEL BUILDER OBJECTS =========================//
	
	// coins				
	public enum enumCoinPattern{
		Line,Column;
	}	
	
	public enum Move{
		UP,DOWN,NONE;
	}	
	public enum enumBlockType{
		brick,steel,wood;
	}
	public enum Color{
		NONE,RED,BLUE,GREEN,YELLOW,ALL;
	}
		
	public enum object {
		coin,block,tool,space,postsign;
	}	
	private object lastObject = object.space;
	
	// ================================ GAME OBJECTS ================================//
	
	// ObjectThread list
	public LinkedList<ObjectThread> ObjectThreadList = new LinkedList<ObjectThread>();
	
	// ================================ GAME OBJECTS ================================//
	
	// constructor	
	public 	LevelBuilder(){		
		
		// contorl object speed speed
		_ObjectsSpeed = Conf.SCREEN_REGULAR_COLLISION_LAYER_SCROLL_SPEED;
		BackGroundSpeed = Conf.SCREEN_REGULAR_BACKGROUND_SCROLL_SPEED;
		
		// initial settings
		Settings(0);		
		newDifficultyConfiguration(_Distance);	
		
		ThreadInterface ThreadListener = this;		
		newRound = true; // every new round there is a new levelbuilder
		
	}
	
	public void Settings(int stageDiff)
	{
		Log.i("Difficulty","new stageDiff, stageDiff was set: " +
					String.valueOf(stageDiff));
		
		switch(stageDiff)
		{
		case 0:
			
			_GenerationObjectInterval=0.25f;
			 blockGenerateRange =  0.55f;			 
			 cointGenerateRange = 0.35f;
			 toolGenerateRange = 0.15f;
			 spaceMinGenerateBlock = 3;
			 spaceMaxGenerateBlock = 5;
			 _ObjectsSpeed +=0.55;
			break;
			
		case 1:
			
			_GenerationObjectInterval=0.23f;
			 blockGenerateRange =  0.65f;			 
			 cointGenerateRange = 0.30f;
			 toolGenerateRange = 0.05f;
			 spaceMinGenerateBlock = 4;
			 spaceMaxGenerateBlock = 4;
			 
			break;
			
		case 2:
			
			_GenerationObjectInterval=0.21f;
			 blockGenerateRange =  0.75f;			 
			 cointGenerateRange = 0.20f;
			 toolGenerateRange = 0.05f;
			 spaceMinGenerateBlock = 4;
			 spaceMaxGenerateBlock = 6;	
			 _LengthOffsetTwoParts = 1;
			break;
			
		case 3:
			
			_GenerationObjectInterval=0.19f;
			 blockGenerateRange =  0.85f;			 
			 cointGenerateRange = 0.15f;
			 toolGenerateRange = 0.00f;
			 spaceMinGenerateBlock = 4;
			 spaceMaxGenerateBlock = 6;
			 _ObjectsSpeed +=0.75;
			 _LengthOffsetTwoParts = 2;
			 _LengthOffsetOneParts = 1;
			break;
			
		case 4:		
			
			_GenerationObjectInterval=0.17f;
			 blockGenerateRange =  0.90f;			 
			 cointGenerateRange = 0.10f;
			 toolGenerateRange = 0.00f;
			 spaceMinGenerateBlock = 4;
			 spaceMaxGenerateBlock = 5;
			 _ObjectsSpeed +=0.85;
			 _LengthOffsetTwoParts = 2;
			 _LengthOffsetOneParts = 1;
			break;
			
		case 5:		
			
			_GenerationObjectInterval=0.13f;
			 blockGenerateRange =  0.95f;			 
			 cointGenerateRange = 0.05f;
			 toolGenerateRange = 0.00f;
			 spaceMinGenerateBlock = 4;
			 spaceMaxGenerateBlock = 4;
			 _ObjectsSpeed +=1.15;
			 _LengthOffsetTwoParts = 2;
			 _LengthOffsetOneParts = 1;
			break;
			
		}
		
		// decrement generation interval
		 DecrementGenerationInterval();
		 
	}
	// ======================= Difficulty Configuration ======================= //
	 public void newDifficultyConfiguration(double _Distance){
		 
		 setNewSpeed = false;
		 
		 if ((_Distance)> _CheckPointDistanceInterval){
	 			
			 	if (ObjectThreadList.isEmpty()==true)
			 	{
			 		
			 		// probability			 
					setNewTarget = false;					 
					setNewSpeed = false;
					warmUp = false;
					
			 	}
			 	else
			 	{
			 		Log.i("Difficulty","waiting to set new speed, stop creating object.");
			 		setNewSpeed = true;
			 		
			 		 if (setNewTarget==false){
			 			setNewTarget = true;
			 			
			 			 if (warmUp==false){
					 			
				 			stageDiff++; 
				 			Settings(stageDiff);
				 							 			
				 			/*
				 			if ((stageDiff % 3)==0){
				 				_ObjectsSpeed +=0.75; // 0.55
				 				stageDiff = 0;
				 			}
				 			*/			 		
			 			 }
			 			 
			 			 warmUp = false;
			 			_CheckPointDistanceInterval +=10 + stageDiff * 2;
			 		
						  ScreenGameplay.fntCheckPoint.setBitmapText("CheckPoint: " + 
									_CheckPointDistanceInterval + "m");
						 
						 gamePoints = gamePoints + 15 + stageDiff * 2;
						 ScreenGameplay.fntScore.setBitmapText("Score: " + String.valueOf(gamePoints));						
			 		 } 
			 	}
			 	
		  }
		 
	 }
	 
	 private double DecrementGenerationInterval()
	 {
		 // As the interval number reduced, the generations of the objects
		 // becomes more frequent. This value cannot be zero, so the function
		 // I'll use is 1/x while increasing x each difficulty.
		 
		 _GenerationObjectInterval =  1 / _GenerationObjectInterval;
		 _GenerationObjectInterval += 0.7;
		 _GenerationObjectInterval = 1/ _GenerationObjectInterval;
		 return _GenerationObjectInterval;
	 }
	// ======================= Difficulty Configuration ======================= //
	 
	 
	// ============================= Thread Managemnet ===========================//		
	 public void SyncGenerateCreator(boolean pause){	
		 
		 if (pause==true){
			 return;
		 }
		 newDifficultyConfiguration(_Distance);
		 
		 if (setNewSpeed==false){
			 
			 // decide next item in case there is no new difficulty
			 // meanning that the speed is not increased.
			 
			 if (newRound){
				 newRound=false;
				 lastObject = object.postsign;
			 }
			 
			 if (lastObject==object.space){
				 float r = rand.nextFloat();			 
				 if (lastObject == object.space){
					 if (Conf.IsRange(r,0.f,blockGenerateRange)){
							lastObject = object.block;
					 }
					 else if (Conf.IsRange(r,blockGenerateRange,
							 blockGenerateRange+cointGenerateRange)){
						 lastObject = object.coin;
					 }
					 else if (Conf.IsRange(r,blockGenerateRange+cointGenerateRange,
							 blockGenerateRange+cointGenerateRange+toolGenerateRange)){
						 lastObject = object.tool;
					 }
					 
				 }								
			 }
		 
		 
		 if (ObjectThreadList.size()<100){
			 
			 // generate next item				
			 switch(lastObject){
			 	case block:
			 		// generate block depending on space counter
					if (spaceCounter==0){
						 // first time: setting spaceCounter
						 CreateNewObjectThread(object.block); 
						 spaceCounter = Conf.randInt(spaceMinGenerateBlock, 
								 spaceMaxGenerateBlock);					 
					 }
					 else{
						 spaceCounter--;
						 // if spaceCounter reached 0: free lastObject
						 if (spaceCounter==0) lastObject = object.space;							 
					 }				 
					break;
					
				case coin:
					if (spaceCounter==0){
						CreateNewObjectThread(object.coin);
						lastObject = object.coin;
						ObjectThread CoinobjThrd = null;
						CoinobjThrd = ObjectThreadList.get(ObjectThreadList.size()-1);	
						if (CoinobjThrd.coinPatternType==enumCoinPattern.Line){
							spaceCounter = CoinobjThrd.Coin_Num + 1;
						}
						else{
							spaceCounter = 3;
							lastObject = object.space;
						}												
					} 
					else{
						spaceCounter--;
						 // if spaceCounter reached 0: free lastObject
						 if (spaceCounter==0) lastObject = object.space;
					}					
					break;
					
				case tool:
					if (spaceCounter==0){
						CreateNewObjectThread(object.tool); 					 						
						spaceCounter=3;
					}
					else{
						spaceCounter--;
						 // if spaceCounter reached 0: free lastObject
						 if (spaceCounter==0) lastObject = object.space;
					}
					break;
					
				case postsign:
			 		// generate block depending on space counter
					if (spaceCounter==0){
						 // first time: setting spaceCounter
						 CreateNewObjectThread(object.postsign); 
						 spaceCounter = Conf.randInt(spaceMinGenerateBlock, 
								 spaceMaxGenerateBlock);					 
					 }
					 else{
						 spaceCounter--;
						 // if spaceCounter reached 0: free lastObject
						 if (spaceCounter==0) lastObject = object.space;							 
					 }				 
					break;
				default:
					break;				 
			 }
		}
		 
	  }
	 }
	 
	 private void CreateNewObjectThread(object type){
		 
		 	// only create new object threads when new level
		 	// speed was set for the new objects
		 
		 	if (setNewSpeed==false){
		 	// create new thread to the thread list
			 	ThreadInterface threadListener = this;
				 ObjectThread objThrd = new ObjectThread(_ObjectsSpeed, type, threadListener,this);
				 ObjectThreadList.add(objThrd);				 
				 Log.i("ObjectThread", "new objThrd : " + type.toString());
				 Log.i("ObjectThread", "new objThrd ObjectsSpeed : " + String.valueOf(_ObjectsSpeed));
		 	}
			
	 }
	
	 @Override
	 public void OnThreadDone() {
		// TODO Auto-generated method stub		 
		 ObjectThreadList.removeFirst();
	 }	 
	// ============================= Thread Managemnet ===========================//		
		 
	 
	
	

	
	 
}
