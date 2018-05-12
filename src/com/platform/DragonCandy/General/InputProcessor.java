package com.platform.DragonCandy.General;

import java.util.ArrayList;
import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.platform.DragonCandy.Screens.ScreenGameplay.GameState;


public class InputProcessor  {

	private static enum ScreenTouchState{
		NotTouched,TouchDown,TouchUp;
	}
	
	//=================== Variables ==================//
	private boolean FirstTouch=false;
	private ScreenTouchState ScreenState = ScreenTouchState.NotTouched;
	float TouchX, TouchY;
	private LinkedList<Rectangle> m_area = new LinkedList<Rectangle>();		
	//================================================//
	
	
	//==================================================================//		
	public InputProcessor(){
		clearArea();
	}	
	//==================================================================//		
	public void clearArea(){
		
		m_area.clear();
	}
	//==================================================================//
	public void setListener(Rectangle area){
		
		boolean found = false;
		
		int len = m_area.size()-1;
		for (int j=len;j>=0;j--){
			Rectangle tmp_area = m_area.get(j);
			if (tmp_area==area){
				found = true;				
			}
		}
		
		if (!found){
			m_area.add(area);
		}
	}
	//==================================================================//
	@SuppressWarnings("incomplete-switch")
	private boolean TouchDetect(){
		
		 boolean touchInput= Gdx.input.isTouched();
		 TouchX = -1;
		 TouchY = -1;
		 
		 switch (ScreenState)
		 {
		 	
			 case NotTouched: // screen not been touched yet.
				 
				 // if there is a touch -> then this is the first time the
				 // player pressed the screen
				 if (touchInput) ScreenState=ScreenTouchState.TouchDown;
				 FirstTouch=false;
				 return false;
				 
			 case TouchDown: // screen is being touch
				 
				 // if there is No a touch -> then this means that the 
				 // player stop pressing the screen
				 if (touchInput){
					 if (FirstTouch==false){
						 FirstTouch=true;
						 TouchX = Gdx.input.getX();
						 TouchY = Gdx.input.getY();								
						 return true;
					 }						 
				 }
				 else{
					 ScreenState=ScreenTouchState.NotTouched;
				 }
				 return false;		 
		 }
		 
		return false;			 
	}
	//==================================================================//
	public int IsButtonTouched(){
		
		int i = -1; // no press
		
		// if one of the costum button were pressed
		if (TouchDetect()){
			
			for (Rectangle area : m_area){
				
				i++;
				
				if ((TouchX>=area.x)&&(TouchX<(area.x+area.width)) && 
						(TouchY<Conf.HEIGHT-area.y)&&(TouchY>=Conf.HEIGHT-(area.y+area.height))){
					return i; 
				}			
			}
			
			return -1;
		}
		
		return -1;			
	}
	//==================================================================//
		
}
