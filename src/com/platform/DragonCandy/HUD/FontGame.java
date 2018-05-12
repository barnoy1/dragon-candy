package  com.platform.DragonCandy.HUD;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.platform.DragonCandy.General.Conf;


public class FontGame {

	public static enum FontType{
		Score,Distance,CheckPoint,PostSignTitle,
			PostSignBestScore,Pause;
	}

	private BitmapFont gamefont;

	private String BitmapText;
	
	public FontGame(FontType fntType){
		switch(fntType){
		case Distance:
			gamefont = new BitmapFont(Gdx.files.internal("data/fonts/gameFont.fnt"),
			         Gdx.files.internal("data/fonts/gameFont.png"), false);		
			gamefont.setScale(Conf.BITMAP_SCALE_SMALL, Conf.BITMAP_SCALE_SMALL);
			break;
			
		case Score:
			gamefont = new BitmapFont(Gdx.files.internal("data/fonts/gameFont.fnt"),
			         Gdx.files.internal("data/fonts/gameFont.png"), false);		
			gamefont.setScale(Conf.BITMAP_SCALE_SMALL, Conf.BITMAP_SCALE_SMALL);
			break;
			
		case Pause:
			gamefont = new BitmapFont(Gdx.files.internal("data/fonts/PauseFont.fnt"),
			         Gdx.files.internal("data/fonts/PauseFont.png"), false);		
			gamefont.setScale(Conf.BITMAP_SCALE_LARGE, Conf.BITMAP_SCALE_LARGE);
			break;
			
		case CheckPoint:
			gamefont = new BitmapFont(Gdx.files.internal("data/fonts/gameFont.fnt"),
			         Gdx.files.internal("data/fonts/gameFont.png"), false);		
			gamefont.setScale(Conf.BITMAP_SCALE_SMALL, Conf.BITMAP_SCALE_SMALL);
			break;
			
		case PostSignTitle:
			gamefont = new BitmapFont(Gdx.files.internal("data/fonts/PostSign.fnt"),
			         Gdx.files.internal("data/fonts/PostSign.png"), false);		
			gamefont.setScale(Conf.BITMAP_SCALE_SMALL, Conf.BITMAP_SCALE_SMALL);
			break;
			
		case PostSignBestScore:
			gamefont = new BitmapFont(Gdx.files.internal("data/fonts/PostSign.fnt"),
			         Gdx.files.internal("data/fonts/PostSign.png"), false);		
			gamefont.setScale(Conf.BITMAP_SCALE_SMALL, Conf.BITMAP_SCALE_SMALL);
			break;
			
		default:
			break;		
		}	
	}

	public String getBitmapText() {
		return BitmapText;
	}

	public void setBitmapText(String bitmapText) {
		BitmapText = bitmapText;
	}

	public BitmapFont getGamefont() {
		return gamefont;
	}		
	
	public void Dispose(){
		BitmapText=null;
		gamefont.dispose();
	}
}

