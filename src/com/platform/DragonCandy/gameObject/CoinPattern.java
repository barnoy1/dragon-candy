package com.platform.DragonCandy.gameObject;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Pool;
import com.platform.DragonCandy.General.Conf;
import com.platform.DragonCandy.General.PhoneDevice;
import com.platform.DragonCandy.General.PhoneDevice.Settings;
import com.platform.DragonCandy.General.PhoneDevice.Settings.SoundsEnum;
import com.platform.DragonCandy.gameObject.Effects.enumEffects;
import com.platform.DragonCandy.gameObject.TextureAtlas.CoinTexture;
import com.platform.DragonCandy.logic.LevelBuilder;
import com.platform.DragonCandy.logic.LevelBuilder.enumCoinPattern;


public class CoinPattern{
	

	// pattern variables //
	public int StartX=-1;
	public int StartY=-1;
	private int[][] CoinPatternCoordinates=null;
	
	// coins variables
	
	private Pool<Rectangle> rectPool = new Pool<Rectangle>()
			{
				@Override
				protected Rectangle newObject()
				{
					return new Rectangle();
				}
			};
	
	/*
	 * Private coin class. 
	 * Contains location data
	 * Contains Collected data
	 */
	public class CoinData{
		
		public ParticleEffect pe =  Effects.setNewEffect(enumEffects.CoinCollection);
		public Rectangle location=null; 
		public boolean Collected=false;
		public int value;
		public CoinTexture coinTexture;
		
		public void PlayCoinCollectedSound(){
			if (this.Collected) {
				PhoneDevice.Settings.playSound(PhoneDevice.Settings.SoundsEnum.CollectCoin);	}
		}	
	}
	
	public  ArrayList<CoinData> Coins = new ArrayList<CoinData>();
	
	/* 
	 * contructor: a vector that contains data about this pattern
	 * will be implemented in each level.
	 * each pattern contains an array of coins
	*/
	public CoinPattern(int coinsNum, enumCoinPattern selectedCoinPattern,
			int StartX, int StartY){
	
		BuildPattern(coinsNum, selectedCoinPattern,StartX,StartY);
	
	}
	/*
	public static Texture getCoinImg(int coinValue) {
				
		if (coinValue>=5){
			return coinImgValueHigh;	
		}
		else{
			return coinImgValueLow;
		}
	}
	*/
	private void BuildPattern(int coinsNum,enumCoinPattern selectedCoinPattern, 
			int StartX, int StartY){
		
		/*
		 * build pattern coordinates for the batch.
		 */
		
		rectPool.clear();		
		Coins.clear();
		
		CoinPatternCoordinates = new int [coinsNum][2];
						
		int coinIndex=0;
		int CordY = PosToCord(StartY);
		
		switch (selectedCoinPattern){
		case Line: 
			for (int[] CoinCell: CoinPatternCoordinates){
				CoinCell[0]=  StartX + coinIndex*Conf.COIN_SPACE_SMALL[0];				
				CoinCell[1]=  CordY;
				coinIndex++;
				
				// set the 2D array of layer to rectPool
				CoinData c = new CoinData();				
				c.Collected=false;
				c.value = Conf.randInt(1, 7);	
				c.coinTexture = new TextureAtlas.CoinTexture();
				Rectangle rect = rectPool.obtain();
				rect.set((int) CoinCell[0], (int) CoinCell[1], (int) c.coinTexture.Width,
						(int) c.coinTexture.Height);				
				c.location=rect;				
				Coins.add(c);
				
			}
			break;
			
		case Column: 
			
			for (int[] CoinCell: CoinPatternCoordinates){
				CoinCell[0]=  StartX;
				CoinCell[1]=  CordY + coinIndex* Conf.COIN_SPACE_SMALL[1];
				coinIndex++;

				// set the 2D array of layer to rectPool
				CoinData c = new CoinData();				
				c.Collected=false;
				c.value = Conf.randInt(1, 7);	
				c.coinTexture = new TextureAtlas.CoinTexture();
				Rectangle rect = rectPool.obtain();
				rect.set((int) CoinCell[0], (int) CoinCell[1], (int) c.coinTexture.Width,
						(int) c.coinTexture.Height);				
				c.location=rect;				
				Coins.add(c);
				
			}
			break;
		}
		
	}
	
	public void setCoins(float defaultSpeed, float overrideObjectsSpeed) {
		
		if (overrideObjectsSpeed==0){ 
			return;
		}			
		else if (overrideObjectsSpeed==-1){
			// change nothing: dont change default speed
			
		}
		else{
			// change default speed to overrite value
			defaultSpeed = overrideObjectsSpeed;
		}
		
	
		Rectangle tmpRect;		
		for (int i=0; i<=Coins.size()-1;i++){
			tmpRect=Coins.get(i).location;
			tmpRect.x= tmpRect.x - defaultSpeed;			
		}
		
	}
	
	public ArrayList<CoinData> getCoins() {
		return Coins;
	}

	public void ReleaseCoin(CoinData thisCoin) {
		// TODO Auto-generated method stub		
		thisCoin.pe.reset();
		thisCoin.pe.dispose();		
		Coins.removeAll(Coins);
	}
	
	private int PosToCord(int PosY){		
		return PosY * (int) (Conf.SCREEN_BACKGROUND_GRASS_HEIGHT +
				Conf.COIN_WIDTH/4);
		//110
	}
}


