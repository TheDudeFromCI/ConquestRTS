package wraithaven.conquest.client;

import java.io.File;
import wraithaven.conquest.client.GameWorld.WindowUtil.Game;

public class ClientLauncher{
	static Game game;
	public static String assetFolder;
	public static String textureFolder;
	public static String screenShotFolder;
	public static void main(String[] args){
		String dir = System.getProperty("user.dir");
		screenShotFolder=dir+File.separatorChar+"Screenshots";
		assetFolder=dir+File.separatorChar+"Assets";
		textureFolder=dir+File.separatorChar+"Textures";
		game=new Game("Wraithaven's Conquest", new File(assetFolder));
		game.addGameStartListener(new Runnable(){
			public void run(){
				game.getScreen().setRenderSize(4, 3, true);
				game.setGameRenderer(new TitleScreen(game));
			}
		});
		game.showSplash(new LogInSplash());
	}
}