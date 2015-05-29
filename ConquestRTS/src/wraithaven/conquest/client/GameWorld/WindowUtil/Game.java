package wraithaven.conquest.client.GameWorld.WindowUtil;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

public class Game{
	private GameDataFolder gameDataFolder;
	private GameRenderer gameRenderer;
	private ArrayList<Runnable> gameStartListeners = new ArrayList();
	private GameScreen screen;
	private GameThread thread;
	private String title;
	public Game(final String title, File dataFolder){
		gameDataFolder = new GameDataFolder(dataFolder);
		this.title = title;
	}
	public void addGameStartListener(Runnable run){
		gameStartListeners.add(run);
	}
	private void callGameStart(){
		for(int i = 0; i<gameStartListeners.size(); i++)
			gameStartListeners.get(i).run();
		gameStartListeners = null;
	}
	public GameDataFolder getFolder(){
		return gameDataFolder;
	}
	public GameRenderer getGameRenderer(){
		return gameRenderer;
	}
	public GameThread getGameThread(){
		return thread;
	}
	public int getRenderX(){
		return screen.getRenderX();
	}
	public int getRenderY(){
		return screen.getRenderY();
	}
	public GameScreen getScreen(){
		return screen;
	}
	public int getScreenHeight(){
		return screen.getRenderSize().height;
	}
	public int getScreenWidth(){
		return screen.getRenderSize().width;
	}
	public void setGameRenderer(GameRenderer gameRenderer){
		this.gameRenderer = gameRenderer;
		screen.setGameRenderer(gameRenderer);
	}
	public void showSplash(){
		showSplash(new DefaultSplashScreen(gameDataFolder.getImageWindow(), 40, 70, 40, 20));
	}
	public void showSplash(SplashScreenProtocol splash){
		final BufferedImage icon = gameDataFolder.getIcon();
		splash.setIcon(icon);
		splash.setTitle(title);
		splash.addCompletionListener(new Runnable(){
			public void run(){
				screen = new GameScreen(title, icon, gameRenderer, null);
				thread = new GameThread();
				callGameStart();
			}
		});
		splash.showSplash();
	}
}