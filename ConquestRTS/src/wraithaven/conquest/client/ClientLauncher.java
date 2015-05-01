package wraithaven.conquest.client;

import java.io.File;
import wraith.library.WindowUtil.GameWindow.Game;

public class ClientLauncher{
	public static Game game;
	public static String folder;
	public static void main(String[] args){
		game=new Game("Wraithaven's Conquest", new File(folder=(System.getProperty("user.dir")+File.separatorChar+"Assets")));
		game.addGameStartListener(new Runnable(){
			public void run(){
				game.getScreen().setRenderSize(4, 3, true);
				game.setGameRenderer(new TitleScreen(game));
			}
		});
		game.showSplash(new LogInSplash());
	}
}