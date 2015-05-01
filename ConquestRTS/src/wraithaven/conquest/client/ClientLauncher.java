package wraithaven.conquest.client;

import java.io.File;
import wraith.library.WindowUtil.GameWindow.Game;

public class ClientLauncher{
	public static Game game;
	public static String assetFolder;
	public static String screenShotFolder;
	public static void main(String[] args){
		String dir = System.getProperty("user.dir");
		screenShotFolder=dir+File.separatorChar+"Screenshots";
		game=new Game("Wraithaven's Conquest", new File(assetFolder=(dir+File.separatorChar+"Assets")));
		game.addGameStartListener(new Runnable(){
			public void run(){
				game.getScreen().setRenderSize(4, 3, true);
				game.setGameRenderer(new TitleScreen(game));
			}
		});
		game.showSplash(new LogInSplash());
	}
}