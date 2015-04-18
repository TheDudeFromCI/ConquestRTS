package wraithaven.conquest.client;

import java.io.File;
import wraith.library.WindowUtil.GameWindow.Game;

public class ClientLauncher{
	public static void main(String[] args){
		Game game = new Game("Wraithaven's Conquest", new File("C:/Users/Phealoon/Desktop/Conquest Folder"));
		game.getScreen().setRenderSize(4, 3, true);
	}
}