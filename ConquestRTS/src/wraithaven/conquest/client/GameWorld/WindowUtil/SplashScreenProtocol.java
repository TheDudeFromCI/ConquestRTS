package wraithaven.conquest.client.GameWorld.WindowUtil;

import java.awt.Image;

public interface SplashScreenProtocol{
	public void addCompletionListener(Runnable run);
	public void setIcon(Image icon);
	public void setTitle(String title);
	public void showSplash();
}