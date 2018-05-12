package com.platform.DragonCandy.General;

import android.content.Context;
import android.os.Vibrator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.platform.DragonCandy.Screens.MainActivity;

public class PhoneDevice{
	
	// sound
	private static Sound wavCoinCollected = Gdx.audio.newSound(Gdx.files.internal("data/object/Coin/coin_collected.wav"));
	private static Sound wavBrickBreakThough = Gdx.audio.newSound(Gdx.files.internal("data/object/block/smash_wall.wav"));				
	private static Sound wavGrabTool = Gdx.audio.newSound(Gdx.files.internal("data/object/Tool/effects/grab-tool-sound-effect.mp3"));	
	private static Sound wavButtonClick = Gdx.audio.newSound(Gdx.files.internal("data/buttons/effects/buttonClick.mp3"));	
	private static Sound wavBrickCrash = Gdx.audio.newSound(Gdx.files.internal("data/object/block/crash.mp3"));	
	public Context mContext;
	
	// music
	private static com.badlogic.gdx.audio.Music music_ten_k_and_counting = Gdx.audio.newMusic(Gdx.files.internal("data/music/psyche_up.mp3"));
	
	// vibrate
	private static Vibrator vibrator = MainActivity.getVibrator();
	
	
	// vibration settings
	public PhoneDevice(){
	
	}		
	
	public static void Vibrate(){		
		if (Settings.Vibration==true) vibrator.vibrate(100);				
	}
	
	public static class Settings{
			
		// sounds settings
		public static boolean MuteSound = true;
		public static boolean MuteMusic = true;
		public static boolean Vibration = true;
		
		// Sounds
		public static enum SoundsEnum{
			CollectCoin, GrabTool,BrickBreakThough,ButtonClick,Crash;
		}
		
		// Sounds
		public static enum MusicEnum{
			psyche_up;
		}
		private static MusicEnum thisMusic;
		
		public static boolean isVibration() {
			return Vibration;
		}
		public static void setVibration(boolean vibration) {
			Vibration = vibration;
		}
		public static boolean isSound() {
			return MuteSound;
		}
		public static void setSound(boolean sound) {
			MuteSound = sound;
		}
		public static boolean isMusic() {
			return MuteMusic;
		}
		public static void setMusic(boolean music) {
			MuteMusic = music;
		}
		
		public static void playMusic (MusicEnum track){			
			if (MuteMusic==false) return;
			thisMusic = track;
			switch(track){
			case psyche_up:
				music_ten_k_and_counting.setLooping(true);
				music_ten_k_and_counting.setVolume(0.15f);
				music_ten_k_and_counting.play();
				break;
			default:
				break;			
			}			
		}
		
		public static void stopMusic(){
			
			if (MuteMusic==false) return;
			switch(thisMusic){
			case psyche_up: if (music_ten_k_and_counting.isPlaying()==true) music_ten_k_and_counting.stop();break;
			default:
				break;			
			}		
			
		}
		
		public static void playSound (SoundsEnum soundType){
			
			if (MuteSound==false) return;
			
			switch (soundType){
			case CollectCoin: wavCoinCollected.play();break;				
			case GrabTool: wavGrabTool.play(0.15f);break;				
			case BrickBreakThough: wavBrickBreakThough.play(0.1f); break;				
			case ButtonClick: wavButtonClick.play(); break;				
			case Crash: wavBrickCrash.play();break;							
			default:break;			
			}
		}
		
		public static void setMusicValue(boolean state){
			
			if (isMusic()==false)
				return;
			
			if (state==true){
				// restore music Volume
				music_ten_k_and_counting.setVolume(0.15f);
			}
			else{
				// mute
				music_ten_k_and_counting.setVolume(0.00f);				
			}
			
		}
			
	}
	
}
