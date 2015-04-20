package wraithaven.conquest.client;

import java.io.File;
import wraith.library.WindowUtil.GameWindow.Game;
import wraith.library.WindowUtil.GameWindow.PendingTask;

public class ClientLauncher{
	public static void main(String[] args){
		final Game game = new Game("Wraithaven's Conquest", new File("C:/Users/Phealoon/Desktop/Conquest Folder"));
		game.getScreen().setRenderSize(4, 3, true);
		new PendingTask(game.getGameThread(), 0){
			public void run(){
				TitleScreen titleScreen = new TitleScreen(game);
				game.setGameRenderer(titleScreen);
				game.setInputListener(titleScreen.getGui());
			}
			public int getPriority(){ return 100; }
		};
	}
}