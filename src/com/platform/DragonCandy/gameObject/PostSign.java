package com.platform.DragonCandy.gameObject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.platform.DragonCandy.R;
import com.platform.DragonCandy.General.Conf;
import com.platform.DragonCandy.General.Memory;
import com.platform.DragonCandy.HUD.FontGame;
import com.platform.DragonCandy.HUD.FontGame.FontType;
import com.platform.DragonCandy.Screens.MainActivity;

public class PostSign {

	// location
	public Rectangle SignPostRect;
	public float[] acc = new float[1];
	private static Texture SignPostTexture;
	
	// gameFont
	public static FontGame PostSignTitle;
	public static FontGame PostSignBestScore;
	private String sBestScore;
	 
	public PostSign(Vector2 location, float[] acc){
		
		 InitiatePostSignText();
		 
		 SignPostRect = new Rectangle().set(location.x, location.y,
				 Conf.POST_SIGN_WIDTH, Conf.POST_SIGN_HIEGHT);
		 this.acc = acc;
	}
	
	private void InitiatePostSignText()
	{
		 
		SignPostTexture = new Texture(Gdx.files
				.internal("data/object/Other/post_sign.png"));
		 sBestScore = Memory.LoadFromInternalMemory("Score");
		 if (sBestScore.length()==0){
			 Memory.SaveToInternalMemory("Score",
						String.valueOf(0));
		 }
		 // initialy draw the post sign
		 PostSignBestScore = new FontGame(FontType.PostSignBestScore);
		 PostSignTitle = new FontGame(FontType.PostSignTitle);
		 
		 // post sign values
		 PostSignTitle.setBitmapText(MainActivity.getmContext().getResources().
				 getString(R.string.str_BestScore));
		 
		 PostSignBestScore.setBitmapText(sBestScore);
		 
	}
	
	public Texture getSignPostTexture() {
		return SignPostTexture;
	}

	public Rectangle getSignPostRect() {
		return SignPostRect;
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
	
		SignPostRect.set(SignPostRect.x - acc[0], SignPostRect.y,
				Conf.POST_SIGN_WIDTH, Conf.POST_SIGN_HIEGHT);	
		
	}

	public static void Destroy(){
		
		SignPostTexture = null;
		
		// gameFont
		PostSignTitle = null;
		PostSignBestScore = null;
	}
}
