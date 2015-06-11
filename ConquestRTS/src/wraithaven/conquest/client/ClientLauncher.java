package wraithaven.conquest.client;

import java.io.File;
import wraithaven.conquest.client.GameWorld.WindowUtil.Game;

public class ClientLauncher{
	public static String assetFolder;
	static Game game;
	public static String screenShotFolder;
	public static String textureFolder;
	public static void main(String[] args){
		String dir = System.getProperty("user.dir");
		ClientLauncher.screenShotFolder = dir+File.separatorChar+"Screenshots";
		ClientLauncher.assetFolder = dir+File.separatorChar+"Assets";
		ClientLauncher.textureFolder = dir+File.separatorChar+"Textures";
		ClientLauncher.game = new Game("Wraithaven's Conquest", new File(ClientLauncher.assetFolder));
		ClientLauncher.game.addGameStartListener(new Runnable(){
			public void run(){
				ClientLauncher.game.getScreen().setRenderSize(4, 3, true);
				ClientLauncher.game.setGameRenderer(new TitleScreen(ClientLauncher.game));
			}
		});
		ClientLauncher.game.showSplash(new LogInSplash());
	}
}