package wraithaven.conquest.client.GameWorld.WindowUtil;

import java.awt.Image;
import java.awt.image.BufferedImage;

public class DefaultSplashScreen implements SplashScreenProtocol{
	private Runnable completionListener;
	private int fadeIn,
			fadeStay,
			fadeOut,
			tickDelay;
	private Image icon;
	private BufferedImage img;
	private ImageWindow splash;
	private String title;
	public DefaultSplashScreen(BufferedImage img, int fadeIn, int fadeStay, int fadeOut, int tickDelay){
		this.img = img;
		this.fadeIn = fadeIn;
		this.fadeStay = fadeStay;
		this.fadeOut = fadeOut;
		this.tickDelay = tickDelay;
	}
	public void addCompletionListener(Runnable run){
		completionListener = run;
	}
	public void setIcon(Image icon){
		this.icon = icon;
	}
	public void setTitle(String title){
		this.title = title;
	}
	public void showSplash(){
		splash = new ImageWindow(img);
		splash.setIconImage(icon);
		splash.setTitle(title);
		splash.addFadeTimer(fadeIn, fadeStay, fadeOut, tickDelay);
		img = null;
		icon = null;
		title = null;
		try{
			Thread.sleep((fadeIn+fadeStay+fadeOut)*tickDelay);
		}catch(Exception exception){}
		completionListener.run();
	}
}