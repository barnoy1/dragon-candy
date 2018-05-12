package com.platform.DragonCandy.General;

import com.platform.DragonCandy.Screens.MainActivity;

import android.content.Context;
import android.content.SharedPreferences;

public class Memory {

	//----------------------------------Begin SaveToInternalMemory----------------------//
		public static void SaveToInternalMemory(String variableName, String variableValue)
		{			
			// Save new value
			
			/*
			 * SaveToInternalMemory(getApplicationContext(),"Language","en");
			 */
			Context mContext = MainActivity.getmContext();
			SharedPreferences prefs = mContext.getSharedPreferences("preferencename", 0);  
		    SharedPreferences.Editor editor = prefs.edit(); 
		    editor.putString(variableName , variableValue);  
		    editor.commit();		      
		}
		//----------------------------------End SaveToInternalMemory------------------------//
		
		//-----------------Begin LoadFromInternalMemory---------------------------//
		public static String LoadFromInternalMemory(String variableName)
		{
			/*
			 * sYesterday=LoadFromInternalMemory(getApplicationContext(), "Yesterday");
			 */
			
			String value = "";
			SharedPreferences prefs;
			Context mContext = MainActivity.getmContext();
			
			// Load previous value
			prefs = mContext.getSharedPreferences("preferencename", 0);  
			value = prefs.getString(variableName, "");
			return value;
		}	
		//-----------------End LoadFromInternalMemory---------------------------//	
}
